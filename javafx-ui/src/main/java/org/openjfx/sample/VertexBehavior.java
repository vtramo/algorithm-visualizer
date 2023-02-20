package org.openjfx.sample;

public abstract class VertexBehavior {
  protected static final int IGNORE_KEY = -1;
  protected final Controller controller;
  protected final VertexBehaviourManager vertexBehaviourManager;

  protected VertexBehavior(Controller controller, VertexBehaviourManager vertexBehaviourManager) {
    this.controller = controller;
    this.vertexBehaviourManager = vertexBehaviourManager;
  }
}
