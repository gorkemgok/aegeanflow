<template>
  <div>
    <h1>AegeanFlow Editor</h1>
    <at-button v-for="nodeDef in nodeRepository" :key="nodeDef.type"
            @click="addNode(nodeDef)">Add {{nodeDef.label}} Node</at-button>
    <at-button @click="saveFlow">Save Flow</at-button>
    <svg @mouseup="mouseUp" @mousemove="mouseMove" class="editor-canvas">
      <!--Temporary connection-->
      <template v-if="connectingNode">
        <path class="temporary-connection rolling-dashes"
              :d="calculateConnectionPoints({source: connectingSourcePos, target: connectingTargetPos}, 10)">
        </path>
      </template>

      <!--Connections-->
      <template v-for="connection in connections">
        <path class="connection" v-if="connection.type == 'line'"
              :key="connection.uuid"
              :d="calculateConnectionPoints(connection)">
        </path>
      </template>

      <!--Nodes-->
      <template v-for="node in nodes">
        <node :key="node.uuid"
              :node="node"
              :connectingNode="connectingNode"
              @nodeMouseDown="beginDrag"
              @nodeMouseUp="endDrag"
              @outputMouseDown="beginConnection"
              @inputMouseUp="endConnection"
              @inputMouseOver="inputMouseOver"
              @inputMouseOut="inputMouseOut">
        </node>
      </template>
    </svg>
  </div>
</template>
<script>
import Node from '@/flow/Node'
import { uuid } from 'vue-uuid'
import { POS_CALC, TYPES } from '@/helpers/node-helpers.js'
import { HTTP } from '@/helpers/http-helpers.js'

export default {
  name: 'Editor',
  components: {Node},
  data: function () {
    return {
      percent: 30,
      nodeRepository: [],
      nodes: [],
      connections: [],
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
    }
  },
  methods: {
    saveFlow: function () {
      console.log({
        nodes: this.nodes,
        connections: this.connections
      })
    },
    addNode: function (nodeDef) {
      const colors = ['#F9D194', '#FF6C57', '#977CD5', '#54B375']
      const node = {
        uuid: uuid.v1(),
        type: nodeDef.type,
        x: 50,
        y: 50,
        w: 50,
        h: 50,
        color: colors[Math.floor(Math.random() * 3)],
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
          targetInput: targetInput.name
        }
        this.connections.push(connection)
      }
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
        inputPos = POS_CALC.calculateInputPos(connection.target, connection.targetInput)
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
    HTTP.get('node/list').then(res => {
      this.nodeRepository = res.data
    })
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
</style>
