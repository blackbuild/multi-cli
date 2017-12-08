package com.blackbuild.multicli.base;

import picocli.CommandLine;

public interface CommandLineAware {
    void setCommandLine(CommandLine commandLine);
}
