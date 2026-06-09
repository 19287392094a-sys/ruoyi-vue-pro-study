package cn.iocoder.yudao.module.iot.controller.admin.camera.vo.record;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Admin - IoT camera record response")
@Data
public class IotCameraRecordRespVO {

    @Schema(description = "Record id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "Camera id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long cameraId;

    @Schema(description = "Camera name", example = "Camera 1")
    private String cameraName;

    @Schema(description = "Department id", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private Long deptId;

    @Schema(description = "Record start time", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime startTime;

    @Schema(description = "Record end time")
    private LocalDateTime endTime;

    @Schema(description = "Local file path or object storage key", requiredMode = Schema.RequiredMode.REQUIRED)
    private String filePath;

    @Schema(description = "Playback or download URL")
    private String fileUrl;

    @Schema(description = "File size in bytes")
    private Long fileSize;

    @Schema(description = "Duration in seconds")
    private Integer duration;

    @Schema(description = "Record status: 0 recording, 1 finished, 2 failed", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "Failure reason")
    private String errorMsg;

    @Schema(description = "Upload status: 0 pending, 1 success, 2 failed")
    private Integer uploadStatus;

    @Schema(description = "Upload finished time")
    private LocalDateTime uploadTime;

    @Schema(description = "Upload failure reason")
    private String uploadErrorMsg;

    @Schema(description = "Create time", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
