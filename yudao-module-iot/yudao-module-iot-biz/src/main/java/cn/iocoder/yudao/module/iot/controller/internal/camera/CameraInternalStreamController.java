package cn.iocoder.yudao.module.iot.controller.internal.camera;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.iot.framework.camera.config.IotCameraProperties;
import cn.iocoder.yudao.module.iot.service.camera.CameraInternalStreamService;
import cn.iocoder.yudao.module.iot.service.camera.CameraStreamClient;
import lombok.Data;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@RestController
@PermitAll
public class CameraInternalStreamController {

    private static final String INTERNAL_TOKEN_HEADER = "X-Internal-Token";

    @Resource
    private CameraInternalStreamService streamService;
    @Resource
    private IotCameraProperties cameraProperties;

    @PostMapping("/internal/stream/start")
    public CommonResult<CameraStreamClient.StreamStartRespDTO> start(
            @RequestHeader(value = INTERNAL_TOKEN_HEADER, required = false) String token,
            @Valid @RequestBody StreamStartReqDTO reqVO) {
        validateToken(token);
        return success(streamService.startStream(reqVO.getCameraCode(), reqVO.getRtspUrl(), reqVO.getTranscode()));
    }

    @PostMapping("/internal/stream/stop")
    public CommonResult<Boolean> stop(
            @RequestHeader(value = INTERNAL_TOKEN_HEADER,required = false) String token,
            @Valid @RequestBody StreamStopReqDTO reqVO){
        validateToken(token);
        return success(streamService.stopStream(reqVO.getCameraCode()));
    }



    @GetMapping("/internal/stream/status")
    public CommonResult<CameraStreamClient.StreamStatusRespDTO> status(
            @RequestHeader(value = INTERNAL_TOKEN_HEADER, required = false) String token,
            @RequestParam("cameraCode") String cameraCode){
        validateToken(token);
        return success(streamService.getStreamStatus(cameraCode));
    }

    @GetMapping("/hls/{cameraCode}/{fileName:.+}")
    public ResponseEntity<org.springframework.core.io.Resource> hls(@PathVariable String cameraCode,@PathVariable String fileName){
        try{
            Path root = Paths.get(cameraProperties.getHlsRootPath()).toAbsolutePath().normalize();
            Path file = root.resolve(cameraCode).resolve(fileName).normalize();
            if(!file.startsWith(root) || !Files.exists(file)){
                return ResponseEntity.notFound().build();
            }

            MediaType mediaType = fileName.endsWith(".m3u8")
                    ?MediaType.valueOf("application/vnd.apple.mpegurl")
                    :MediaType.valueOf("video/MP2T");
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(new FileSystemResource(file));
        }catch (Exception e){
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    private void validateToken(String token){
        if(!StrUtil.equals(token,cameraProperties.getStreamServiceToken())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid internal token");
        }
    }



    @Data
    public static  class StreamStartReqDTO{
            @NotBlank
            private String cameraCode;

            @NotBlank
            private String rtspUrl;

            private Boolean transcode;
    }

    @Data
    public static class StreamStopReqDTO{

        @NotBlank
        private String cameraCode;
    }
}
