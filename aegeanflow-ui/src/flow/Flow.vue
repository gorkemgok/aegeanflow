<template>
  <div class="sessionProxy-container">
    <at-input v-model="title"/>
    <at-modal v-for="node in nodes" :key="node.uuid" v-model="nodeModal[node.uuid]" :title="node.definition.label + ' Configuration'">
      <p v-for="config in node.definition.configurations" :key="config.label">
        <label :for="config.label">{{config.label}}</label>
        <at-input :label="config.label" v-model="node.configuration[config.label]"></at-input>
      </p>
    </at-modal>
    <drop @drop="dropNode">
      <svg @mouseup="mouseUp" @mousemove="mouseMove" class="editor-canvas">
        <!--Temporary connection-->
        <template v-if="connectingNode && connectingOutput">
          <path class="temporary-connection rolling-dashes"
                :d="calculateConnectionPoints({source: connectingSourcePos, target: connectingTargetPos}, 1)">
          </path>
        </template>
        <template v-if="connectingNode && !connectingOutput">
          <path class="temporary-wait-connection rolling-dashes"
                :d="calculateConnectionPoints({source: connectingSourcePos, target: connectingTargetPos}, 1)">
          </path>
        </template>
        <!--Connections-->
        <template v-for="(connection, idx) in connections">
          <path class="connection" v-if="connection.type == 'FLOW'"
                :key="connection.uuid"
                :class="isSelectedConnArr[idx] ? 'selected-connection' : ''"
                :d="calculateConnectionPoints(connection)"
                @click="selectConnection(connection)"
                @contextmenu.prevent="connectionContextMenu(connection)">
          </path>
          <path class="wait-connection" v-else-if="connection.type == 'WAIT'"
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
                :connectingOutput="connectingOutput"
                @nodeContextMenu="nodeContextMenu"
                @nodeClick="nodeClick"
                @nodeMouseDown="beginDrag"
                @nodeMouseUp="endDrag"
                @outputMouseDown="beginConnection"
                @inputMouseUp="endConnection"
                @inputMouseOver="inputMouseOver"
                @inputMouseOut="inputMouseOut"
                @waitMouseDown="beginWaitConnection"
                @waitMouseUp="endWaitConnection">
          </node>
        </template>
      </svg>
    </drop>
    <context-menu ref="nodeCtxMenu" style="margin-left:-200px">
      <at-menu mode="vertical" @on-select="nodeMenuClicked">
        <at-menu-item label="configure"><i class="icon icon-settings"></i>Configure</at-menu-item>
        <at-menu-item label="remove"><i class="icon icon-x"></i>Remove</at-menu-item>
      </at-menu>
    </context-menu>
    <context-menu ref="connectionCtxMenu">
      <at-menu mode="vertical" @on-select="connectionMenuClicked">
        <at-menu-item label="remove"><i class="icon icon-x"></i>Remove</at-menu-item>
      </at-menu>
    </context-menu>
  </div>
</template>
<script>
import Node from '@/sessionProxy/Node'
import { uuid } from 'vue-uuid'
import { POS_CALC, TYPES } from '@/helpers/node-helpers.js'
import { HTTP } from '@/helpers/http-helpers.js'
import contextMenu from 'vue-context-menu'
import Vue from 'vue'
import VueDragDrop from 'vue-drag-drop'

Vue.use(VueDragDrop)

