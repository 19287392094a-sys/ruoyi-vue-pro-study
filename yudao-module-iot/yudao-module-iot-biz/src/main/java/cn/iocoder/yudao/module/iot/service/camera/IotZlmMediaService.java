package cn.iocoder.yudao.module.iot.service.camera;

import cn.iocoder.yudao.module.iot.dal.dataobject.camera.IotCameraDO;

/**
 * ZLMediaKit 适配 Service
 */
public interface IotZlmMediaService {

    /**
     * 是否启用真实 ZLMediaKit 调用
     */
    Boolean isEnabled();

    /**
     * 添加或复用 RTSP 拉流代理
     */
    void addStreamProxy(IotCameraDO camera);

    /**
     * 删除拉流代理
     */
    void delStreamProxy(IotCameraDO camera);

    /**
     * 查询流是否在线
     */
    Boolean isStreamOnline(IotCameraDO camera);

    /**
     * 开始录像
     */
    void startRecord(IotCameraDO camera);

    /**
     * 停止录像
     */
    void stopRecord(IotCameraDO camera);

    /**
     * 查询是否正在录像
     */
    Boolean isRecording(IotCameraDO camera);

}
