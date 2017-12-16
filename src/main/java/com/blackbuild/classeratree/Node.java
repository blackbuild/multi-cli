package com.blackbuild.classeratree;

import java.util.Set;

final class Node<T> {

    private final T payload;
    private final Node parent;
    private Set<Node> children;

    Node(Node parent, T info) {
        this.payload = info;
        this.parent = parent;
    }

    public T getPayload() {
        return payload;
    }

    public Node getParent() {
        return parent;
    }

    void setChildren(Set<Node> children) {
        if (this.children != null)
            throw new IllegalStateException(String.format("Node %s already has children. Possible Circular dependency?", payload));

        this.children = children;
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
