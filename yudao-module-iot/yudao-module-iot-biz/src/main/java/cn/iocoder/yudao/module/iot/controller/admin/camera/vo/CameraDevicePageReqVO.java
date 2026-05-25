package cn.iocoder.yudao.module.iot.controller.admin.camera.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 摄像头设备分页 Request VO")
@Data
public class CameraDevicePageReqVO extends PageParam {

    @Schema(description = "摄像头编码")
    private String code;

    @Schema(description = "摄像头名称", example = "李四")
    private String name;

    @Schema(description = "所属部门ID", example = "15792")
    private Long deptId;

    @Schema(description = "安装位置")
    private String installLocation;

    @Schema(description = "在线状态：0未知 1在线 2离线", example = "2")
    private Integer onlineStatus;

    @Schema(description = "设备状态：0启用 1停用", example = "2")
    private Integer status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}