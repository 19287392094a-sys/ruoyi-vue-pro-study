package cn.iocoder.yudao.module.iot.service.camera;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.module.iot.framework.camera.config.IotCameraProperties;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class CameraInternalStreamService {

    @Resource
    private IotCameraProperties iotCameraProperties;

    private final Map<String,StreamRuntime> streams = new ConcurrentHashMap<>();

    public synchronized CameraStreamClient.StreamStartRespDTO startStream(String cameraCode,String rtspUrl, Boolean transcode){
        StreamRuntime old = streams.get(cameraCode);
        if(old != null && old.getProcess() != null && old.getProcess().isAlive()){
            return buildStartResp(cameraCode,old);
        }
        try{
            String safeCode = safeFileName(cameraCode);
            Path outputDir = Paths.get(iotCameraProperties.getHlsRootPath(),safeCode);
            Files.createDirectories(outputDir);

            Path indexFile = outputDir.resolve("index.m3u8");
            Path segmentFile = outputDir.resolve("%03d.ts");
            File logFile = outputDir.resolve("ffmpeg.log").toFile();

            List<String> command = new ArrayList<>();
            command.add(iotCameraProperties.getFfmpegPath());
            command.add("-rtsp_transport");
            command.add("tcp");
            command.add("-i");
            command.add(rtspUrl);

            if(Boolean.TRUE.equals(transcode)){
                command.add("-c:v");
                command.add("libx264");
                command.add("-preset");
                command.add("veryfast");
                command.add("-c:a");
                command.add("aac");
            }else{
                command.add("-c");
                command.add("copy");
            }

            command.add("-f");
            command.add("hls");
            command.add("-hls_time");
            command.add("2");
            command.add("-hls_list_size");
            command.add("6");
            command.add("-hls_flags");
            command.add("delete_segments");
            command.add("-hls_segment_filename");
            command.add(segmentFile.toString());
            command.add(indexFile.toString());


            ProcessBuilder builder = new ProcessBuilder(command);
            builder.redirectErrorStream(true);
            builder.redirectOutput(logFile);
            Process process = builder.start();

            StreamRuntime runtime = new StreamRuntime()
                    .setCameraCode(cameraCode)
                    .setSafeCode(safeCode)
                    .setProcess(process)
                    .setStartedAt(LocalDateTime.now().toString())
                    .setStatus("running")
                    .setPlayUrl("/hls/" + safeCode + "/index.m3u8");

            streams.put(cameraCode, runtime);
            return buildStartResp(cameraCode, runtime);
        } catch (Exception e) {
            throw new IllegalStateException("启动 FFmpeg 失败：" + e.getMessage(), e);
        }
    }

    public synchronized Boolean stopStream(String cameraCode){
        StreamRuntime runtime = streams.get(cameraCode);
        if(runtime == null || runtime.getProcess() == null){
            return true;
        }

        Process process = runtime.getProcess();
        if(process.isAlive()){
            process.destroy();
            try{
                Thread.sleep(500);
            }catch (InterruptedException ignored){
                Thread.currentThread().interrupt();
            }
            if(process.isAlive()){
                process.destroyForcibly();
            }
        }

        runtime.setStatus("stopped");
        runtime.setStoppedAt(LocalDateTime.now().toString());
        return true;
    }

    public CameraStreamClient.StreamStatusRespDTO getStreamStatus(String cameraCode){
        StreamRuntime runtime = streams.get(cameraCode);
        if(runtime == null){
            return new CameraStreamClient.StreamStatusRespDTO()
                    .setCameraCode(cameraCode)
                    .setStatus("stopped");
        }

        Process process = runtime.getProcess();
        if(process != null && !process.isAlive() && !"stopped".equals(runtime.getStatus())){
            runtime.setStatus("stopped");
            runtime.setStoppedAt(LocalDateTime.now().toString());
        }

        return new CameraStreamClient.StreamStatusRespDTO().setCameraCode(cameraCode)
                .setStatus(runtime.getStatus())
                .setPid(getPid(process))
                .setStartedAt(runtime.getStartedAt())
                .setStoppedAt(runtime.getStoppedAt())
                .setLastError(runtime.getLastError());
    }

    private CameraStreamClient.StreamStartRespDTO buildStartResp(String cameraCode,StreamRuntime runtime){
        return new CameraStreamClient.StreamStartRespDTO()
                .setCameraCode(cameraCode)
                .setStatus(runtime.getStatus())
                .setPid(getPid(runtime.getProcess()))
                .setPlayUrl(runtime.getPlayUrl());
    }

    private String safeFileName(String value){
        return StrUtil.blankToDefault(value,"camera").replaceAll("[^a-zA-Z0-9_-]","_");
    }

    private Long getPid(Process process){
        if(process == null){
            return null;
        }
        try{
            Method method = Process.class.getMethod("pid");
            Object pid = method.invoke(process);
            return ((Number) pid).longValue();
        }catch (Exception ignored){

        }
        try{
            Field field = process.getClass().getDeclaredField("pid");
            field.setAccessible(true);
            return ((Number) field.get(process)).longValue();
        }catch (Exception ignored){
            return null;
        }
    }

    @Data
    @Accessors(chain = true)
    private static class StreamRuntime{
        private String cameraCode;
        private String safeCode;
        private Process process;
        private String status;
        private String startedAt;
        private String stoppedAt;
        private String lastError;
        private String playUrl;
    }
}
