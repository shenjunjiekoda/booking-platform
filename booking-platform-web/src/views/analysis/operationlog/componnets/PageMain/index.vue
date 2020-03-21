<template>
  <div>
    <el-table
      :data="currentTableData"
      v-loading="loading"
      size="mini"
      stripe
      style="width: 100%;font-size:16px"
      :default-sort = "{prop: 'executeAt', order: 'ascending'}"
      @selection-change="handleSelectionChange">
      <el-table-column
          width="55">
        </el-table-column>
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-form label-position="left" inline class="demo-table-expand">
              <el-form-item label="页面/对象">
                <span>{{ decoratePage(props.row.page) }}页/{{ decorateTarget(props.row.target) }}</span>
              </el-form-item>
              <el-form-item label="类型">
                <span>{{ decorateType(props.row.type) }}</span>
              </el-form-item>
              <el-form-item label="操作人">
                <span>{{ props.row.createdBy === '' ? '未知用户':props.row.createdBy }}</span>
              </el-form-item>
              <el-form-item label="操作时间">
                <span>{{ props.row.createdAt }}</span>
              </el-form-item>
              <el-form-item label="操作参数" v-if="props.row.param!==undefined">
                <span>
                  <vue-json-pretty
                    deep=1
                    :data="resolveJSON(props.row.param)"
                    highlightMouseoverNode=true
                    >
                  </vue-json-pretty>
                </span>
              </el-form-item>
              <!-- <el-form-item label="操作后参数" v-if="props.row.after!==undefined">
                <span><vue-json-pretty :data="JSON.parse(props.row.after)">
              </vue-json-pretty></span>
              </el-form-item> -->
            </el-form>
          </template>
        </el-table-column>
      <div v-if="!tableStyle">
        <el-table-column label="" v-if="!tableStyle">
          <template slot-scope="scope">
            <div style="float:left">
              <div style="margin: 5px;">
                {{scope.row.createdBy===''?'未知用户':scope.row.createdBy}}
             {{decorateType(scope.row.type)}} {{decorateTarget(scope.row.target)}}
              </div>
              <div style="color: rgba(0, 0, 0, 0.45);font-size:14px;line-height: 22px;margin: 5px;">
                {{scope.row.createdAt}}
              </div>
            </div>
            <div style="text-align:rightl;float:right;display:block;margin: 5px;">
              {{decoratePage(scope.row.page)}}页
            </div>
          </template>
        </el-table-column>
      </div>
      <div v-if="tableStyle">

        <el-table-column label="页面" align="center">
          <template slot-scope="scope">
            {{decoratePage(scope.row.page)}}
          </template>
        </el-table-column>

        <el-table-column label="类型" align="center">
          <template slot-scope="scope">
            {{ decorateType(scope.row.type) }}
          </template>
        </el-table-column>

        <el-table-column label="对象" align="center">
          <template slot-scope="scope">
            {{ decorateTarget(scope.row.target) }}
          </template>
        </el-table-column>

        <el-table-column label="操作时间" prop="createdBy" align="center" sortable>
          <template slot-scope="scope">
            {{scope.row.createdBy === '' ? '未知用户':scope.row.createdBy}}
          </template>
        </el-table-column>
        <el-table-column label="操作时间" prop="executeAt" align="center" sortable>
          <template slot-scope="scope">
            {{scope.row.createdAt}}
          </template>
        </el-table-column>
      </div>
    </el-table>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :model="temp" label-position="left" label-width="70px" style="width: 400px; margin-left:50px;">
        <el-form-item label="计划名" prop="name" :rules="[{ required: dialogStatus === 'create', message: '计划名不能为空'}]">
          <el-input v-model="temp.name" />
        </el-form-item>
        <el-form-item label="类型" prop="type" :rules="[{ required: dialogStatus === 'create', message: '类型不能为空'}]">
          <el-radio-group v-model="temp.type">
          <el-radio-button label="reg"></el-radio-button>
          <el-radio-button label="dev"></el-radio-button>
          <el-radio-button label="hot"></el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="负责人" prop="manager" :rules="[{ required: dialogStatus === 'create', message: '负责人不能为空'}]">
          <el-select
            v-model="temp.manager"
            multiple
            filterable
            remote
            reserve-keyword
            placeholder="请输入负责人"
            :remote-method="remoteMethod"
            :loading="searchLoading"
            loading-text="正在加载用户">
            <el-option
              v-for="item in options"
              :key="item.id"
              :label="item.name"
              :value="item.name">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="执行日期" prop="executeAt" :rules="[{ required: dialogStatus === 'create', message: '执行日期不能为空'}]">
          <!-- <el-input :disabled="rowStatus === 'confirmed'" v-model="temp.executeAt" /> -->
          <el-date-picker
            :disabled="rowStatus === 'confirmed'"
            v-model="temp.executeAt"
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
import { getUserByKeyword } from '@api/user'
import { addPlanInfo, updatePlanInfo, deletePlan } from '@api/plan'
import VueJsonPretty from 'vue-json-pretty'
Vue.use(D2Crud)

