package cn.iocoder.yudao.module.iot.dal.mysql.camera;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.iot.controller.admin.camera.vo.camera.IotCameraPageReqVO;
import cn.iocoder.yudao.module.iot.dal.dataobject.camera.IotCameraDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * IoT 摄像头 Mapper
 */
@Mapper
public interface IotCameraMapper extends BaseMapperX<IotCameraDO> {

    default PageResult<IotCameraDO> selectPage(IotCameraPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<IotCameraDO>()
                .eqIfPresent(IotCameraDO::getDeptId, reqVO.getDeptId())
                .likeIfPresent(IotCameraDO::getName, reqVO.getName())
                .likeIfPresent(IotCameraDO::getCode, reqVO.getCode())
                .eqIfPresent(IotCameraDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(IotCameraDO::getCreateTime, reqVO.getCreateTime())
                .orderByAsc(IotCameraDO::getSort)
                .orderByDesc(IotCameraDO::getId));
    }

    default List<IotCameraDO> selectListByDeptId(Long deptId) {
        return selectList(new LambdaQueryWrapperX<IotCameraDO>()
                .eq(IotCameraDO::getDeptId, deptId)
                .orderByAsc(IotCameraDO::getSort)
                .orderByDesc(IotCameraDO::getId));
    }

    default IotCameraDO selectByCode(String code) {
        return selectOne(IotCameraDO::getCode, code);
    }

    default IotCameraDO selectByStream(String streamApp, String streamId) {
        return selectOne(new LambdaQueryWrapperX<IotCameraDO>()
                .eq(IotCameraDO::getStreamApp, streamApp)
                .eq(IotCameraDO::getStreamId, streamId));
    }

}
