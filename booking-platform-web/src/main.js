// Vue
import Vue from 'vue'
import i18n from './i18n'
import App from './App'
// 核心插件
import d2Admin from '@/plugin/d2admin'
// store
import store from '@/store/index'

// 菜单和路由设置
import router from './router'
import menuHeader from '@/menu/header'
import menuAside from '@/menu/aside'
import { frameInRoutes } from '@/router/routes'
import pluginImport from '@d2-projects/vue-table-import'
// 允许携带cookie
// axios.defaults.withCredentials = true

// 使用excel util
Vue.use(pluginImport)
// 核心插件
Vue.use(d2Admin)

new Vue({
  router,
  store,
  i18n,
  render: h => h(App),
  async created () {
    // 处理路由 得到每一级的路由设置
    this.$store.commit('d2admin/page/init', frameInRoutes)
    // 设置顶栏菜单
    this.$store.commit('d2admin/menu/headerSet', menuHeader)
    // 设置侧边栏菜单
    this.$store.commit('d2admin/menu/asideSet', menuAside)
    // 初始化菜单搜索功能
    this.$store.commit('d2admin/search/init', menuHeader)
    // 加载接口配置
    await this.$store.dispatch('d2admin/api/load')
  },
  mounted () {
    // 展示系统信息
    this.$store.commit('d2admin/releases/versionShow')
    // this.$store.dispatch('d2admin/sys/load')
    // 用户登录后从数据库加载一系列的设置
    this.$store.dispatch('d2admin/account/load')
    // 获取并记录用户 UA
    this.$store.commit('d2admin/ua/get')
    // 初始化全屏监听
    this.$store.dispatch('d2admin/fullscreen/listen')
  },
  watch: {
    // 检测路由变化切换侧边栏内容
    '$route.matched': {
      handler (matched) {
        if (matched.length > 0) {
          var _side = menuAside.filter(menu => {
            // console.log('matched[1] path:', matched[1].path)
            if (menu.path !== undefined && menu.path !== '/index') {
              if (matched[1] !== undefined && matched[1].path !== undefined) {
                return menu.path === matched[1].path
              }
            }
          })
          if (_side.length === 0) {
            _side = menuAside.filter(menu => {
              // console.log('find in children')
              if (menu.children !== undefined) {
                for (let j = 0, len = menu.children.length; j < len; j++) {
                  // console.log('children:', menu.children[j])
                  if (matched[1] !== undefined) {
                    if (menu.children[j].path === matched[1].path) {
                      return menu.children[j].path === matched[1].path
                    }
                  }
                }
              }
            })
          }
          // console.log('_side:', _side)
          // console.log('_side[0]:', _side[0])
          this.$store.commit('d2admin/menu/asideSet', _side.length > 0 ? _side : [])
        }
      },
      immediate: true
    }
  }
}).$mount('#app')
