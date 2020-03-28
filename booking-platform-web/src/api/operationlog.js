import request from '@/plugin/axios'

export function getOperationLogs (params) {
  return request({
    url: '/subscribe/log/list',
    method: 'get',
    params
  })
}
