import request from '@/plugin/axios'

export function getEmailConfig () {
  return request({
    url: '/config/email/get',
    method: 'get'
  })
}

export function getBookConfig () {
  return request({
    url: '/config/book/get',
    method: 'get'
  })
}

export function switchEmailConfig () {
  return request({
    url: '/config/email/switch',
    method: 'post'
  })
}

export function updateBooks () {
  return request({
    url: '/config/book/update',
    method: 'post'
  })
}

export function scheduledUpdateBooks () {
  return request({
    url: '/config/book/schedule',
    method: 'post'
  })
}

export function stopScheduled () {
  return request({
    url: '/config/book/stop',
    method: 'post'
  })
}
