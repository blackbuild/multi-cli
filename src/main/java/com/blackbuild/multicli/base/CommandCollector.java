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

    public CommandCollector(FastClasspathScanner scanner) {
        this.scanner = scanner;
        scanClasspath();
    }

    public CommandCollector(String... packages) {
        this(new FastClasspathScanner(packages));
    }

    private void scanClasspath() {
        ScanResult scan = scanner.scan();
        commandClasses = scan.classNamesToClassRefs(scan.getNamesOfClassesWithAnnotation(SubCommandOf.class));
    }

    public CommandLine createCommandLineTree(Class<?> rootType) {
        CommandLine root = createFor(rootType);
        addChildren(root);
        return root;
    }

    private void addChildren(final CommandLine parent) {

        commandClasses
                .stream()
                .filter(type -> type.getAnnotation(SubCommandOf.class).value() == parent.getCommand().getClass())
                .map(this::createCommandLineTree)
                .forEach(subCommand -> {
                    parent.addSubcommand(subCommand.getCommandName(), subCommand);
                    applyAdditionalInterfaces(parent, subCommand);
                });
    }

    private void applyAdditionalInterfaces(CommandLine parent, CommandLine child) {
        Object subCommand = child.getCommand();
        if (subCommand instanceof ParentAware) {
            //noinspection unchecked
            ((ParentAware) subCommand).setParent(parent.getCommand());
        }
        if (subCommand instanceof CommandLineAware)
            ((CommandLineAware) subCommand).setCommandLine(child);
    }

    private CommandLine createFor(Class<?> type) {
        try {
            CommandLine result = new CommandLine(type.newInstance());
            CommandLine.Command command = type.getAnnotation(CommandLine.Command.class);

            String name = command != null ? command.name() : "<main class>";

            if (name.equals("<main class>"))
                name = type.getSimpleName().toLowerCase();

            result.setCommandName(name);
            return result;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }
}
