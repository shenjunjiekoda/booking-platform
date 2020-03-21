import request from '@/plugin/axios'

export function getOperationLogs (params) {
  return request({
    url: '/log/list',
    method: 'get',
    params
  })
}
