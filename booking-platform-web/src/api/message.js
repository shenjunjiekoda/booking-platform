import request from '@/plugin/axios'

export function getMessages (params) {
  return request({
    url: '/message/list',
    method: 'get',
    params
  })
}

export function read (id) {
  return request({
    url: '/message/read/' + id,
    method: 'post'
  })
}

export function getUnReadedCount () {
  return request({
    url: '/message/unread/count',
    method: 'get'
  })
}
