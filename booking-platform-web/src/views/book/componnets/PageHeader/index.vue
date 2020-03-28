<template>
  <el-form
    :inline="true"
    :model="form"
    :rules="rules"
    ref="form"
    size="mini"
    style="margin-bottom: -18px;">
    <el-form-item label="书名" prop="name">
      <el-input
        v-model="form.name"
        placeholder=""
        style="width: 140px;"/>
    </el-form-item>
    <template v-if="advanced">
      <el-form-item label="ISBN号" prop="isbn">
        <el-input
          v-model="form.isbn"
          placeholder=""
          style="width: 100px;"/>
      </el-form-item>

      <el-form-item label="作者" prop="author">
        <el-input
          v-model="form.author"
          placeholder=""
          style="width: 100px;"/>
      </el-form-item>

      <el-form-item label="出版社" prop="press">
        <el-input
          v-model="form.press"
          placeholder=""
          style="width: 100px;"/>
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
