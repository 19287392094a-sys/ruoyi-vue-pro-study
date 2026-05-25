package cn.iocoder.yudao.module.iot.controller.admin.camera.vo.camera;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - IoT 摄像头 Response VO")
@Data
public class IotCameraRespVO {

    @Schema(description = "摄像头编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "摄像头名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "深圳大学实验室-摄像头01")
    private String name;

    @Schema(description = "摄像头编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "SZU-LAB-CAM-001")
    private String code;

    @Schema(description = "绑定部门 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private Long deptId;

    @Schema(description = "绑定部门名称", example = "深圳大学")
    private String deptName;

    @Schema(description = "安装位置", example = "实验室入口")
    private String location;

    @Schema(description = "脱敏后的 RTSP 地址")
    private String rtspUrl;

    @Schema(description = "播放协议：1 HLS，2 WebRTC，3 FLV", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer streamProtocol;

    @Schema(description = "ZLMediaKit 应用名", requiredMode = Schema.RequiredMode.REQUIRED, example = "live")
    private String streamApp;

    @Schema(description = "ZLMediaKit 流 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "SZU-LAB-CAM-001")
    private String streamId;

    @Schema(description = "是否启用录像", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    private Boolean recordEnabled;

    @Schema(description = "录像保留天数", requiredMode = Schema.RequiredMode.REQUIRED, example = "7")
    private Integer recordRetentionDays;

    @Schema(description = "状态：0 正常，1 停用", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Integer status;

    @Schema(description = "排序", example = "1")
    private Integer sort;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime updateTime;

}
