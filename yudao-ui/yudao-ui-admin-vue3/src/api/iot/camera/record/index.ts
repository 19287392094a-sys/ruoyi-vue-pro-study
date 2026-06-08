import request from '@/config/axios'

export interface IotCameraRecordVO {
  id: number
  cameraId: number
  cameraName?: string
  deptId: number
  startTime: string | number
  endTime?: string | number
  filePath: string
  fileUrl?: string
  fileSize?: number
  duration?: number
  status: number
  errorMsg?: string
  createTime: string | number
}

export interface IotCameraRecordPlayUrlRespVO {
  recordId: number
  cameraId: number
  playUrl: string
  fileUrl?: string
  status: number
}

export interface CameraDeviceVO {
  id: number
  code: string
  name: string
  deptId?: number
}

export const IotCameraRecordApi = {
  getRecordPage: async (params: any) => {
    return await request.get({ url: '/iot/camera-record/page', params })
  },

  getRecordPlayUrl: async (id: number) => {
    return await request.get({ url: '/iot/camera-record/play-url?id=' + id })
  },

  getRecordFile: async (id: number) => {
    return await request.download({ url: '/iot/camera-record/file?id=' + id })
  }
}

export const CameraDeviceApi = {
  getCameraDevicePage: async (params: any) => {
    return await request.get({ url: '/iot/camera-device/page', params })
  }
}
