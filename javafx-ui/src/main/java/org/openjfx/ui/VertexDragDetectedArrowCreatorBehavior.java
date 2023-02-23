package org.openjfx.ui;

import javafx.scene.input.MouseEvent;

import static org.openjfx.ui.style.VertexStyles.RED_TRANSPARENT_VERTEX;

class VertexDragDetectedArrowCreatorBehavior extends VertexBehavior {
  protected static final Object ACTION_ONE_KEY = new Object(), ACTION_TWO_KEY = new Object();
  private Vertex vertexOne, vertexTwo;
  private Arrow edge;
  private final VertexBehaviorManager vertexOneBehaviourManager = super.vertexBehaviorManager;
  VertexDragDetectedArrowCreatorBehavior(Controller controller, VertexBehaviorManager vertexBehaviorManager) {
    super(controller, vertexBehaviorManager);
  }

  @Override
  public void accept(final Vertex vertexOne, final MouseEvent mouseEvent) {
    if (!mouseEvent.isSecondaryButtonDown()) return;
    spawnVertexTwoWithArrow(vertexOne);
  }
  private void spawnVertexTwoWithArrow(final Vertex vertexOne) {
    this.vertexOne = vertexOne;
    this.vertexTwo = controller.spawnVertex(1, vertexOne.getLayoutX(), vertexOne.getLayoutY());
    edge = controller.linkVertexes(vertexOne, vertexTwo);
    setVertexTwoArrowDraggedStyle();
    vertexOneBehaviourManager.addOnVertexDraggedAction(this::onVertexOneDraggedUpdateVertexTwoPosition, ACTION_ONE_KEY);
    vertexOneBehaviourManager.addOnMouseReleasedAction(this::onMouseReleased, ACTION_TWO_KEY);
  }
  private void setVertexTwoArrowDraggedStyle() {
    final var vertexTwoStyleClass = vertexTwo.getStyleClass();
    final var edgeStyleClass = edge.getStyleClass();
    vertexTwoStyleClass.add(RED_TRANSPARENT_VERTEX);
    edgeStyleClass.add(RED_TRANSPARENT_VERTEX);
  }
  private void onVertexOneDraggedUpdateVertexTwoPosition(final Vertex __, final MouseEvent mouseEvent) {
    if (!mouseEvent.isSecondaryButtonDown()) return;
    vertexTwo.setVertexPosition(
      vertexOne.getLayoutX() + mouseEvent.getX() + vertexOne.getTranslateX(),
      vertexOne.getLayoutY() + mouseEvent.getY() + vertexOne.getTranslateY()
    );
  }
  private void onMouseReleased(final Vertex __, final MouseEvent ___) {
    removeVertexOneActions();
    removeVertexTwoArrowDraggedStyle();
  }
  private void removeVertexOneActions() {
    vertexOneBehaviourManager.removeOnVertexDraggedAction(ACTION_ONE_KEY);
    vertexOneBehaviourManager.removeOnMouseReleasedAction(ACTION_TWO_KEY);
  }
  private void removeVertexTwoArrowDraggedStyle() {
    final var vertexTwoStyleClass = vertexTwo.getStyleClass();
    final var edgeStyleClass = edge.getStyleClass();
    vertexTwoStyleClass.remove(RED_TRANSPARENT_VERTEX);
    edgeStyleClass.remove(RED_TRANSPARENT_VERTEX);
  }
}