import request from '@/plugin/axios'

export function AccountLogin (data) {
  return request({
    url: '/teacher/login',
    method: 'post',
    data
  })
}

export function PlatformLogin () {
  return request({
    url: '/loginbyhead',
    method: 'post',
    headers: {
      'X-Email': 'junjie.shen@oriente.com'
    }
  })
}

export function LogOut (data) {
  return request({
    url: '/teacher/logout',
    method: 'post',
    data
  })
}

export function Register (data) {
  return request({
    url: '/register',
    method: 'post',
    data
  })
}
