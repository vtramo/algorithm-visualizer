package org.openjfx.ui;

import javafx.scene.input.MouseEvent;

import java.util.function.BiConsumer;

abstract class VertexBehavior implements BiConsumer<Vertex, MouseEvent> {
  protected static final int IGNORE_KEY = -1;
  protected final Controller controller;
  protected final VertexBehaviorManager vertexBehaviorManager;

  protected VertexBehavior(Controller controller, VertexBehaviorManager vertexBehaviorManager) {
    this.controller = controller;
    this.vertexBehaviorManager = vertexBehaviorManager;
  }
}
