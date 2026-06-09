package cn.iocoder.yudao.module.iot.controller.admin.camera.vo.record;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Schema(description = "Admin - IoT camera record playback URL response")
@Data
@Accessors(chain = true)
public class IotCameraRecordPlayUrlRespVO {

    @Schema(description = "Record id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long recordId;

    @Schema(description = "Camera id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long cameraId;

    @Schema(description = "Playback URL")
    private String playUrl;

    @Schema(description = "File URL")
    private String fileUrl;

    @Schema(description = "Record status", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "Upload status: 0 pending, 1 success, 2 failed")
    private Integer uploadStatus;

    @Schema(description = "Upload finished time")
    private LocalDateTime uploadTime;

    @Schema(description = "Upload failure reason")
    private String uploadErrorMsg;

}
