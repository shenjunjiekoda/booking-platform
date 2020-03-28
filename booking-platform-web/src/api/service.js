import request from '@/plugin/axios'

export function getServiceByKeyword (keyword) {
  return request({
    url: '/service/list/' + keyword,
    method: 'get'
  })
}

// export function deployPlanService (data) {
//   return request({
//     url: '/planservice/deploy',
//     method: 'post',
//     data
//   })
// }

// export function addPlanService (data) {
//   return request({
//     url: '/planservice/add',
//     method: 'post',
//     data
//   })
// }
