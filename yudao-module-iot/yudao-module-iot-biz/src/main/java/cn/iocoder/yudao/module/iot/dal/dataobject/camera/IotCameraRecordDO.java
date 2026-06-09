package cn.iocoder.yudao.module.iot.dal.dataobject.camera;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import cn.iocoder.yudao.module.iot.enums.camera.IotCameraRecordStatusEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * IoT 摄像头录像记录 DO
 */
@TableName("iot_camera_record")
@KeySequence("iot_camera_record_seq")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IotCameraRecordDO extends TenantBaseDO {

    /**
     * 录像编号
     */
    @TableId
    private Long id;
    /**
     * 摄像头编号
     */
    private Long cameraId;
    /**
     * 部门 ID，冗余用于查询和权限过滤
     */
    private Long deptId;
    /**
     * 录像开始时间
     */
    private LocalDateTime startTime;
    /**
     * 录像结束时间
     */
    private LocalDateTime endTime;
    /**
     * 录像文件路径或对象存储 Key
     */
    private String filePath;
    /**
     * 录像播放或下载地址
     */
    private String fileUrl;
    /**
     * 文件大小，字节
     */
    private Long fileSize;
    /**
     * 时长，秒
     */
    private Integer duration;
    /**
     * 状态
     *
     * 枚举 {@link IotCameraRecordStatusEnum}
     */
    private Integer status;
    /**
     * 失败原因
     */
    private String errorMsg;

    /**
     * 上传状态：0未上传 1上传成功 2上传失败
     */
    private Integer uploadStatus;

    /**
     * 上传完成时间
     */
    private LocalDateTime uploadTime;

    /**
     * 上传失败原因
     */
    private String uploadErrorMsg;

}
