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
            ((CommandLineAware) subCommand).setCommandLine(parent);
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
