package cn.iocoder.yudao.module.iot.controller.admin.camera.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - 摄像头设备 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CameraDeviceRespVO {

    @Schema(description = "摄像头ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "7201")
    @ExcelProperty("摄像头ID")
    private Long id;

    @Schema(description = "摄像头编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("摄像头编码")
    private String code;

    @Schema(description = "摄像头名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @ExcelProperty("摄像头名称")
    private String name;

    @Schema(description = "所属部门ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15792")
    @ExcelProperty("所属部门ID")
    private Long deptId;

    @Schema(description = "安装位置", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("安装位置")
    private String installLocation;

    @Schema(description = "摄像头厂商", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("摄像头厂商")
    private String vendor;

    @Schema(description = "接入协议", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("接入协议")
    private String protocol;

    @Schema(description = "摄像头IP或域名", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("摄像头IP或域名")
    private String host;

    @Schema(description = "RTSP端口", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("RTSP端口")
    private Integer rtspPort;

    @Schema(description = "HTTP端口", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("HTTP端口")
    private Integer httpPort;

    @Schema(description = "摄像头用户名", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @ExcelProperty("摄像头用户名")
    private String username;

    @Schema(description = "主码流通道号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("主码流通道号")
    private String mainChannelNo;

    @Schema(description = "子码流通道号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("子码流通道号")
    private String subChannelNo;

    @Schema(description = "是否支持云台控制", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("是否支持云台控制")
    private Boolean ptzEnabled;

    @Schema(description = "云台控制通道号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("云台控制通道号")
    private Integer ptzChannel;

    @Schema(description = "流服务边缘服务编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("流服务边缘服务编码")
    private String streamServiceCode;

    @Schema(description = "是否开启录像", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("是否开启录像")
    private Boolean recordEnabled;

    @Schema(description = "录像保留天数", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("录像保留天数")
    private Integer retentionDays;

    @Schema(description = "在线状态：0未知 1在线 2离线", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("在线状态：0未知 1在线 2离线")
    private Integer onlineStatus;

    @Schema(description = "设备状态：0启用 1停用", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("设备状态：0启用 1停用")
    private Integer status;

    @Schema(description = "最近在线时间")
    @ExcelProperty("最近在线时间")
    private LocalDateTime lastOnlineTime;

    @Schema(description = "备注", requiredMode = Schema.RequiredMode.REQUIRED, example = "随便")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}