package pb.graphtask

class GraphTask {

    private static final String ROOT = 'ROOT'
    private GraphNode root
    private Map<String, GraphNode> nodeMap = [:]

    private class UnvisitedAndResult {
        Set<GraphNode> unvisited
        Set<GraphNode> result

        UnvisitedAndResult(Set<GraphNode> result) {
            this.unvisited = []
            this.result = result
        }

    }

    GraphTask() {
        root = makeOrGet(new Task(name: ROOT))
        add(root.task)
    }

    // Add a task to the graph
    GraphTask add(Task task) {
        GraphNode node = makeOrGet(task)
        if (node.task.parent != null) {
            GraphNode parent = makeOrGet(node.task.parent)
            parent.children.add(node)
        } else {
            root.children.add(node)
        }
        node.children.add(makeOrGet(node.task))
        return this
    }

    // Fetch all dependencies tasks
    Collection<Task> getAllTask() {
        Set<GraphNode> data = traversal([root] as Set, [root] as Set)
        return data.task.tail()
    }

    private GraphNode makeOrGet(Task task) {
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
            UnvisitedAndResult data = listOfNode.inject(new UnvisitedAndResult(result)) { t, n ->
                if (!t.result.contains(n)) {
                    t.result.add(n)
                    t.unvisited.add(n)
                }
                return t
            }
            return traversal(data.unvisited, data.result)
        }
    }

}
