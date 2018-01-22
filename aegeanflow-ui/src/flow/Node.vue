<template>
  <g>
    <rect class="main-rect"
          @contextmenu.prevent="nodeContextMenu"
          @click="nodeClick"
          @mousedown.left="nodeMouseDown"
          @mouseup.left="nodeMouseUp"
          :x="node.x" :y="node.y" :width="node.w" :height="node.h"
          rx="4" ry="4" :style="{fill: node.color}"></rect>
    <text class="node-label" :x="node.x" :y="labelY" font-family="Verdana" font-size="11">
      {{node.definition.label}}
    </text>
    <circle class="output-circle"
            r="6"
            :cx="outputPos.x"
            :cy="outputPos.y"
            @mousedown.left="outputMouseDown"
            @contextmenu.prevent="outputContextMenu">
    </circle>
    <circle v-for="(input, idx) in node.definition.inputs" :key="input.name"
            :class="'check-type-' + typeMatches[input.name]"
            class="input-circle"
            r="6"
            :cx="node.x"
            :cy="inputY[idx]"
            @contextmenu.prevent="inputContextMenu(input)"
            @mouseup.left="inputMouseUp(input)"
            @mouseover="inputMouseOver(input)"
            @mouseout="inputMouseOut(input)">
    </circle>
  </g>
</template>
<script>
import {POS_CALC, TYPES} from '@/helpers/node-helpers.js'

export default {
  name: 'node',
  props: {
    node: {
      type: Object,
      required: true
    },
    connectingNode: null
  },
  methods: {
    nodeContextMenu: function () {
      this.$emit('nodeContextMenu', this.node)
    },
    outputContextMenu: function () {
      this.$emit('outContextMenu', this.node)
    },
    inputContextMenu: function (input) {
      this.$emit('inputContextMenu', input, this.node)
    },
    nodeClick: function () {
      this.$emit('nodeClick', this.node)
    },
    nodeMouseDown: function ($event) {
      this.$emit('nodeMouseDown', this.node, $event)
    },
    nodeMouseUp: function () {
      this.$emit('nodeMouseUp')
    },
    outputMouseDown: function ($event) {
      this.$emit('outputMouseDown', this.node, {x: this.outputPos.x, y: this.outputPos.y}, $event)
    },
    inputMouseUp: function (input) {
      this.$emit('inputMouseUp', this.node, input)
    },
    inputMouseOver: function (input) {
      this.$emit('inputMouseOver', input)
    },
    inputMouseOut: function (input) {
      this.$emit('inputMouseOut', input)
    },
    calculateInputX: function (idx) {
      return this.node.x
    },
    calculateInputY: function (idx) {
      return this.node.y + (14 * idx)
    },
    calculateLabelY: function () {
      return this.node.y + this.node.h + 10
    }
  },
  computed: {
    inputY: function () {
      const inputYArr = []
      for (let i = 0; i < this.node.definition.inputs.length; i++) {
        inputYArr.push(this.node.y + (14 * i))
      }
      return inputYArr
    },
    labelY: function () {
      return this.node.y + this.node.h + 10
    },
    typeMatches: function () {
      const matches = {}
      this.node.definition.inputs.forEach(input => {
        if (this.connectingNode === this.node) {
          matches[input.name] = 'notr'
        } else {
          matches[input.name] = TYPES.checkType(this.connectingNode, input)
        }
      })
      return matches
    },
    outputPos: function () {
      return POS_CALC.calculateOutputPos(this.node)
    }
  },
  created: function () {
  }
}
</script>
<style scoped>
  .check-type-ok{
    fill: #8ff762;
  }
  .check-type-not-ok{
    fill: #fc3535;
  }
  .check-type-notr{
    fill: white;
  }
  .main-rect{
    stroke:silver;
    stroke-width:1;
  }
  .input-circle{
    stroke:silver;
    stroke-width:1;
  }
  .output-circle{
    fill:white;
    stroke:silver;
    stroke-width:1;
  }
  .node-label{
    fill: white;
  }
</style>
