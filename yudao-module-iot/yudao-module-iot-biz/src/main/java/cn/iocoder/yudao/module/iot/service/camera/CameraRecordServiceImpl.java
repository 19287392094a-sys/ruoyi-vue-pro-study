package cn.iocoder.yudao.module.iot.service.camera;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.iot.controller.admin.camera.vo.record.IotCameraRecordPageReqVO;
import cn.iocoder.yudao.module.iot.controller.admin.camera.vo.record.IotCameraRecordPlayUrlRespVO;
import cn.iocoder.yudao.module.iot.dal.dataobject.camera.IotCameraRecordDO;
import cn.iocoder.yudao.module.iot.dal.mysql.camera.IotCameraRecordMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import cn.hutool.core.util.StrUtil;


import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.iot.enums.ErrorCodeConstants.CAMERA_RECORD_FILE_NOT_EXISTS;
import static cn.iocoder.yudao.module.iot.enums.ErrorCodeConstants.CAMERA_RECORD_NOT_EXISTS;

import javax.annotation.Resource;
import java.io.File;

@Service
@Validated
public class CameraRecordServiceImpl implements CameraRecordService{

    @Resource
    private IotCameraRecordMapper cameraRecordMapper;

    @Resource
    private CameraRecordStorageService cameraRecordStorageService;

    @Override
    public PageResult<IotCameraRecordDO> getCameraRecordPage(IotCameraRecordPageReqVO pageReqVO) {
        return cameraRecordMapper.selectPage(pageReqVO);
    }

    @Override
    public IotCameraRecordPlayUrlRespVO getCameraRecordPlayUrl(Long id) {
        IotCameraRecordDO record = validateAndGetCameraRecord(id);

        String playUrl = StrUtil.blankToDefault(
                record.getFileUrl(),
                cameraRecordStorageService.buildFileUrl(record)
        );

        return new IotCameraRecordPlayUrlRespVO()
                .setRecordId(record.getId())
                .setCameraId(record.getCameraId())
                .setPlayUrl(playUrl)
                .setFileUrl(record.getFileUrl())
                .setStatus(record.getStatus())
                .setUploadStatus(record.getUploadStatus())
                .setUploadTime(record.getUploadTime())
                .setUploadErrorMsg(record.getUploadErrorMsg());
    }

    @Override
    public File getCameraRecordFile(Long id) {
        IotCameraRecordDO record = validateAndGetCameraRecord(id);

        if (StrUtil.isBlank(record.getFilePath())) {
            throw exception(CAMERA_RECORD_FILE_NOT_EXISTS);
        }

        File file = new File(record.getFilePath());
        if (!file.exists() || !file.isFile() || !file.canRead() || file.length() <= 0) {
            throw exception(CAMERA_RECORD_FILE_NOT_EXISTS);
        }

        return file;
    }

    private IotCameraRecordDO validateAndGetCameraRecord(Long id) {
        IotCameraRecordDO record = cameraRecordMapper.selectById(id);
        if (record == null) {
            throw exception(CAMERA_RECORD_NOT_EXISTS);
        }
        return record;
    }
}
