package cn.iocoder.yudao.module.iot.service.camera;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.module.iot.dal.dataobject.camera.IotCameraDO;
import cn.iocoder.yudao.module.iot.framework.camera.config.IotCameraProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ZLMediaKit 适配 Service 实现
 */
@Service
@Slf4j
public class IotZlmMediaServiceImpl implements IotZlmMediaService {

    private static final String DEFAULT_VHOST = "__defaultVhost__";

    @Resource
    private IotCameraProperties cameraProperties;

    @Override
    public Boolean isEnabled() {
        return BooleanUtil.isTrue(cameraProperties.getZlmEnabled());
    }

    @Override
    public void addStreamProxy(IotCameraDO camera) {
        if (!isEnabled()) {
            return;
        }
        Map<String, Object> params = baseParams(camera);
        params.put("url", camera.getRtspUrl());
        params.put("enable_hls", 1);
        params.put("enable_mp4", 0);
        request("addStreamProxy", params);
    }

    @Override
    public void delStreamProxy(IotCameraDO camera) {
        if (!isEnabled()) {
            return;
        }
        request("delStreamProxy", baseParams(camera));
    }

    @Override
    public Boolean isStreamOnline(IotCameraDO camera) {
        if (!isEnabled()) {
            return false;
        }
        Map<String, Object> response = request("getMediaList", baseParams(camera));
        Object data = response.get("data");
        return data instanceof List && !((List<?>) data).isEmpty();
    }

    @Override
    public void startRecord(IotCameraDO camera) {
        if (!isEnabled()) {
            return;
        }
        Map<String, Object> params = baseParams(camera);
        params.put("type", 1);
        params.put("customized_path", cameraProperties.getRecordRootPath());
        request("startRecord", params);
    }

    @Override
    public void stopRecord(IotCameraDO camera) {
        if (!isEnabled()) {
            return;
        }
        Map<String, Object> params = baseParams(camera);
        params.put("type", 1);
        request("stopRecord", params);
    }

    @Override
    public Boolean isRecording(IotCameraDO camera) {
        if (!isEnabled()) {
            return false;
        }
        Map<String, Object> params = baseParams(camera);
        params.put("type", 1);
        Map<String, Object> response = request("isRecording", params);
        Object status = response.get("status");
        return BooleanUtil.toBoolean(String.valueOf(status));
    }

    private Map<String, Object> baseParams(IotCameraDO camera) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("secret", StrUtil.nullToDefault(cameraProperties.getZlmSecret(), ""));
        params.put("vhost", DEFAULT_VHOST);
        params.put("app", camera.getStreamApp());
        params.put("stream", camera.getStreamId());
        return params;
    }

    private Map<String, Object> request(String api, Map<String, Object> params) {
        String url = StrUtil.removeSuffix(cameraProperties.getZlmBaseUrl(), "/") + "/index/api/" + api;
        String body = HttpUtil.get(url, params);
        Map<String, Object> response = JsonUtils.parseObjectQuietly(body, new TypeReference<Map<String, Object>>() {});
        if (response == null) {
            throw new IllegalStateException("ZLMediaKit 返回结果无法解析：" + body);
        }
        Object code = response.get("code");
        if (code != null && !"0".equals(String.valueOf(code))) {
            log.warn("[request][ZLMediaKit api({}) params({}) response({})]", api, params, body);
            throw new IllegalStateException("ZLMediaKit 调用失败：" + body);
        }
        return response;
    }

}
