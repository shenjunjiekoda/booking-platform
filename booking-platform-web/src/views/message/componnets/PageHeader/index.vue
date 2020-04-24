<template>
  <div>
    <el-button type="info" icon="el-icon-refresh" @click="refreshPage">刷新</el-button>
    <el-button type="success" icon="el-icon-plus" @click="newMessage">写信</el-button>
    <el-button type="primary" icon="el-icon-check" @click="readAllMsgs">一键已读</el-button>
  </div>
</template>

<script>
import { readAll } from '@api/message'
export default {
  data () {
    return {
      options: [],
      list: [],
      searchLoading: false,
      // 高级搜索 展开/关闭
      advanced: false,
      tableStyle: false,
      form: {
        // user: ''
      },
      rules: {
        // type: [ { required: true, message: '请选择类型', trigger: 'change' } ],
        user: [ { required: true, message: '请输入用户', trigger: 'change' } ]
      },
      typeOptions: [{
        value: '',
        label: 'all'
      }, {
        value: 'add',
        label: 'add'
      }, {
        value: 'modify',
        label: 'modify'
      }, {
        value: 'delete',
        label: 'delete'
      }, {
        value: 'deploy',
        label: 'deploy'
      }, {
        value: 'deploy success',
        label: 'deploy success'
      }, {
        value: 'deploy fail',
        label: 'deploy fail'
      }, {
        value: 'sort',
        label: 'sort'
      }, {
        value: 'set',
        label: 'set'
      }, {
        value: 'confirm',
        label: 'confirm'
      }, {
        value: 'resume',
        label: 'resume'
      }, {
        value: 'terminate',
        label: 'terminate'
      }, {
        value: 'rollback',
        label: 'rollback'
      }, {
        value: 'rollback success',
        label: 'rollback success'
      }, {
        value: 'rollback fail',
        label: 'rollback fail'
      }],
      targetOptions: [{
        value: '',
        label: 'all'
      }, {
        value: 'plan service',
        label: 'plan service'
      }, {
        value: 'role',
        label: 'role'
      }, {
        value: 'role resource',
        label: 'role resource'
      }, {
        value: 'service',
        label: 'service'
      }, {
        value: 'service relation',
        label: 'service relation'
      }, {
        value: 'user',
        label: 'user'
      }, {
        value: 'logger',
        label: 'logger'
      }, {
        value: 'swimlane group',
        label: 'swimlane group'
      }, {
        value: 'swimlane instance',
        label: 'swimlane instance'
      }, {
        value: 'swimlane strategy',
        label: 'swimlane strategy'
      }, {
        value: 'protect project',
        label: 'protect project'
      }, {
        value: 'swimlane group service',
        label: 'swimlane group service'
      }, {
        value: 'swimlane group tester',
        label: 'swimlane group tester'
      }, {
        value: 'swimlane service weight',
        label: 'swimlane service weight'
      }, {
        value: 'swimlane group weight',
        label: 'swimlane group weight'
      }],
      pageOptions: [{
        value: '',
        label: 'all'
      }, {
        value: 'plan',
        label: 'plan'
      }, {
        value: 'role',
        label: 'role'
      }, {
        value: 'user',
        label: 'user'
      }, {
        value: 'service',
        label: 'service'
      }, {
        value: 'report',
        label: 'report'
      }, {
        value: 'logger switch',
        label: 'logger switch'
      }, {
        value: 'swimlane group',
        label: 'swimlane group'
      }, {
        value: 'swimlane instance',
        label: 'swimlane instance'
      }, {
        value: 'swimlane strategy',
        label: 'swimlane strategy'
      }, {
        value: 'protect project',
        label: 'protect project'
      }],
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick (picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近一个月',
          onClick (picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近三个月',
          onClick (picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
            picker.$emit('pick', [start, end])
          }
        }]
      }
    }
  },
  watch: {
    'tableStyle': {
      handler (newVal) {
        this.$emit('changeStyle', newVal)
      }
    }
  },
  methods: {
    newMessage () {
      this.$router.push({
        name: 'newMsg'
      })
    },
    refreshPage () {
      this.$emit('reload', {})
    },
    readAllMsgs () {
      readAll().then(res => {
        console.log('readAll res:', res.data)
        if (res.errorCode === 0) {
          this.$notify({
            title: 'OK!'
          })
          this.$emit('reload', {})
        } else {
          this.$notify({
            title: '异常信息: ' + res.msg
          })
        }
      })
        .catch(err => {
          console.log('err', err)
        })
    }
  }
}
</script>
