package pb.graphtask

import groovy.transform.PackageScope

@PackageScope
class GraphNode {
    Task task
    Collection<GraphNode> children = []
}
