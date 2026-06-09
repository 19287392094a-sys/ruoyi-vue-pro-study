package cn.iocoder.yudao.module.iot.service.camera;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import cn.iocoder.yudao.module.iot.dal.dataobject.camera.CameraDeviceDO;
import cn.iocoder.yudao.module.iot.dal.mysql.camera.CameraDeviceMapper;
import cn.iocoder.yudao.module.iot.framework.camera.config.IotCameraProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class CameraAutoRecordLifecycle implements ApplicationRunner {

    @Resource
    private IotCameraProperties cameraProperties;

    @Resource
    private CameraDeviceMapper cameraDeviceMapper;

    @Resource
    private CameraDeviceRecordService cameraDeviceRecordService;

    private ScheduledExecutorService scheduler;

    private ExecutorService recordExecutor;

    @Override
    public void run(org.springframework.boot.ApplicationArguments args) {
        if (!Boolean.TRUE.equals(cameraProperties.getAutoRecordEnabled())) {
            log.info("[camera-auto-record] disabled");
            return;
        }

        scheduler = Executors.newSingleThreadScheduledExecutor();
        recordExecutor = Executors.newFixedThreadPool(cameraProperties.getRecordThreadPoolSize());

        scheduler.scheduleWithFixedDelay(
                this::dispatchRecordTasks,
                cameraProperties.getAutoRecordInitialDelaySeconds(),
                cameraProperties.getRecordIntervalSeconds(),
                TimeUnit.SECONDS
        );

        log.info("[camera-auto-record] started, interval={}s, duration={}s",
                cameraProperties.getRecordIntervalSeconds(),
                cameraProperties.getRecordDurationSeconds());
    }

    private void dispatchRecordTasks() {
        try {
            List<CameraDeviceDO> cameras = TenantUtils.executeIgnore(
                    () -> cameraDeviceMapper.selectAutoRecordEnabledList()
            );

            for (CameraDeviceDO camera : cameras) {
                if (camera.getTenantId() == null || StrUtil.isBlank(camera.getRtspUrl())) {
                    continue;
                }

                recordExecutor.submit(() -> TenantUtils.execute(camera.getTenantId(), () -> {
                    try {
                        cameraDeviceRecordService.recordSegment(
                                camera.getId(),
                                cameraProperties.getRecordDurationSeconds()
                        );
                    } catch (Exception ex) {
                        log.warn("[camera-auto-record] record failed, cameraId={}", camera.getId(), ex);
                    }
                }));
            }
        } catch (Exception ex) {
            log.warn("[camera-auto-record] dispatch failed", ex);
        }
    }

    @PreDestroy
    public void shutdown() {
        log.info("[camera-auto-record] shutting down");

        if (scheduler != null) {
            scheduler.shutdownNow();
        }
        if (recordExecutor != null) {
            recordExecutor.shutdownNow();
        }

        cameraDeviceRecordService.stopAllRecords();

        log.info("[camera-auto-record] stopped");
    }

}