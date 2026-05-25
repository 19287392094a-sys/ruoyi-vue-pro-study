package cn.iocoder.yudao.module.iot.enums.camera;

import cn.iocoder.yudao.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 摄像头录像状态枚举
 */
@Getter
@AllArgsConstructor
public enum IotCameraRecordStatusEnum implements ArrayValuable<Integer> {

    RECORDING(0, "录制中"),
    COMPLETED(1, "已完成"),
    FAILED(2, "失败");

    public static final Integer[] ARRAYS = Arrays.stream(values())
            .map(IotCameraRecordStatusEnum::getStatus).toArray(Integer[]::new);

    private final Integer status;
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

}
