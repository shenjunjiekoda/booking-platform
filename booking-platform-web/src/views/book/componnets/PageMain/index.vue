<template>
  <div>
    <el-form
      :inline="true"
      size="mini">
      <el-button type="success" size="mini" style="margin:0.1em;margin-right:1em" icon="el-icon-plus" @click="createBook()">新增书</el-button>
    </el-form>

    <el-table
      :data="currentTableData"
      v-loading="loading"
      size="mini"
      stripe
      style="width: 100%;">

      <el-table-column
        width="55">
      </el-table-column>
      <el-table-column type="expand">
      <template slot-scope="props">
        <el-form label-position="left" inline class="demo-table-expand">
          <el-form-item label="书名">
            <span>{{ props.row.name }}</span>
          </el-form-item>
          <el-form-item label="isbn">
            <span>{{ props.row.isbn }}</span>
          </el-form-item>
          <el-form-item label="作者">
            <span>{{ props.row.author }}</span>
          </el-form-item>
          <el-form-item label="出版社">
            <span>{{ props.row.press }}</span>
          </el-form-item>
          <el-form-item label="发版时间">
            <span>{{ props.row.publishedAt }}</span>
          </el-form-item>
          <el-form-item label="版次">
            <span>{{ props.row.edition }}</span>
          </el-form-item>
        </el-form>
      </template>
    </el-table-column>

      <el-table-column label="书名" align="center">
        <template slot-scope="scope">
          {{scope.row.name}}
        </template>
      </el-table-column>

      <el-table-column label="isbn" align="center">
        <template slot-scope="scope">
          {{ scope.row.isbn }}
        </template>
      </el-table-column>

      <el-table-column label="作者" align="center">
        <template slot-scope="scope">
          {{ scope.row.author }}
        </template>
      </el-table-column>

      <el-table-column label="出版社" prop="executeAt" align="center">
        <template slot-scope="scope">
          {{scope.row.press}}
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" width="300">
        <template slot-scope="row">
          <!-- <el-button size="mini" icon="el-icon-tickets" @click="goToPlanServicePage(row)">详情</el-button> -->
          <el-button :disabled="row.row.status==='completed'" type="primary" size="mini" icon="el-icon-edit" @click="editBook(row)">编辑</el-button>
          <el-popconfirm
            title="确定要删除这个书吗？"
            @onConfirm="deleteBook(row)"
          >
            <el-button :disabled="row.row.status==='completed'" type="danger" size="mini" slot="reference" icon="el-icon-delete">删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :model="temp" label-position="left" label-width="80px" style="width: 400px; margin-left:50px;">
        <div style="margin-left:12em;margin-bottom:1em">
          <el-radio-group v-model="radio" v-if="dialogStatus==='create'">
            <el-radio-button label="手动"></el-radio-button>
            <el-radio-button label="搜索"></el-radio-button>
          </el-radio-group>
        </div>
        <div v-if="radio==='搜索'" style="margin:1em;">
          <div style="margin-left:5.5em;" v-if="!showResult">
            <el-input v-model="temp.bookName" placeholder="请输入书名" @input="change($event)" />
          </div>
          <div style="margin-left:5.5em;margin-top:1em;" v-if="!showResult">
            <el-input v-model="temp.author" placeholder="请输入作者" @input="change($event)" />
          </div>
          <div style="margin-left:5.5em;margin-top:1em;" v-if="!showResult">
            <el-input v-model="temp.press" placeholder="请输入出版社" @input="change($event)" />
          </div>
          <div style="margin-left:5.5em;margin-top:1em;" v-if="!showResult">
            <el-input v-model="temp.isbn" placeholder="请输入ISBN号" @input="change($event)" />
          </div>
          <div v-if="!showResult" style="margin:1em;margin-left:12.5em;margin-bottom:2em">
            <el-button type="primary" :loading="searchLoading" icon="el-icon-search" @click=search()>搜索</el-button>
          </div>
            <el-button type="primary" v-if="showResult" @click=returnSearch()>返回搜索</el-button>
          <div style="width: 160%">
            <el-table
            v-if="showResult"
            :data="results"
            border>
            <el-table-column
              fixed
              prop="name"
              label="书名"
              :show-overflow-tooltip="true">
            </el-table-column>
            <el-table-column
              fixed
              prop="author"
              label="作者">
            </el-table-column>
            <el-table-column
              fixed
              prop="press"
              label="出版社">
            </el-table-column>
            <el-table-column
              fixed="right"
              label="操作"
              width="100">
              <template slot-scope="scope">
                <el-tooltip class="item" effect="dark" content="添加至书库" placement="top">
                  <el-button @click="handleChoose(scope.row)" type="text" size="small">选择</el-button>
                </el-tooltip>
              </template>
            </el-table-column>
          </el-table>
          </div>
        </div>
        <div v-if="radio!=='搜索'">
        <el-form-item label="书名" prop="name" :rules="[{ required: dialogStatus === 'create', message: '书名不能为空'}]">
          <el-input v-model="temp.name" @input="change($event)" />
        </el-form-item>
        <el-form-item label="ISBN号" prop="isbn" :rules="[{ required: dialogStatus === 'create', message: 'ISBN号不能为空'}]">
          <el-input v-model="temp.isbn" @input="change($event)" />
        </el-form-item>
        <el-form-item label="作者" prop="author" :rules="[{ required: dialogStatus === 'create', message: '作者不能为空'}]">
          <el-input v-model="temp.author" @input="change($event)" />
        </el-form-item>
        <el-form-item label="出版社" prop="press" :rules="[{ required: dialogStatus === 'create', message: '出版社不能为空'}]">
          <el-input v-model="temp.press" @input="change($event)" />
        </el-form-item>
        <el-form-item label="出版年月" prop="press" :rules="[{ required: dialogStatus === 'create', message: '出版年份不能为空'}]">
            <el-input placeholder="填数字" @input="change($event)"  v-model="temp.publishedAtYear" style="width: 8em;float:left;"/><span style="margin-left:1em;">年</span>
        </el-form-item>
        <el-form-item label="" prop="press" :rules="[{ required: dialogStatus === 'create', message: '出版月份不能为空'}]">
            <el-input placeholder="填数字" @input="change($event)"  v-model="temp.publishedAtMonth" style="width: 8em;float:left;"/><span style="margin-left:1em;">月</span>
        </el-form-item>
        <el-form-item label="版次" prop="edition" placeholder="填数字">
          <el-input v-model="temp.edition" @input="change($event)" />
        </el-form-item>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          Cancel
        </el-button>
        <el-button v-if="radio!=='搜索'" type="primary" @click="dialogStatus==='create'?createData():updateData()">
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
import { addBook, updateBook, deleteBookById, searchFromWeb } from '@api/book'

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
      showResult: false,
      choosed: false,
      radio: '手动',
      rowId: 0,
      rowStatus: '',
      searchLoading: false,
      options: [],
      list: [],
      results: [],
      currentTableData: [],
      multipleSelection: [],
      textMap: {
        update: '更新书',
        create: '新增书',
        type: ''
      },
      temp: {
        name: '',
        isbn: '',
        author: '',
        press: '',
        edition: 0,
        publishedAtYear: 2020,
        publishedAtMonth: 0
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
    returnSearch () {
      this.showResult = false
    },
    handleChoose (row) {
      console.log('choose row:', row)
      const publishedAt = row.publishedAt.split('.')
      const publishedAtYear = publishedAt[0]
      const publishedAtMonth = publishedAt[1]
      addBook({
        name: row.name === undefined ? null : row.name,
        isbn: row.isbn === undefined ? null : row.isbn,
        author: row.author === undefined ? null : row.author,
        press: row.press === undefined ? null : row.press,
        publishedAtYear: publishedAtYear === undefined ? null : publishedAtYear,
        publishedAtMonth: publishedAtMonth === undefined ? null : publishedAtMonth
      }).then(res => {
        console.log('addBook res:', res)
        if (res.errorCode === 0) {
          console.log('addBook success!')
          this.dialogFormVisible = false
          this.$notify({
            title: 'OK',
            message: '从网络搜索添加新书至书库成功',
            type: 'success',
            duration: 2000
          })
          this.$emit('reload', {})
        } else {
          this.$message({
            title: '添加书库失败',
            message: res.msg,
            type: 'error',
            duration: 2000
          })
        }
      }).catch(err => {
        console.log('err: ', err)
      })
    },
    search () {
      const bookName = this.temp.bookName
      const author = this.temp.author
      const press = this.temp.press
      const isbn = this.temp.isbn
      console.log('now bookName:', bookName)
      console.log('now author:', author)
      console.log('now press:', press)
      console.log('now isbn:', isbn)
      if (bookName !== undefined || author !== undefined || press !== undefined || isbn !== undefined) {
        this.searchLoading = true
        searchFromWeb({
          bookName: bookName,
          author: author,
          press: press,
          isbn: isbn
        })
          .then(res => {
            this.searchLoading = false
            this.results = res.data
            // console.log('name res:', res.data)
            console.log('search from web res:', this.results)
            if (this.results.length === 0) {
              this.$notify({
                title: '无结果！'
              })
            }
            this.showResult = true
          })
          .catch(err => {
            this.searchLoading = false
            this.$notify({
              title: '数据请求异常'
            })
            console.log('err', err)
          })
      } else {
        this.$notify({
          title: '请至少输入一项！'
        })
      }
    },
    change (e) {
      this.$forceUpdate()
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
    createBook () {
      this.resetTemp()
      this.rowStatus = ''
      this.showResult = false
      this.results = []
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    editBook (row) {
      this.resetTemp()
      this.radio = '手动'
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
      console.log('edit modal:', row.row)
      this.rowId = row.row.id
      this.rowStatus = row.row.status
      this.temp.name = row.row.name
      this.temp.isbn = row.row.isbn
      this.temp.author = row.row.author
      this.temp.press = row.row.press
      this.temp.edition = row.row.edition
      const publishedAt = row.row.publishedAt.split('.')
      this.temp.publishedAtYear = publishedAt[0]
      this.temp.publishedAtMonth = publishedAt[1]
    },
    deleteBook (row) {
      console.log('deleteBook id:', row.row.id)
      deleteBookById(row.row.id)
        .then(res => {
          console.log('deleteBook res:', res)
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
          console.log('bookName:', this.temp.name)
          console.log('isbn:', this.temp.isbn)
          console.log('author:', this.temp.author)
          console.log('press:', this.temp.press)
          console.log('publishedAtYear:', this.temp.publishedAtYear)
          console.log('publishedAtMonth:', this.temp.publishedAtMonth)
          console.log('edition:', this.temp.edition)
          updateBook({
            id: this.rowId,
            name: this.temp.name === undefined ? null : this.temp.name,
            isbn: this.temp.isbn === undefined ? null : this.temp.isbn,
            author: this.temp.author === undefined ? null : this.temp.author,
            press: this.temp.press === undefined ? null : this.temp.press,
            publishedAtYear: this.temp.publishedAtYear === undefined ? null : this.temp.publishedAtYear,
            publishedAtMonth: this.temp.publishedAtMonth === undefined ? null : this.temp.publishedAtMonth,
            edition: this.temp.edition === undefined ? null : this.temp.edition
          }).then(res => {
            console.log('updateBook res:', res)
            if (res.errorCode === 0) {
              console.log('updateBook success!')
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
          console.log('bookName:', this.temp.name)
          console.log('isbn:', this.temp.isbn)
          console.log('author:', this.temp.author)
          console.log('press:', this.temp.press)
          console.log('publishedAtYear:', this.temp.publishedAtYear)
          console.log('publishedAtMonth:', this.temp.publishedAtMonth)
          console.log('edition:', this.temp.edition)
          addBook({
            name: this.temp.name === undefined ? null : this.temp.name,
            isbn: this.temp.isbn === undefined ? null : this.temp.isbn,
            author: this.temp.author === undefined ? null : this.temp.author,
            press: this.temp.press === undefined ? null : this.temp.press,
            publishedAtYear: this.temp.publishedAtYear === undefined ? null : this.temp.publishedAtYear,
            publishedAtMonth: this.temp.publishedAtMonth === undefined ? null : this.temp.publishedAtMonth,
            edition: this.temp.edition === undefined ? null : this.temp.edition
          }).then(res => {
            console.log('addBook res:', res)
            if (res.errorCode === 0) {
              console.log('addBook success!')
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
    }
  }
}
</script>
