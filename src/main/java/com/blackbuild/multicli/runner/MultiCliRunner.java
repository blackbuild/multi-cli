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
package com.blackbuild.multicli.runner;

import com.blackbuild.multicli.base.CommandCollector;
import picocli.CommandLine;

/**
 * Runner for a MultiCli application.
 *
 * Usage: Create a subclass of this class and include a main method. Otherwise, if there is exactly one
 * class annotated with {@link com.blackbuild.multicli.base.RootCommand} in the classpath, you can also
 * simply use {@link MultiCliRunner} as main class of the application
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

    public static void main(String[] args) {
        CommandCollector commandCollector = new CommandCollector();
        Class<?> singleMainCommand = commandCollector.getSingleRootCommand();
        commandCollector.createCommandLineTree(singleMainCommand).parseWithHandler(new CommandLine.RunLast(), System.out, args);
    }
}
