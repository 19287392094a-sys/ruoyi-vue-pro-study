package cn.iocoder.yudao.module.iot.controller.admin.camera.vo.camera;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Schema(description = "管理后台 - IoT 摄像头状态 Response VO")
@Data
@Accessors(chain = true)
public class IotCameraStatusRespVO {

    @Schema(description = "摄像头编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long cameraId;

    @Schema(description = "摄像头名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "深圳大学实验室-摄像头01")
    private String name;

    @Schema(description = "是否启用", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private Boolean enabled;

    @Schema(description = "是否启用 ZLMediaKit 真实调用", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    private Boolean zlmEnabled;

    @Schema(description = "流是否在线", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    private Boolean streamOnline;

    @Schema(description = "是否正在录像", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    private Boolean recording;

}
