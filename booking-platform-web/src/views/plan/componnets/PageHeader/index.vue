<template>
  <el-form
    :inline="true"
    :model="form"
    :rules="rules"
    ref="form"
    size="mini"
    style="margin-bottom: -18px;">

    <template v-if="advanced">
      <el-form-item label="课程:" prop="courseName">
        <el-input
          v-model="form.courseName"
          placeholder="如:数据结构"
          style="width: 140px;"/>
      </el-form-item>
      <el-form-item label="班级:" prop="className">
        <el-input
          v-model="form.className"
          placeholder="如:f160117"
          style="width: 100px;"/>
      </el-form-item>
      <el-form-item label="老师:" prop="teacherName">
        <el-input
          v-model="form.teacherName"
          placeholder=""
          style="width: 100px;"/>
      </el-form-item>
      <el-form-item label="学年:" prop="year">
         <el-input
          v-model="form.year"
          placeholder="如:2020(数字)"
          style="width: 100px;"/>
      </el-form-item>
      <el-form-item label="学期:" prop="semester">
        <el-select v-model="form.semester" clearable placeholder="请选择">
          <el-option label="第一学期" value=1></el-option>
          <el-option label="第二学期" value=2></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="周次:" prop="weekStart">
        <el-input
          v-model="form.weekStart"
          placeholder=""
          style="width: 50px;"/>
      </el-form-item>

      <el-form-item label="~" prop="weekEnd">
        <el-input
          v-model="form.weekEnd"
          placeholder=""
          style="width: 50px;"/>
      </el-form-item>
    </template>
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
    <el-form-item>
      <a @click="toggleAdvanced" style="margin-left: 8px">
        {{ advanced ? '收起' : '展开' }}
        <i v-if="advanced" class="el-icon-caret-top"></i>
        <i v-else class="el-icon-caret-bottom"></i>
        <!-- <el-icon :type="advanced ? 'up' : 'down'"/> -->
      </a>
    </el-form-item>

  </el-form>
</template>

<script>

export default {
  data () {
    return {
      // 高级搜索 展开/关闭
      advanced: false,
      form: {
        type: 'all',
        status: 'all'
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
