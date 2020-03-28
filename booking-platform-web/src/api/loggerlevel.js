import request from '@/plugin/axios'

export function getAllLoggerLevel () {
  return request({
    url: '/loggerswitch/getAllLogger',
    method: 'get'
  })
}

export function modifyLoggerLevel (data) {
  return request({
    url: '/loggerswitch/submitLoggers',
    method: 'post',
    data
  })
}