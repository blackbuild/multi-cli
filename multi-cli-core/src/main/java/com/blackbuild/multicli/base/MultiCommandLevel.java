package com.blackbuild.multicli.base;

/**
 * An element in the command tree that can be executed.
 */
interface MultiCommandLevel {

    void prepare();

    void execute();

    void cleanUp();

}