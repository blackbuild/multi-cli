package com.blackbuild.multicli.base;

import java.util.List;
import java.util.stream.Collectors;

public class IllegalAnnotatedCommandsException extends IllegalStateException {

    private List<Class<?>> illegalClasses;

    /**
     * Constructs an IllegalStateException with the specified detail
     * message.  A detail message is a String that describes this particular
     * exception.
     *
     * @param s the String that contains a detailed message
     */
    public IllegalAnnotatedCommandsException(String s, List<Class<?>> illegalClasses) {
        super(s);
        this.illegalClasses = illegalClasses;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " (Failing classes: " + illegalClasses.stream().map(Class::getName).collect(Collectors.joining(", ")) + ")";
    }
}
