<template>
  <d2-container>
    <demo-page-header
      slot="header"
      @submit="handleSubmit"
      ref="header"/>
    <demo-page-main
      :isAdmin="isAdmin"
      :table-data="table"
      :planStatus="planStatus"
      @reload="refresh"
      :classId="classId"
      :planId="planId"
      :loading="loading"/>
    <demo-page-footer
      slot="footer"
      :current="page.pageCurrent"
      :size="page.pageSize"
      :total="page.pageTotal"
      @change="handlePaginationChange"/>
  </d2-container>
</template>

<script>
import { getPlanBookList } from '@api/planbook'
import { getExactPlanInfo } from '@api/plan'
import util from '@/libs/util.js'

export default {
  // name 值和本页的 $route.name 一致才可以缓存页面
  name: 'planbook',
  components: {
    'DemoPageHeader': () => import('./componnets/PageHeader'),
    'DemoPageMain': () => import('./componnets/PageMain'),
    'DemoPageFooter': () => import('./componnets/PageFooter')
  },
  beforeRouteEnter (to, from, next) {
    const id = to.params.id
    console.log('id:', id)
    next(async instance => {
      if (from.name === 'plan') {
        console.log('from plan')
        // await instance.getFormData(id)
        instance.loadDataFromDb()
      } else {
        console.log('not from plan...')
        // instance.loadDataFromDb(to)
        instance.loadDataFromDb()
      }
    })
  },
  data () {
    return {
      isAdmin: 0,
      classId: 0,
      year: 2020,
      semester: '第一学期',
      planName: '',
      planId: 0,
      planStatus: '',
      table: [],
      loading: false,
      page: {
        pageCurrent: 1,
        pageSize: 10,
        pageTotal: 0
      }
    }
  },
  methods: {
    addService () {

    },
    goBack () {
      this.$router.push({
        name: 'plan'
      })
    },
    refresh () {
      console.log('refresh')
      console.log('cur classId:', this.classId)
      this.handleSubmit({
        classId: [this.classId],
        year: this.year,
        semester: this.semester,
        status: '全部'
      })
    },
    loadDataFromDb () {
      // console.log('load id:', id)
      // console.log('load name:', planName)\
      // this.getPlanInfo(id)
      // getPlanServiceList({
      //   planId: id,
      //   ...this.page
      // })
      //   .then(res => {
      //     console.log('getplanservice res:', res.data.items)
      //     this.loading = false
      //     // this.$notify({
      //     //   title: '表格数据请求完毕'
      //     // })
      //     this.table = res.data.items
      //     this.page.pageTotal = res.data.total
      //     if (res.errorCode !== 0) {
      //       this.$notify({
      //         title: '异常信息: ' + res.msg
      //       })
      //     }
      //   })
      //   .catch(err => {
      //     this.loading = false
      //     console.log('err', err)
      //   })
    },
    getPlanInfo (id) {
      getExactPlanInfo(id)
        .then(res => {
          console.log('getExactplanInfo res:', res.data)
          this.loading = false
          this.planName = res.data.name
          this.planId = res.data.id
          this.planStatus = res.data.status
          if (res.errorCode !== 0) {
            this.$notify({
              title: '异常信息: ' + res.msg
            })
          }
        })
        .catch(err => {
          this.loading = false
          console.log('err', err)
        })
    },
    handlePaginationChange (val) {
      // this.$notify({
      //   title: '分页变化',
      //   message: `当前第${val.current}页 共${val.total}条 每页${val.size}条`
      // })
      this.page = {
        pageCurrent: val.current,
        pageSize: val.size,
        pageTotal: val.total
      }
      // nextTick 只是为了优化示例中 notify 的显示
      this.$nextTick(() => {
        this.$refs.header.handleFormSubmit()
      })
    },
    handleSubmit (form) {
      console.log('request form:', form)
      if (form.classId === null || form.classId === undefined || form.classId === '' || form.classId.length === 0) {
        this.$notify.warning({
          title: '错误',
          message: '请选择班级！'
        })
        return
      }
      var classId = 0
      if (form.classId.length === 1) {
        classId = form.classId[0]
      } else {
        this.$notify.warning({
          title: '错误',
          message: '请选择一个班级！'
        })
        return
      }
      this.classId = classId
      if (form.status === undefined) {
        this.$notify.warning({
          title: '错误',
          message: '请选择一个状态！'
        })
        return
      }
      var status = ''
      switch (form.status) {
        case '全部':
          status = 'all'
          break
        case '已提交':
          status = 'submitted'
          break
        case '待提交':
          status = 'new'
          break
        case '已到货':
          status = 'instock'
          break
        case '货不足':
          status = 'notinstock'
          break
        default:
          return
      }
      const re = /^[0-9]+.?[0-9]*/
      if (form.year === undefined || form.year === null || form.year === '' || !re.test(form.year)) {
        this.$notify.warning({
          title: '错误',
          message: '请输入一个学年！必须是数字如：2020'
        })
        return
      }
      var semester = 1
      if (form.semester === '第一学期') {
        semester = 1
      } else {
        semester = 2
      }
      console.log('semester:', semester)
      this.classId = classId
      this.semester = form.semester
      this.year = form.year
      console.log('request classId:', classId)
      this.loading = true
      getPlanBookList({
        classId: classId,
        status: status,
        year: form.year,
        semester: semester
      })
        .then(res => {
          console.log('getPlanBookList res:', res.data)
          this.loading = false
          this.table = res.data.items
          this.page.pageTotal = res.data.total
          this.page.pageCurrent = res.data.pageNo
          this.page.pageSize = res.data.pageSize
          if (res.errorCode !== 0) {
            this.$notify({
              title: '异常信息: ' + res.msg
            })
          }
        })
        .catch(err => {
          this.loading = false
          console.log('err', err)
        })
    },
    checkIsAdmin () {
      this.isAdmin = util.cookies.get('isAdmin')
      console.log('check isAdmin:', this.isAdmin)
    }
  },
  mounted () {
    // this.refresh()
    this.checkIsAdmin()
  }
}
</script>
