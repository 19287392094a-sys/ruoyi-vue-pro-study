package cn.iocoder.yudao.module.iot.dal.dataobject.camera;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;

/**
 * 摄像头设备 DO
 *
 * @author 芋道源码
 */
@TableName("iot_camera_device")
@KeySequence("iot_camera_device_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CameraDeviceDO extends TenantBaseDO {

    /**
     * 摄像头ID
     */
    @TableId
    private Long id;
    /**
     * 摄像头编码
     */
    private String code;
    /**
     * 摄像头名称
     */
    private String name;
    /**
     * 所属部门ID
     */
    private Long deptId;
    /**
     * 安装位置
     */
    private String installLocation;
    /**
     * 摄像头厂商
     */
    private String vendor;
    /**
     * 接入协议
     */
    private String protocol;
    /**
     * RTSP地址
     */
    private String rtspUrl;
    /**
     * 摄像头IP或域名
     */
    private String host;
    /**
     * RTSP端口
     */
    private Integer rtspPort;
    /**
     * HTTP端口
     */
    private Integer httpPort;
    /**
     * 摄像头用户名
     */
    private String username;
    /**
     * 摄像头密码密文
     */
    private String passwordCipher;
    /**
     * 主码流通道号
     */
    private String mainChannelNo;
    /**
     * 子码流通道号
     */
    private String subChannelNo;
    /**
     * 是否支持云台控制
     */
    private Boolean ptzEnabled;
    /**
     * 云台控制通道号
     */
    private Integer ptzChannel;
    /**
     * 流服务边缘服务编码
     */
    private String streamServiceCode;
    /**
     * 是否开启录像
     */
    private Boolean recordEnabled;
    /**
     * 录像保留天数
     */
    private Integer retentionDays;
    /**
     * 在线状态：0未知 1在线 2离线
     */
    private Integer onlineStatus;
    /**
     * 设备状态：0启用 1停用
     */
    private Integer status;
    /**
     * 最近在线时间
     */
    private LocalDateTime lastOnlineTime;
    /**
     * 备注
     */
    private String remark;


}