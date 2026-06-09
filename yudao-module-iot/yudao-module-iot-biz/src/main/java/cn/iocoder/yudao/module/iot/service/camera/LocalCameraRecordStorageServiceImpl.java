package cn.iocoder.yudao.module.iot.service.camera;

import cn.iocoder.yudao.module.iot.dal.dataobject.camera.IotCameraRecordDO;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class LocalCameraRecordStorageServiceImpl implements CameraRecordStorageService {

    @Override
    public String buildFileUrl(IotCameraRecordDO record) {
        return "/admin-api/iot/camera-record/file?id=" + record.getId();
    }

    @Override
    public CameraRecordStorageResult upload(IotCameraRecordDO record) {
        if (record.getFilePath() == null) {
            throw new IllegalStateException("Record file path is blank");
        }
        File file = new File(record.getFilePath());
        if (!file.exists() || !file.isFile() || !file.canRead() || file.length() <= 0) {
            throw new IllegalStateException("Record file is not accessible: " + record.getFilePath());
        }
        return new CameraRecordStorageResult()
                .setFileUrl(buildFileUrl(record));
    }

}
