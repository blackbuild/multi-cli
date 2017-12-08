package com.blackbuild.multicli.base;

import picocli.CommandLine;

public abstract class AbstractMultiCommandLevel implements MultiCommandLevel {

    private CommandLine commandLine;

    @Override
    public void execute() {
        prepare();
        doExecute();
        cleanUp();
    }

    protected abstract void doExecute();
}
