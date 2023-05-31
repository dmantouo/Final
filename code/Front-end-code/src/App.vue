<template>
  <div id="app">
    <router-view v-if="isRouterAlive"/>
  </div>
</template>

<script>

export default {
    name: 'app',
    provide(){
        return{
            reload:this.reload
        }
    },
    data(){
      return {
          isRouterAlive:true
      }
    },
    methods:{
        reload(){
            this.isRouterAlive=false;
            this.$nextTick(function () {
                this.isRouterAlive=true;
            })
        }
    }
}
</script>

<style>
  body {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
  }
  li, ul, p, div, body, html, table {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
  }
  html, body,#app{
    height: 100%;
    margin: 0;
    padding: 0;
  }
  li {
    list-style: none;
  }
</style>
