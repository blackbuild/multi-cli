package com.blackbuild.classeratree

import spock.lang.Specification

class NodeTest extends Specification {

    def "setting the children twice results in an Exception"() {
        given:
        def node = new Node(null, new Object())
        node.setChildren([] as Set<Node>)

        when:
        node.setChildren([] as Set<Node>)

        then:
        thrown IllegalStateException
    }

    def "test flatten"() {
        given:
        Node root = new TreeBuilder<Integer>(1..10).getTreeFor(1, { parent, child -> child == parent * 3 || child == parent * 4 })

        when:
        Set<Node<Integer>> flat = root.flattened()

        then:
        flat*.payload == [1,3,4,9]
    }


}
