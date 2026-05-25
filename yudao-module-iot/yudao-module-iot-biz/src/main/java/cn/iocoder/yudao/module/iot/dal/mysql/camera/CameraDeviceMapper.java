package cn.iocoder.yudao.module.iot.dal.mysql.camera;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.iot.dal.dataobject.camera.CameraDeviceDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.iot.controller.admin.camera.vo.*;

/**
 * 摄像头设备 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface CameraDeviceMapper extends BaseMapperX<CameraDeviceDO> {

    default PageResult<CameraDeviceDO> selectPage(CameraDevicePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CameraDeviceDO>()
                .likeIfPresent(CameraDeviceDO::getCode, reqVO.getCode())
                .likeIfPresent(CameraDeviceDO::getName, reqVO.getName())
                .eqIfPresent(CameraDeviceDO::getDeptId, reqVO.getDeptId())
                .likeIfPresent(CameraDeviceDO::getInstallLocation, reqVO.getInstallLocation())
                .eqIfPresent(CameraDeviceDO::getOnlineStatus, reqVO.getOnlineStatus())
                .eqIfPresent(CameraDeviceDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(CameraDeviceDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CameraDeviceDO::getId));
    }

}