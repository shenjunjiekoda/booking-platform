<template>
  <div>
    <el-form
      :inline="true"
      size="mini">
      <el-button type="success" size="mini" style="margin:0.1em;margin-right:1em" icon="el-icon-plus" @click="createPlan()">新增计划</el-button>
      <el-form-item :label="`已选数据下载 [ ${currentTableData.length} ]`">
        <el-button-group>
          <el-button
            type="primary"
            size="mini"
            :disabled="currentTableData.length === 0"
            @click="handleDownloadXlsx(currentTableData)">
            xlsx
          </el-button>
          <el-button
            type="primary"
            size="mini"
            :disabled="currentTableData.length === 0"
            @click="handleDownloadCsv(currentTableData)">
            csv
          </el-button>
        </el-button-group>
      </el-form-item>
      <el-form-item :label="`已选数据下载 [ ${multipleSelection.length} ]`">
        <el-button-group>
          <el-button
            type="primary"
            size="mini"
            :disabled="multipleSelection.length === 0"
            @click="handleDownloadXlsx(multipleSelection)">
            xlsx
          </el-button>
          <el-button
            type="primary"
            size="mini"
            :disabled="multipleSelection.length === 0"
            @click="handleDownloadCsv(multipleSelection)">
            csv
          </el-button>
        </el-button-group>
      </el-form-item>
    </el-form>

    <el-table
      :data="currentTableData"
      v-loading="loading"
      size="mini"
      stripe
      style="width: 100%;"
      :default-sort = "{prop: 'executeAt', order: 'descending'}"
      @selection-change="handleSelectionChange">

      <el-table-column
        type="selection"
        width="55">
      </el-table-column>
      <el-table-column type="expand">
      <template slot-scope="props">
        <el-form label-position="left" inline class="demo-table-expand">
          <el-form-item label="课程">
            <span>{{ props.row.courseName }}</span>
          </el-form-item>
          <el-form-item label="班级">
            <span>{{ props.row.className }}</span>
          </el-form-item>
          <el-form-item label="老师">
            <span>{{ props.row.teacherName }}</span>
          </el-form-item>
          <el-form-item label="学年">
            <span>{{ props.row.year }}</span>
          </el-form-item>
          <el-form-item label="学期">
            <span>{{ props.row.semester }}</span>
          </el-form-item>
          <el-form-item label="周次">
            <span>{{ props.row.week }}</span>
          </el-form-item>
        </el-form>
      </template>
    </el-table-column>

      <el-table-column label="课程" align="center">
        <template slot-scope="scope">
          {{scope.row.courseName}}
        </template>
      </el-table-column>

      <el-table-column label="班级" align="center">
        <template slot-scope="scope">
          {{ scope.row.className }}
        </template>
      </el-table-column>

      <el-table-column label="老师" align="center">
        <template slot-scope="scope">
          {{scope.row.teacherName}}
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" width="300">
        <template slot-scope="row">
          <el-button size="mini" icon="el-icon-tickets" @click="goToPlanServicePage(row)">详情</el-button>
          <el-button :disabled="row.row.status==='completed'" type="primary" size="mini" icon="el-icon-edit" @click="editPlan(row)">编辑</el-button>
          <el-popconfirm
            title="确定要删除这个目标计划吗？"
            @onConfirm="deletePlan(row)"
          >
            <el-button :disabled="row.row.status==='completed'" type="danger" size="mini" slot="reference" icon="el-icon-delete">删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :model="temp" label-position="left" label-width="80px" style="width: 400px; margin-left:50px;">
        <el-form-item label="课程" prop="courseName" :rules="[{ required: dialogStatus === 'create', message: '课程不能为空'}]">
          <el-input v-model="temp.courseName" />
        </el-form-item>
        <el-form-item label="班级" prop="className" :rules="[{ required: dialogStatus === 'create', message: '班级不能为空'}]">
          <el-select
            v-model="temp.className"
            multiple
            filterable
            remote
            reserve-keyword
            placeholder="请输入班级"
            :remote-method="remoteMethod"
            :loading="searchLoading"
            loading-text="正在加载班级">
            <el-option
              v-for="item in classes"
              :key="item"
              :label="item"
              :value="item">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="老师" prop="className" :rules="[{ required: dialogStatus === 'create', message: '老师不能为空'}]">
          <el-select
            v-model="temp.teacherName"
            multiple
            filterable
            remote
            reserve-keyword
            placeholder="请输入老师"
            :remote-method="remoteMethod2"
            :loading="searchLoading"
            loading-text="正在加载老师">
            <el-option
              v-for="item in teachers"
              :key="item"
              :label="item"
              :value="item">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="学年" prop="year" :rules="[{ required: dialogStatus === 'create', message: '学年不能为空'}]">
          <el-input v-model="temp.year" placeholder="如:2020"/>
        </el-form-item>
        <el-form-item label="学期" prop="semester" :rules="[{ required: dialogStatus === 'create', message: '学期不能为空'}]">
          <el-select v-model="temp.semester" placeholder="请选择">
            <el-option label="第一学期" value=1></el-option>
            <el-option label="第二学期" value=2></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="周次" prop="week">
          <el-input v-model="temp.week" placeholder="如:1~16"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          Cancel
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          Confirm
        </el-button>
      </div>
    </el-dialog>

  </div>
