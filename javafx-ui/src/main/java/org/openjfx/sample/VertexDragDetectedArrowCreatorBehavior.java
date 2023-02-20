package org.openjfx.sample;

import javafx.scene.input.MouseEvent;

class VertexDragDetectedArrowCreatorBehavior extends VertexBehavior {

  protected static final Object ACTION_ONE_KEY = new Object(), ACTION_TWO_KEY = new Object();
  private Vertex vertexOne, vertexTwo;
  private final VertexBehaviourManager vertexOneBehaviourManager = super.vertexBehaviourManager;

  VertexDragDetectedArrowCreatorBehavior(Controller controller, VertexBehaviourManager vertexBehaviourManager) {
    super(controller, vertexBehaviourManager);
  }

  @Override
  public void accept(final Vertex vertexOne, final MouseEvent mouseEvent) {
    if (!mouseEvent.isSecondaryButtonDown()) return;
    spawnVertexTwoWithArrow(vertexOne);
  }
  private void spawnVertexTwoWithArrow(final Vertex vertexOne) {
    this.vertexOne = vertexOne;
    this.vertexTwo = controller.spawnVertexOnGraph(vertexOne.getLayoutX(), vertexOne.getLayoutY());
    controller.setLastVertexCreated(vertexTwo);
    controller.spawnArrowOnGraph(vertexOne, vertexTwo);
    vertexOneBehaviourManager.addOnVertexDraggedAction(this::onVertexOneDraggedUpdateVertexTwoPosition, ACTION_ONE_KEY);
    vertexOneBehaviourManager.addOnMouseReleasedAction(this::onMouseReleasedRemoveVertexOneActions, ACTION_TWO_KEY);
  }
  private void onVertexOneDraggedUpdateVertexTwoPosition(Vertex __, MouseEvent mouseEvent) {
    if (!mouseEvent.isSecondaryButtonDown()) return;
    vertexTwo.setVertexPosition(
      vertexOne.getLayoutX() + mouseEvent.getX() + vertexOne.getTranslateX(),
      vertexOne.getLayoutY() + mouseEvent.getY() + vertexOne.getTranslateY()
    );
  }
  private void onMouseReleasedRemoveVertexOneActions(Vertex __, MouseEvent ___) {
    vertexOneBehaviourManager.removeOnVertexDraggedAction(ACTION_ONE_KEY);
    vertexOneBehaviourManager.removeOnMouseReleasedAction(ACTION_TWO_KEY);
    controller.setLastVertexCreated(null);
  }
}
