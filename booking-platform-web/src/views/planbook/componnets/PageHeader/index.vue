<template>
  <el-form
    :inline="true"
    :model="form"
    :rules="rules"
    ref="form"
    size="mini"
    style="margin-bottom: -18px;">
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
    <el-form-item label="状态" prop="status">
      <el-radio-group v-model="form.status">
      <el-radio-button label="全部"></el-radio-button>
      <el-radio-button label="待提交"></el-radio-button>
      <el-radio-button label="已提交"></el-radio-button>
      <el-radio-button label="已到货"></el-radio-button>
      <el-radio-button label="货不足"></el-radio-button>
    </el-radio-group>
    <el-form-item label="学年">
      <el-input v-model="form.year" placeholder="请输入学年"></el-input>
    </el-form-item>
    <el-form-item label="学期" prop="semester">
      <el-select v-model="form.semester" placeholder="请选择">
        <el-option label="第一学期" value="第一学期"></el-option>
        <el-option label="第二学期" value="第二学期"></el-option>
      </el-select>
    </el-form-item>
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
    <!-- <el-form-item>
      <a @click="toggleAdvanced" style="margin-left: 8px">
        {{ advanced ? '收起' : '展开' }}
        <i v-if="advanced" class="el-icon-caret-top"></i>
        <i v-else class="el-icon-caret-bottom"></i> -->
        <!-- <el-icon :type="advanced ? 'up' : 'down'"/> -->
      <!-- </a>
    </el-form-item> -->

  </el-form>
</template>

<script>
import { getClassByKeyword } from '@api/class'
export default {
  data () {
    return {
      searchLoading: false,
      options: [],
      // 高级搜索 展开/关闭
      advanced: false,
      form: {
        status: '全部',
        semester: '第一学期'
        // user: ''
      },
      rules: {
        // type: [ { required: true, message: '请选择类型', trigger: 'change' } ],
        user: [ { required: true, message: '请输入用户', trigger: 'change' } ]
      },
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
  methods: {
    remoteMethod (query) {
      var _this = this
      if (query !== '') {
        console.log('name query:', query)
        this.searchLoading = true
        getClassByKeyword(query)
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
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
    }
  }
}
</script>
