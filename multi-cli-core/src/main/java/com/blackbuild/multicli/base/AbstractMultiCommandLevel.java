package com.blackbuild.multicli.base;

public abstract class AbstractMultiCommandLevel implements MultiCommandLevel {

    private MultiCommandLevel parent;

    @Override
    public void setParent(MultiCommandLevel parent) {
        this.parent = parent;
    }
}
