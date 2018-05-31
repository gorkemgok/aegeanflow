<template>
  <div class="editor-container">
    <at-modal title="Select Workspace" v-model="showWorkspaceModel">
      <label for="workspacePath">Path</label>
      <at-input id = "workspacePath" name="workspacePath" v-model="workspacePath"></at-input>
    </at-modal>
    <div class="toolbox-container">
      <at-collapse simple accordion :value="0">
        <at-collapse-item title="Base Components">
            <template v-for="nodeDef in nodeRepository">
              <drag class="component-container" :transferData="nodeDef" :key="nodeDef.type">
                {{ nodeDef.label }}
              </drag>
            </template>
        </at-collapse-item>
      </at-collapse>
    </div>
    <div class="flow-tabs">
      <at-tabs type="card" size="small" @on-change="tabChange">
        <at-tab-pane v-for="flow in flowList" :key="flow.uuid"
                     :label="flow.title" :name="flow.uuid">
          <flow v-if="flow" :flow="flow" ref="flows"></flow>
        </at-tab-pane>
        <div slot="extra">
          <at-button size="small" @click="addFlow"><i class="icon icon-plus"></i></at-button>
          <at-button size="small" @click="saveFlow"><i class="icon icon-save"></i></at-button>
          <at-button size="small" @click="runFlow"><i class="icon icon-play"></i></at-button>
        </div>
      </at-tabs>
    </div>
  </div>
</template>
<script>
import Node from '@/flow/Node'
import { HTTP } from '@/helpers/http-helpers.js'
import Vue from 'vue'
import VueDragDrop from 'vue-drag-drop'
import Flow from '@/flow/Flow'

Vue.use(VueDragDrop)

export default {
  name: 'Editor',
  components: {
    Flow,
    Node
  },
  data: function () {
    return {
      nodeRepository: [],
      selectedFlow: null,
      selectedTab: null,
      flowList: [],
      workspacePath: null,
      showWorkspaceModel: true
    }
  },
  computed: {
  },
  methods: {
    saveFlow: function () {
      this.$refs.flows[this.selectedTab.index].saveFlow()
    },
    runFlow: function () {
      this.$refs.flows[this.selectedTab.index].runFlow()
    },
    tabChange: function (tab) {
      this.selectedTab = tab
      this.selectedFlow = this.flowList.filter(flow => flow.uuid === tab.name)[0]
    },
    addFlow: function () {
      const flow = {}
      flow.uuid = null
      flow.title = 'Untitled'
      flow.nodes = []
      flow.connections = []
      this.flowList.push(flow)
    },
    getFlows: function () {
      HTTP.get('/flow/list')
        .then(res => {
          const flowList = []
          res.data.forEach(rawFlow => {
            const flow = {}
            flow.uuid = rawFlow.uuid
            flow.title = rawFlow.title
            flow.nodes = rawFlow.nodeList.map(node => {
              node.definition = this.nodeRepository.filter(nodeDef => nodeDef.type === node.type)[0]
              return node
            })
            flow.connections = rawFlow.connectionList.map(connection => {
              const newConn = {}
              newConn.uuid = connection.uuid
              newConn.type = connection.type
              newConn.inputName = connection.inputName
              newConn.outputName = connection.outputName
              newConn.source = flow.nodes.filter(node => node.uuid === connection.fromUUID)[0]
              newConn.target = flow.nodes.filter(node => node.uuid === connection.toUUID)[0]
              return newConn
            })
            flowList.push(flow)
          })
          this.flowList = flowList
        })
    },
    click: function (object) {
    }
  },
  created: function () {
    HTTP.get('node/list').then(res => {
      this.nodeRepository = res.data
      this.getFlows()
    })

    if (!this.workspacePath) {
      HTTP.get('workspace/path').then(res => {
        this.workspacePath = res.data.path
      })
    }
  },
  watch: {
    workspacePath: function (newVal, oldVal) {
      this.workspacePath = newVal
      HTTP.post('workspace/path', {path: this.workspacePath}).then(res => {
        this.getFlows()
      })
    }
  }
}
</script>
<style>
  .editor-container{
    display: flex;
  }
  .toolbox-container{
    width: 200px;
  }
  .component-container{
    text-align: left;
  }
  .flow-tabs{
    width: calc(100% - 200px);
  }
</style>
