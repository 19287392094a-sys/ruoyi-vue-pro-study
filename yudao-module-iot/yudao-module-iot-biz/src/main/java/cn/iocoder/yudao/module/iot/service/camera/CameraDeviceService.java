package cn.iocoder.yudao.module.iot.service.camera;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.iot.controller.admin.camera.vo.*;
import cn.iocoder.yudao.module.iot.dal.dataobject.camera.CameraDeviceDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 摄像头设备 Service 接口
 *
 * @author 芋道源码
 */
public interface CameraDeviceService {

    /**
     * 创建摄像头设备
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCameraDevice(@Valid CameraDeviceSaveReqVO createReqVO);

    /**
     * 更新摄像头设备
     *
     * @param updateReqVO 更新信息
     */
    void updateCameraDevice(@Valid CameraDeviceSaveReqVO updateReqVO);

    /**
     * 删除摄像头设备
     *
     * @param id 编号
     */
    void deleteCameraDevice(Long id);

    /**
    * 批量删除摄像头设备
    *
    * @param ids 编号
    */
    void deleteCameraDeviceListByIds(List<Long> ids);

    /**
     * 获得摄像头设备
     *
     * @param id 编号
     * @return 摄像头设备
     */
    CameraDeviceDO getCameraDevice(Long id);

    /**
     * 获得摄像头设备分页
     *
     * @param pageReqVO 分页查询
     * @return 摄像头设备分页
     */
    PageResult<CameraDeviceDO> getCameraDevicePage(CameraDevicePageReqVO pageReqVO);

}