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

}
