package cn.iocoder.yudao.module.iot.dal.dataobject.camera;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import cn.iocoder.yudao.module.iot.enums.camera.IotCameraStreamProtocolEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * IoT 摄像头 DO
 */
@TableName("iot_camera")
@KeySequence("iot_camera_seq")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IotCameraDO extends TenantBaseDO {

    /**
     * 摄像头编号
     */
    @TableId
    private Long id;
    /**
     * 摄像头名称
     */
    private String name;
    /**
     * 摄像头编码
     */
    private String code;
    /**
     * 绑定部门 ID
     */
    private Long deptId;
    /**
     * 安装位置
     */
    private String location;
    /**
     * RTSP 地址
     */
    private String rtspUrl;
    /**
     * 播放协议
     *
     * 枚举 {@link IotCameraStreamProtocolEnum}
     */
    private Integer streamProtocol;
    /**
     * ZLMediaKit 应用名
     */
    private String streamApp;
    /**
     * ZLMediaKit 流 ID
     */
    private String streamId;
    /**
     * 是否启用录像
     */
    private Boolean recordEnabled;
    /**
     * 录像保留天数
     */
    private Integer recordRetentionDays;
    /**
     * 状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 备注
     */
    private String remark;

}
