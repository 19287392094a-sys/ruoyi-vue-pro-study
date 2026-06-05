package cn.iocoder.yudao.module.iot.service.camera;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.iot.controller.admin.camera.vo.record.IotCameraRecordPageReqVO;
import cn.iocoder.yudao.module.iot.controller.admin.camera.vo.record.IotCameraRecordPlayUrlRespVO;
import cn.iocoder.yudao.module.iot.dal.dataobject.camera.IotCameraRecordDO;

import java.io.File;

public interface CameraRecordService {
    PageResult<IotCameraRecordDO> getCameraRecordPage(IotCameraRecordPageReqVO pageReqVO);

    IotCameraRecordPlayUrlRespVO getCameraRecordPlayUrl(Long id);

    File getCameraRecordFile(Long id);
}
