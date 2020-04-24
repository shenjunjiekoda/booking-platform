<template>
    <d2-container>
        <div style="margin: 1em">
            <span style="margin:0.5em">收件人:</span>
            <el-select
                v-model="to"
                multiple
                filterable
                remote
                reserve-keyword
                placeholder="请选择收件人"
                :remote-method="remoteMethod"
                :loading="searchLoading"
                loading-text="正在加载收件人">
                <el-option
                v-for="item in teachers"
                :key="item"
                :label="item"
                :value="item">
                </el-option>
            </el-select>
        </div>
        <div style="margin: 1em;width: 50%">
            <span style="margin:0.5em">内容:</span>
            <el-input
                type="textarea"
                :autosize="{ minRows: 2, maxRows: 6}"
                maxlength="100"
                show-word-limit
                placeholder="请输入内容"
                v-model="msg">
            </el-input>
        </div>
        <div style="margin:1em">
            <el-button type="primary" @click="send">发送</el-button>
        </div>
    </d2-container>
</template>
<script>
import { getTeacherByKeyword } from '@api/teacher'
import { addMsg } from '@api/message'
export default {
  // name 值和本页的 $route.name 一致才可以缓存页面
  name: 'newMsg',
  data () {
    return {
      msg: '',
      to: [],
      list: [],
      teachers: [],
      options: [],
      searchLoading: false
    }
  },
  methods: {
    remoteMethod (query) {
      var _this = this
      if (query !== '') {
        console.log('name query:', query)
        this.searchLoading = true
        getTeacherByKeyword(query)
          .then(res => {
            _this.searchLoading = false
            _this.list = res.data
            console.log('teacher name res:', res.data)
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
          _this.teachers = _this.list.filter(item => {
            console.log('filter item:', item)
            return item
              .indexOf(query.toLowerCase()) > -1
          })
        }, 1500)
      } else {
        this.teachers = []
      }
    },
    send () {
      console.log('收件人：', this.to)
      if (this.to === undefined || this.to.length === 0) {
        this.$message({
          title: '失败',
          message: '至少要有一个收件人',
          type: 'error',
          duration: 2000
        })
      }
      addMsg({
        tos: this.to,
        msg: this.msg
      }).then(res => {
        console.log('addMsg res:', res)
        if (res.errorCode === 0) {
          console.log('addMsg success!')
          this.dialogFormVisible = false
          this.$notify({
            title: 'OK',
            message: '发送成功！',
            type: 'success',
            duration: 2000
          })
          this.$emit('reload', {})
        } else {
          this.$message({
            title: '发送失败！',
            message: res.msg,
            type: 'error',
            duration: 2000
          })
        }
      }).catch(err => {
        console.log('err: ', err)
      })
    }
  },
  mounted () {
  }
}
</script>
