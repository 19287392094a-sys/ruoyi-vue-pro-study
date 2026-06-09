import request from '@/config/axios'

export interface IotCameraRecordVO {
  id: number
  cameraId: number
  cameraName?: string
  deptId: number
  startTime: string | number
  endTime?: string | number
  filePath?: string
  fileUrl?: string
  fileSize?: number
  duration?: number
  status: number
  errorMsg?: string
  uploadStatus?: number
  uploadTime?: string | number
  uploadErrorMsg?: string
  createTime: string | number
}

export interface IotCameraRecordPlayUrlRespVO {
  recordId: number
  cameraId: number
  playUrl: string
  fileUrl?: string
  status: number
  uploadStatus?: number
  uploadTime?: string | number
  uploadErrorMsg?: string
}

export interface CameraDeviceVO {
  id: number
  code: string
  name: string
  deptId?: number
}

const normalizeRecordFileUrl = (fileUrl: string) => {
  if (fileUrl.startsWith('/admin-api')) {
    return fileUrl.substring('/admin-api'.length)
  }
  return fileUrl
}

export const IotCameraRecordApi = {
  getRecordPage: async (params: any) => {
    return await request.get({ url: '/iot/camera-record/page', params })
  },

  getRecordPlayUrl: async (id: number) => {
    return await request.get({ url: '/iot/camera-record/play-url?id=' + id })
  },

  downloadRecordFile: async (fileUrl: string) => {
    return await request.download({ url: normalizeRecordFileUrl(fileUrl) })
  }
}

export const CameraDeviceApi = {
  getCameraDevicePage: async (params: any) => {
    return await request.get({ url: '/iot/camera-device/page', params })
  }
}
