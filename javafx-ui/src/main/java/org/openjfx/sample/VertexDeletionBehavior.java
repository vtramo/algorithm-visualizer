package org.openjfx.sample;

import javafx.scene.input.MouseEvent;

public class VertexDeletionBehavior extends VertexBehavior {

  protected VertexDeletionBehavior(Controller controller, VertexBehaviourManager vertexBehaviourManager) {
    super(controller, vertexBehaviourManager);
  }

  @Override
  public void accept(Vertex vertex, MouseEvent mouseEvent) {
    if (!mouseEvent.isMiddleButtonDown()) return;
    controller.removeVertex(vertex);
  }
}
