<template>
  <d2-container>
    <demo-page-header
      slot="header"
      @reload="handleSubmit"
      @submit="handleSubmit"
      @changeStyle="handleChangeStyle"
      ref="header"/>
    <demo-page-main
      :table-data="table"
      :table-style="tableStyle"
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
import { getMessages } from '@api/message'
export default {
  // name 值和本页的 $route.name 一致才可以缓存页面
  name: 'message',
  components: {
    'DemoPageHeader': () => import('./componnets/PageHeader'),
    'DemoPageMain': () => import('./componnets/PageMain'),
    'DemoPageFooter': () => import('./componnets/PageFooter')
  },
  data () {
    return {
      table: [],
      tableStyle: false,
      loading: false,
      page: {
        pageCurrent: 1,
        pageSize: 10,
        pageTotal: 0
      }
    }
  },
  methods: {
    handleChangeStyle (val) {
      console.log('main handle:', val)
      this.tableStyle = val
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
      console.log('log request form:', form)
      console.log('page form:', this.page)
      this.loading = true
      form.createdAtFrom = undefined
      form.createdAtTo = undefined
      if (form.createdAt !== undefined && form.createdAt.length === 2) {
        form.createdAtFrom = form.createdAt[0]
        form.createdAtTo = form.createdAt[1]
      }
      getMessages({
        createdAtFrom: form.createdAtFrom === undefined ? null : form.createdAtFrom,
        createdAtTo: form.createdAtTo === undefined ? null : form.createdAtTo,
        pageCurrent: this.page.pageCurrent,
        pageSize: this.page.pageSize
      })
        .then(res => {
          console.log('getMessages res:', res.data)
          this.loading = false
          if (res.errorCode === 0) {
            console.log('getMessages data:', res.data.items)
            this.table = res.data.items
            this.page.pageTotal = res.data.total
            this.page.pageCurrent = res.data.pageNo
            this.page.pageSize = res.data.pageSize
            // console.log('afeter getlog page:', this.page)
          } else {
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
