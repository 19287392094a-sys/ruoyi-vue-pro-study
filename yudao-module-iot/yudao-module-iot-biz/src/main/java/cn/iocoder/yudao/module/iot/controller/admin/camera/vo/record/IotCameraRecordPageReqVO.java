package cn.iocoder.yudao.module.iot.controller.admin.camera.vo.record;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.validation.InEnum;
import cn.iocoder.yudao.module.iot.enums.camera.IotCameraRecordStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - IoT 摄像头录像分页 Request VO")
@Data
public class IotCameraRecordPageReqVO extends PageParam {

    @Schema(description = "绑定部门 ID", example = "100")
    private Long deptId;

    @Schema(description = "摄像头编号", example = "1")
    private Long cameraId;

    @Schema(description = "录像状态：0 录制中，1 已完成，2 失败", example = "1")
    @InEnum(IotCameraRecordStatusEnum.class)
    private Integer status;

    @Schema(description = "录像开始时间范围")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] startTime;

}
