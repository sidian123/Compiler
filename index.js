/**
 * 节点
 */
class Node{
    /**
     * 前继节点
     * @type {Array}
     */
    preNodes=[];

    /**
     * 后继节点
     * @type {Array}
     */
    sucNodes=[];

    /**
     * 是否为开始节点
     * @returns {boolean}
     */
    isStartNode(){
        return this.preNodes == null;
    }

    /**
     * 是否为结束节点
     * @returns {boolean}
     */
    isEndNode(){
        return this.sucNodes==null;
    }
}

/**
 * 图
 */
class Graph{
    /**
     * 开始节点集合
     * @type {Array}
     */
    startNodes=[];
    /**
     * 结束节点集合
     * @type {Array}
     */
    endNodes=[];
}