import request from '@/plugin/axios'

export function getClassByKeyword (keyword) {
  return request({
    url: '/class/list/name/' + keyword,
    method: 'get'
  })
}
