package cn.iocoder.yudao.module.iot.controller.admin.camera;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.http.HttpUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.iot.controller.admin.camera.vo.record.IotCameraRecordPageReqVO;
import cn.iocoder.yudao.module.iot.controller.admin.camera.vo.record.IotCameraRecordPlayUrlRespVO;
import cn.iocoder.yudao.module.iot.controller.admin.camera.vo.record.IotCameraRecordRespVO;
import cn.iocoder.yudao.module.iot.dal.dataobject.camera.IotCameraRecordDO;
import cn.iocoder.yudao.module.iot.service.camera.CameraRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "Admin - IoT camera record")
@RestController
@RequestMapping("/iot/camera-record")
@Validated
public class CameraRecordController {

    private static final int BUFFER_SIZE = 8192;

    @Resource
    private CameraRecordService cameraRecordService;

    @GetMapping("/page")
    @Operation(summary = "Get camera record page")
    @PreAuthorize("@ss.hasPermission('iot:camera-record:query')")
    public CommonResult<PageResult<IotCameraRecordRespVO>> getCameraRecordPage(
            @Valid IotCameraRecordPageReqVO pageReqVO) {
        PageResult<IotCameraRecordDO> pageResult = cameraRecordService.getCameraRecordPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, IotCameraRecordRespVO.class));
    }

    @GetMapping("/play-url")
    @Operation(summary = "Get camera record playback URL")
    @Parameter(name = "id", description = "Record id", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('iot:camera-record:query')")
    public CommonResult<IotCameraRecordPlayUrlRespVO> getCameraRecordPlayUrl(@RequestParam("id") Long id) {
        return success(cameraRecordService.getCameraRecordPlayUrl(id));
    }

    @GetMapping("/file")
    @Operation(summary = "Access camera record file")
    @Parameter(name = "id", description = "Record id", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('iot:camera-record:query')")
    public void getCameraRecordFile(@RequestParam("id") Long id,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws IOException {
        File file = cameraRecordService.getCameraRecordFile(id);
        writeVideoFile(request, response, file);
    }

    private void writeVideoFile(HttpServletRequest request, HttpServletResponse response, File file) throws IOException {
        long fileLength = file.length();

        response.setContentType("video/mp4");
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("Content-Disposition", "inline;filename=" + HttpUtils.encodeUtf8(file.getName()));

        String rangeHeader = request.getHeader("Range");
        if (rangeHeader == null || !rangeHeader.startsWith("bytes=")) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setHeader("Content-Length", String.valueOf(fileLength));
            writeFileRange(file, response, 0, fileLength - 1);
            return;
        }

        long[] range = parseRange(rangeHeader, fileLength);
        if (range == null) {
            response.setStatus(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
            response.setHeader("Content-Range", "bytes */" + fileLength);
            return;
        }

        long start = range[0];
        long end = range[1];
        long contentLength = end - start + 1;

        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
        response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + fileLength);
        response.setHeader("Content-Length", String.valueOf(contentLength));

        writeFileRange(file, response, start, end);
    }

    private long[] parseRange(String rangeHeader, long fileLength) {
        String range = rangeHeader.substring("bytes=".length()).trim();
        int commaIndex = range.indexOf(",");
        if (commaIndex >= 0) {
            range = range.substring(0, commaIndex);
        }

        int dashIndex = range.indexOf("-");
        if (dashIndex < 0) {
            return null;
        }

        String startText = range.substring(0, dashIndex).trim();
        String endText = range.substring(dashIndex + 1).trim();

        try {
            long start;
            long end;

            if (startText.isEmpty()) {
                if (endText.isEmpty()) {
                    return null;
                }
                long suffixLength = Long.parseLong(endText);
                if (suffixLength <= 0) {
                    return null;
                }
                start = Math.max(fileLength - suffixLength, 0);
                end = fileLength - 1;
            } else {
                start = Long.parseLong(startText);
                end = endText.isEmpty() ? fileLength - 1 : Long.parseLong(endText);
            }

            if (start < 0 || end < start || start >= fileLength) {
                return null;
            }

            return new long[]{start, Math.min(end, fileLength - 1)};
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private void writeFileRange(File file, HttpServletResponse response, long start, long end) throws IOException {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
            OutputStream outputStream = response.getOutputStream();
            randomAccessFile.seek(start);

            byte[] buffer = new byte[BUFFER_SIZE];
            long remaining = end - start + 1;

            while (remaining > 0) {
                int readLength = randomAccessFile.read(buffer, 0, (int) Math.min(buffer.length, remaining));
                if (readLength == -1) {
                    break;
                }
                outputStream.write(buffer, 0, readLength);
                remaining -= readLength;
            }

            outputStream.flush();
        }
    }

}
