import request from '@/plugin/axios'

export function getSubscribeClass () {
  return request({
    url: '/subscribe/class/list',
    method: 'get'
  })
}
export function setSubscribeClasses (data) {
  return request({
    url: '/subscribe/class/set',
    method: 'post',
    data
  })
}
