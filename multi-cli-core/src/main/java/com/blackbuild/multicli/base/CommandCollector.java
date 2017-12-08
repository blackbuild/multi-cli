package com.blackbuild.multicli.base;

import com.blackbuild.multicli.base.exceptions.IllegalAnnotatedCommandsException;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.scanner.ScanResult;
import picocli.CommandLine;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Builds the picocli tree (with commands and subcommands)
 */
public class CommandCollector {

    private final FastClasspathScanner scanner;
    private List<Class<? extends MultiCommandLevel>> commandClasses;

    public CommandCollector(FastClasspathScanner scanner) {
        this.scanner = scanner;
        scanClasspath();
    }

    public CommandCollector(String... packages) {
        this(new FastClasspathScanner(packages));
    }

    private void scanClasspath() {
        ScanResult scan = scanner.scan();
        List<Class<?>> annotatedClasses = scan.classNamesToClassRefs(scan.getNamesOfClassesWithAnnotation(MultiCommand.class));
        assertNotIllegallyAnnotatedClasses(annotatedClasses);
        assertAllMultiCommandClassesHaveNameSet(annotatedClasses);

        //noinspection unchecked,RedundantCast
        commandClasses = (List<Class<? extends MultiCommandLevel>>)(List) annotatedClasses;
    }

    private void assertAllMultiCommandClassesHaveNameSet(List<Class<?>> annotatedClasses) {
        List<Class<?>> missingNames = annotatedClasses.stream().filter(type -> {
            CommandLine.Command annotation = type.getAnnotation(CommandLine.Command.class);
            return annotation == null || annotation.name().equals("<main class>");
        }).collect(Collectors.toList());

        if (!missingNames.isEmpty())
            throw new IllegalAnnotatedCommandsException("All @MultiCommand classes must have a @Command annotation with a name ", missingNames);
    }

    private void assertNotIllegallyAnnotatedClasses(List<Class<?>> annotatedClasses) {
        List<Class<?>> illegalClasses = annotatedClasses.stream()
                .filter(type -> !MultiCommandLevel.class.isAssignableFrom(type))
                .collect(Collectors.toList());

        if (!illegalClasses.isEmpty())
            throw new IllegalAnnotatedCommandsException("All @MultiCommand classes must implement MultiCommandLevel", illegalClasses);
    }

    public CommandLine createCommandLineTree(Class<? extends MultiCommandLevel> rootType) {
        CommandLine root = createFor(rootType);
        addChildren(root);
        return root;
    }

    private void addChildren(final CommandLine parent) {
        commandClasses
                .stream()
                .filter(type -> type.getAnnotation(MultiCommand.class).value() == parent.getCommand().getClass())
                .map(this::createCommandLineTree)
                .forEach(subCommand -> {
                    parent.addSubcommand(subCommand.getCommandName(), subCommand);
                });
    }

    private CommandLine createFor(Class<? extends MultiCommandLevel> type) {
        try {
            return new CommandLine(type.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }
}
