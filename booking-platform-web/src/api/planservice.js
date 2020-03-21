import request from '@/plugin/axios'

export function getPlanServiceList (params) {
  return request({
    url: '/planservice/list',
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

export function updatePlanService (data) {
  return request({
    url: '/planservice/update',
    method: 'put',
    data
  })
}

export function deletePlanService (id) {
  return request({
    url: '/planservice/delete/' + id,
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
