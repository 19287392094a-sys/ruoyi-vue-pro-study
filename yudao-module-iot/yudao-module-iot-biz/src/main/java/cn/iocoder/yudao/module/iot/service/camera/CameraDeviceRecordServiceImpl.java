package cn.iocoder.yudao.module.iot.service.camera;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.module.iot.dal.dataobject.camera.CameraDeviceDO;
import cn.iocoder.yudao.module.iot.dal.dataobject.camera.IotCameraRecordDO;
import cn.iocoder.yudao.module.iot.dal.mysql.camera.CameraDeviceMapper;
import cn.iocoder.yudao.module.iot.dal.mysql.camera.IotCameraRecordMapper;
import cn.iocoder.yudao.module.iot.framework.camera.config.IotCameraProperties;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CameraDeviceRecordServiceImpl implements CameraDeviceRecordService {

    private static final int STATUS_RECORDING = 0;
    private static final int STATUS_FINISHED = 1;
    private static final int STATUS_FAILED = 2;

    private static final int UPLOAD_STATUS_PENDING = 0;
    private static final int UPLOAD_STATUS_SUCCESS = 1;
    private static final int UPLOAD_STATUS_FAILED = 2;

    private final Map<Long, Process> recordProcessMap = new ConcurrentHashMap<>();

    @Resource
    private CameraDeviceMapper cameraDeviceMapper;

    @Resource
    private IotCameraRecordMapper cameraRecordMapper;

    @Resource
    private IotCameraProperties cameraProperties;

    @Resource
    private CameraRecordStorageService cameraRecordStorageService;

    @Override
    public Long startRecord(Long cameraId) {
        CameraDeviceDO camera = cameraDeviceMapper.selectById(cameraId);
        if (camera == null) {
            throw new IllegalArgumentException("Camera not found, cameraId=" + cameraId);
        }
        if (!Boolean.TRUE.equals(camera.getRecordEnabled())) {
            throw new IllegalStateException("Camera record is not enabled");
        }
        if (StrUtil.isBlank(camera.getRtspUrl())) {
            throw new IllegalStateException("Camera rtsp_url is blank");
        }
        if (recordProcessMap.containsKey(cameraId)) {
            throw new IllegalStateException("Camera is recording");
        }

        String filePath = buildRecordFilePath(camera);

        IotCameraRecordDO record = IotCameraRecordDO.builder()
                .cameraId(camera.getId())
                .deptId(camera.getDeptId())
                .startTime(LocalDateTime.now())
                .filePath(filePath)
                .status(STATUS_RECORDING)
                .uploadStatus(UPLOAD_STATUS_PENDING)
                .build();

        cameraRecordMapper.insert(record);

        record.setFileUrl(cameraRecordStorageService.buildFileUrl(record));
        cameraRecordMapper.updateById(record);

        try {
            Process process = startFfmpeg(camera.getRtspUrl(), filePath);
            recordProcessMap.put(cameraId, process);
            return record.getId();
        } catch (Exception ex) {
            record.setStatus(STATUS_FAILED);
            record.setErrorMsg(ex.getMessage());
            record.setEndTime(LocalDateTime.now());
            record.setUploadStatus(UPLOAD_STATUS_FAILED);
            record.setUploadErrorMsg(ex.getMessage());
            cameraRecordMapper.updateById(record);
            throw new IllegalStateException("Start camera record failed: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void stopRecord(Long cameraId) {
        Process process = recordProcessMap.remove(cameraId);
        if (process == null) {
            throw new IllegalStateException("Camera is not recording");
        }

        if (process.isAlive()) {
            try {
                process.getOutputStream().write("q\n".getBytes());
                process.getOutputStream().flush();

                if (!process.waitFor(10, java.util.concurrent.TimeUnit.SECONDS)) {
                    process.destroy();
                }
            } catch (Exception e) {
                process.destroy();
            }

            if (process.isAlive()) {
                process.destroyForcibly();
            }
        }

        IotCameraRecordDO record = cameraRecordMapper.selectRecordingByCameraId(cameraId);
        if (record == null) {
            throw new IllegalStateException("Recording camera record not found");
        }

        LocalDateTime endTime = LocalDateTime.now();
        record.setEndTime(endTime);
        record.setStatus(STATUS_FINISHED);

        if (record.getStartTime() != null) {
            record.setDuration((int) java.time.Duration.between(record.getStartTime(), endTime).getSeconds());
        }

        if (record.getFilePath() != null) {
            File file = new File(record.getFilePath());
            if (file.exists()) {
                record.setFileSize(file.length());
            }
        }

        try {
            CameraRecordStorageResult storageResult = cameraRecordStorageService.upload(record);
            record.setFileUrl(storageResult.getFileUrl());
            record.setUploadStatus(UPLOAD_STATUS_SUCCESS);
            record.setUploadTime(LocalDateTime.now());
            record.setUploadErrorMsg(null);
        } catch (Exception ex) {
            record.setUploadStatus(UPLOAD_STATUS_FAILED);
            record.setUploadErrorMsg(ex.getMessage());
        }

        cameraRecordMapper.updateById(record);
    }

    @Override
    public Long recordSegment(Long cameraId, Integer durationSeconds) {
        Long recordId = startRecord(cameraId);
        try {
            Thread.sleep(durationSeconds * 1000L);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } finally {
            try {
                stopRecord(cameraId);
            } catch (Exception ignored) {
            }
        }
        return recordId;
    }

    @Override
    public void stopAllRecords() {
        for (Long cameraId : new java.util.ArrayList<>(recordProcessMap.keySet())) {
            try {
                stopRecord(cameraId);
            } catch (Exception ignored) {
            }
        }
    }

    private String buildRecordFilePath(CameraDeviceDO camera) {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));

        String dirPath = cameraProperties.getRecordRootPath()
                + File.separator + camera.getCode()
                + File.separator + date;

        File dir = new File(dirPath);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IllegalStateException("Create record directory failed: " + dirPath);
        }

        return dirPath + File.separator + "record-" + timestamp + ".mp4";
    }

    private Process startFfmpeg(String rtspUrl, String filePath) throws Exception {
        ProcessBuilder builder = new ProcessBuilder(
                cameraProperties.getFfmpegPath(),
                "-y",
                "-rtsp_transport", "tcp",
                "-i", rtspUrl,
                "-c", "copy",
                "-movflags", "+faststart",
                filePath
        );

        builder.redirectErrorStream(true);
        Process process = builder.start();

        // Drain FFmpeg output to avoid blocking on a full process buffer.
        new Thread(() -> {
            try (InputStream inputStream = process.getInputStream()) {
                byte[] buffer = new byte[1024];
                while (inputStream.read(buffer) != -1) {
                    // Discard FFmpeg output.
                }
            } catch (Exception ignored) {
            }
        }, "camera-record-ffmpeg-log").start();

        return process;
    }
}
