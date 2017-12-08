package com.blackbuild.multicli.base

import com.blackbuild.multicli.base.base.Root
import com.blackbuild.multicli.base.base.Sub1
import com.blackbuild.multicli.base.base.SubSub1
import spock.lang.Specification

class CommandCollectorTest extends Specification {

    CommandCollector collector

    def "CommandLines are correctly wired"() {
        given:
        withCollectorIn("base")

        when:
        def root = collector.createCommandLineTree(Root)

        then:
        root.command.class == Root
        root.subcommands.sub1.command.class == Sub1
        root.subcommands.sub1.subcommands.subsub1.command.class == SubSub1
    }

    def "Wrong parent type results in IllegalStateException"() {
        given:
        withCollectorIn("illegalparent")

        when:
        collector.createCommandLineTree(com.blackbuild.multicli.base.illegalparent.Root)

        then:
        thrown(ClassCastException)
    }

    def "ParentAwareClasses get their parent set"() {
        given:
        withCollectorIn("parentaware")

        when:
        def root = collector.createCommandLineTree(com.blackbuild.multicli.base.parentaware.Root)

        then:
        root.subcommands.sub2.command.parent.is(root.command)
    }

    CommandCollector withCollectorIn(String packageName) {
        collector = new CommandCollector("${this.class.package.name}.$packageName")
    }
}