</template>

<style>
  .demo-table-expand {
    font-size: 0;
  }
  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }
</style>

<script>
import Vue from 'vue'
import pluginExport from '@d2-projects/vue-table-export'
import D2Crud from '@d2-projects/d2-crud'
// import { getUserByKeyword } from '@api/user'
import { addPlanInfo, updatePlanInfo, deletePlan } from '@api/plan'
import { getClassByKeyword } from '@api/class'
import { getTeacherByKeyword } from '@api/teacher'

Vue.use(D2Crud)

Vue.use(pluginExport)

export default {
  props: {
    tableData: {
      default: () => []
    },
    loading: {
      default: false
    }
  },
  filters: {
    statusFilter (status) {
      const statusMap = {
        completed: 'success',
        confirmed: '',
        new: 'info'
      }
      return statusMap[status]
    }
  },
  data () {
    return {
      rowId: 0,
      rowStatus: '',
      searchLoading: false,
      teachers: [],
      classes: [],
      list: [],
      currentTableData: [],
      multipleSelection: [],
      textMap: {
        update: '更新上课计划',
        create: '新增上课计划',
        type: ''
      },
      temp: {
        name: '',
        rowStatus: ''
      },
      pickerOptions: {
        shortcuts: [{
          text: '今天',
          onClick (picker) {
            picker.$emit('pick', new Date())
          }
        }, {
          text: '明天',
          onClick (picker) {
            const date = new Date()
            date.setTime(date.getTime() + 3600 * 1000 * 24)
            picker.$emit('pick', date)
          }
        }, {
          text: '一周后',
          onClick (picker) {
            const date = new Date()
            date.setTime(date.getTime() + 3600 * 1000 * 24 * 7)
            picker.$emit('pick', date)
          }
        }]
      },
      dialogStatus: '',
      dialogFormVisible: false,
      downloadColumns: [
        { label: 'No', prop: 'id' },
        { label: '计划名', prop: 'name' },
        { label: '类型', prop: 'type' },
        { label: '创建人', prop: 'createdBy' },
        { label: '负责人', prop: 'manager' },
        { label: '创建时间', prop: 'createdAt' },
        { label: '执行时间', prop: 'executeAt' },
        { label: '描述', prop: 'desc' },
        { label: '状态', prop: 'status' },
        { label: '用时', prop: 'timeUsed' }
      ]
    }
  },
  watch: {
    tableData: {
      handler (val) {
        this.currentTableData = val
      },
      immediate: true
    }
  },
  methods: {
    goToPlanServicePage (row) {
      // console.log('curId:', row.row.id)
      // console.log('curplanName:', row.row.name)
      this.$router.push({
        name: 'planservice',
        params: {
          id: row.row.id,
          planName: row.row.name,
          planStatus: row.row.status
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
              .indexOf(query.toLowerCase()) > -1
          })
        }, 1500)
      } else {
        this.classes = []
      }
    },
    remoteMethod2 (query) {
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
    createPlan () {
      this.resetTemp()
      this.rowStatus = ''
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    editPlan (row) {
      this.resetTemp()
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
      console.log('edit modal:', row.row)
      this.rowId = row.row.id
      this.rowStatus = row.row.status
      this.temp.courseName = row.row.courseName
      this.temp.year = row.row.year
      this.temp.semester = row.row.semester
      this.temp.week = row.row.week
      this.temp.teacherName = [row.row.teacherName]
      this.temp.className = [row.row.className]
    },
    deletePlan (row) {
      console.log('deletePlan id:', row.row.id)
      deletePlan(row.row.id)
        .then(res => {
          console.log('deletePlan res:', res)
          if (res.errorCode === 0) {
            this.$notify({
              title: 'OK',
              message: '删除成功',
              type: 'success',
              duration: 2000
            })
            this.$emit('reload', {})
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
    },
    updateData () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const teacherList = this.temp.teacherName
          const classList = this.temp.className
          console.log('courseName:', this.temp.courseName)
          console.log('year:', this.temp.year)
          console.log('semester:', this.temp.semester)
          console.log('week:', this.temp.week)
          var teacherName = ''
          if (teacherList !== undefined && teacherList.length === 1) {
            teacherName = teacherList[0]
          } else if (teacherList !== undefined && teacherList.length > 1) {
            this.$message({
              title: '失败',
              message: '只能选择一位老师',
              type: 'error',
              duration: 2000
            })
            return
          }
          var className = ''
          if (classList !== undefined && classList.length === 1) {
            className = classList[0]
          } else if (classList !== undefined && classList.length > 1) {
            this.$message({
              title: '失败',
              message: '只能选择一个班级',
              type: 'error',
              duration: 2000
            })
            return
          }
          console.log('teacherName:', teacherName)
          console.log('className:', className)
          updatePlanInfo({
            id: this.rowId,
            year: this.temp.year === '' ? null : this.temp.year,
            semester: this.temp.semester === undefined ? null : this.temp.semester,
            className: className === '' ? null : className,
            teacherName: teacherName === '' ? null : teacherName,
            week: this.temp.week === undefined ? null : this.temp.week,
            courseName: this.temp.courseName === undefined ? null : this.temp.courseName
          }).then(res => {
            console.log('updatePlan res:', res)
            if (res.errorCode === 0) {
              console.log('updatePlan success!')
              this.dialogFormVisible = false
              this.$notify({
                title: 'OK',
                message: '更新成功',
                type: 'success',
                duration: 2000
              })
              this.$emit('reload', {})
              console.log('刷新')
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
    createData () {
      console.log('create Data form....')
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const teacherList = this.temp.teacherName
          const classList = this.temp.className
          console.log('courseName:', this.temp.courseName)
          console.log('year:', this.temp.year)
          console.log('semester:', this.temp.semester)
          console.log('week:', this.temp.week)
          var teacherName = ''
          if (teacherList !== undefined && teacherList.length === 1) {
            teacherName = teacherList[0]
          } else if (teacherList !== undefined && teacherList.length > 1) {
            this.$message({
              title: '失败',
              message: '只能选择一位老师',
              type: 'error',
              duration: 2000
            })
            return
          }
          var className = ''
          if (classList !== undefined && classList.length === 1) {
            className = classList[0]
          } else if (classList !== undefined && classList.length > 1) {
            this.$message({
              title: '失败',
              message: '只能选择一个班级',
              type: 'error',
              duration: 2000
            })
            return
          }
          console.log('teacherName:', teacherName)
          console.log('className:', className)
          addPlanInfo({
            courseName: this.temp.courseName === undefined ? null : this.temp.courseName,
            year: this.temp.year === '' ? null : this.temp.year,
            semester: this.temp.semester === undefined ? null : this.temp.semester,
            className: className === '' ? null : className,
            teacherName: teacherName === '' ? null : teacherName,
            week: this.temp.week === undefined ? null : this.temp.week
          }).then(res => {
            console.log('addPlan res:', res)
            if (res.errorCode === 0) {
              console.log('addPlan success!')
              this.dialogFormVisible = false
              this.$notify({
                title: 'OK',
                message: '添加成功',
                type: 'success',
                duration: 2000
              })
              this.$emit('reload', {})
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
    handleSwitchChange (val, index) {
      const oldValue = this.currentTableData[index]
      this.$set(this.currentTableData, index, {
        ...oldValue,
        type: val
      })
      // 注意 这里并没有把修改后的数据传递出去 如果需要的话请自行修改
    },
    handleSelectionChange (val) {
      this.multipleSelection = val
    },
    downloadDataTranslate (data) {
      return data.map(row => ({
        ...row,
        type: row.type ? '禁用' : '正常',
        used: row.used ? '已使用' : '未使用'
      }))
    },
    handleDownloadXlsx (data) {
      this.$export.excel({
        title: '发布计划',
        columns: this.downloadColumns,
        data: this.downloadDataTranslate(data)
      })
        .then(() => {
          this.$message.success('导出表格成功')
        })
    },
    handleDownloadCsv (data) {
      this.$export.csv({
        title: '发布计划',
        columns: this.downloadColumns,
        data: this.downloadDataTranslate(data)
      })
        .then(() => {
          this.$message.success('导出CSV成功')
        })
    }
  }
}
</script>
