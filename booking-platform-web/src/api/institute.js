import request from '@/plugin/axios'

export function getNameByClassId (classId) {
  return request({
    url: '/institute/list/class/' + classId,
    method: 'get'
  })
}

export function getExactPlanInfo (id) {
  return request({
    url: '/plan/' + id,
    method: 'get'
  })
}
