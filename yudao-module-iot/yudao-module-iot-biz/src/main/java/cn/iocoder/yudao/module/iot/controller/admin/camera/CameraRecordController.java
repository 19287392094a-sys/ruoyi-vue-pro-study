package cn.iocoder.yudao.module.iot.controller.admin.camera;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
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

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - IoT 摄像头录像")
@RestController
@RequestMapping("/iot/camera-record")
@Validated
public class CameraRecordController {

    private static final int BUFFER_SIZE = 8192;

    @Resource
    private CameraRecordService cameraRecordService;

    @GetMapping("/page")
    @Operation(summary = "获得摄像头录像分页")
    @PreAuthorize("@ss.hasPermission('iot:camera-record:query')")
    public CommonResult<PageResult<IotCameraRecordRespVO>> getCameraRecordPage(
            @Valid IotCameraRecordPageReqVO pageReqVO) {
        PageResult<IotCameraRecordDO> pageResult = cameraRecordService.getCameraRecordPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, IotCameraRecordRespVO.class));
    }

    @GetMapping("/play-url")
    @Operation(summary = "获得摄像头录像播放地址")
    @Parameter(name = "id", description = "录像编号", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('iot:camera-record:query')")
    public CommonResult<IotCameraRecordPlayUrlRespVO> getCameraRecordPlayUrl(@RequestParam("id") Long id) {
        return success(cameraRecordService.getCameraRecordPlayUrl(id));
    }

    @GetMapping("/file")
    @Operation(summary = "访问摄像头录像文件")
    @Parameter(name = "id", description = "录像编号", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('iot:camera-record:query')")
    public void getCameraRecordFile(@RequestParam("id") Long id,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws IOException {
        File file = cameraRecordService.getCameraRecordFile(id);
        writeVideoFile(request, response, file);
    }
}
