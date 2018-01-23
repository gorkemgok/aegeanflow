<template>
  <div class="flow-container">
    <at-input v-model="title"/>
    <drop @drop="dropNode">
      <svg @mouseup="mouseUp" @mousemove="mouseMove" class="editor-canvas">
        <!--Temporary connection-->
        <template v-if="connectingNode">
          <path class="temporary-connection rolling-dashes"
                :d="calculateConnectionPoints({source: connectingSourcePos, target: connectingTargetPos}, 1)">
          </path>
        </template>
        <!--Connections-->
        <template v-for="(connection, idx) in connections">
          <path class="connection" v-if="connection.type == 'line'"
                :key="connection.uuid"
                :class="isSelectedConnArr[idx] ? 'selected-connection' : ''"
                :d="calculateConnectionPoints(connection)"
                @click="selectConnection(connection)"
                @contextmenu.prevent="connectionContextMenu(connection)">
          </path>
        </template>
        <!--Nodes-->
        <template v-for="node in nodes">
          <node :key="node.uuid"
                :node="node"
                :connectingNode="connectingNode"
                @nodeContextMenu="nodeContextMenu"
                @nodeClick="nodeClick"
                @nodeMouseDown="beginDrag"
                @nodeMouseUp="endDrag"
                @outputMouseDown="beginConnection"
                @inputMouseUp="endConnection"
                @inputMouseOver="inputMouseOver"
                @inputMouseOut="inputMouseOut">
          </node>
        </template>
      </svg>
    </drop>
    <context-menu ref="nodeCtxMenu">
      <at-menu mode="vertical" @on-select="nodeMenuClicked">
        <at-menu-item name="remove"><i class="icon icon-x"></i>Remove</at-menu-item>
      </at-menu>
    </context-menu>
    <context-menu ref="connectionCtxMenu">
      <at-menu mode="vertical" @on-select="connectionMenuClicked">
        <at-menu-item name="remove"><i class="icon icon-x"></i>Remove</at-menu-item>
      </at-menu>
    </context-menu>
  </div>
</template>
<script>
import Node from '@/flow/Node'
import { uuid } from 'vue-uuid'
import { POS_CALC, TYPES } from '@/helpers/node-helpers.js'
import { HTTP } from '@/helpers/http-helpers.js'
import contextMenu from 'vue-context-menu'
import Vue from 'vue'
import VueDragDrop from 'vue-drag-drop'

Vue.use(VueDragDrop)

