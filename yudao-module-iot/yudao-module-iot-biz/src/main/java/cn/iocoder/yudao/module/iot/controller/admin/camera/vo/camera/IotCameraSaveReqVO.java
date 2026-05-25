package cn.iocoder.yudao.module.iot.controller.admin.camera.vo.camera;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.validation.InEnum;
import cn.iocoder.yudao.module.iot.enums.camera.IotCameraStreamProtocolEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.*;

@Schema(description = "管理后台 - IoT 摄像头新增/修改 Request VO")
@Data
public class IotCameraSaveReqVO {

    @Schema(description = "摄像头编号", example = "1")
    private Long id;

    @Schema(description = "摄像头名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "深圳大学实验室-摄像头01")
    @NotEmpty(message = "摄像头名称不能为空")
    @Size(max = 64, message = "摄像头名称不能超过 64 个字符")
    private String name;

    @Schema(description = "摄像头编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "SZU-LAB-CAM-001")
    @NotEmpty(message = "摄像头编码不能为空")
    @Size(max = 64, message = "摄像头编码不能超过 64 个字符")
    private String code;

    @Schema(description = "绑定部门 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    @NotNull(message = "绑定部门不能为空")
    private Long deptId;

    @Schema(description = "安装位置", example = "实验室入口")
    @Size(max = 128, message = "安装位置不能超过 128 个字符")
    private String location;

    @Schema(description = "RTSP 地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "rtsp://user:password@127.0.0.1/stream1")
    @NotEmpty(message = "RTSP 地址不能为空")
    @Size(max = 512, message = "RTSP 地址不能超过 512 个字符")
    private String rtspUrl;

    @Schema(description = "播放协议：1 HLS，2 WebRTC，3 FLV", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "播放协议不能为空")
    @InEnum(IotCameraStreamProtocolEnum.class)
    private Integer streamProtocol;

    @Schema(description = "ZLMediaKit 应用名", example = "live")
    @Size(max = 64, message = "应用名不能超过 64 个字符")
    private String streamApp;

    @Schema(description = "ZLMediaKit 流 ID", example = "SZU-LAB-CAM-001")
    @Size(max = 128, message = "流 ID 不能超过 128 个字符")
    private String streamId;

    @Schema(description = "是否启用录像", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    @NotNull(message = "是否启用录像不能为空")
    private Boolean recordEnabled;

    @Schema(description = "录像保留天数", requiredMode = Schema.RequiredMode.REQUIRED, example = "7")
    @NotNull(message = "录像保留天数不能为空")
    @Min(value = 1, message = "录像保留天数不能小于 1")
    private Integer recordRetentionDays;

    @Schema(description = "状态：0 正常，1 停用", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @NotNull(message = "状态不能为空")
    @InEnum(CommonStatusEnum.class)
    private Integer status;

    @Schema(description = "排序", example = "1")
    private Integer sort;

    @Schema(description = "备注")
    @Size(max = 255, message = "备注不能超过 255 个字符")
    private String remark;

}
