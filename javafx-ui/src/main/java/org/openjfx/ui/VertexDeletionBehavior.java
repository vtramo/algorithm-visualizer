package org.openjfx.ui;

import javafx.scene.input.MouseEvent;

class VertexDeletionBehavior extends VertexBehavior {

  protected VertexDeletionBehavior(Controller controller, VertexBehaviorManager vertexBehaviorManager) {
    super(controller, vertexBehaviorManager);
  }

  @Override
  public void accept(Vertex vertex, MouseEvent mouseEvent) {
    if (!mouseEvent.isMiddleButtonDown()) return;
    vertex.forAllEdges(controller::removeNode);
    controller.removeVertex(vertex);
  }
}
