<template>
  <div class="hello">
    <button @click="toogleComp">ToggleComp</button>
    <component :is="dynamicComponent"></component>
  </div>
</template>

<script>
import Vue from 'vue'

export default {
  label: 'HelloWorld',
  data () {
    return {
      dynamicComponent: 'component-1'
    }
  },
  methods: {
    toogleComp () {
      if (this.dynamicComponent === 'component-1') {
        this.dynamicComponent = 'component-2'
      } else {
        this.dynamicComponent = 'component-1'
      }
    }
  },
  created () {
    Vue.component('component-1', {
      template: '<p>Wheee</p>',
      data () {
        return {
          msg: ''
        }
      }
    })
    Vue.component('component-2', {
      template: '<p>Wheee2</p>'
    })
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
