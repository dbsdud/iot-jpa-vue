<template>
  <div class="container">
    <h1>About Page</h1>
    <button @click="getData">Test</button>
    <h3>{{ testData }}</h3>
  </div>
</template>

<script>
export default {
  name: "About",
  methods: {
    getData() {
      this.$axios.get('/api').then(res => {
        console.log(res.data)
        this.testData = res.data;
      })
    }
  },
  data() {
    return {
      testData: '',
    }
  }
}
</script>

<style scoped>

</style>