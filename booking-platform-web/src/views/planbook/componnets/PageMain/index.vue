<template>
  <div>
    <el-form
      :inline="true"
      size="mini">
      <el-form-item :label="`已选数据下载 [ ${currentTableData.length} ]`">
        <el-button-group>
          <el-button
            type="primary"
            size="mini"
            :disabled="currentTableData.length === 0"
            @click="handleDownloadXlsx(currentTableData)">
            xlsx
          </el-button>
          <!-- <el-button
            type="primary"
            size="mini"
            :disabled="currentTableData.length === 0"
            @click="handleDownloadCsv(currentTableData)">
            csv
          </el-button> -->
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
          <!-- <el-button
            type="primary"
            size="mini"
            :disabled="multipleSelection.length === 0"
            @click="handleDownloadCsv(multipleSelection)">
            csv
          </el-button> -->
        </el-button-group>
      </el-form-item>
    </el-form>
    <el-row>
      <!-- <el-button size="mini" icon="el-icon-refresh" @click="flush()">刷新页面</el-button> -->
    </el-row>
    <el-table
      :data="currentTableData"
      v-loading="loading"
      size="mini"
      stripe
      style="width: 100%;"
      :default-sort = "{prop: 'className'}"
      @selection-change="handleSelectionChange">

      <el-table-column
        type="selection"
        width="55">
      </el-table-column>
      <el-table-column type="expand">
      <template slot-scope="props">
        <el-form label-position="left" inline class="demo-table-expand">
          <el-form-item label="班级:">
            <span>{{ props.row.className }}</span>
          </el-form-item>
          <el-form-item label="课程:">
            <span>{{ props.row.courseName }}</span>
          </el-form-item>
          <el-form-item label="老师:">
            <span>{{ props.row.teacherName }}</span>
          </el-form-item>
          <el-form-item label="书名:">
            <span>{{ props.row.bookName }}</span>
          </el-form-item>
          <el-form-item label="书名:">
            <span>{{ props.row.bookName }}</span>
          </el-form-item>
          <el-form-item label="ISBN号:">
            <span>{{ props.row.isbn }}</span>
          </el-form-item>
          <el-form-item label="作者:">
            <span>{{ props.row.author }}</span>
          </el-form-item>
          <el-form-item label="出版社:">
            <span>{{ props.row.press }}</span>
          </el-form-item>
          <el-form-item label="出版年月:">
            <span>{{ props.row.publishedAt }}</span>
          </el-form-item>
          <el-form-item label="版次:">
            <span>{{ props.row.edition === null ? '未知' : '第' + props.row.edition + '版' }}</span>
          </el-form-item>
          <el-form-item label="学生订书数:">
            <span>{{ props.row.stuNum + '本' }}</span>
          </el-form-item>
          <el-form-item label="老师订书数:">
            <span>{{ props.row.teacherNum + '本' }}</span>
          </el-form-item>
          <el-form-item label="周次:">
            <span>{{ props.row.week }}</span>
          </el-form-item>
          <el-form-item label="状态:">
            <span>{{ decorateStatus(props.row.status) }}</span>
          </el-form-item>
          <el-form-item v-if="props.row.status === 'notinstock' ||props.row.status === 'instock'" label="实际到货:">
            <span>{{ props.row.actualNum }}</span>
          </el-form-item>
        </el-form>
      </template>
    </el-table-column>

      <el-table-column label="班级" align="center">
        <template slot-scope="scope">
          {{scope.row.className}}
        </template>
      </el-table-column>

      <el-table-column label="课程" align="center">
        <template slot-scope="scope">
          {{ scope.row.courseName }}
        </template>
      </el-table-column>

      <el-table-column label="老师" align="center">
        <template slot-scope="scope">
          {{ scope.row.teacherName }}
        </template>
      </el-table-column>

      <el-table-column label="书名" align="center" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.stuNum === 0 && scope.row.teacherNum > 0">教辅</el-tag>
          {{ scope.row.bookName }}
        </template>
      </el-table-column>

      <el-table-column label="状态" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status | statusFilter">{{ decorateStatus(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center">
        <template slot-scope="row">
          <el-button :disabled="row.row.status==='submitted'" slot="reference" type="warning" :loading="submitLoading" size="mini" icon="el-icon-delete" @click="submitPlanBook(row)">提交</el-button>
          <el-button :disabled="isAdmin<=0 && row.row.status==='submitted'" type="primary" size="mini" icon="el-icon-edit" @click="editPlanBook(row)">编辑</el-button>
          <el-button :disabled="isAdmin<=0 && row.row.status==='submitted'" type="primary" size="mini" icon="el-icon-add" @click="addBook(row)">添加书</el-button>
          <el-button :disabled="row.row.status!=='new'" slot="reference" type="danger" size="mini" icon="el-icon-delete"  @click="deletePlanBook(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
      title="选择新的执行时间"
      :visible.sync="resumeDialogVisible">
      <el-form ref="dataForm" label-position="left" label-width="70px" style="width: 400px; margin-left:50px;">
        <el-form-item label="执行日期" prop="executedAt">
          <el-date-picker
            v-model="newExecuteAt"
            type="datetime"
            placeholder="选择日期时间"
            align="right"
            value-format="yyyy-MM-dd HH:mm:ss"
            format="yyyy-MM-dd HH:mm:ss"
            :picker-options="pickerOptions">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resumeDialogVisible = false">
          Cancel
        </el-button>
        <el-button type="primary" :loading="resumeLoading" @click="doResume()">
          Confirm
        </el-button>
      </div>
    </el-dialog>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :model="temp" label-position="left" label-width="70px" style="width: 400px; margin-left:50px;">
        <el-form-item label="书名" prop="book">
          <el-select
            :disabled="rowStatus === 'submitted'"
            @input="change($event)"
            v-model="temp.book"
            multiple
            filterable
            remote
            reserve-keyword
            placeholder="请输入书"
            :remote-method="remoteMethod"
            :loading="searchLoading"
            loading-text="正在加载书">
            <el-option
              v-for="(item,index) in options"
              :key="index"
              :label="item.name"
              :value="item.name">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="老师书数" prop="teacherNum" :rules="[{ required: dialogStatus === 'choose', message: '老师书数不能为空'}]">
          <el-input @input="change($event)" :disabled="rowStatus === 'submitted'" v-model="temp.teacherNum" />
        </el-form-item>
        <el-form-item label="学生书数" prop="stuNum" :rules="[{ required: dialogStatus === 'choose', message: '老师书数不能为空'}]">
          <el-input  @input="change($event)" :disabled="rowStatus === 'submitted'" v-model="temp.stuNum" />
        </el-form-item>
        <el-form-item v-if="isAdmin > 0 && rowStatus !== 'new'" label="到货书数" prop="actualNum">
          <el-input @input="change($event)" v-model="temp.actualNum" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          Cancel
        </el-button>
        <el-button type="primary" @click="dialogStatus==='journal'?addData():updateData()">
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
import { submitPlanBookInfo, addPlanBook, updatePlanBookInfo, deletePlanBookInfo } from '@api/planbook'
import { getNameByClassId } from '@api/institute'
import { getSpecificBookByKeyword } from '@api/book'
import util from '@/libs/util.js'

