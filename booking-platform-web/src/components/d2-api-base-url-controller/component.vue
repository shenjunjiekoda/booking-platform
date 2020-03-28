<style lang="scss" scoped>
.d2-api-base-url-controller {
  .el-dialog__body {
    .wrapper {
      max-height: 220px;
      .item {
        &:last-child {
          .el-button {
            margin-bottom: 0px;
          }
        }
        .el-button {
          margin-bottom: 10px;
          .item-name {
            font-size: 14px;
            font-weight: bold;
            margin-bottom: 4px;
          }
          .item-value {
            font-size: 12px;
          }
          .item-icon {
            font-size: 24px;
          }
        }
      }
    }
  }
}
</style>

<template>
  <span>
    <el-dialog
      title="切换设置"
      :visible.sync="active"
      width="300px"
      custom-class="d2-api-base-url-controller"
      append-to-body>
      <el-scrollbar>
        <div class="wrapper">
          <div
            v-for="option of options"
            :key="option.value"
            class="item">
            <d2-button
              size="default"
              :type="isItemActive(option.name) ? 'primary' : 'default'"
              style="width: 100%;"
              @click="onSelect(option.name)">
              <div flex="main:justify cross:center">
                <div flex="dir:top cross:top">
                  <div class="item-name">
                    {{option.name}}
                  </div>
                  <div class="item-value" style="text-align:right">
                    {{option.value}}
                  </div>
                </div>
                <span v-if="isItemActive(option.name)">
                  <d2-icon class="item-icon" name="check-circle"/>
                </span>
                <span
                  v-else-if="option.type === 'custom'"
                  @click.stop="onRemove(option.value)">
                  <d2-icon class="item-icon" name="close"/>
                </span>
              </div>
            </d2-button>
          </div>
        </div>
      </el-scrollbar>
      <el-divider/>
      <d2-button
        size="default"
        type="primary"
        style="width:100%;"
        label="确定"
        @click="onClose"/>
    </el-dialog>
    <span @click="onOpen">
      <slot/>
    </span>
  </span>
</template>

<script>
// import { mapActions } from 'vuex'
import { getEmailConfig, getBookConfig, switchEmailConfig, updateBooks, scheduledUpdateBooks, stopScheduled } from '@api/config'
export default {
  name: 'd2-api-base-url-controller',
  data () {
    return {
      email: 0,
      schedule: 0,
      options: [
        {
          name: '邮箱推送'
        },
        {
          name: '自动更新书库'
        },
        {
          name: '更新一次书库'
        }
      ],
      active: false,
      custom: 'http://127.0.0.1:8088'
    }
  },
  computed: {

  },
  methods: {
    // ...mapActions('d2admin/api', [
    //   'set', 'remove'
    // ]),
    getConfigs () {
      getEmailConfig()
        .then(res => {
          this.email = res.data
          console.log('getEmailConfig res:', res.data)
          console.log('after getemail config:', this.email)
        })
        .catch(err => {
          this.$notify({
            title: '数据请求异常'
          })
          console.log('err', err)
        })
      getBookConfig()
        .then(res => {
          this.schedule = res.data
          console.log('getBookConfig res:', res.data)
          console.log('after getBookConfig schedule:', this.schedule)
        })
        .catch(err => {
          this.$notify({
            title: '数据请求异常'
          })
          console.log('err', err)
        })
    },
    onOpen () {
      this.getConfigs()
      console.log('onOpen')
      this.active = true
    },
    onClose () {
      console.log('onClose')
      this.active = false
    },
    onSelect (name) {
      console.log('onSelect name:', name)
      if (name === '邮箱推送') {
        switchEmailConfig()
          .then(res => {
            if (res.errorCode === 0) {
              this.email = res.data
              console.log('switchEmailConfig res:', res.data)
              this.getConfigs()
              this.$notify({
                title: '成功切换！'
              })
            } else {
              this.$notify({
                title: '变更异常'
              })
            }
          })
          .catch(err => {
            this.$notify({
              title: '数据请求异常'
            })
            console.log('err', err)
          })
      }
      if (name === '自动更新书库' && this.schedule === 1) {
        stopScheduled()
          .then(res => {
            if (res.errorCode === 0) {
              this.schedule = 0
              console.log('stopScheduled res:', res.data)
              this.getConfigs()
              this.$notify({
                title: '成功停止自动更新！'
              })
            } else {
              this.$notify({
                title: '请求异常'
              })
            }
          })
          .catch(err => {
            this.$notify({
              title: '数据请求异常'
            })
            console.log('err', err)
          })
      }
      if (name === '自动更新书库' && this.schedule === 0) {
        scheduledUpdateBooks()
          .then(res => {
            if (res.errorCode === 0) {
              this.schedule = 1
              console.log('scheduledUpdateBooks res:', res.data)
              this.getConfigs()
              this.$notify({
                title: '成功开始自动更新！每个月这个时刻会自动更新一次书库'
              })
            } else {
              this.$notify({
                title: '请求异常'
              })
            }
          })
          .catch(err => {
            this.$notify({
              title: '数据请求异常'
            })
            console.log('err', err)
          })
      }
      if (name === '更新一次书库') {
        updateBooks()
          .then(res => {
            if (res.errorCode === 0) {
              console.log('updateBooks res:', res.data)
              this.getConfigs()
              this.$notify({
                title: '开始更新！'
              })
            } else {
              this.$notify({
                title: '更新请求失败！'
              })
            }
          })
          .catch(err => {
            this.$notify({
              title: '数据请求异常'
            })
            console.log('err', err)
          })
      }
      // this.set(value)
    },
    onRemove (value) {
      console.log('onRemove value', value)
      // this.remove(value)
    },
    isItemActive (name) {
      console.log('isItemActive name', name)
      // console.log('this.email:', this.email)
      // console.log('this.email === 0', this.email === 0)
      if (name === '邮箱推送' && this.email === 1) {
        return true
      }
      if (name === '自动更新书库' && this.schedule === 1) {
        return true
      }
      // return this.base === value
      return false
    }
  }
}
</script>
