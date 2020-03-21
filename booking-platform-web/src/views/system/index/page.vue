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
          <div v-if="item.title !== undefined" class="title">{{item.title}}</div>
          <template v-if="item.i === '0'">
            <div class="d2-mb" style="padding:2px">
              <span class="welcome">
                {{info.name ? `${hello}, ${info.name}, 要不要发布一把` : '未登录'}}
              </span>
            </div>
          </template>
          <template v-if="item.i === '1'">
            <el-button @click="goPlan">发布计划</el-button>
            <el-button @click="goOperationLog">操作日志</el-button>
            <el-button @click="goSwimlane">泳道</el-button>
          </template>
          <template v-if="item.i === '3'">
            <el-link class="d2-mb" style="font-size: 18px" href="https://git.oriente.com/backend-srv/deployment-srv" target="_blank" type="primary">教程</el-link>
            <div class="d2-mb">拖拽卡片调整位置</div>
            <div class="d2-mb">拖拽卡片右下角的手柄调整卡片大小</div>
            <div class="d2-mb">在控制台打印出数据变化</div>
          </template>
        </el-card>
      </d2-grid-item>
    </d2-grid-layout>
  </d2-container>
</template>

<script>
import Vue from 'vue'
import { GridLayout, GridItem } from 'vue-grid-layout'
import { mapState, mapActions } from 'vuex'
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
      hello: this.timeFix(),
      layout: {
        layout: [
          { 'x': 0, 'y': 0, 'w': 12, 'h': 4, 'i': '0', 'title': '欢迎' },
          { 'x': 8, 'y': 4, 'w': 4, 'h': 5, 'i': '1', 'title': '快速开始 / 便捷导航' },
          { 'x': 4, 'y': 4, 'w': 4, 'h': 12, 'i': '2', 'title': 'XX指数' },
          { 'x': 8, 'y': 9, 'w': 4, 'h': 7, 'i': '3', 'title': '帮助' },
          { 'x': 0, 'y': 4, 'w': 4, 'h': 12, 'i': '4', 'title': '动态' }
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
  },
  methods: {
    ...mapActions('d2admin/account', [
      'logout'
    ]),
    goPlan () {
      this.$router.push({
        name: 'plan'
      })
    },
    goOperationLog () {
      this.$router.push({
        name: 'operationlog'
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
