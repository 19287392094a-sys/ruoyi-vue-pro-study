package cn.iocoder.yudao.module.iot.controller.admin.camera.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 摄像头设备新增/修改 Request VO")
@Data
public class CameraDeviceSaveReqVO {

    @Schema(description = "摄像头ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "7201")
    private Long id;

    @Schema(description = "摄像头编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "摄像头编码不能为空")
    private String code;

    @Schema(description = "摄像头名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @NotEmpty(message = "摄像头名称不能为空")
    private String name;

    @Schema(description = "所属部门ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15792")
    @NotNull(message = "所属部门ID不能为空")
    private Long deptId;

    @Schema(description = "安装位置", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "安装位置不能为空")
    private String installLocation;

    @Schema(description = "摄像头厂商", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "摄像头厂商不能为空")
    private String vendor;

    @Schema(description = "接入协议", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "接入协议不能为空")
    private String protocol;

    @Schema(description = "RTSP地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn")
    @NotEmpty(message = "RTSP地址不能为空")
    private String rtspUrl;

    @Schema(description = "摄像头IP或域名", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "摄像头IP或域名不能为空")
    private String host;

    @Schema(description = "RTSP端口", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "RTSP端口不能为空")
    private Integer rtspPort;

    @Schema(description = "HTTP端口", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "HTTP端口不能为空")
    private Integer httpPort;

    @Schema(description = "摄像头用户名", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @NotEmpty(message = "摄像头用户名不能为空")
    private String username;

    @Schema(description = "主码流通道号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "主码流通道号不能为空")
    private String mainChannelNo;

    @Schema(description = "子码流通道号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "子码流通道号不能为空")
    private String subChannelNo;

    @Schema(description = "是否支持云台控制", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否支持云台控制不能为空")
    private Boolean ptzEnabled;

    @Schema(description = "云台控制通道号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "云台控制通道号不能为空")
    private Integer ptzChannel;

    @Schema(description = "流服务边缘服务编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "流服务边缘服务编码不能为空")
    private String streamServiceCode;

    @Schema(description = "是否开启录像", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否开启录像不能为空")
    private Boolean recordEnabled;

    @Schema(description = "录像保留天数", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "录像保留天数不能为空")
    private Integer retentionDays;

    @Schema(description = "设备状态：0启用 1停用", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "设备状态：0启用 1停用不能为空")
    private Integer status;

    @Schema(description = "备注", requiredMode = Schema.RequiredMode.REQUIRED, example = "随便")
    @NotEmpty(message = "备注不能为空")
    private String remark;

}