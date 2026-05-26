package cn.iocoder.yudao.module.iot.service.camera;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.iot.controller.admin.camera.vo.*;
import cn.iocoder.yudao.module.iot.dal.dataobject.camera.CameraDeviceDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.iot.dal.mysql.camera.CameraDeviceMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.iot.enums.ErrorCodeConstants.*;

/**
 * 摄像头设备 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class CameraDeviceServiceImpl implements CameraDeviceService {

    @Resource
    private CameraStreamClient cameraStreamClient;

    @Resource
    private CameraDeviceMapper cameraDeviceMapper;

    @Override
    public Long createCameraDevice(CameraDeviceSaveReqVO createReqVO) {
        // 插入
        CameraDeviceDO cameraDevice = BeanUtils.toBean(createReqVO, CameraDeviceDO.class);
        cameraDeviceMapper.insert(cameraDevice);

        // 返回
        return cameraDevice.getId();
    }

    @Override
    public void updateCameraDevice(CameraDeviceSaveReqVO updateReqVO) {
        // 校验存在
        validateCameraDeviceExists(updateReqVO.getId());
        // 更新
        CameraDeviceDO updateObj = BeanUtils.toBean(updateReqVO, CameraDeviceDO.class);
        cameraDeviceMapper.updateById(updateObj);
    }

    @Override
    public void deleteCameraDevice(Long id) {
        // 校验存在
        validateCameraDeviceExists(id);
        // 删除
        cameraDeviceMapper.deleteById(id);
    }

    @Override
        public void deleteCameraDeviceListByIds(List<Long> ids) {
        // 删除
        cameraDeviceMapper.deleteByIds(ids);
        }


    private void validateCameraDeviceExists(Long id) {
        validateAndGetCameraDevice(id);
    }

    @Override
    public CameraDeviceDO getCameraDevice(Long id) {
        return cameraDeviceMapper.selectById(id);
    }

    @Override
    public PageResult<CameraDeviceDO> getCameraDevicePage(CameraDevicePageReqVO pageReqVO) {
        return cameraDeviceMapper.selectPage(pageReqVO);
    }

    @Override
    public CameraStreamClient.StreamStartRespDTO startStream(Long cameraId){
        CameraDeviceDO cameraDeviceDO = validateAndGetCameraDevice(cameraId);
        return cameraStreamClient.startStream(cameraDeviceDO.getCode(),cameraDeviceDO.getRtspUrl(),false);
    }

    @Override
    public Boolean stopStream(Long cameraId) {
        CameraDeviceDO cameraDeviceDO = validateAndGetCameraDevice(cameraId);
        return cameraStreamClient.stopStream(cameraDeviceDO.getCode());
    }

    @Override
    public CameraStreamClient.StreamStatusRespDTO getStreamStatus(Long cameraId) {
        CameraDeviceDO cameraDeviceDO = validateAndGetCameraDevice(cameraId);
        return cameraStreamClient.getStreamStatus(cameraDeviceDO.getCode());
    }

    private CameraDeviceDO validateAndGetCameraDevice(Long id){
        CameraDeviceDO cameraDeviceDO = cameraDeviceMapper.selectById(id);
        if(cameraDeviceDO == null){
            throw exception(CAMERA_DEVICE_NOT_EXISTS);
        }
        return cameraDeviceDO;
    }

}