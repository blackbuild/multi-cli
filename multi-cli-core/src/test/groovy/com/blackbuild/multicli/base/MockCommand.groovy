package com.blackbuild.multicli.base

import picocli.CommandLine

/**
 * Helper class for smaller tests, implements all methods of AbstractMultiCommandLevel empty.
 */
@CommandLine.Command()
abstract class MockCommand extends AbstractMultiCommandLevel {

    @Override
    void execute() {
    }

    @Override
    void prepare() {
    }

    @Override
    void cleanUp() {
    }
}
