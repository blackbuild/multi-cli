package com.blackbuild.multicli.base;

/**
 * An element in the command tree that can be executed.
 */
interface MultiCommandLevel {

    void setParent(MultiCommandLevel parent);

    void prepare();

    void execute();

    void cleanUp();

}