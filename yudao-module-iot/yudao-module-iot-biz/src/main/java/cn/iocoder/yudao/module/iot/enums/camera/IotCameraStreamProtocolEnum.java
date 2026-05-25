package cn.iocoder.yudao.module.iot.enums.camera;

import cn.iocoder.yudao.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 摄像头播放协议枚举
 */
@Getter
@AllArgsConstructor
public enum IotCameraStreamProtocolEnum implements ArrayValuable<Integer> {

    HLS(1, "HLS"),
    WEBRTC(2, "WebRTC"),
    FLV(3, "FLV");

    public static final Integer[] ARRAYS = Arrays.stream(values())
            .map(IotCameraStreamProtocolEnum::getProtocol).toArray(Integer[]::new);

    private final Integer protocol;
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

    public static String getNameByProtocol(Integer protocol) {
        return Arrays.stream(values())
                .filter(item -> item.getProtocol().equals(protocol))
                .map(IotCameraStreamProtocolEnum::getName)
                .findFirst()
                .orElse(HLS.getName());
    }

}
