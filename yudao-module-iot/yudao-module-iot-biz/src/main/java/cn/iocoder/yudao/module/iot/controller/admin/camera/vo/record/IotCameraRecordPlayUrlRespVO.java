package cn.iocoder.yudao.module.iot.controller.admin.camera.vo.record;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Schema(description = "管理后台 - IoT 摄像头录像播放地址 Response VO")
@Data
@Accessors(chain = true)
public class IotCameraRecordPlayUrlRespVO {

    @Schema(description = "录像编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long recordId;

    @Schema(description = "摄像头编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long cameraId;

    @Schema(description = "播放地址")
    private String playUrl;

    @Schema(description = "文件地址")
    private String fileUrl;

    @Schema(description = "录像状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

}
