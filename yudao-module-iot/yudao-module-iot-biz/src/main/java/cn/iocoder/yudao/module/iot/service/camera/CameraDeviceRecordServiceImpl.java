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
public class CameraDeviceRecordServiceImpl implements CameraDeviceRecordService{

    private static final int STATUS_RECORDING = 0;
    private static final int STATUS_FINISHED = 1;
    private static final int STATUS_FAILED = 2;

    private final Map<Long, Process> recordProcessMap = new ConcurrentHashMap<>();

    @Resource
    private CameraDeviceMapper cameraDeviceMapper;

    @Resource
    private IotCameraRecordMapper cameraRecordMapper;

    @Resource
    private IotCameraProperties cameraProperties;

    @Override
    public Long startRecord(Long cameraId) {
        CameraDeviceDO camera = cameraDeviceMapper.selectById(cameraId);
        if (camera == null) {
            throw new IllegalArgumentException("摄像头不存在，cameraId=" + cameraId);
        }
        if (!Boolean.TRUE.equals(camera.getRecordEnabled())) {
            throw new IllegalStateException("摄像头未启用录像");
        }
        if (StrUtil.isBlank(camera.getRtspUrl())) {
            throw new IllegalStateException("摄像头 rtsp_url 为空");
        }
        if (recordProcessMap.containsKey(cameraId)) {
            throw new IllegalStateException("摄像头正在录像中");
        }

        String filePath = buildRecordFilePath(camera);

        IotCameraRecordDO record = IotCameraRecordDO.builder()
                .cameraId(camera.getId())
                .deptId(camera.getDeptId())
                .startTime(LocalDateTime.now())
                .filePath(filePath)
                .status(STATUS_RECORDING)
                .build();

        cameraRecordMapper.insert(record);

        try {
            Process process = startFfmpeg(camera.getRtspUrl(), filePath);
            recordProcessMap.put(cameraId, process);
            return record.getId();
        } catch (Exception ex) {
            record.setStatus(STATUS_FAILED);
            record.setErrorMsg(ex.getMessage());
            record.setEndTime(LocalDateTime.now());
            cameraRecordMapper.updateById(record);
            throw new IllegalStateException("启动录像失败：" + ex.getMessage(), ex);
        }

    }

    @Override
    public void stopRecord(Long cameraId) {
        Process process = recordProcessMap.remove(cameraId);
        if (process == null) {
            throw new IllegalStateException("摄像头未在录像中");
        }

        if (process.isAlive()) {
            process.destroy();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            if (process.isAlive()) {
                process.destroyForcibly();
            }
        }

        IotCameraRecordDO record = cameraRecordMapper.selectRecordingByCameraId(cameraId);
        if (record == null) {
            throw new IllegalStateException("未找到录制中的录像记录");
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

        cameraRecordMapper.updateById(record);
    }

    private String buildRecordFilePath(CameraDeviceDO camera) {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));

        String dirPath = cameraProperties.getRecordRootPath()
                + File.separator + camera.getCode()
                + File.separator + date;

        File dir = new File(dirPath);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IllegalStateException("创建录像目录失败：" + dirPath);
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

        // Demo 先丢弃 FFmpeg 输出，避免输出缓冲区堵塞进程。
        new Thread(() -> {
            try (InputStream inputStream = process.getInputStream()) {
                byte[] buffer = new byte[1024];
                while (inputStream.read(buffer) != -1) {
                    // 丢弃 FFmpeg 输出，避免缓冲区堵塞
                }
            } catch (Exception ignored) {
            }
        }, "camera-record-ffmpeg-log").start();

        return process;
    }
}
