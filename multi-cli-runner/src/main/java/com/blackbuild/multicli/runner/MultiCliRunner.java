package com.blackbuild.multicli.runner;

import com.blackbuild.multicli.base.CommandCollector;
import picocli.CommandLine;

/**
 * Runner for a MultiCli application.
 *
 * Usage: Create a subclass of this class and include a main method. Then
 */
public abstract class MultiCliRunner {

    private Class<?> rootCommandType;
    private String[] args;

    public MultiCliRunner(Class<?> rootCommandType) {
        this.rootCommandType = rootCommandType;
    }

    public void run(String... args) {
        CommandLine command = new CommandCollector().createCommandLineTree(rootCommandType);
        command.parseWithHandler(new CommandLine.RunLast(), System.out, args);
    }

}
