import request from '@/plugin/axios'

export function getPlanBookList (params) {
  return request({
    url: '/plan/book/list',
    method: 'get',
    params
  })
}

export function deployPlanService (data) {
  return request({
    url: '/planservice/deploy',
    method: 'post',
    data
  })
}

export function addPlanService (data) {
  return request({
    url: '/planservice/add',
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

export function autoSorting (id) {
  return request({
    url: '/planservice/sort/' + id,
    method: 'put'
  })
}

export function resumePlanService (data) {
  return request({
    url: '/planservice/resume',
    method: 'post',
    data
  })
}

export function terminatePlanService (data) {
  return request({
    url: '/planservice/terminate',
    method: 'post',
    data
  })
}

export function getCurWaitingServiceIds (id) {
  return request({
    url: '/planservice/services/' + id,
    method: 'get'
  })
}
