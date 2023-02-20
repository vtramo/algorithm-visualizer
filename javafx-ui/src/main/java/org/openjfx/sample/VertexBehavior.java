package org.openjfx.sample;

import javafx.scene.input.MouseEvent;

import java.util.function.BiConsumer;

abstract class VertexBehavior implements BiConsumer<Vertex, MouseEvent> {
  protected static final int IGNORE_KEY = -1;
  protected final Controller controller;
  protected final VertexBehaviourManager vertexBehaviourManager;

  protected VertexBehavior(Controller controller, VertexBehaviourManager vertexBehaviourManager) {
    this.controller = controller;
    this.vertexBehaviourManager = vertexBehaviourManager;
  }
}