Vue.use(D2Crud)
Vue.use(pluginExport)
export default {
  props: {
    tableData: {
      default: () => []
    },
    classId: {
      default: 0
    },
    loading: {
      default: false
    },
    planStatus: {
      default: ''
    },
    planId: {
      default: 0
    }
  },
  filters: {
    statusFilter (status) {
      const statusMap = {
        new: 'success',
        submitted: '',
        instock: 'info',
        notinstock: 'warning'
      }
      return statusMap[status]
    }
  },
  data () {
    return {
      admin: false,
      isAdmin: 0,
      submitLoading: false,
      deployAllLoading: false,
      resumeDialogVisible: false,
      terminateLoading: false,
      resumeLoading: false,
      newExecuteAt: undefined,
      rowId: 0,
      rowPlanId: 0,
      resumeRowId: -1,
      rowStatus: '',
      sortLoading: false,
      searchLoading: false,
      nowDeployLoading: false,
      confirmLoading: false,
      options: [],
      list: [],
      currentTableData: [],
      multipleSelection: [],
      textMap: {
        update: '编辑信息',
        choose: '填写信息',
        journal: '添加书籍',
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
        // { label: 'No', prop: 'id' },
        { label: '课程名称', prop: 'courseName' },
        { label: '教材名称', prop: 'bookName' },
        { label: 'ISBN', prop: 'isbn' },
        { label: '作者', prop: 'author' },
        { label: '出版社', prop: 'press' },
        { label: '出版年月', prop: 'publishedAt' },
        { label: '版次', prop: 'edition' },
        { label: '使用班级', prop: 'className' },
        { label: '周次', prop: 'week' },
        { label: '老师订购数', prop: 'teacherNum' },
        { label: '学生订购数', prop: 'stuNum' },
        { label: '任课老师签名' }
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
    change (e) {
      this.$forceUpdate()
    },
    submitPlanBook (row) {
      console.log('submitPlanBook id', row.row.id)
      this.submitLoading = true
      submitPlanBookInfo(row.row.id)
        .then(res => {
          console.log('submitPlanBookInfo res:', res)
          this.submitLoading = false
          if (res.errorCode === 0) {
            this.$notify({
              title: 'OK',
              message: '成功提交',
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
            this.$emit('reload', {})
          }
        }).catch(err => {
          this.submitLoading = false
          console.log('err: ', err)
        })
    },
    rollBackAll () {
      this.$notify({
        title: 'Warning',
        message: '暂不支持回滚',
        type: 'warning',
        duration: 2000
      })
    },
    rollbackOne () {
      this.$notify({
        title: 'Warning',
        message: '暂不支持回滚',
        type: 'warning',
        duration: 2000
      })
    },
    flush () {
      this.$emit('reload')
    },
    decorateStatus (status) {
      switch (status) {
        case 'new':
          return '待提交'
        case 'submitted':
          return '已提交'
        case 'instock':
          return '已到货'
        case 'notinstock':
          return '货不足'
        default:
          return '未知'
      }
    },
    remoteMethod (query) {
      var _this = this
      if (query !== '') {
        console.log('name query:', query)
        this.searchLoading = true
        getSpecificBookByKeyword(query)
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
              .indexOf(query.toLowerCase()) > -1 && item !== undefined && item !== null
          })
        }, 1500)
      } else {
        this.options = []
      }
    },
    chooseBook () {
      this.resetTemp()
      this.rowStatus = ''
      this.dialogStatus = 'choose'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    editPlanBook (row) {
      console.log('edit modal:', row.row)
      this.resetTemp()
      this.rowId = row.row.id
      this.rowStatus = row.row.status
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.temp.book = [row.row.bookName]
      this.temp.teacherNum = row.row.teacherNum
      this.temp.stuNum = row.row.stuNum
      this.temp.actualNum = row.row.actualNum
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    addBook (row) {
      console.log('addPlanBook modal:', row.row)
      this.resetTemp()
      console.log(row.row.planId)
      this.rowPlanId = row.row.planId
      this.rowStatus = row.row.status
      this.dialogStatus = 'journal'
      this.dialogFormVisible = true
      this.temp.teacherNum = row.row.teacherNum
      this.temp.stuNum = row.row.stuNum
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    deletePlanBook (row) {
      console.log('deletePlanBook id:', row.row.id)
      deletePlanBookInfo(row.row.id)
        .then(res => {
          console.log('deletePlanBook res:', res)
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
      console.log('update planBook Data form....')
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          console.log('planBookId:', this.rowId)
          const bookList = this.temp.book
          console.log('teacherNum:', this.temp.teacherNum)
          console.log('stuNum:', this.temp.stuNum)
          console.log('actualNum:', this.temp.actualNum)
          var bookName = ''
          console.log('bookList', bookList)
          if (bookList !== undefined && bookList.length === 1) {
            bookName = bookList[0]
          } else if (bookList.length === 2) {
            bookName = bookList[1]
          } else if (bookList !== undefined && bookList.length > 1) {
            this.$message({
              title: '失败',
              message: '只能选择一本书',
              type: 'error',
              duration: 2000
            })
            return
          }
          console.log('bookName:', bookName)
          updatePlanBookInfo({
            planBookId: this.rowId,
            bookName: bookName === undefined ? null : bookName,
            teacherNum: this.temp.teacherNum === undefined ? null : this.temp.teacherNum,
            stuNum: this.temp.stuNum === undefined ? null : this.temp.stuNum,
            actualNum: this.temp.actualNum === undefined ? null : this.temp.actualNum
          }).then(res => {
            console.log('updatePlanBookInfo res:', res)
            if (res.errorCode === 0) {
              console.log('updatePlanBookInfo success!')
              this.dialogFormVisible = false
              this.$notify({
                title: 'OK',
                message: '更新成功',
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
    addData () {
      console.log('addData journal Data form....')
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const bookList = this.temp.book
          console.log('teacherNum:', this.temp.teacherNum)
          var bookName = ''
          console.log('bookList', bookList)
          if (bookList === undefined || bookList.length === 0) {
            this.$message({
              title: '失败',
              message: '请选择一本书',
              type: 'error',
              duration: 2000
            })
            return
          } else if (bookList !== undefined && bookList.length === 1) {
            bookName = bookList[0]
          } else if (bookList.length === 2) {
            bookName = bookList[1]
          } else if (bookList !== undefined && bookList.length > 1) {
            this.$message({
              title: '失败',
              message: '只能选择一本书',
              type: 'error',
              duration: 2000
            })
            return
          }
          console.log('bookName:', bookName)
          addPlanBook({
            planId: this.rowPlanId,
            bookName: bookName === undefined ? null : bookName,
            stuNum: this.temp.stuNum === undefined ? null : this.temp.stuNum,
            teacherNum: this.temp.teacherNum === undefined ? null : this.temp.teacherNum
          }).then(res => {
            console.log('addPlanBook res:', res)
            if (res.errorCode === 0) {
              console.log('addPlanBook success!')
              this.dialogFormVisible = false
              this.$notify({
                title: 'OK',
                message: '添加书籍成功',
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
      }
    },
    handleSelectionChange (val) {
      this.multipleSelection = val
    },
    downloadDataTranslate (data) {
      console.log('translate... row', data)
      return data.filter(row => row.bookName !== null)
    },
    handleDownloadXlsx (data) {
      console.log('xlsx data:', data)
      const year = data[0].year + '~' + (data[0].year + 1) + '学年'
      const semester = data[0].semester === 1 ? '第一' : '第二' + '学期'
      const title = year + semester + '教材申报表_' + data[0].className
      console.log('title:', title)
      console.log('classId', this.classId)
      if (this.classId !== undefined && this.classId !== null && this.classId !== 0) {
        getNameByClassId(this.classId)
          .then(res => {
            console.log('getNameByClassId res:', res.data)
            if (res.errorCode === 0) {
              const date = new Date()
              const header = '学院:' + res.data + '   班级:' + data[0].className + '   ' + year + ' ' + semester +
              '    日期:' + date.toLocaleDateString()
              console.log('header:', header)
              if (res.data !== null && res.data !== '') {
                this.$export.excel({
                  header: header,
                  title: title,
                  columns: this.downloadColumns,
                  data: this.downloadDataTranslate(data)
                })
                  .then(() => {
                    this.$message.success('导出表格成功')
                  })
              }
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
    },
    handleDownloadCsv (data) {
      this.$export.csv({
        title: '上课计划',
        columns: this.downloadColumns,
        data: this.downloadDataTranslate(data)
      })
        .then(() => {
          this.$message.success('导出CSV成功')
        })
    }
  },
  mounted () {
    this.isAdmin = util.cookies.get('isAdmin')
    console.log('mounted.... isAdmin:', this.isAdmin)
    console.log('isAdmin>0:', util.cookies.get('isAdmin') > 0)
  }
}
</script>
