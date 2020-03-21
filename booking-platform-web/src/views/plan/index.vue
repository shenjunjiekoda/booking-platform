<template>
  <d2-container>
    <demo-page-header
      slot="header"
      @submit="handleSubmit"
      ref="header"/>
    <demo-page-main
      :table-data="table"
      @reload="handleSubmit"
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
import { getPlanList } from '@api/plan'

export default {
  // name 值和本页的 $route.name 一致才可以缓存页面
  name: 'plan',
  components: {
    'DemoPageHeader': () => import('./componnets/PageHeader'),
    'DemoPageMain': () => import('./componnets/PageMain'),
    'DemoPageFooter': () => import('./componnets/PageFooter')
  },
  data () {
    return {
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
      this.loading = true
      if (form !== {}) {
        if (form.type === 'all') {
          form.type = null
        }
        if (form.status === 'all') {
          form.status = null
        }
        if (form.createdAt != null) {
          form.createdAtFrom = form.createdAt[0]
          form.createdAtTo = form.createdAt[1]
          form.createdAt = null
        }
      } else {
        form = {}
      }
      // this.$notify({
      //   title: '开始请求表格数据'
      // })
      getPlanList({
        ...form,
        ...this.page
      })
        .then(res => {
          console.log('getplan res:', res.data)
          this.loading = false
          this.table = res.data.items
          this.page.pageTotal = res.data.total
          this.page.pageCurrent = res.data.pageCurrent
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
    }
  },
  mounted () {
    this.handleSubmit({})
  }
}
</script>
