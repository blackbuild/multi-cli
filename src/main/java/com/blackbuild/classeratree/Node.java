package com.blackbuild.classeratree;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Simple node of a graph. Each node has an optional parent as well as a number of possible children.
 * @param <T>
 */
public class Node<T> {

    private final T payload;
    private final Node<T> parent;
    private Set<Node<T>> children;

    Node(Node<T> parent, T info) {
        this.payload = info;
        this.parent = parent;
    }

    public T getPayload() {
        return payload;
    }

    public Node<T> getParent() {
        return parent;
    }

    public Node<T> getRoot() {
        Node<T> level = this;
        while (!level.isRoot()) level = level.getParent();

        return level;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public Stream<Node<T>> flatStream() {
        return Stream.concat(Stream.of(this), children.stream().flatMap(Node::flatStream));
    }

    public Set<Node<T>> flattened() {
        return flatStream().collect(Collectors.toSet());
    }

    public Set<Node<T>> getLeaves() {
        return flatStream().filter(Node::isLeaf).collect(Collectors.toSet());
    }

    void setChildren(Set<Node<T>> children) {
        if (this.children != null)
            throw new IllegalStateException(String.format("Node %s already has children. Possible Circular dependency?", payload));

        this.children = children;
    }

    public Set<Node<T>> getChildren() {
        return Collections.unmodifiableSet(children);
    }

    @Override
    public String toString() {
        return "Node: " + payload.toString();
    }

    @Override
    public int hashCode() {
        return payload.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Node && ((Node) obj).payload.equals(payload);
    }

}