export default {
  name: 'Flow',
  components: {Node, contextMenu},
  props: {
    flow: {
      type: Object,
      required: true
    }
  },
  data: function () {
    return {
      percent: 30,
      uuid: null,
      title: null,
      nodes: [],
      connections: [],
      selectedNode: null,
      selectedConnection: null,
      draggingNode: null,
      dragOffset: {
        x: 0,
        y: 0
      },
      connectingNode: null,
      connectingSourcePos: {
        x: 0,
        y: 0
      },
      connectingTargetPos: {
        x: 0,
        y: 0
      }
    }
  },
  computed: {
    candidateType: function () {
      if (this.connectingNode) {
        return this.connectingNode.returnType
      }
    },
    isSelectedConnArr: function () {
      const arr = []
      for (let connection of this.connections) {
        arr.push(connection === this.selectedConnection)
      }
      return arr
    }
  },
  methods: {
    dropNode: function (nodeDef, $event) {
      const x = $event.offsetX
      const y = $event.offsetY
      this.addNode(nodeDef, x, y)
    },
    nodeMenuClicked: function (name) {
      switch (name) {
        case 'remove':
          this.removeSelectedNode()
          break
        default:
          break
      }
    },
    connectionMenuClicked: function (name) {
      switch (name) {
        case 'remove':
          this.removeSelectedConnection()
          break
        default:
          break
      }
    },
    runFlow: function () {
      const flow = TYPES.createFlow(this.uuid, this.title, this.nodes, this.connections)
      HTTP.post('/flow/run', flow).then(res => {
        console.log(res)
      })
    },
    saveFlow: function () {
      const flow = TYPES.createFlow(this.uuid, this.title, this.nodes, this.connections)
      HTTP.post('/flow', flow).then(res => {
        console.log(res)
      })
    },
    addNode: function (nodeDef, x = 50, y = 50) {
      const colors = ['#F9D194', '#FF6C57', '#977CD5', '#54B375']
      const node = {
        uuid: uuid.v1(),
        type: nodeDef.type,
        x: x,
        y: y,
        w: 50,
        h: 50,
        color: colors[Math.floor(Math.random() * 3)],
        configuration: {},
        definition: nodeDef
      }
      this.nodes.push(node)
    },
    connectNodes: function (sourceNode, targetNode, targetInput) {
      if (sourceNode === targetNode) {
        return
      }
      for (let i = 0; i < this.connections.length; i++) {
        if (this.connections[i].target === targetNode) {
          return
        }
      }
      if (TYPES.checkType(sourceNode, targetInput) === 'ok') {
        const connection = {
          uuid: uuid.v1(),
          type: 'line',
          source: sourceNode,
          target: targetNode,
          inputName: targetInput.name
        }
        this.connections.push(connection)
      }
    },
    connectionContextMenu: function (connection) {
      this.selectedConnection = connection
      this.$refs.connectionCtxMenu.open()
    },
    nodeContextMenu: function (node) {
      console.log(node)
      this.selectedNode = node
      this.$refs.nodeCtxMenu.open()
    },
    nodeClick: function (node) {
      this.selectedNode = node
    },
    selectConnection: function (connection) {
      this.selectedConnection = connection
    },
    removeSelectedNode: function () {
      this.nodes = this.nodes.filter(node => node !== this.selectedNode)
      this.connections = this.connections
        .filter(connection => connection.source !== this.selectedNode && connection.target !== this.selectedNode)
    },
    removeSelectedConnection: function () {
      this.connections = this.connections
        .filter(connection => connection !== this.selectedConnection)
    },
    beginDrag: function (node, $event) {
      this.dragOffset.x = node.x - $event.offsetX
      this.dragOffset.y = node.y - $event.offsetY
      this.draggingNode = node
      this.mouseMove($event)
    },
    endDrag: function () {
      this.draggingNode = null
    },
    inputMouseOver: function (input) {
      if (this.connectingNode) {
        this.$set(input, 'candidateType', this.connectingNode.definition.returnType)
      }
    },
    inputMouseOut: function (input) {
      this.$set(input, 'candidateType', null)
    },
    mouseMove: function ($event) {
      if (this.draggingNode) {
        this.draggingNode.x = $event.offsetX + this.dragOffset.x
        this.draggingNode.y = $event.offsetY + this.dragOffset.y
      }
      if (this.connectingNode) {
        this.connectingTargetPos.x = $event.offsetX
        this.connectingTargetPos.y = $event.offsetY
      }
    },
    mouseUp: function () {
      this.endDrag()
      this.endConnection()
    },
    beginConnection: function (sourceNode, sourcePos, $event) {
      this.connectingNode = sourceNode
      this.connectingSourcePos.x = sourcePos.x
      this.connectingSourcePos.y = sourcePos.y
      this.mouseMove($event)
    },
    endConnection: function (targetNode, input, $event) {
      if (targetNode && this.connectingNode) {
        this.connectNodes(this.connectingNode, targetNode, input)
      }
      this.connectingNode = null
    },
    calculateConnectionPoints: function (connection, padding = 0, bezier = true) {
      let outputPos = connection.source
      let inputPos = connection.target
      if (connection.uuid) {
        outputPos = POS_CALC.calculateOutputPos(connection.source)
        inputPos = POS_CALC.calculateInputPos(connection.target, connection.inputName)
      }
      if (bezier) {
        return this._calculateBezierPoints(outputPos.x, outputPos.y, inputPos.x + padding, inputPos.y + padding)
      }
      return this._calculatePolylinePoints(outputPos.x, outputPos.y, inputPos.x + padding, inputPos.y + padding)
    },
    _calculatePolylinePoints: function (x1, y1, x2, y2) {
      const cx = x2 + ((x1 - x2) / 2)
      return `${x1},${y1} ${cx},${y1} ${cx},${y2} ${x2},${y2}`
    },
    _calculateBezierPoints: function (x1, y1, x2, y2) {
      const cx = x2 + ((x1 - x2) / 2)
      return `M${x1},${y1} C${cx},${y1} ${cx},${y2} ${x2},${y2}`
    },
    click: function (object) {
    }
  },
  created: function () {
    this.uuid = this.flow.uuid
    this.title = this.flow.title
    this.nodes = this.flow.nodes
    this.connections = this.flow.connections
  }
}
</script>
<style>
  .editor-canvas{
    border: 1px silver solid;
    box-shadow: #111111;
    width: 100%;
    height: 800px;
    background-image: url("../../static/grid-bg.jpg");
    background-size: 2%;
  }
  .temporary-connection{
    fill:none;stroke:white;stroke-width:3;
  }
  .selected-connection{
    stroke-dasharray: 5;
    stroke-dasharray:"5, 5";
    animation: dash 2s linear;
    animation-iteration-count: infinite;
  }
  .rolling-dashes{
    stroke-dasharray: 5;
    stroke-dasharray:"5, 5";
    animation: dash 2s linear;
    animation-iteration-count: infinite;
  }
  @keyframes dash {
    to {
      stroke-dashoffset: -50;
    }
  }

  .connection{
    fill:none;stroke:white;stroke-width:3
  }
  svg text {
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
  }
  svg text::selection {
    background: none;
  }
  .flow-container{
    width: 100%;
  }
</style>
