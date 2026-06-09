package cn.iocoder.yudao.module.iot.service.camera;

import cn.iocoder.yudao.module.iot.dal.dataobject.camera.IotCameraRecordDO;

public interface CameraRecordStorageService {

    /**
     * Build the URL used by the frontend to play this record.
     * Local storage returns the backend streaming endpoint; object storage returns the object URL.
     */
    String buildFileUrl(IotCameraRecordDO record);

    /**
     * Handle storage after recording is finished.
     * Local storage only verifies the file is accessible; object storage can upload the file here.
     */
    CameraRecordStorageResult upload(IotCameraRecordDO record);

}
