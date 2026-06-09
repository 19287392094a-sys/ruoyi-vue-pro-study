package cn.iocoder.yudao.module.iot.service.camera;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CameraRecordStorageResult {

    private String fileUrl;

}