export default {
  label: 'Flow',
  components: {
    Node,
    contextMenu
  },
  props: {
    sessionProxy: {
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
      connectingOutput: null,
      connectingSourcePos: {
        x: 0,
        y: 0
      },
      connectingTargetPos: {
        x: 0,
        y: 0
      },
      nodeModal: {}
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
    nodeMenuClicked: function (label) {
      switch (label) {
        case 'remove':
          this.removeSelectedNode()
          break
        case 'configure':
          this.$set(this.nodeModal, this.selectedNode.uuid, true)
          break
        default:
          break
      }
    },
    connectionMenuClicked: function (label) {
      switch (label) {
        case 'remove':
          this.removeSelectedConnection()
          break
        default:
          break
      }
    },
    runFlow: function () {
      const sessionProxy = TYPES.createFlow(this.uuid, this.title, this.nodes, this.connections)
      HTTP.post('/sessionProxy/run', sessionProxy).then(res => {
        console.log(res)
      })
    },
    saveFlow: function () {
      const sessionProxy = TYPES.createFlow(this.uuid, this.title, this.nodes, this.connections)
      HTTP.post('/sessionProxy', sessionProxy).then(res => {
        console.log(res)
      })
    },
    addNode: function (nodeDef, x = 50, y = 50) {
      const colors = ['#F9D194', '#FF6C57', '#977CD5', '#54B375']
      const node = {
        uuid: uuid.v1(),
        label: 'Node #' + this.getMaxNodeNumber(),
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
    getMaxNodeNumber: function () {
      let max = 0
      this.nodes.forEach(node => {
        try {
          let no = node.label.split('#')[1]
          console.log(no)
          max = Math.max(max, no)
        } catch (e) {}
      })
      return max + 1
    },
    connectNodes: function (sourceNode, targetNode, sourceOutput, targetInput) {
      if (sourceNode === targetNode) {
        return
      }
      for (let i = 0; i < this.connections.length; i++) {
        if (this.connections[i].target === targetNode && this.connections[i].inputName === targetInput.label) {
          return
        }
      }
      if (TYPES.checkType(sourceOutput, targetInput) === 'ok') {
        const connection = {
          uuid: uuid.v1(),
          type: 'FLOW',
          source: sourceNode,
          target: targetNode,
          inputName: targetInput.label,
          output: sourceOutput.label
        }
        this.connections.push(connection)
      }
    },
    connectWaitNodes: function (sourceNode, targetNode) {
      if (sourceNode === targetNode) {
        return
      }
      const connection = {
        uuid: uuid.v1(),
        type: 'WAIT',
        source: sourceNode,
        target: targetNode
      }
      this.connections.push(connection)
    },
    connectionContextMenu: function (connection) {
      this.selectedConnection = connection
      this.$refs.connectionCtxMenu.open()
    },
    nodeContextMenu: function (node) {
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
    beginConnection: function (sourceNode, sourceOutput, sourcePos, $event) {
      this.connectingSourcePos.x = sourcePos.x
      this.connectingSourcePos.y = sourcePos.y
      this.connectingNode = sourceNode
      this.connectingOutput = sourceOutput
      this.mouseMove($event)
    },
    beginWaitConnection: function (sourceNode, $event) {
      this.connectingSourcePos.x = sourceNode.x + sourceNode.w
      this.connectingSourcePos.y = sourceNode.y + sourceNode.h - 8
      this.connectingNode = sourceNode
      this.mouseMove($event)
    },
    endWaitConnection: function (targetNode) {
      if (targetNode && this.connectingNode) {
        this.connectWaitNodes(this.connectingNode, targetNode)
      }
      this.connectingNode = null
    },
    endConnection: function (targetNode, input, $event) {
      if (targetNode && this.connectingNode) {
        this.connectNodes(this.connectingNode, targetNode, this.connectingOutput, input)
      }
      this.connectingNode = null
      this.connectingOutput = null
    },
    calculateConnectionPoints: function (connection, padding = 0, bezier = true) {
      let outputPos = connection.source
      let inputPos = connection.target
      if (connection.uuid) {
        if (connection.type === 'FLOW') {
          outputPos = POS_CALC.calculateOutputPos(connection.source, connection.output)
          inputPos = POS_CALC.calculateInputPos(connection.target, connection.inputName)
        } else if (connection.type === 'WAIT') {
          outputPos = POS_CALC.calculateOutputWaitPos(connection.source)
          inputPos = POS_CALC.calculateInputWaitPos(connection.target)
        }
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
    this.uuid = this.sessionProxy.uuid
    this.title = this.sessionProxy.title
    this.nodes = this.sessionProxy.nodes
    this.connections = this.sessionProxy.connections
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
  .temporary-wait-connection{
    fill:none;stroke:black;stroke-width:2;
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
  .wait-connection{
    fill:none;stroke:black;stroke-width:2
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
  .sessionProxy-container{
    width: 100%;
  }
</style>
