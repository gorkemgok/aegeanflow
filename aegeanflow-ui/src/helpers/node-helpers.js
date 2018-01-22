export const POS_CALC = {
  calculateOutputPos: function (node) {
    return {
      x: node.x + node.w,
      y: node.y + (node.h / 2)
    }
  },
  calculateInputPos: function (node, inputName) {
    let idx
    for (let i = 0; i < node.definition.inputs.length; i++) {
      if (node.definition.inputs[i].name === inputName) {
        idx = i
        break
      }
    }
    return {
      x: node.x,
      y: node.y + (14 * idx)
    }
  }
}

export const TYPES = {
  checkType (node, input) {
    if (node && input) {
      if (node.definition.returnType === input.type) {
        return 'ok'
      } else {
        return 'not-ok'
      }
    } else {
      return 'notr'
    }
  }
}