import request from '@/plugin/axios'

export function getBookList (params) {
  return request({
    url: '/book/list',
    method: 'get',
    params
  })
}

export function getExactPlanInfo (id) {
  return request({
    url: '/plan/' + id,
    method: 'get'
  })
}

export function addPlanInfo (data) {
  return request({
    url: '/plan/add',
    method: 'post',
    data
  })
}

export function updatePlanInfo (data) {
  return request({
    url: '/plan/update',
    method: 'put',
    data
  })
}

export function deletePlan (id) {
  return request({
    url: '/plan/delete/' + id,
    method: 'post'
  })
}

export function confirmPlan (id) {
  return request({
    url: '/plan/confirm/' + id,
    method: 'post'
  })
}
