package com.blackbuild.multicli.base

import com.blackbuild.multicli.base.base.Root
import com.blackbuild.multicli.base.base.Sub1
import com.blackbuild.multicli.base.base.SubSub1
import com.blackbuild.multicli.base.deep.Info
import com.blackbuild.multicli.base.deep.MyCommand
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

    def "ParentAware classes get their parent set"() {
        given:
        withCollectorIn("parentaware")

        when:
        def root = collector.createCommandLineTree(com.blackbuild.multicli.base.parentaware.Root)

        then:
        root.subcommands.sub2.command.parent.is(root.command)
    }

    def "CommandLineAware classes get their commandLine set"() {
        given:
        withCollectorIn("cliaware")

        when:
        def root = collector.createCommandLineTree(com.blackbuild.multicli.base.cliaware.Root)
        def sub2 = root.subcommands.sub2

        then:
        sub2.command.commandLine.is(sub2)
    }

    def "Bigger test"() {
        given:
        withCollectorIn("deep")
        def cli = collector.createCommandLineTree(MyCommand)

        when:
        def clis = cli.parse('-l', 'debug', "info", "-v", "name", "-v", "name2")

        then:
        clis*.command*.class == [MyCommand, Info]

        when:
        MyCommand command = clis[0].command
        Info info = clis[1].command

        then:
        command.logLevel == MyCommand.LogLevel.debug
        info.values == ['name', 'name2']
    }

    CommandCollector withCollectorIn(String packageName) {
        collector = new CommandCollector("${this.class.package.name}.$packageName")
    }
}
