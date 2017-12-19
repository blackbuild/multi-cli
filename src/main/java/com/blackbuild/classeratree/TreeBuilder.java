package com.blackbuild.classeratree;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

/**
 * Creates a simple tree graph from a collection of elements.
 *
 */
public class TreeBuilder<T> {

    private final HashSet<T> allElements;

    /**
     * Create a new TreeBuilder instance on a set of elements. The TreeBuilder can be reused to create different
     * graphs using different rules.
     * @param elements The collection of elements. The elements must follow the equals/hashcode contract.
     */
    public TreeBuilder(Collection<T> elements) {
        allElements = new HashSet<>(elements);
    }

    /**
     * Calculate a tree from given root node and a parent child relation. In case of an encountered cycle, an {@link IllegalStateException}
     * is thrown.
     * @param root The root element for the tree to be created.
     * @param isParentChild A (asymmetric) relation that checks its two parameters for a parent child relationship.
     * @return The root node of the generated tree.
     */
    public Node<T> getTreeFor(T root, BiPredicate<T, T> isParentChild) {
        Map<T, Node<T>> usedNodes = new HashMap<>();
        return createNode(null, root, isParentChild, usedNodes);
    }

    private Node<T> createNode(Node<T> parent, T type, BiPredicate<T, T> isParentChild, Map<T, Node<T>> usedNodes) {
        if (usedNodes.containsKey(type))
            throw new IllegalStateException(
                    String.format("Already created a node for %s, this usually means a cycle (new parent: %s)",
                            type, parent != null ? parent.getPayload() : null)
            );

        Node<T> result = new Node<>(parent, type);
        usedNodes.put(type, result);

        result.setChildren(
                allElements.parallelStream()
                        .filter(child -> child != type && isParentChild.test(type, child))
                        .map(child -> createNode(result, child, isParentChild, usedNodes))
                        .collect(Collectors.toSet())
        );
        return result;
    }

}
