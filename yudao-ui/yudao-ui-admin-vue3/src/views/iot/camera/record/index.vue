<template>
  <ContentWrap>
    <el-form
      ref="queryFormRef"
      :model="queryParams"
      :inline="true"
      label-width="80px"
      class="-mb-15px"
    >
      <el-form-item label="部门" prop="deptId">
        <el-input
          v-model="queryParams.deptId"
          placeholder="请输入部门ID"
          clearable
          class="!w-220px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>

      <el-form-item label="摄像头" prop="cameraId">
        <el-select
          v-model="queryParams.cameraId"
          placeholder="请选择摄像头"
          clearable
          filterable
          class="!w-240px"
        >
          <el-option
            v-for="item in cameraOptions"
            :key="item.id"
            :label="item.name || item.code || String(item.id)"
            :value="item.id"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="时间范围" prop="startTime">
        <el-date-picker
          v-model="queryParams.startTime"
          type="datetimerange"
          value-format="YYYY-MM-DD HH:mm:ss"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          class="!w-360px"
        />
      </el-form-item>

      <el-form-item>
        <el-button @click="handleQuery">
          <Icon icon="ep:search" class="mr-5px" />
          搜索
        </el-button>
        <el-button @click="resetQuery">
          <Icon icon="ep:refresh" class="mr-5px" />
          重置
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <ContentWrap>
    <el-table v-loading="loading" :data="list" stripe>
      <el-table-column label="摄像头ID" prop="cameraId" align="center" width="100" />
      <el-table-column label="部门ID" prop="deptId" align="center" width="100" />
      <el-table-column
        label="开始时间"
        prop="startTime"
        align="center"
        :formatter="dateFormatter"
        width="180"
      />
      <el-table-column
        label="结束时间"
        prop="endTime"
        align="center"
        :formatter="dateFormatter"
        width="180"
      />
      <el-table-column label="时长" prop="duration" align="center" width="110">
        <template #default="{ row }">
          {{ formatDuration(row.duration) }}
        </template>
      </el-table-column>
      <el-table-column label="文件大小" prop="fileSize" align="center" width="120">
        <template #default="{ row }">
          {{ formatFileSize(row.fileSize) }}
        </template>
      </el-table-column>
      <el-table-column label="录像状态" prop="status" align="center" width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusTagType(row.status)">
            {{ getStatusText(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="上传状态" prop="uploadStatus" align="center" width="110">
        <template #default="{ row }">
          <el-tag :type="getUploadStatusTagType(row.uploadStatus)">
            {{ getUploadStatusText(row.uploadStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="失败原因" prop="errorMsg" align="center" min-width="160" />
      <el-table-column label="操作" align="center" width="120" fixed="right">
        <template #default="{ row }">
          <el-button
            link
            type="primary"
            :disabled="!canPlay(row)"
            @click="handlePlay(row)"
          >
            播放
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <Pagination
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </ContentWrap>

  <el-dialog v-model="playDialogVisible" title="录像回放" width="800px" @close="closePlayer">
    <video v-if="videoUrl" :src="videoUrl" controls autoplay class="record-video" />
    <div v-else class="record-video-empty">视频加载中...</div>
  </el-dialog>
</template>

<script setup lang="ts">
import { dateFormatter } from '@/utils/formatTime'
import {
  CameraDeviceApi,
  CameraDeviceVO,
  IotCameraRecordApi,
  IotCameraRecordPlayUrlRespVO,
  IotCameraRecordVO
} from '@/api/iot/camera/record'

defineOptions({ name: 'IotCameraRecord' })

const message = useMessage()

const loading = ref(false)
const list = ref<IotCameraRecordVO[]>([])
const total = ref(0)
const queryFormRef = ref()

const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  deptId: undefined,
  cameraId: undefined,
  startTime: []
})

const cameraOptions = ref<CameraDeviceVO[]>([])
const playDialogVisible = ref(false)
const videoUrl = ref('')

const getList = async () => {
  loading.value = true
  try {
    const data = await IotCameraRecordApi.getRecordPage(queryParams)
    list.value = data.list || []
    total.value = data.total || 0
  } finally {
    loading.value = false
  }
}

const getCameraOptions = async () => {
  const data = await CameraDeviceApi.getCameraDevicePage({
    pageNo: 1,
    pageSize: 100
  })
  cameraOptions.value = data.list || []
}

const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

const resetQuery = () => {
  queryFormRef.value?.resetFields()
  queryParams.pageNo = 1
  getList()
}

const handlePlay = async (row: IotCameraRecordVO) => {
  try {
    const data = await IotCameraRecordApi.getRecordPlayUrl(row.id)
    const playData = unwrapResponse<IotCameraRecordPlayUrlRespVO>(data)
    const playUrl = playData?.playUrl || playData?.fileUrl || row.fileUrl

    if (!playUrl) {
      message.error('录像播放地址为空')
      return
    }

    closePlayer()
    videoUrl.value = await resolveVideoUrl(playUrl)
    playDialogVisible.value = true
  } catch {
    message.error('录像加载失败')
  }
}

const resolveVideoUrl = async (fileUrl: string) => {
  if (isHttpUrl(fileUrl)) {
    return fileUrl
  }

  const blob = await IotCameraRecordApi.downloadRecordFile(fileUrl)
  return URL.createObjectURL(blob)
}

const unwrapResponse = <T>(data: T | { data?: T }): T => {
  return ((data as any)?.data || data) as T
}

const isHttpUrl = (url: string) => {
  return /^https?:\/\//i.test(url)
}

const canPlay = (row: IotCameraRecordVO) => {
  return row.status === 1 && !!row.fileSize && (row.uploadStatus == null || row.uploadStatus === 1)
}

const closePlayer = () => {
  if (videoUrl.value.startsWith('blob:')) {
    URL.revokeObjectURL(videoUrl.value)
  }
  videoUrl.value = ''
}

const getStatusText = (status: number) => {
  if (status === 0) return '录制中'
  if (status === 1) return '已完成'
  if (status === 2) return '失败'
  return '未知'
}

const getStatusTagType = (status: number) => {
  if (status === 1) return 'success'
  if (status === 2) return 'danger'
  return 'warning'
}

const getUploadStatusText = (status?: number) => {
  if (status == null) return '-'
  if (status === 0) return '待处理'
  if (status === 1) return '成功'
  if (status === 2) return '失败'
  return '未知'
}

const getUploadStatusTagType = (status?: number) => {
  if (status == null) return 'info'
  if (status === 1) return 'success'
  if (status === 2) return 'danger'
  return 'warning'
}

const formatDuration = (duration?: number) => {
  if (!duration) return '-'
  const minutes = Math.floor(duration / 60)
  const seconds = duration % 60
  return minutes > 0 ? `${minutes}分${seconds}秒` : `${seconds}秒`
}

const formatFileSize = (size?: number) => {
  if (!size) return '-'
  if (size < 1024) return `${size} B`
  if (size < 1024 * 1024) return `${(size / 1024).toFixed(1)} KB`
  return `${(size / 1024 / 1024).toFixed(1)} MB`
}

onMounted(() => {
  getCameraOptions()
  getList()
})
</script>

<style scoped>
.record-video {
  width: 100%;
  max-height: 560px;
  background: #000;
}

.record-video-empty {
  height: 320px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #909399;
  background: #f5f7fa;
}
</style>
