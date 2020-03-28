import request from '@/plugin/axios'

export function getCourseByKeyword (keyword) {
  return request({
    url: '/course/list/' + keyword,
    method: 'get'
  })
}
