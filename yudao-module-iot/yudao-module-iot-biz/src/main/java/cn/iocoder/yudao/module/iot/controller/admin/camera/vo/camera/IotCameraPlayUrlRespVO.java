package cn.iocoder.yudao.module.iot.controller.admin.camera.vo.camera;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Schema(description = "管理后台 - IoT 摄像头播放地址 Response VO")
@Data
@Accessors(chain = true)
public class IotCameraPlayUrlRespVO {

    @Schema(description = "摄像头编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long cameraId;

    @Schema(description = "摄像头名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "深圳大学实验室-摄像头01")
    private String name;

    @Schema(description = "播放协议", requiredMode = Schema.RequiredMode.REQUIRED, example = "HLS")
    private String protocol;

    @Schema(description = "播放地址", requiredMode = Schema.RequiredMode.REQUIRED)
    private String playUrl;

    @Schema(description = "ZLMediaKit 应用名", requiredMode = Schema.RequiredMode.REQUIRED, example = "live")
    private String streamApp;

    @Schema(description = "ZLMediaKit 流 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "SZU-LAB-CAM-001")
    private String streamId;

}
