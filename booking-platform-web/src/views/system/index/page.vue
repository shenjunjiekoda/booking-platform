<template>
  <d2-container type="full" class="page">
    <d2-grid-layout
      v-bind="layout"
      @layout-updated="layoutUpdatedHandler">
      <d2-grid-item
        v-for="(item, index) in layout.layout"
        :key="index"
        v-bind="item"
        @resize="resizeHandler"
        @move="moveHandler"
        @resized="resizedHandler"
        @moved="movedHandler">
        <el-card shadow="never" class="page_card">
          <div v-if="item.title !== undefined" class="title" style="font-size:20px">{{item.title}}</div>
          <template v-if="item.i === '0'">
            <div class="d2-mb" style="padding:2px">
              <span class="welcome">
                {{info.name ? `${hello}， ${info.name}， 欢迎来到教材申购领取平台` : '未登录'}}
                ，你当前有 {{ count }} 条未读的消息。
              </span>
              <el-link v-if="count > 0" type="primary" @click="routerTo('message')"> 前往查看 </el-link>
            </div>
          </template>
          <template v-if="item.i === '1'">
            <el-button style="margin:0.5em" @click="routerTo('plan')">上课计划</el-button>
            <el-button style="margin:0.5em" @click="routerTo('book')">书库</el-button>
            <el-button style="margin:0.5em" @click="routerTo('planbook')">订书</el-button>
            <el-button style="margin:0.5em" @click="routerTo('operationlog')">动态</el-button>
            <el-button style="margin:0.5em" @click="routerTo('message')">站内信</el-button>
          </template>
          <template v-if="item.i === '3'">
            <el-link class="d2-mb" style="font-size: 18px" href="https://github.com/shenjunjiekoda/booking-platform" target="_blank" type="primary">教程</el-link>
            <div class="d2-mb">拖拽卡片调整位置</div>
            <div class="d2-mb">拖拽卡片右下角的手柄调整卡片大小</div>
            <div class="d2-mb">在控制台打印出数据变化</div>
          </template>
          <template v-if="item.i === '4'">
            <div v-if="subscribe" class="d2-mb" style="margin-left:3em;font-size:14px" @click="createClass">你已订阅班级: <span style="color:#86ABD2">{{ className }}</span></div>
            <el-link v-if="!subscribe" type="primary" @click="createClass">你还未订阅任何班级，点击选择订阅</el-link>
            <el-table
              :data="tableData"
              v-loading="loading"
              size="mini"
              stripe
              style="width: 100%;font-size:16px">
              <el-table-column label="" align="center">
                <template slot-scope="scope">
                  <div style="text-align:left;margin:1em">{{scope.row.msg}}</div>
                  <div style="text-align:right;font-size:14px">{{scope.row.createdAt}}</div>
                </template>
              </el-table-column>
            </el-table>
            <div style="text-align:right;margin:1em"><el-link v-if="subscribe" type="primary" @click="routerTo('operationlog')">查看详情</el-link></div>
          </template>
        </el-card>
      </d2-grid-item>
    </d2-grid-layout>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
          <el-form ref="dataForm" :model="temp" label-position="left" label-width="80px" style="width: 400px; margin-left:50px;">
            <el-form-item label="班级" prop="className">
              <el-select
                v-model="temp.classIds"
                multiple
                filterable
                remote
                reserve-keyword
                @input="change($event)"
                placeholder="请输入班级"
                :remote-method="remoteMethod"
                :loading="searchLoading"
                loading-text="正在加载班级">
                <el-option
                  v-for="item in classes"
                  :key="item.name"
                  :label="item.name"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogFormVisible = false">
              Cancel
            </el-button>
            <el-button type="primary" @click="createData()">
              Confirm
            </el-button>
          </div>
        </el-dialog>
  </d2-container>
</template>