Vue.use(pluginExport)

export default {
  props: {
    tableData: {
      default: () => []
    },
    loading: {
      default: false
    },
    tableStyle: {
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
  components: {
    VueJsonPretty
  },
  data () {
    return {
      rowId: 0,
      rowStatus: '',
      searchLoading: false,
      options: [],
      list: [],
      currentTableData: [],
      multipleSelection: [],
      textMap: {
        update: '更新计划',
        create: '新增计划',
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
    resolveJSON (data) {
      this.clog(data)
      return JSON.parse(data)
    },
    clog (data) {
      console.log('clog:', data)
      console.log('stringfy...')
      console.log(JSON.stringify(data))
      const obj = JSON.parse(data)
      console.log('obj:', obj)
    },
    decorateTarget (target) {
      switch (target) {
        case 'plan':
          return '发布计划'
        case 'plan service':
          return '服务项'
        case 'role':
          return '角色'
        case 'role resource':
          return '角色资源'
        case 'service':
          return '服务'
        case 'service relation':
          return '服务关系'
        case 'user':
          return '用户'
        case 'logger':
          return '日志级别设置'
        case 'swimlane group':
          return '路由组'
        case 'swimlane instance':
          return '服务示例'
        case 'swimlane strategy':
          return '路由策略'
        case 'protect project':
          return 'protect项目'
        case 'swimlane group service':
          return '路由组服务'
        case 'swimlane group tester':
          return '路由组测试人员'
        case 'swimlane service weight':
          return '服务实例权重'
        case 'swimlane group weight':
          return '路由组权重'
        case 'unknown':
          return '未知对象'
        case '':
          return '未知对象'
        default:
          return '未知对象'
      }
    },
    decoratePage (page) {
      switch (page) {
        case 'plan':
          return '发布计划'
        case 'role':
          return '角色'
        case 'service':
          return '服务'
        case 'user':
          return '用户'
        case 'report':
          return '报表'
        case 'logger switch':
          return '日志级别'
        case 'swimlane group':
          return '路由组'
        case 'swimlane instance':
          return '服务实例'
        case 'swimlane strategy':
          return '路由策略'
        case 'unknown':
          return '未知页面'
        case '':
          return '未知页面'
        default:
          return page
      }
    },
    decorateType (type) {
      switch (type) {
        case 'add':
          return '增加'
        case 'modify':
          return '修改'
        case 'delete':
          return '删除'
        case 'deploy':
          return '请求发布'
        case 'deploy success':
          return '发布成功'
        case 'deploy fail':
          return '发布失败'
        case 'sort':
          return '排序'
        case 'confirm':
          return '确认'
        case 'resume':
          return '重新请求发布'
        case 'terminate':
          return '终止未开始的发布'
        case 'rollback':
          return '请求回滚'
        case 'rollback success':
          return '回滚成功'
        case 'rollback fail':
          return '回滚失败'
        case 'unknown':
          return '未知操作'
        case '':
          return '未知操作'
        default:
          return type
      }
    },
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
        getUserByKeyword(query)
          .then(res => {
            _this.searchLoading = false
            _this.list = res.data
            console.log('name res:', res.data)
            console.log('search name res:', _this.list)
          })
          .catch(err => {
            _this.searchLoading = false
            _this.$notify({
              title: '书库数据请求异常'
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
      console.log('edit modal:', row.row)
      this.rowId = row.row.id
      this.rowStatus = row.row.status
      this.temp.name = row.row.name
      console.log('this.temp.name:', this.temp.name)
      this.resetTemp()
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
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
          const managerList = this.temp.manager
          console.log('planName:', this.temp.name)
          console.log('type:', this.temp.type)
          console.log(this.temp.type === '')
          console.log('managerList', managerList)
          console.log('planId:', this.rowId)
          console.log('desc:', this.temp.desc)
          var manager = ''
          if (managerList !== undefined && managerList.length === 1) {
            manager = managerList[0]
          } else if (managerList !== undefined && managerList.length > 1) {
            this.$message({
              title: '失败',
              message: '负责人只能有一个',
              type: 'error',
              duration: 2000
            })
            return
          }
          console.log('manager:', manager)
          if (this.temp.name === undefined && this.temp.type === '' && this.temp.executeAt === undefined && manager === '' && this.temp.desc === undefined) {
            this.dialogFormVisible = false
            return
          }
          // console.log('executeat num:', Date.parse(this.temp.executeAt))
          // console.log('now num:', new Date().valueOf())
          if (this.temp.executeAt !== undefined && Date.parse(this.temp.executeAt) - new Date().getTime() <= 5000) {
            console.log('wrong executeAt')
            this.$message({
              title: '失败',
              message: '执行时间不能比现在早',
              type: 'error',
              duration: 2000
            })
            return
          }
          updatePlanInfo({
            id: this.rowId,
            name: this.temp.name === undefined ? null : this.temp.name,
            type: this.temp.type === '' ? null : this.temp.type,
            executeAt: this.temp.executeAt === undefined ? null : this.temp.executeAt,
            manager: manager === '' ? null : manager,
            desc: this.temp.desc === undefined ? null : this.temp.desc
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
          const managerList = this.temp.manager
          console.log('planName:', this.temp.name)
          console.log('type:', this.temp.type)
          // console.log('managerList', managerList)
          console.log('desc:', this.temp.desc)
          console.log('executeAt:', this.temp.executeAt)
          var manager = ''
          if (managerList !== undefined && managerList.length === 1) {
            manager = managerList[0]
          } else if (managerList !== undefined && managerList.length > 1) {
            this.$message({
              title: '失败',
              message: '负责人只能有一个',
              type: 'error',
              duration: 2000
            })
            return
          }
          console.log('createAt time:', this.temp.executeAt)
          console.log('now time:', new Date().getTime())
          if (Date.parse(this.temp.executeAt) - new Date().getTime() <= 5000) {
            this.$message({
              title: '失败',
              message: '执行时间不能比现在早',
              type: 'error',
              duration: 2000
            })
            return
          }
          console.log('manager:', manager)
          if (this.temp.name === undefined && this.temp.type === '' && this.temp.executeAt === undefined && manager === '' && this.temp.desc === undefined) {
            this.dialogFormVisible = false
            return
          }
          addPlanInfo({
            name: this.temp.name === undefined ? null : this.temp.name,
            type: this.temp.type === '' ? null : this.temp.type,
            executeAt: this.temp.executeAt === undefined ? null : this.temp.executeAt,
            manager: manager === '' ? null : manager,
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
