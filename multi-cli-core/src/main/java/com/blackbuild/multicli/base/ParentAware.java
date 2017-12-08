package com.blackbuild.multicli.base;

/**
 * Designates a class that is parent aware, i.e. contains a reference to its parent command.
 */
public interface ParentAware<T> {

    /**
     * Set the parent of this command before executing the command.
     * @param parent The parent.
     */
    void setParent(T parent);
}
