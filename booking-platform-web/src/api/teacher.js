import request from '@/plugin/axios'

export function getTeacherByKeyword (keyword) {
  return request({
    url: '/teacher/list/' + keyword,
    method: 'get'
  })
}
