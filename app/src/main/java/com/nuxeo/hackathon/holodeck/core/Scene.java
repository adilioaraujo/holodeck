package com.nuxeo.hackathon.holodeck.core;

public abstract class Scene extends Node {

    private Node[] nodes;
    private int i;

    public Node add(Node... nodes) {
        if (this.nodes == null) {
            this.nodes = nodes;
        } else {
            Node[] array = new Node[this.nodes.length + nodes.length];
            System.arraycopy(this.nodes, 0, array, 0, this.nodes.length);
            System.arraycopy(nodes, 0, array, this.nodes.length, nodes.length);
            this.nodes = array;
        }
        return this;
    }

    protected void event(String id) {

    }

    @Override
    protected void step() {
        if (nodes != null) {
            for (i = 0; i < nodes.length; i++) {
                nodes[i].step();
            }
        }
    }

    @Override
    protected void draw() {
        if (nodes != null) {
            for (i = 0; i < nodes.length; i++) {
                nodes[i].draw();
            }
        }
    }

}
