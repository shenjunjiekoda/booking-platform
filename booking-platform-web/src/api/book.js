import request from '@/plugin/axios'

export function getBookList (params) {
  return request({
    url: '/book/list',
    method: 'get',
    params
  })
}

export function getSpecificBookByKeyword (keyword) {
  return request({
    url: '/book/list/' + keyword,
    method: 'get'
  })
}

export function searchFromWeb (params) {
  return request({
    url: '/book/search',
    method: 'get',
    params
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
