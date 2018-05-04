package pb.graphtask

import groovy.transform.PackageScope

@PackageScope
class GraphNode {
    Task task
    List<GraphNode> children = []
}
