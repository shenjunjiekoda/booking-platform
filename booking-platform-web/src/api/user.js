import request from '@/plugin/axios'

export function getUserList (params) {
  return request({
    url: '/user/list',
    method: 'get',
    params
  })
}

export function getUserByKeyword (keyword) {
  return request({
    url: '/user/list/' + keyword,
    method: 'get'
  })
}
