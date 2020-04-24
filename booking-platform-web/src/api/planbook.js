import request from '@/plugin/axios'

export function getPlanBookList (params) {
  return request({
    url: '/plan/book/list',
    method: 'get',
    params
  })
}

export function addPlanBook (data) {
  return request({
    url: 'plan/book/add',
    method: 'post',
    data
  })
}

export function updatePlanBookInfo (data) {
  return request({
    url: '/plan/book/update',
    method: 'put',
    data
  })
}

export function deletePlanBookInfo (id) {
  return request({
    url: '/plan/book/delete/' + id,
    method: 'post'
  })
}

export function submitPlanBookInfo (id) {
  return request({
    url: '/plan/book/submit/' + id,
    method: 'post'
  })
}
