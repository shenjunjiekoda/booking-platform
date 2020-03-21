import { Notification, Message, MessageBox } from 'element-ui'
import util from '@/libs/util.js'
import router from '@/router'
import { LogOut } from '@api/sys.login'
const context = process.env
const optionsEnv = Object.keys(context)
  .filter(keyName => /^VUE_APP_API_\d+_\w+$/.test(keyName))
  .map(keyName => {
    const [index, ...name] = keyName.replace('VUE_APP_API_', '').split('_')
    return {
      index: Number(index),
      name: name.join('_'),
      value: context[keyName]
    }
  })
  .sort((a, b) => a.index - b.index)
  .map(e => ({
    name: e.name,
    value: e.value,
    type: 'env'
  }))

const base = context.VUE_APP_API || (optionsEnv.length > 0 ? optionsEnv[0].value : '')
export default {

  namespaced: true,
  state: {
    // 网络请求地址
    base,
    // env 中设置的地址选项
    optionsEnv,
    // 用户自己添加的地址
    optionsUser: []
  },
  getters: {
    // 混合系统提供的地址和用户自己设置的地址记录
    options (state) {
      return [
        ...state.optionsUser,
        ...state.optionsEnv
      ]
    }
  },
  actions: {
    logout ({ commit, dispatch }, { confirm = false } = {}) {
      /**
       * @description 注销
       */
      async function logout () {
        // 删除cookie
        util.cookies.remove('token')
        util.cookies.remove('uuid')
        // 清空 vuex 用户信息
        await dispatch('d2admin/user/set', {}, { root: true })
        // 跳转路由
        router.push({
          name: 'login'
        })
        LogOut()
      }
      // 判断是否需要确认
      if (confirm) {
        commit('d2admin/gray/set', true, { root: true })
        MessageBox.confirm('确定要注销当前用户吗', '注销用户', {
          type: 'warning'
        })
          .then(() => {
            commit('d2admin/gray/set', false, { root: true })
            logout()
          })
          .catch(() => {
            commit('d2admin/gray/set', false, { root: true })
            Message({
              message: '取消注销操作'
            })
          })
      } else {
        logout()
      }
    },
    /**
     * @description 改变网络请求地址
     * @param {Object} vuex context
     * @param {String} value
     */
    async set ({ state, dispatch }, value) {
      console.log('state:', state)
      console.log('aync set value:', value)
      // 如果和现在的值一样 直接返回
      if (state.base === value) return
      // 如果不是选项中的值 将其加入选项
      const findIndex = [
        ...state.optionsEnv,
        ...state.optionsUser
      ].map(e => e.value).indexOf(value)
      if (findIndex === -1) {
        state.optionsUser.push({
          name: '自定义',
          value,
          type: 'custom'
        })
        // 持久化用户自定义选项
        await dispatch('d2admin/db/set', {
          dbName: 'sys',
          path: 'api.optionsUser',
          value: state.optionsUser,
          user: false
        }, { root: true })
      }
      // 注销当前登录
      // this.logout()
      // dispatch('logout', { confirm: false })
      // 应用变更
      state.base = value
      // process.env = value
      console.log('now...base api:', state.base)
      // 持久化接口地址设置
      await dispatch('d2admin/db/set', {
        dbName: 'sys',
        path: 'api.base',
        value: state.base,
        user: false
      }, { root: true })
      // 显示提示
      Notification({
        title: '接口地址变更',
        message: value
      })
    },
    /**
     * @description 删除一个用户自己的地址配置
     * @param {Object} vuex context
     * @param {String} value
     */
    async remove ({ state, dispatch }, value) {
      const index = state.optionsUser.map(e => e.value).indexOf(value)
      if (index >= 0) {
        // 移除
        state.optionsUser.splice(index, 1)
        // 持久化用户自定义选项
        await dispatch('d2admin/db/set', {
          dbName: 'sys',
          path: 'api.optionsUser',
          value: state.optionsUser,
          user: false
        }, { root: true })
        // 显示提示
        Message({
          message: `${value} 已经删除`,
          type: 'success'
        })
      }
    },
    /**
     * @description 加载设置
     * @param {Object} vuex context
     */
    async load ({ state, dispatch }) {
      // 加载接口地址设置
      console.log('load....')
      state.base = await dispatch('d2admin/db/get', {
        dbName: 'sys',
        path: 'api.base',
        defaultValue: base,
        user: false
      }, { root: true })
      // 加载接口地址设置
      console.log('load state base api:', state.base)
      // process.env = state.base
      state.optionsUser = await dispatch('d2admin/db/get', {
        dbName: 'sys',
        path: 'api.optionsUser',
        defaultValue: [],
        user: false
      }, { root: true })
    }
  }

}
