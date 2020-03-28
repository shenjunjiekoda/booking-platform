import request from '@/plugin/axios'

export function getPlanList (params) {
  return request({
    url: '/plan/list',
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


