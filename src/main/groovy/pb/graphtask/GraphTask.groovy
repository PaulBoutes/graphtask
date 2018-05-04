package pb.graphtask

class GraphTask {

    private static final String ROOT = 'ROOT'
    private GraphNode root
    private Map<String, GraphNode> nodeMap = [:]

    private class UnvisitedAndResult {
        Set<GraphNode> unvisited = []
        Set<GraphNode> result

        UnvisitedAndResult(Set<GraphNode> result) {
            this.result = result
        }

        UnvisitedAndResult addIfNotSeen(GraphNode node) {
            if (!result.contains(node)) {
                result.add(node)
                unvisited.add(node)
            }
            return this
        }

    }

    GraphTask() {
        root = getOrCreate(new Task(name: ROOT))
        add(root.task)
    }

    // Add a task to the graph
    GraphTask add(Task task) {
        GraphNode node = getOrCreate(task)
        if (node.task.parent != null) {
            GraphNode parent = getOrCreate(node.task.parent)
            parent.children.add(node)
        } else {
            root.children.add(node)
        }
        node.children.add(getOrCreate(node.task))
        return this
    }

    // Fetch all dependencies tasks
    Collection<Task> getAllTask() {
        Set<GraphNode> data = traversal([root] as Set, [root] as Set)
        return data.task.tail()
    }

    private GraphNode getOrCreate(Task task) {
        if (nodeMap.containsKey(task.name)) {
            return nodeMap.get(task.name)
        }
        GraphNode node = new GraphNode(task: task)
        nodeMap[task.name] = node
        return node
    }

    // BFS traversal
    private Set<GraphNode> traversal(Set<GraphNode> unvisited, Set<GraphNode> result) {
        if (unvisited.size() == 0) {
            return result
        } else {
            Collection<GraphNode> listOfNode = unvisited.children.flatten()
            UnvisitedAndResult data = listOfNode.inject(new UnvisitedAndResult(result)) { t, n -> t.addIfNotSeen(n) }
            return traversal(data.unvisited, data.result)
        }
    }

}
