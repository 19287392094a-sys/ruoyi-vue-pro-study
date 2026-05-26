package cn.iocoder.yudao.module.iot.framework.camera.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * IoT 摄像头配置
 */
@Component
@ConfigurationProperties(prefix = "yudao.iot.camera")
@Data
public class IotCameraProperties {

    /**
     * 是否启用 ZLMediaKit 真实接口调用。Demo 阶段默认关闭，仅返回播放地址。
     */
    private Boolean zlmEnabled = false;

    /**
     * ZLMediaKit HTTP API 地址
     */
    private String zlmBaseUrl = "http://127.0.0.1:8080";

    /**
     * ZLMediaKit API 密钥
     */
    private String zlmSecret;

    /**
     * 前端可访问的流媒体公开地址
     */
    private String hlsPublicBaseUrl = "http://127.0.0.1:8080";

    /**
     * 默认应用名
     */
    private String defaultApp = "live";

    /**
     * 录像保存根目录
     */
    private String recordRootPath = "/data/camera-records";

    /**
     * 流服务基础地址
     */
    private String streamServiceBaseUrl = "http://127.0.0.1:18080";

    /**
     * 流服务验证
     */
    private String streamServiceToken = "dev-internal-token";


}
