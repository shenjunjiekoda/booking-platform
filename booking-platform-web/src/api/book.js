import request from '@/plugin/axios'

export function getBookList (params) {
  return request({
    url: '/book/list',
    method: 'get',
    params
  })
}

export function searchFromWeb (keyword) {
  return request({
    url: '/book/search/' + keyword,
    method: 'get'
  })
}

export function addBook (data) {
  return request({
    url: '/book/add',
    method: 'post',
    data
  })
}

export function updateBook (data) {
  return request({
    url: '/book/update',
    method: 'put',
    data
  })
}

export function deleteBookById (id) {
  return request({
    url: '/book/delete/' + id,
    method: 'post'
  })
}
