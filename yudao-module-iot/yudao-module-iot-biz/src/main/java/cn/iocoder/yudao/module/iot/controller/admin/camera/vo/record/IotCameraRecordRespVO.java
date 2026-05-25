package cn.iocoder.yudao.module.iot.controller.admin.camera.vo.record;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - IoT 摄像头录像 Response VO")
@Data
public class IotCameraRecordRespVO {

    @Schema(description = "录像编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "摄像头编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long cameraId;

    @Schema(description = "摄像头名称", example = "深圳大学实验室-摄像头01")
    private String cameraName;

    @Schema(description = "绑定部门 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private Long deptId;

    @Schema(description = "录像开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime startTime;

    @Schema(description = "录像结束时间")
    private LocalDateTime endTime;

    @Schema(description = "录像文件路径或对象存储 Key", requiredMode = Schema.RequiredMode.REQUIRED)
    private String filePath;

    @Schema(description = "录像播放或下载地址")
    private String fileUrl;

    @Schema(description = "文件大小，字节")
    private Long fileSize;

    @Schema(description = "时长，秒")
    private Integer duration;

    @Schema(description = "状态：0 录制中，1 已完成，2 失败", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "失败原因")
    private String errorMsg;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
