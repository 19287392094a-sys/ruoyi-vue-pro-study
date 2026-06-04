package cn.iocoder.yudao.module.iot.controller.admin.camera;

import cn.iocoder.yudao.module.iot.service.camera.CameraDeviceRecordService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.iot.controller.admin.camera.vo.*;
import cn.iocoder.yudao.module.iot.dal.dataobject.camera.CameraDeviceDO;
import cn.iocoder.yudao.module.iot.service.camera.CameraDeviceService;
import cn.iocoder.yudao.module.iot.service.camera.CameraStreamClient;

@Tag(name = "管理后台 - 摄像头设备")
@RestController
@RequestMapping("/iot/camera-device")
@Validated
public class CameraDeviceController {

    @Resource
    private CameraDeviceService cameraDeviceService;

    @Resource
    private CameraDeviceRecordService cameraDeviceRecordService;

    @PostMapping("/create")
    @Operation(summary = "创建摄像头设备")
    @PreAuthorize("@ss.hasPermission('iot:camera-device:create')")
    public CommonResult<Long> createCameraDevice(@Valid @RequestBody CameraDeviceSaveReqVO createReqVO) {
        return success(cameraDeviceService.createCameraDevice(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新摄像头设备")
    @PreAuthorize("@ss.hasPermission('iot:camera-device:update')")
    public CommonResult<Boolean> updateCameraDevice(@Valid @RequestBody CameraDeviceSaveReqVO updateReqVO) {
        cameraDeviceService.updateCameraDevice(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除摄像头设备")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('iot:camera-device:delete')")
    public CommonResult<Boolean> deleteCameraDevice(@RequestParam("id") Long id) {
        cameraDeviceService.deleteCameraDevice(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除摄像头设备")
                @PreAuthorize("@ss.hasPermission('iot:camera-device:delete')")
    public CommonResult<Boolean> deleteCameraDeviceList(@RequestParam("ids") List<Long> ids) {
        cameraDeviceService.deleteCameraDeviceListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得摄像头设备")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('iot:camera-device:query')")
    public CommonResult<CameraDeviceRespVO> getCameraDevice(@RequestParam("id") Long id) {
        CameraDeviceDO cameraDevice = cameraDeviceService.getCameraDevice(id);
        return success(BeanUtils.toBean(cameraDevice, CameraDeviceRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得摄像头设备分页")
    @PreAuthorize("@ss.hasPermission('iot:camera-device:query')")
    public CommonResult<PageResult<CameraDeviceRespVO>> getCameraDevicePage(@Valid CameraDevicePageReqVO pageReqVO) {
        PageResult<CameraDeviceDO> pageResult = cameraDeviceService.getCameraDevicePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, CameraDeviceRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出摄像头设备 Excel")
    @PreAuthorize("@ss.hasPermission('iot:camera-device:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCameraDeviceExcel(@Valid CameraDevicePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CameraDeviceDO> list = cameraDeviceService.getCameraDevicePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "摄像头设备.xls", "数据", CameraDeviceRespVO.class,
                        BeanUtils.toBean(list, CameraDeviceRespVO.class));
    }

    @PostMapping("/stream/start")
    @Operation(summary = "启动摄像头实时流")
    @Parameter(name = "id",description = "摄像头设备 ID",required = true)
    @PreAuthorize("@ss.hasPermission('iot:camera-device:query')")
    public CommonResult<CameraStreamClient.StreamStartRespDTO> startStream(@RequestParam("id") Long id){
        return success(cameraDeviceService.startStream(id));
    }

    @PostMapping("/stream/stop")
    @Operation(summary = "停止摄像头实时流")
    @Parameter(name = "id",description = "摄像头设备 ID",required = true)
    @PreAuthorize("@ss.hasPermission('iot:camera-device:query')")
    public CommonResult<Boolean> stopStream(@RequestParam("id") Long id){
        return success(cameraDeviceService.stopStream(id));
    }

    @GetMapping("/stream/status")
    @Operation(summary = "查询摄像头实时流状态")
    @Parameter(name = "id", description = "摄像头设备 ID", required = true)
    @PreAuthorize("@ss.hasPermission('iot:camera-device:query')")
    public CommonResult<CameraStreamClient.StreamStatusRespDTO> getStreamStatus(@RequestParam("id") Long id){
        return success(cameraDeviceService.getStreamStatus(id));
    }

    @PostMapping("/record/start")
    @Operation(summary = "开始摄像头录像")
    @Parameter(name = "id", description = "摄像头设备 ID", required = true)
    @PreAuthorize("@ss.hasPermission('iot:camera-device:query')")
    public CommonResult<Long> startRecord(@RequestParam("id") Long id) {
        return success(cameraDeviceRecordService.startRecord(id));
    }
}