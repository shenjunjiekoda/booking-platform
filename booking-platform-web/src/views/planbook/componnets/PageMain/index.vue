<template>
  <div>
    <el-form
      :inline="true"
      size="mini">
      <!-- <el-button type="success" size="mini" style="margin:0.1em;margin-right:1em" icon="el-icon-plus" @click="createPlan()">新增计划</el-button> -->
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
    <el-row>
      <el-button size="mini" icon="el-icon-refresh" @click="flush()">刷新页面</el-button>
      <el-tooltip class="item" effect="dark" content="在该计划中添加一个新的服务项" placement="top">
        <el-button v-if="planStatus==='new'" size="mini" icon="el-icon-document-add" @click="createPlanService()">新增服务项</el-button>
      </el-tooltip>
      <el-tooltip class="item" effect="dark" content="查看服务间的依赖关系" placement="top">
        <el-button size="mini" icon="el-icon-connection">依赖关系</el-button>
      </el-tooltip>
      <el-tooltip class="item" effect="dark" content="通过服务间的依赖关系对该计划中的服务项自动排序，排序序号相同的服务可以同时部署" placement="top">
        <el-button v-if="planStatus==='new'" size="mini" icon="el-icon-sort" @click="sortItems" :loading="sortLoading">自动排序</el-button>
      </el-tooltip>
      <el-tooltip class="item" effect="dark" content="只有负责人可以确认，确认后会自动发布所有服务项" placement="top">
        <el-button v-if="planStatus==='new'" size="mini" icon="el-icon-truck" @click="confirmDeploy" :loading="confirmLoading">确认发布</el-button>
      </el-tooltip>
      <el-tooltip class="item" effect="dark" content="即时执行当前待发布的所有服务项" placement="top">
        <el-button v-if="planStatus==='confirmed'" size="mini" icon="el-icon-finished" :loading="deployAllLoading" @click="deployAll">批量执行</el-button>
      </el-tooltip>
      <el-button v-if="planStatus==='completed'" size="mini" icon="el-icon-refresh-left" @click="rollBackAll">全部回滚</el-button>
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
          <el-tooltip class="item" effect="dark" content="立即执行正在等待发布的服务项" placement="top">
            <el-button v-if="row.row.status=='waiting'" size="mini" :loading="nowDeployLoading" icon="el-icon-bicycle" @click="deployOne(row)">即时执行</el-button>
          </el-tooltip>
          <el-button v-if="row.row.status=='new'" type="primary" size="mini" icon="el-icon-edit" @click="editPlanService(row)">编辑</el-button>
          <el-popconfirm title="确定要删除这个服务项吗？" @onConfirm="deletePlanService(row)">
            <el-button v-if="row.row.status=='new'" slot="reference" type="danger" size="mini" icon="el-icon-delete">删除</el-button>
          </el-popconfirm>
          <el-tooltip class="item" effect="dark" content="中止正在等待发布或被阻塞的服务项，取消发布" placement="top">
            <el-button v-if="row.row.status=='waiting' || row.row.status=='blocked'" type="danger" size="mini" icon="el-icon-delete" :loading="terminateLoading" @click="terminate(row)">中止等待</el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="暂不支持" placement="top">
            <el-button v-if="row.row.status=='completed'" type="primary" size="mini" icon="el-icon-delete" @click="rollbackOne">回滚</el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="暂不支持" placement="top">
            <el-button v-if="row.row.status=='failed' || row.row.status=='terminated' || row.row.status=='rollbacked'" size="mini" icon="el-icon-delete" @click="resume(row)">重新执行</el-button>
          </el-tooltip>
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
        <el-form-item label="服务名" prop="serviceName">
          <el-select
            v-model="temp.serviceId"
            multiple
            filterable
            remote
            reserve-keyword
            placeholder="请输入服务"
            :remote-method="remoteMethod"
            :loading="searchLoading"
            loading-text="正在加载服务">
            <el-option
              v-for="item in options"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="标签" prop="tag" :rules="[{ required: dialogStatus === 'create', message: '标签不能为空'}]">
          <el-input v-model="temp.tag" />
        </el-form-item>
        <el-form-item label="泳道" prop="unit">
          <el-input v-model="temp.unit" />
        </el-form-item>
        <el-form-item label="个数" prop="count" :rules="[{ required: dialogStatus === 'create', message: '负责人不能为空'}]">
          <el-input v-model="temp.count" />
        </el-form-item>
        <el-form-item v-if="dialogStatus==='update'" label="发布顺序" prop="order">
          <el-input v-model="temp.order" />
        </el-form-item>
        <el-form-item v-if="dialogStatus==='update'" label="执行日期" prop="executedAt">
          <!-- <el-input :disabled="rowStatus === 'confirmed'" v-model="temp.executeAt" /> -->
          <el-date-picker
            :disabled="rowStatus === 'confirmed'"
            v-model="temp.executedAt"
            type="datetime"
            placeholder="选择日期时间"
            align="right"
            value-format="yyyy-MM-dd HH:mm:ss"
            format="yyyy-MM-dd HH:mm:ss"
            :picker-options="pickerOptions">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="描述" prop="desc">
          <el-input v-model="temp.desc" />
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
import { deployPlanService, addPlanService, updatePlanService, deletePlanService, autoSorting, resumePlanService, terminatePlanService, getCurWaitingServiceIds } from '@api/planbook'
import { confirmPlan } from '@api/plan'
import { getServiceByKeyword } from '@api/service'
import { getNameByClassId } from '@api/institute'

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
      deployAllLoading: false,
      resumeDialogVisible: false,
      terminateLoading: false,
      resumeLoading: false,
      newExecuteAt: undefined,
      rowId: 0,
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
        update: '更新服务项(不填则不修改)',
        create: '新增服务项',
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
    deployAll () {
      console.log('deployAll')
      console.log('planId:', this.planId)
      this.deployAllLoading = true
      var serviceIds = []
      getCurWaitingServiceIds(this.planId)
        .then(res => {
          console.log('getCurWaitingServiceIds res:', res)
          if (res.errorCode === 0) {
            serviceIds = res.data
            console.log('serviceIds', serviceIds)
            console.log(serviceIds.length === 0)
            if (serviceIds.length === 0) {
              console.log('serviceIds === []')
              this.$message({
                title: '失败',
                message: '当前没有可以发布的服务项',
                type: 'error',
                duration: 2000
              })
              return
            }
            deployPlanService({
              planId: this.planId,
              serviceIds: serviceIds
            })
              .then(res => {
                console.log('deployAll res:', res)
                this.deployAllLoading = false
                if (res.errorCode === 0) {
                  this.$notify({
                    title: 'OK',
                    message: '批量执行成功',
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
                this.deployAllLoading = false
                console.log('err: ', err)
              })
          } else {
            this.$message({
              title: '失败',
              message: res.msg,
              type: 'error',
              duration: 2000
            })
            this.$emit('reload', {})
            this.deployAllLoading = false
          }
        }).catch(err => {
          console.log('err: ', err)
        })
      this.deployAllLoading = false
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
    terminate (row) {
      this.terminateLoading = true
      console.log('terminate planServiceId:', row.row.id)
      terminatePlanService({
        planServiceIds: [row.row.id]
      })
        .then(res => {
          console.log('terminate res:', res)
          this.terminateLoading = false
          if (res.errorCode === 0) {
            this.$notify({
              title: 'OK',
              message: '成功中止发布',
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
          this.terminateLoading = false
          console.log('err: ', err)
        })
    },
    resume (row) {
      console.log('open resume dialog')
      this.resumeDialogVisible = true
      this.newExecuteAt = undefined
      this.resumeRowId = row.row.id
    },
    doResume () {
      this.resumeLoading = true
      console.log('do resume.....')
      console.log('planServiceId:', this.resumeRowId)
      console.log('new executeAt:', this.newExecuteAt)
      if (this.newExecuteAt === undefined) {
        console.log('wrong executedAt')
        this.$message({
          title: '失败',
          message: '新的执行时间不能为空',
          type: 'error',
          duration: 2000
        })
        this.resumeLoading = false
        return
      }
      if (this.newExecuteAt !== undefined && Date.parse(this.newExecuteAt) - new Date().getTime() <= 1000) {
        console.log('wrong executedAt')
        this.$message({
          title: '失败',
          message: '新的执行时间不能比现在早',
          type: 'error',
          duration: 2000
        })
        this.resumeLoading = false
        return
      }
      resumePlanService({
        planServiceId: this.resumeRowId,
        executedAt: this.newExecuteAt
      })
        .then(res => {
          console.log('resume res:', res)
          this.resumeLoading = false
          if (res.errorCode === 0) {
            this.$notify({
              title: 'OK',
              message: '重新进入待发布状态',
              type: 'success',
              duration: 2000
            })
            this.resumeDialogVisible = false
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
          this.resumeLoading = false
          console.log('err: ', err)
        })
    },
    confirmDeploy () {
      console.log('confirm deploy planId:', this.planId)
      this.confirmLoading = true
      confirmPlan(this.planId)
        .then(res => {
          console.log('confirm res:', res)
          this.confirmLoading = false
          if (res.errorCode === 0) {
            this.$notify({
              title: 'OK',
              message: '确认发布成功，计划进入发布状态',
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
          this.confirmLoading = false
          console.log('err: ', err)
        })
    },
    sortItems () {
      console.log('sort planId:', this.planId)
      this.sortLoading = true
      autoSorting(this.planId)
        .then(res => {
          console.log('sort res:', res)
          this.sortLoading = false
          if (res.errorCode === 0) {
            this.$notify({
              title: 'OK',
              message: '自动排序成功',
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
          this.sortLoading = false
          console.log('err: ', err)
        })
    },
    flush () {
      this.$emit('reload')
    },
    decorateStatus (status) {
      switch (status) {
        case 'new':
          return '未提交'
        case 'submitted':
          return '已提交'
        case 'instock':
          return '有货'
        case 'notinstock':
          return '无货'
        default:
          return '未知'
      }
    },
    remoteMethod (query) {
      var _this = this
      if (query !== '') {
        console.log('name query:', query)
        this.searchLoading = true
        getServiceByKeyword(query)
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
    deployOne (row) {
      this.rowId = row.row.id
      // console.log('deployOne row:', row.row)
      const serviceId = [ row.row.serviceId ]
      this.nowDeployLoading = true
      console.log('deployOne planServiceId:', serviceId)
      deployPlanService({
        planId: this.planId,
        serviceIds: serviceId
      })
        .then(res => {
          console.log('deployone res:', res)
          this.nowDeployLoading = false
          if (res.errorCode === 0) {
            this.$notify({
              title: 'OK',
              message: '当前服务项进入执行状态',
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
          this.nowDeployLoading = false
          console.log('err: ', err)
        })
    },
    createPlanService () {
      this.resetTemp()
      this.rowStatus = ''
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    editPlanService (row) {
      console.log('edit modal:', row.row)
      this.resetTemp()
      this.rowId = row.row.id
      this.rowStatus = row.row.status
      // this.temp.serviceName = row.row.serviceName
      // console.log('modify:', this.temp.serviceName)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    deletePlanService (row) {
      console.log('deletePlanService id:', row.row.id)
      deletePlanService(row.row.id)
        .then(res => {
          console.log('deletePlanService res:', res)
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
      console.log('update planservice Data form....')
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          console.log('planServiceId:', this.rowId)
          const serviceList = this.temp.serviceId
          console.log('serviceList:', serviceList)
          console.log('tag:', this.temp.tag)
          console.log('desc:', this.temp.desc)
          console.log('count:', this.temp.count)
          console.log('order:', this.temp.order)
          console.log('executedAt:', this.temp.executedAt)
          var serviceId = -1
          if (serviceList !== undefined && serviceList.length === 1) {
            serviceId = serviceList[0]
          } else if (serviceList !== undefined && serviceList.length > 1) {
            this.$message({
              title: '失败',
              message: '只能选择一个服务',
              type: 'error',
              duration: 2000
            })
            return
          }
          if (serviceId === -1 && this.temp.tag === undefined && this.temp.count === undefined && this.temp.unit === undefined &&
          this.temp.desc === undefined && this.temp.order === undefined && this.temp.executedAt === undefined) {
            this.dialogFormVisible = false
            return
          }
          if (this.temp.order !== undefined && isNaN(this.temp.order)) {
            this.$message({
              title: '失败',
              message: '执行顺序必须是个数字类型',
              type: 'error',
              duration: 2000
            })
            return
          }
          if (this.temp.executedAt !== undefined && Date.parse(this.temp.executedAt) - new Date().getTime() <= 1000) {
            console.log('wrong executedAt')
            this.$message({
              title: '失败',
              message: '执行时间不能比现在早',
              type: 'error',
              duration: 2000
            })
            return
          }
          console.log('serviceId:', serviceId)
          updatePlanService({
            id: this.rowId,
            unit: this.temp.unit === undefined ? null : this.temp.unit,
            tag: this.temp.tag === undefined ? null : this.temp.tag,
            count: this.temp.count === undefined ? null : this.temp.count,
            serviceId: serviceId === -1 ? null : serviceId,
            desc: this.temp.desc === undefined ? null : this.temp.desc,
            order: this.temp.order === undefined ? null : this.temp.order,
            executedAt: this.temp.executedAt === undefined ? null : this.temp.executedAt
          }).then(res => {
            console.log('update res:', res)
            if (res.errorCode === 0) {
              console.log('addPlan success!')
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
    createData () {
      console.log('create planservice Data form....')
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          console.log('planId:', this.planId)
          const serviceList = this.temp.serviceId
          if (serviceList === undefined) {
            this.$message({
              title: '失败',
              message: '必须选择一个服务名',
              type: 'error',
              duration: 2000
            })
            return
          }
          console.log('tag:', this.temp.tag)
          console.log('type:', this.temp.type)
          console.log('desc:', this.temp.desc)
          console.log('count:', this.temp.count)
          var serviceId = -1
          if (serviceList !== undefined && serviceList.length === 1) {
            serviceId = serviceList[0]
          } else if (serviceList !== undefined && serviceList.length > 1) {
            this.$message({
              title: '失败',
              message: '只能选择一个服务',
              type: 'error',
              duration: 2000
            })
            return
          }
          console.log('serviceId:', serviceId)
          addPlanService({
            planId: this.planId,
            unit: this.temp.unit === undefined ? null : this.temp.unit,
            tag: this.temp.tag === undefined ? null : this.temp.tag,
            count: this.temp.count === undefined ? null : this.temp.count,
            serviceId: serviceId === -1 ? null : serviceId,
            desc: this.temp.desc === undefined ? null : this.temp.desc
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
              '    日期:' + date.getMonth() + '月' + date.getDate() + '日'
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
