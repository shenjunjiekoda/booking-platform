// const userDB = [
//   // { username: 'admin', password: 'admin', uuid: 'admin-uuid', name: 'Admin' },
//   { username: 'editor', password: 'editor', uuid: 'editor-uuid', name: 'Editor' },
//   { username: 'koda', password: 'koda', uuid: 'koda-uuid', name: '可达大帅哥' }
// ]

// export default [
//   {
//     path: '/teacher/login',
//     method: 'post',
//     handle ({ body }) {
//       const user = userDB.find(e => e.username === body.username && e.password === body.password)
//       if (user) {
//         return {
//           code: 0,
//           msg: '登录成功',
//           data: {
//             ...user,
//             token: '8dfhassad0asdjwoeiruty'
//           }
//         }
//       } else {
//         return {
//           code: 401,
//           msg: '用户名或密码错误',
//           data: {}
//         }
//       }
//     }
//   }
// ]
