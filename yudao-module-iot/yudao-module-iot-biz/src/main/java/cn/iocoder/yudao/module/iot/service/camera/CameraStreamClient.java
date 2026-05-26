package cn.iocoder.yudao.module.iot.service.camera;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.module.iot.framework.camera.config.IotCameraProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.Map;



/**
 * FFmpeg 摄像头流服务 Client
 */
@Component
public class CameraStreamClient {
    private static  final  String INTERNAL_TOKEN_HEADER = "X-Internal-Token";

    @Resource
    private IotCameraProperties cameraProperties;

    /**
     * 启动实时流
     * @param cameraCode 摄像头编码
     * @param rtspUrl RTSP拉流地址
     * @param transcode 是否转码
     * @return 实时流启动结果
     */
    public StreamStartRespDTO startStream(String cameraCode,String rtspUrl,Boolean transcode){
        Map<String,Object> body = new LinkedHashMap<>();
        body.put("cameraCode",cameraCode);
        body.put("rtspUrl",rtspUrl);
        body.put("transcode",Boolean.TRUE.equals(transcode));

        Map<String,Object> data = post("/internal/stream/start",body);

        return new StreamStartRespDTO()
                .setCameraCode(MapUtil.getStr(data,"cameraCode"))
                .setStatus(MapUtil.getStr(data,"status"))
                .setPid(MapUtil.getLong(data,"pid"))
                .setPlayUrl(toPublicPlayUrl(MapUtil.getStr(data,"playUrl")));
    }

    /**
     * 停止实时流
     * @param cameraCode 摄像头编码
     * @return 实时流停止结果
     */
    public Boolean stopStream(String cameraCode){
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("cameraCode",cameraCode);

        Object data = postRaw("/internal/stream/stop",body);
        return Boolean.TRUE.equals(data);
    }

    /**
     * 查询实时流状态
     * @param cameraCode 摄像头编码
     * @return 实时流状态信息
     */
    public StreamStatusRespDTO getStreamStatus(String cameraCode) {
        String url = buildUrl("/internal/stream/status")
                + "?cameraCode="
                + URLUtil.encodeQuery(cameraCode);

        Map<String, Object> data = get(url);

        return new StreamStatusRespDTO()
                .setCameraCode(MapUtil.getStr(data, "cameraCode"))
                .setStatus(MapUtil.getStr(data, "status"))
                .setPid(MapUtil.getLong(data, "pid"))
                .setStartedAt(MapUtil.getStr(data, "startedAt"))
                .setStoppedAt(MapUtil.getStr(data, "stoppedAt"))
                .setLastError(MapUtil.getStr(data, "lastError"));
    }

    /**
     * 向流服务发送POST请求，并要求返回的data必须是对象。
     * @param path 流服务接口路径，例如：/internal/stream/start
     * @param body 请求体参数
     * @return 流服务返回的data 对象
     */
    @SuppressWarnings("unchecked")
    private Map<String,Object> post(String path,Map<String,Object> body){
        Object data = postRaw(path,body);
        if(!(data instanceof Map)) {
            throw new IllegalStateException("流服务返回的data不是对象：" + data);
        }
        return (Map<String, Object>) data;
    }

    /**
     * 向流服务发送POST请求，并返回响应中的data字段。
     * @param path 流服务接口路径，例如/internal/stream/start
     * @param body 请求体参数，会被转换成JSON后发送
     * @return 流服务响应中的data字段
     */
    private Object postRaw(String path, Map<String, Object> body) {
        try (HttpResponse httpResponse = HttpRequest.post(buildUrl(path))
                .header(INTERNAL_TOKEN_HEADER, cameraProperties.getStreamServiceToken())
                .body(JsonUtils.toJsonString(body))
                .contentType("application/json")
                .execute()) {
            String response = httpResponse.body();
            return parseData(response);
        }
    }

    /**
     * 向流服务发送GET请求，并要求返回的data必须是对象
     * @param url 完整请求地址。
     * @return 流服务返回的data对象
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> get(String url) {
        try (HttpResponse httpResponse = HttpRequest.get(url)
                .header(INTERNAL_TOKEN_HEADER, cameraProperties.getStreamServiceToken())
                .execute()) {

            Object data = parseData(httpResponse.body());

            if (!(data instanceof Map)) {
                throw new IllegalStateException("流服务返回data不是对象：" + data);
            }

            return (Map<String, Object>) data;
        }
    }

    /**
     * 解析流服务响应JSON,并去除data字段
     * @param response 流服务返回的原始JSON，字符串
     * @return 响应中的data字段
     */
    private Object parseData(String response){
        Map<String,Object> result = JsonUtils.parseObjectQuietly(
                response, new TypeReference<Map<String, Object>>() {});
        if(result == null){
            throw new IllegalStateException("流服务返回结果无法解析： " + response);
        }

        Object code = result.get("code");
        if(code != null && !"0".equals(String.valueOf(code))){
            throw new IllegalStateException("流服务调用失败：" + response);
        }
        return result.get("data");
    }

    /**
     * 拼接流服务接口地址
     * @param path 接口路径
     * @return 完整的流服务接口地址
     */
    private String buildUrl(String path){
        return StrUtil.removeSuffix(cameraProperties.getStreamServiceBaseUrl(),"/") + path;
    }

    /**
     * 将流服务返回的播放地址转换为前端可访问的完整地址
     * @param playUrl 流服务返回的播放地址，可能是相对路径或完整路径
     * @return 前端可访问的播放地址
     */
    private String toPublicPlayUrl(String playUrl){
        if(StrUtil.isBlank(playUrl)){
            return null;
        }
        if(StrUtil.startWithIgnoreCase(playUrl,"http://")
                || StrUtil.startWithIgnoreCase(playUrl,"https://")){
            return playUrl;
        }

        return StrUtil.removeSuffix(cameraProperties.getHlsPublicBaseUrl(),"/")
                + "/"
                + StrUtil.removePrefix(playUrl,"/hls/");
    }

    /**
     * 启动实时流返回结果
     */
    @Data
    @Accessors(chain = true)
    public static class StreamStartRespDTO{

        /**
         * 摄像头编码
         */
        private String cameraCode;

        /**
         * 流状态
         */
        private String status;

        /**
         * FFmpeg 进程ID
         */
        private Long pid;

        /**
         * 前端播放地址
         */
        private String playUrl;
    }


    /**
     * 实时流状态响应DTO
     */
    @Data
    @Accessors(chain = true)
    public static class StreamStatusRespDTO{

        /**
         * 摄像头编码
         */
        private String cameraCode;

        /**
         * 流状态:starting,running,stopped
         */
        private String status;

        /**
         * FFmpeg 进程ID
         */
        private Long pid;

        /**
         * 启动时间
         */
        private String startedAt;

        /**
         * 停止时间
         */
        private String stoppedAt;

        /**
         * 最近错误信息
         */
        private String lastError;
    }
}
