package com.blackbuild.classeratree;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.BiPredicate;

import static java.util.stream.Collectors.toSet;

/**
 * Creates a simple tree graph from a collection of elements.
 *
 */
public class TreeBuilder<T> {

    private final HashSet<T> allElements;

    public TreeBuilder(Collection<T> elements) {
        allElements = new HashSet<>(elements);
    }

    public Node getTreeFor(T root, BiPredicate<T, T> isParentChild) {
        return createNode(null, root, isParentChild);
    }

    private Node createNode(Node<T> parent, T type, BiPredicate<T, T> isParentChild) {
        Node<T> result = new Node<>(parent, type);
        result.setChildren(
                allElements.stream()
                        .filter(child -> isParentChild.test(type, child))
                        .map(child -> createNode(result, child, isParentChild))
                        .collect(toSet())
        );
        return result;
    }

}
