<template>
  <div class="editor-container">
    <at-modal title="Select Workspace" v-model="showWorkspaceModel">
      <label for="workspacePath">Path</label>
      <at-input id = "workspacePath" name="workspacePath" v-model="workspacePath"></at-input>
    </at-modal>
    <div class="toolbox-container">
      <at-collapse simple accordion :value="0">
        <at-collapse-item title="Base Components">
            <template v-for="nodeDef in boxRepository">
              <drag class="component-container" :transferData="nodeDef" :key="nodeDef.type">
                {{ nodeDef.label }}
              </drag>
            </template>
        </at-collapse-item>
      </at-collapse>
    </div>
    <div class="sessionProxy-tabs">
      <at-tabs type="card" size="small" @on-change="tabChange">
        <at-tab-pane v-for="sessionProxy in sessionProxyList" :key="sessionProxy.uuid"
                     :label="sessionProxy.title" :name="sessionProxy.uuid">
          <sessionProxy v-if="sessionProxy" :sessionProxy="sessionProxy" ref="flows"></sessionProxy>
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
import Node from '@/sessionProxy/Node'
import { HTTP } from '@/helpers/http-helpers.js'
import Vue from 'vue'
import VueDragDrop from 'vue-drag-drop'
import Flow from '@/sessionProxy/Flow'

Vue.use(VueDragDrop)

export default {
  name: 'Editor',
  components: {
    Flow,
    Node
  },
  data: function () {
    return {
      boxRepository: [],
      selectedFlow: null,
      selectedTab: null,
      sessionProxyList: [],
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
      this.selectedFlow = this.sessionProxyList.filter(sessionProxy => sessionProxy.uuid === tab.name)[0]
    },
    addFlow: function () {
      const sessionProxy = {}
      sessionProxy.uuid = null
      sessionProxy.title = 'Untitled'
      sessionProxy.nodes = []
      sessionProxy.connections = []
      this.sessionProxyList.push(sessionProxy)
    },
    getFlows: function () {
      HTTP.get('/sessionProxy/list')
        .then(res => {
          const sessionProxyList = []
          res.data.forEach(rawFlow => {
            const sessionProxy = {}
            sessionProxy.uuid = rawFlow.uuid
            sessionProxy.title = rawFlow.title
            sessionProxy.nodes = rawFlow.nodeList.map(node => {
              node.definition = this.boxRepository.filter(nodeDef => nodeDef.type === node.type)[0]
              return node
            })
            sessionProxy.connections = rawFlow.connectionList.map(connection => {
              const newConn = {}
              newConn.uuid = connection.uuid
              newConn.type = connection.type
              newConn.inputName = connection.inputName
              newConn.outputName = connection.outputName
              newConn.source = sessionProxy.nodes.filter(node => node.uuid === connection.fromUUID)[0]
              newConn.target = sessionProxy.nodes.filter(node => node.uuid === connection.toUUID)[0]
              return newConn
            })
            sessionProxyList.push(sessionProxy)
          })
          this.sessionProxyList = sessionProxyList
        })
    },
    click: function (object) {
    }
  },
  created: function () {
    HTTP.get('node/list').then(res => {
      this.boxRepository = res.data
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
  .sessionProxy-tabs{
    width: calc(100% - 200px);
  }
</style>
