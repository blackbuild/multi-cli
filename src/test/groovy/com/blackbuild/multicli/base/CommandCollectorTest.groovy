/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2017 Stephan Pauxberger
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.blackbuild.multicli.base

import com.blackbuild.multicli.base.base.Root
import com.blackbuild.multicli.base.base.Sub1
import com.blackbuild.multicli.base.base.SubSub1
import com.blackbuild.multicli.base.deep.Info
import com.blackbuild.multicli.base.deep.MyCommand
import com.blackbuild.multicli.base.multipleroots.Multiroot
import com.blackbuild.multicli.base.multipleroots.Multiroot2
import com.blackbuild.multicli.base.singleroot.SingleRoot
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

    def "find single root command"() {
        when:
        withCollectorIn("singleroot")

        then:
        collector.rootCommands == [SingleRoot]
        collector.singleRootCommand == SingleRoot
    }

    def "multiple root commands is an error"() {
        when:
        withCollectorIn("multipleroots")

        then:
        collector.rootCommands as Set == [Multiroot, Multiroot2] as Set

        when:
        collector.getSingleRootCommand()

        then:
        thrown IllegalStateException
    }

    def "no root commands is an error"() {
        when:
        withCollectorIn("base")

        then:
        collector.rootCommands.isEmpty()

        when:
        collector.getSingleRootCommand()

        then:
        thrown IllegalStateException
    }

    CommandCollector withCollectorIn(String... packageShortNames) {
        def packageNames = packageShortNames.collect { "${this.class.package.name}.$it" }
        collector = new CommandCollector(packageNames as String[])
    }
}
