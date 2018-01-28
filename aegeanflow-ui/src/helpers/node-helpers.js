export const POS_CALC = {
  calculateIOPos: function (node, ioIdx, side = 'left') {
    const ioCount = side === 'left' ? node.definition.inputs.length : node.definition.outputs.length
    const margin = (node.h - (ioCount * 10)) / 2
    return {
      x: node.x + (side === 'left' ? 0 : node.w),
      y: node.y + margin + (14 * ioIdx)
    }
  },
  calculateOutputPos: function (node, outputName) {
    let idx
    for (let i = 0; i < node.definition.outputs.length; i++) {
      if (node.definition.outputs[i].name === outputName) {
        idx = i
        break
      }
    }
    return this.calculateIOPos(node, idx, 'right')
  },
  calculateInputPos: function (node, inputName) {
    let idx
    for (let i = 0; i < node.definition.inputs.length; i++) {
      if (node.definition.inputs[i].name === inputName) {
        idx = i
        break
      }
    }
    return this.calculateIOPos(node, idx, 'left')
  }
}

export const TYPES = {
  checkType (output, input) {
    if (output && input) {
      if (output.type === input.type) {
        return 'ok'
      } else {
        return 'not-ok'
      }
    } else {
      return 'notr'
    }
  },
  createFlow: function (uuid, title, nodes, connections) {
    const connectionList = connections.map(connection => {
      return {
        uuid: connection.uuid,
        type: connection.type,
        fromUUID: connection.source.uuid,
        toUUID: connection.target.uuid,
        inputName: connection.inputName,
        outputName: connection.outputName
      }
    })
    const flow = {
      uuid: uuid,
      title: title,
      nodeList: nodes,
      connectionList: connectionList
    }
    return flow
  }
}
