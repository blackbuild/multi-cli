package com.blackbuild.classeratree

import spock.lang.Specification

class TreeBuilderTest extends Specification {
    TreeBuilder builder

    def "simple test with numbers"() {
        given:
        def numbers = (1..10)
        builder = new TreeBuilder<Integer>(numbers)

        when:
        def tree = builder.getTreeFor(2, { parent, child -> parent * 2 == child })

        then:
        tree.children*.payload as Set == [4] as Set
        tree.children.first().children*.payload as Set == [8] as Set

        and:
        tree.children.every { it.parent == tree }
    }

    def "simple test with numbers (tree)"() {
        given:
        def numbers = (1..10)
        builder = new TreeBuilder<Integer>(numbers)

        when:
        def tree = builder.getTreeFor(2, { parent, child -> parent * 2 == child || parent * 3 == child})

        then:
        tree.children*.payload as Set == [4,6] as Set
        tree.children.first().children*.payload as Set == [8] as Set

        and:
        tree.children.every { it.parent == tree }
    }

    def "cycles lead to errors"() {
        given:
        def numbers = (1..10)
        builder = new TreeBuilder<Integer>(numbers)

        when:
        def tree = builder.getTreeFor(2, { parent, child -> parent * 2 == child || (parent == 8 && child == 2)})

        then:
        thrown IllegalStateException
    }

}
