package com.blackbuild.multicli.base

import picocli.CommandLine

/**
 * Helper class for smaller tests, implements all methods of AbstractMultiCommandLevel empty.
 */
@CommandLine.Command()
abstract class EmptyCommand extends AbstractMultiCommandLevel {
    @Override
    protected void doExecute() {
    }

    @Override
    void prepare() {
    }

    @Override
    void cleanUp() {
    }
}
