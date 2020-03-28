<template>
  <el-form
    :inline="true"
    :model="form"
    :rules="rules"
    ref="form"
    size="mini"
    style="margin-bottom: -18px;">
    <template v-if="advanced">
      <el-form-item label="班级" prop="classId">
        <el-select
          v-model="form.classId"
          multiple
          filterable
          remote
          reserve-keyword
          placeholder="请输入班级"
          :remote-method="remoteMethod"
          :loading="searchLoading"
          loading-text="正在加载班级">
          <el-option
            v-for="item in options"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="时间" prop="courseName">
        <el-date-picker
          v-model="form.createdAt"
          type="datetimerange"
          value-format="yyyy-MM-dd HH:mm:ss"
          format="yyyy-MM-dd HH:mm:ss"
          :picker-options="pickerOptions"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          align="right">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          @click="handleFormSubmit">
          <d2-icon name="search"/>
          查询
        </el-button>
      </el-form-item>

      <el-form-item>
        <el-button
          @click="handleFormReset">
          <d2-icon name="refresh"/>
          重置
        </el-button>
      </el-form-item>
    </template>
    <el-form-item>
      <a @click="toggleAdvanced" style="margin-left: 8px">
        {{ advanced ? '收起查询' : '展开查询' }}
        <i v-if="advanced" class="el-icon-caret-top"></i>
        <i v-else class="el-icon-caret-bottom"></i>
        <!-- <el-icon :type="advanced ? 'up' : 'down'"/> -->
      </a>
    </el-form-item>
    <el-form-item label=" ">
      <el-switch
        v-model="tableStyle"
        @click="changeStyle"
        active-text="table显示">
      </el-switch>
    </el-form-item>

  </el-form>
</template>

<script>
import { getSubscribeClass } from '@/api/subscribe'
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
    remoteMethod (query) {
      var _this = this
      if (query !== '') {
        console.log('name query:', query)
        this.searchLoading = true
        getSubscribeClass(query)
          .then(res => {
            _this.searchLoading = false
            _this.list = res.data
            console.log('name res:', res.data)
            console.log('search name res:', _this.list)
          })
          .catch(err => {
            _this.searchLoading = false
            _this.$notify({
              title: '服务数据请求异常'
            })
            console.log('err', err)
          })
        setTimeout(() => {
          _this.searchLoading = false
          console.log('timeout list', _this.list)
          _this.options = _this.list.filter(item => {
            console.log('filter item:', item)
            return item.name
              .indexOf(query.toLowerCase()) > -1
          })
        }, 1500)
      } else {
        this.options = []
      }
    },
    changeStyle () {
      console.log('cur table style:', this.tableStyle)
    },
    handleFormSubmit () {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.$emit('submit', this.form)
        } else {
          this.$notify.error({
            title: '错误',
            message: '表单校验失败'
          })
          return false
        }
      })
    },
    handleFormReset () {
      this.$refs.form.resetFields()
      this.$refs.form.createdAt = undefined
      this.$refs.form.createdAtFrom = undefined
      this.$refs.form.createdAtTo = undefined
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
    }
  }
}
</script>
