<template>
  <g>
    <text ref="dummyLabel" v-show="false" x="0" y="0" font-family="Verdana" font-size="11">
      {{node.definition.label}}
    </text>
    <rect class="main-rect" @mousedown="nodeMouseDown" @mouseup="nodeMouseUp"
          :x="node.x" :y="node.y" :width="node.w" :height="node.h"
          rx="4" ry="4" ></rect>
    <text :x="node.x" :y="calculateLabelY()" font-family="Verdana" font-size="11">
      {{node.definition.label}}
    </text>
    <circle class="output-circle"
            r="6"
            :cx="outputPos.x"
            :cy="outputPos.y"
            @mousedown="outputMouseDown">
    </circle>
    <circle v-for="(input, idx) in node.definition.inputs" :key="input.name"
            class="output-circle"
            r="6"
            :cx="calculateInputX(idx)"
            :cy="calculateInputY(idx)"
            @mouseup="inputMouseUp(input)">
    </circle>
  </g>
</template>
<script>
import {POS_CALC} from '@/helpers/node-helpers.js'

export default {
  name: 'node',
  props: {
    node: {
      type: Object,
      required: true
    }
  },
  methods: {
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
    outputPos: function () {
      return POS_CALC.calculateOutputPos(this.node)
    }
  },
  created: function () {
  }
}
</script>
<style scoped>
  .main-rect{
    fill:lightgray;
    stroke:silver;
    stroke-width:1;
  }
  .output-circle{
    fill:black;
    stroke:silver;
    stroke-width:1;
  }
</style>