<script>
import Vue from 'vue'
import { GridLayout, GridItem } from 'vue-grid-layout'
import { mapState, mapActions } from 'vuex'
import { getSubscribeClass, setSubscribeClasses } from '@api/subscribe'
import { getClassByKeyword } from '@api/class'
import { getOperationLogs } from '@api/operationlog'
import { getUnReadedCount } from '@api/message'
Vue.component('d2-grid-layout', GridLayout)
Vue.component('d2-grid-item', GridItem)
export default {
  computed: {
    ...mapState('d2admin/user', [
      'info'
    ])
  },
  data () {
    return {
      count: 0,
      tableData: [],
      list: [],
      searchLoading: false,
      classes: [],
      dialogStatus: '',
      dialogFormVisible: false,
      textMap: {
        update: '设置订阅班级',
        create: '设置订阅班级',
        type: ''
      },
      temp: {
        name: '',
        rowStatus: ''
      },
      className: '',
      subscribe: false,
      loading: false,
      hello: this.timeFix(),
      layout: {
        layout: [
          { 'x': 0, 'y': 0, 'w': 12, 'h': 4, 'i': '0', 'title': '欢迎' },
          { 'x': 8, 'y': 4, 'w': 4, 'h': 5, 'i': '1', 'title': '快速开始 / 便捷导航' },
          // { 'x': 0, 'y': 4, 'w': 4, 'h': 12, 'i': '2', 'title': 'XX指数' },
          { 'x': 8, 'y': 9, 'w': 4, 'h': 7, 'i': '3', 'title': '帮助' },
          { 'x': 0, 'y': 4, 'w': 8, 'h': 12, 'i': '4', 'title': '动态' }
          // { 'x': 0, 'y': 10, 'w': 8, 'h': 5, 'i': '6' },
          // { 'x': 8, 'y': 10, 'w': 4, 'h': 5, 'i': '7' }
        ],
        colNum: 12,
        rowHeight: 30,
        isDraggable: true,
        isResizable: true,
        isMirrored: false,
        verticalCompact: true,
        margin: [10, 10],
        useCssTransforms: true
      }
    }
  },
  mounted () {
    // 加载完成后显示提示
    // this.showInfo()
    this.reload()
  },
  methods: {
    ...mapActions('d2admin/account', [
      'logout'
    ]),
    goOperation () {
      console.log('goOperation..')
    },
    change (e) {
      this.$forceUpdate()
    },
    reload () {
      this.getClasses()
      this.getCount()
    },
    getCount () {
      getUnReadedCount()
        .then(res => {
          console.log('getUnReadedCount res:', res)
          if (res.errorCode === 0) {
            console.log('getUnReadedCount success!')
            this.count = res.data
            console.log('after get count:', this.count)
          } else {
          }
        }).catch(err => {
          console.log('err: ', err)
        })
    },
    createData () {
      console.log('create Data form....')
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const classList = this.temp.classIds
          console.log('classList:', classList)
          if (classList !== undefined && classList.length < 1) {
            this.$message({
              title: '失败',
              message: '至少选择一个班级',
              type: 'error',
              duration: 2000
            })
            return
          }
          setSubscribeClasses({
            classIds: classList === undefined ? null : classList
          }).then(res => {
            console.log('setSubscribeClasses res:', res)
            if (res.errorCode === 0) {
              console.log('setSubscribeClasses success!')
              this.dialogFormVisible = false
              this.$notify({
                title: 'OK',
                message: '订阅成功',
                type: 'success',
                duration: 2000
              })
              this.reload()
            } else {
              this.$message({
                title: '失败',
                message: res.msg,
                type: 'error',
                duration: 2000
              })
            }
          }).catch(err => {
            console.log('err: ', err)
          })
        }
      })
    },
    remoteMethod (query) {
      var _this = this
      if (query !== '') {
        console.log('name query:', query)
        this.searchLoading = true
        getClassByKeyword(query)
          .then(res => {
            _this.searchLoading = false
            _this.list = res.data
            console.log('class name res:', res.data)
            console.log('search name res:', _this.list)
          })
          .catch(err => {
            _this.searchLoading = false
            _this.$notify({
              title: '数据请求异常'
            })
            console.log('err', err)
          })
        setTimeout(() => {
          _this.searchLoading = false
          console.log('timeout list', _this.list)
          _this.classes = _this.list.filter(item => {
            console.log('filter item:', item)
            return item
          })
        }, 1500)
      } else {
        this.classes = []
      }
    },
    resetTemp () {
      this.temp = {
        id: undefined,
        importance: 1,
        remark: '',
        timestamp: new Date(),
        title: '',
        status: 'published',
        type: ''
      }
    },
    createClass () {
      this.resetTemp()
      this.rowStatus = ''
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    routerTo (name) {
      this.$router.push({
        name: name
      })
    },
    getClasses () {
      getSubscribeClass()
        .then(res => {
          console.log('getSubscribeClass res:', res.data)
          this.loading = false
          const list = res.data
          if (list.length === 0) {
            this.subscribe = false
          } else if (list.length > 0) {
            this.subscribe = true
            var i = 0
            var name = ''
            for (i = 0; i < list.length; i++) {
              if (i !== list.length - 1) {
                name = name + list[i].name + ', '
              } else {
                name = name + list[i].name
              }
              if (i === 2) {
                name = name.substring(0, name.length) + '...'
                break
              }
            }
            this.className = name
            getOperationLogs({
              pageCurrent: 1,
              pageSize: 6
            })
              .then(res => {
                this.tableData = res.data.items
                console.log('getOperationLogs res:', res.data)
              })
              .catch(err => {
                this.$notify({
                  title: '数据请求异常'
                })
                console.log('err', err)
              })
          }
          if (res.errorCode !== 0) {
            this.$notify({
              title: '异常信息: ' + res.msg
            })
          }
        })
        .catch(err => {
          this.loading = false
          console.log('err', err)
        })
    },
    log (arg1 = 'log', ...logs) {
      if (logs.length === 0) {
        console.log(arg1)
      } else {
        console.group(arg1)
        logs.forEach(e => {
          console.log(e)
        })
        console.groupEnd()
      }
    },
    timeFix () {
      const time = new Date()
      const hour = time.getHours()
      return hour < 9 ? '早上好' : hour <= 11 ? '上午好' : hour <= 13 ? '中午好' : hour < 20 ? '下午好' : '晚上好'
    },
    showInfo () {
      this.$notify({
        title: '提示',
        message: '你可以按住卡片拖拽改变位置；或者在每个卡片的右下角拖动，调整卡片大小'
      })
    },
    // 测试代码
    layoutUpdatedHandler (newLayout) {
      console.group('layoutUpdatedHandler')
      newLayout.forEach(e => {
        console.log(`{'x': ${e.x}, 'y': ${e.y}, 'w': ${e.w}, 'h': ${e.h}, 'i': '${e.i}'},`)
      })
      console.groupEnd()
    },
    resizeHandler (i, newH, newW) {
      this.log('resizeHandler', `i: ${i}, newH: ${newH}, newW: ${newW}`)
    },
    moveHandler (i, newX, newY) {
      this.log('moveHandler', `i: ${i}, newX: ${newX}, newY: ${newY}`)
    },
    resizedHandler (i, newH, newW, newHPx, newWPx) {
      this.log('resizedHandler', `i: ${i}, newH: ${newH}, newW: ${newW}, newHPx: ${newHPx}, newWPx: ${newWPx}`)
    },
    movedHandler (i, newX, newY) {
      this.log('movedHandler', `i: ${i}, newX: ${newX}, newY: ${newY}`)
    }
  }
}
</script>

<style lang="scss" scoped>
.page {
  .vue-grid-layout {
    background-color: $color-bg;
    border-radius: 4px;
    margin: -10px;
    .page_card {
      height: 100%;
      @extend %unable-select;
    }
    .vue-resizable-handle {
      opacity: .3;
      &:hover{
        opacity: 1;
      }
    }
    .welcome {
      font-size: 20px;
      line-height: 28px;
      font-weight: 500;
      text-align:center;
      color: rgba(0,0,0,.85);
    }
    .title {
      text-align: center;
      color: rgba(0,0,0,.85);
      font-weight: 500;
      font-size: 16px;
      background: transparent;
      // border-bottom: 1px solid #e8e8e8;
      border-radius: 2px 2px 0 0;
      zoom: 1;
      min-height: 48px;
      margin-bottom: -1px;
      padding: 2px 0;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
      display: block;
    }
  }
}

</style>
