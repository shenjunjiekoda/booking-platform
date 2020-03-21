<template>
  <el-form
    :inline="true"
    :model="form"
    :rules="rules"
    ref="form"
    size="mini"
    style="margin-bottom: -18px;">
    <el-form-item label="计划名" prop="name">
      <el-input
        v-model="form.name"
        placeholder=""
        style="width: 140px;"/>
    </el-form-item>
    <el-form-item label="状态" prop="status">
      <el-radio-group v-model="form.status">
      <el-radio-button label="all"></el-radio-button>
      <el-radio-button label="new"></el-radio-button>
      <el-radio-button label="confirmed"></el-radio-button>
      <el-radio-button label="completed"></el-radio-button>
      </el-radio-group>
    </el-form-item>
    <!-- <el-form-item label="类型" prop="type">
      <el-radio-group v-model="form.type">
      <el-radio-button label="all"></el-radio-button>
      <el-radio-button label="reg"></el-radio-button>
      <el-radio-button label="dev"></el-radio-button>
      <el-radio-button label="hot"></el-radio-button>
      </el-radio-group>
    </el-form-item> -->
    <template v-if="advanced">
      <el-form-item label="类型" prop="type">
        <el-radio-group v-model="form.type">
        <el-radio-button label="all"></el-radio-button>
        <el-radio-button label="reg"></el-radio-button>
        <el-radio-button label="dev"></el-radio-button>
        <el-radio-button label="hot"></el-radio-button>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="创建人" prop="createdBy">
        <el-input
          v-model="form.createdBy"
          placeholder=""
          style="width: 80px;"/>
      </el-form-item>

      <el-form-item label="负责人" prop="manager">
        <el-input
          v-model="form.manager"
          placeholder=""
          style="width: 100px;"/>
      </el-form-item>

      <el-form-item label="执行时间" prop="courseName">
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
