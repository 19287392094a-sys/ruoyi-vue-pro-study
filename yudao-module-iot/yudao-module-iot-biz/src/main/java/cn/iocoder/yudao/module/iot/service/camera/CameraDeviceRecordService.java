package cn.iocoder.yudao.module.iot.service.camera;

public interface CameraDeviceRecordService {
    Long startRecord(Long cameraId);

    void stopRecord(Long cameraId);
}
