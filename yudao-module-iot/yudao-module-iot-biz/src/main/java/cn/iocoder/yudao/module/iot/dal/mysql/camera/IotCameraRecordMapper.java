package cn.iocoder.yudao.module.iot.dal.mysql.camera;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.iot.controller.admin.camera.vo.record.IotCameraRecordPageReqVO;
import cn.iocoder.yudao.module.iot.dal.dataobject.camera.IotCameraRecordDO;
import cn.iocoder.yudao.module.iot.enums.camera.IotCameraRecordStatusEnum;
import org.apache.ibatis.annotations.Mapper;

/**
 * IoT 摄像头录像记录 Mapper
 */
@Mapper
public interface IotCameraRecordMapper extends BaseMapperX<IotCameraRecordDO> {

    default PageResult<IotCameraRecordDO> selectPage(IotCameraRecordPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<IotCameraRecordDO>()
                .eqIfPresent(IotCameraRecordDO::getDeptId, reqVO.getDeptId())
                .eqIfPresent(IotCameraRecordDO::getCameraId, reqVO.getCameraId())
                .eqIfPresent(IotCameraRecordDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(IotCameraRecordDO::getStartTime, reqVO.getStartTime())
                .orderByDesc(IotCameraRecordDO::getStartTime)
                .orderByDesc(IotCameraRecordDO::getId));
    }

    default IotCameraRecordDO selectRecordingByCameraId(Long cameraId) {
        return selectOne(new LambdaQueryWrapperX<IotCameraRecordDO>()
                .eq(IotCameraRecordDO::getCameraId, cameraId)
                .eq(IotCameraRecordDO::getStatus, IotCameraRecordStatusEnum.RECORDING.getStatus())
                .orderByDesc(IotCameraRecordDO::getStartTime)
                .last("LIMIT 1"));
    }

}
