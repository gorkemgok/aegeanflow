<template>
  <g>
    <rect class="main-rect"
          @contextmenu.prevent="nodeContextMenu"
          @click="nodeClick"
          @mousedown.prevent.left="nodeMouseDown"
          @mouseup.prevent.left="nodeMouseUp"
          :x="node.x" :y="node.y" :width="node.w" :height="node.h"
          rx="4" ry="4" :style="{fill: node.color}"></rect>
    <text class="node-label" :x="node.x" :y="labelY" font-size="11">
      {{node.label}} - {{node.definition.label}}
    </text>
    <circle v-for="(output, idx) in node.definition.outputs" :key="output.label"
            class="output-circle"
            r="6"
            :cx="ioPos.outputs[idx].x"
            :cy="ioPos.outputs[idx].y"
            @mousedown.left.prevent="outputMouseDown(output, idx, $event)"
            @contextmenu.prevent="outputContextMenu(output)">
    </circle>
    <circle v-for="(input, idx) in node.definition.inputs" :key="input.label"
            :class="'check-type-' + typeMatches[input.label]"
            class="input-circle"
            r="6"
            :cx="ioPos.inputs[idx].x"
            :cy="ioPos.inputs[idx].y"
            @contextmenu.prevent="inputContextMenu(input)"
            @mouseup.left="inputMouseUp(input)"
            @mouseover="inputMouseOver(input)"
            @mouseout="inputMouseOut(input)">
    </circle>
    <rect :x="waitPos.input.x" :y="node.y + node.h - 8" width="8" height="8"
          @mouseup.prevent="waitMouseUp"/>
    <rect :x="waitPos.output.x" :y="node.y + node.h - 8" width="8" height="8"
          @mousedown.prevent="waitMouseDown"/>
  </g>
</template>
<script>
import {POS_CALC, TYPES} from '@/helpers/node-helpers.js'

export default {
  label: 'node',
  props: {
    node: {
      type: Object,
      required: true
    },
    connectingNode: null,
    connectingOutput: null
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
    waitMouseDown: function ($event) {
      this.$emit('waitMouseDown', this.node, $event)
    },
    waitMouseUp: function ($event) {
      this.$emit('waitMouseUp', this.node, $event)
    },
    nodeMouseDown: function ($event) {
      this.$emit('nodeMouseDown', this.node, $event)
    },
    nodeMouseUp: function () {
      this.$emit('nodeMouseUp')
    },
    outputMouseDown: function (output, idx, $event) {
      this.$emit('outputMouseDown', this.node, output, {x: this.ioPos.outputs[idx].x, y: this.ioPos.outputs[idx].y}, $event)
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
    waitPos: function () {
      return POS_CALC.calculateWaitPos(this.node)
    },
    ioPos: function () {
      const pos = {inputs: [], outputs: []}
      for (let i = 0; i < this.node.definition.inputs.length; i++) {
        pos.inputs.push(POS_CALC.calculateIOPos(this.node, i, 'left'))
      }
      for (let i = 0; i < this.node.definition.outputs.length; i++) {
        pos.outputs.push(POS_CALC.calculateIOPos(this.node, i, 'side'))
      }
      return pos
    },
    labelY: function () {
      return this.node.y + this.node.h + 10
    },
    typeMatches: function () {
      const matches = {}
      this.node.definition.inputs.forEach(input => {
        if (this.connectingNode === this.node) {
          matches[input.label] = 'notr'
        } else {
          matches[input.label] = TYPES.checkType(this.connectingOutput, input)
        }
      })
      return matches
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
    font-family: 'Helvetica Neue', Helvetica, 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
  }
</style>
