<template>
  <div>
    <h1>AegeanFlow Editor</h1>
    <button v-for="nodeDef in nodeRepository" :key="nodeDef.type"
            @click="addNode(nodeDef)">Add {{nodeDef.label}} Node</button>
    <button @click="saveFlow">Save Flow</button>
    <svg @mouseup="mouseUp" @mousemove="mouseMove" class="editor-canvas">
      <template v-for="connection in connections">
        <path v-if="connection.type == 'line'"
              :key="connection.uuid"
              :d="calculateConnectionPoints(connection)"
              style="fill:none;stroke:black;stroke-width:3">
        </path>
      </template>
      <template v-for="node in nodes">
        <node :key="node.uuid"
              :node="node"
              @nodeMouseDown="beginDrag"
              @nodeMouseUp="endDrag"
              @outputMouseDown="beginConnection"
              @inputMouseUp="endConnection">
        </node>
      </template>
      <template v-if="connectingNode">
        <path :d="calculateConnectionPoints({source: connectingSourcePos, target: connectingTargetPos}, 10)"
                  style="fill:none;stroke:silver;stroke-width:3">
        </path>
      </template>

    </svg>
  </div>
</template>
<script>
import Node from '@/flow/Node'
import { uuid } from 'vue-uuid'
import { POS_CALC } from '@/helpers/node-helpers.js'
import { HTTP } from '@/helpers/http-helpers.js'

export default {
  name: 'Editor',
  components: {Node},
  data: function () {
    return {
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
  methods: {
    saveFlow: function () {
      console.log({
        nodes: this.nodes,
        connections: this.connections
      })
    },
    addNode: function (nodeDef) {
      /* const node = {
        uuid: uuid.v1(),
        type: 'com.aegeanflow.node.DatabaseConnection',
        x: 50,
        y: 50,
        w: 50,
        h: 50,
        definition: {
          label: 'Database Reader',
          inputs: [
            {name: 'connection', type: 'com.aegeanflow.core.node.DatabaseConnection', label: 'Database Connection'},
            {name: 'query', type: 'java.lang.String', label: 'Query'}
          ]
        }
      } */
      const node = {
        uuid: uuid.v1(),
        type: nodeDef.type,
        x: 50,
        y: 50,
        w: 50,
        h: 50,
        definition: nodeDef
      }
      this.nodes.push(node)
    },
    connectNodes: function (sourceNode, targetNode, targetInput) {
      const connection = {
        uuid: uuid.v1(),
        type: 'line',
        source: sourceNode,
        target: targetNode,
        targetInput: targetInput
      }
      this.connections.push(connection)
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
        this.connectNodes(this.connectingNode, targetNode, input.name)
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
