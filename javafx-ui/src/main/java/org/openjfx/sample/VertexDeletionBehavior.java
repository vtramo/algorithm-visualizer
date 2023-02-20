package org.openjfx.sample;

import javafx.scene.input.MouseEvent;

import java.util.function.BiConsumer;

public class VertexDeletionBehavior extends VertexBehavior implements BiConsumer<Vertex, MouseEvent> {

  protected VertexDeletionBehavior(Controller controller, VertexBehaviourManager vertexBehaviourManager) {
    super(controller, vertexBehaviourManager);
  }

  @Override
  public void accept(Vertex vertex, MouseEvent mouseEvent) {
    if (!mouseEvent.isMiddleButtonDown()) return;
    controller.removeVertex(vertex);
  }
}
