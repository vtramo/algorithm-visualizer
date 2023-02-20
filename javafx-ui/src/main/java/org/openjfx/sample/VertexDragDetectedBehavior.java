package org.openjfx.sample;

import javafx.scene.input.MouseEvent;

import java.util.Optional;

public class VertexDragDetectedBehavior extends VertexBehavior {

  protected static final int ACTION_ONE_KEY = 3, ACTION_TWO_KEY = 4;
  private Vertex vertexOne, vertexTwo;
  private final VertexBehaviourManager vertexOneBehaviourManager = super.vertexBehaviourManager;

  public VertexDragDetectedBehavior(Controller controller, VertexBehaviourManager vertexBehaviourManager) {
    super(controller, vertexBehaviourManager);
  }

  @Override
  public void accept(Vertex vertexOne, MouseEvent mouseEvent) {
    if (!mouseEvent.isSecondaryButtonDown()) return;
    this.vertexOne = vertexOne;
    this.vertexTwo = controller.spawnVertexOnGraph(vertexOne.getLayoutX(), vertexOne.getLayoutY());
    controller.setLastVertexCreated(vertexTwo);
    controller.spawnArrowOnGraph(vertexOne, vertexTwo);
    vertexOneBehaviourManager.addOnVertexDraggedAction(this::onVertexDraggedAction, ACTION_ONE_KEY);
    vertexOneBehaviourManager.addOnMouseReleasedAction(this::onMouseReleasedAction, ACTION_TWO_KEY);
  }
  private void onVertexDraggedAction(Vertex __, MouseEvent mouseEvent) {
    if (!mouseEvent.isSecondaryButtonDown()) return;
    final var lastVertexCreatedOptional = Optional.of(controller.getLastVertexCreated());

    lastVertexCreatedOptional.ifPresent(lastVertexCreated -> {
      lastVertexCreated.setVertexPosition(
        vertexOne.getLayoutX() + mouseEvent.getX() + vertexOne.getTranslateX(),
        vertexOne.getLayoutY() + mouseEvent.getY() + vertexOne.getTranslateY()
      );
    });
  }
  private void onMouseReleasedAction(Vertex __, MouseEvent ___) {
    vertexOneBehaviourManager.removeOnVertexDraggedAction(ACTION_ONE_KEY);
    vertexOneBehaviourManager.removeOnMouseReleasedAction(ACTION_TWO_KEY);
    controller.setLastVertexCreated(null);
  }
}
