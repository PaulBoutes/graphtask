package pb.graphtask

import spock.lang.Specification

class GraphTaskTest extends Specification {

    def 'should build a correct graph'() {
        given:
        def graph = new GraphTask()
        def t1 = new Task(name: 't1')
        def t2 = new Task(name: 't2', parent: t1)
        def t4 = new Task(name: 't4', parent: t1)
        def t3 = new Task(name: 't3', parent: t4)
        def t5 = new Task(name: 't5', parent: t3)

        when:
        graph.add(t1).add(t2).add(t3).add(t4).add(t5)
        def tasks = graph.getAllTask()

        then:
        !tasks.isEmpty()
        tasks.find { it.name == 't1' }.parent == null
        tasks.find { it.name == 't2' }.parent.name == 't1'
        tasks.find { it.name == 't4' }.parent.name == 't1'
        tasks.find { it.name == 't3' }.parent.name == 't4'
        tasks.find { it.name == 't5' }.parent.name == 't3'

    }

}
