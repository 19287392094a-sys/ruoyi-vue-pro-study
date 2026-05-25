package cn.iocoder.yudao.module.iot.controller.admin.camera.vo.camera;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.validation.InEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - IoT 摄像头分页 Request VO")
@Data
public class IotCameraPageReqVO extends PageParam {

    @Schema(description = "绑定部门 ID", example = "100")
    private Long deptId;

    @Schema(description = "摄像头名称", example = "实验室")
    private String name;

    @Schema(description = "摄像头编码", example = "SZU-LAB-CAM-001")
    private String code;

    @Schema(description = "状态：0 正常，1 停用", example = "0")
    @InEnum(CommonStatusEnum.class)
    private Integer status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
