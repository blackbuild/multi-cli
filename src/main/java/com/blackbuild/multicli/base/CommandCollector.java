/**
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
package com.blackbuild.multicli.base;

import com.blackbuild.classeratree.Node;
import com.blackbuild.classeratree.TreeBuilder;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.scanner.ScanResult;
import picocli.CommandLine;

import java.util.List;

/**
 * Builds the picocli tree (with commands and subcommands)
 */
public class CommandCollector {

    private final FastClasspathScanner scanner;
    private List<Class<?>> commandClasses;
    private ScanResult scan;

    public CommandCollector(FastClasspathScanner scanner) {
        this.scanner = scanner;
        scanClasspath();
    }

    public CommandCollector(String... packages) {
        this(new FastClasspathScanner(packages));
    }

    private void scanClasspath() {
        scan = scanner.scan();
        commandClasses = scan.classNamesToClassRefs(scan.getNamesOfClassesWithAnnotation(CommandLine.Command.class));
    }

    public CommandLine createCommandLineTree(Class<?> rootType) {
        TreeBuilder<Class<?>> treeBuilder = new TreeBuilder<>(commandClasses);

        Node<Class<?>> tree = treeBuilder.getTreeFor(rootType, CommandCollector::isParentSubCommand);

        return createFor(tree, null);
    }

    private CommandLine createFor(Node<Class<?>> node, CommandLine parent) {
        try {
            Class<?> type = node.getPayload();
            CommandLine result = new CommandLine(type.newInstance());
            CommandLine.Command command = type.getAnnotation(CommandLine.Command.class);

            String name = command != null ? command.name() : "<main class>";

            if (name.equals("<main class>"))
                name = type.getSimpleName().toLowerCase();

            result.setCommandName(name);

            if (parent != null)
                parent.addSubcommand(name, result);

            applyAdditionalInterfaces(parent, result);
            node.getChildren().forEach(it -> createFor(it, result));

            return result;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    private static boolean isParentSubCommand(Class<?> parent, Class<?> child) {
        SubCommandOf annotation = child.getAnnotation(SubCommandOf.class);

        return annotation != null && annotation.value() == parent;
    }


    public List<Class<?>> getRootCommands() {
        return scan.classNamesToClassRefs(scan.getNamesOfClassesWithAnnotation(RootCommand.class));
    }

    public Class<?> getSingleRootCommand() {
        List<Class<?>> mainCommands = getRootCommands();
        if (mainCommands.isEmpty())
            throw new IllegalStateException(String.format("No class annotated with RootCommand found in scanned path (%s)", scan.getUniqueClasspathElementsAsPathStr()));

        if (mainCommands.size() > 1)
            throw new IllegalStateException(String.format("More than one root command found in scanned classpath (%s)", mainCommands));

        return mainCommands.get(0);
    }

    private void applyAdditionalInterfaces(CommandLine parent, CommandLine child) {
        Object subCommand = child.getCommand();
        if (subCommand instanceof ParentAware && parent != null) {
            //noinspection unchecked
            ((ParentAware) subCommand).setParent(parent.getCommand());
        }
        if (subCommand instanceof CommandLineAware)
            ((CommandLineAware) subCommand).setCommandLine(child);
    }
}
