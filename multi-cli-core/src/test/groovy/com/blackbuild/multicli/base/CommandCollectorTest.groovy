package com.blackbuild.multicli.base

import com.blackbuild.multicli.base.base.Root
import com.blackbuild.multicli.base.base.Sub1
import com.blackbuild.multicli.base.base.SubSub1
import com.blackbuild.multicli.base.exceptions.IllegalAnnotatedCommandsException
import spock.lang.Specification

class CommandCollectorTest extends Specification {

    CommandCollector collector

    def "fail if annotated class does not implement MultiCommandLevel"() {
        when:
        withCollectorIn("illegal")

        then:
        thrown(IllegalAnnotatedCommandsException)
    }

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

    CommandCollector withCollectorIn(String packageName) {
        collector = new CommandCollector("${this.class.package.name}.$packageName")
    }

}
