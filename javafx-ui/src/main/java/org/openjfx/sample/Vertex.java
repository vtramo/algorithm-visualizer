package org.openjfx.sample;

import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import static org.openjfx.sample.VertexBehaviourManager.ActionType.*;

class Vertex extends Button {
  private static int globalVertexCounter = 0;

  final private String id = String.valueOf(globalVertexCounter++);
  final private VertexBehaviourManager vertexBehaviourManager = new VertexBehaviourManager(this);
  final private ObservableList<String> vertexStyleClass = getStyleClass();

  Vertex(final double x, final double y) {
    initVertex(x, y);
  }

  private void initVertex(final double x, final double y) {
    setText(id);
    vertexStyleClass.add("visNode");

    setVertexPosition(x, y);
    centerVertex();

    setOnMouseDragged(this::onVertexDragged);
    setOnDragDetected(this::onVertexDragDetected);
    setOnMousePressed(this::onVertexPressed);
    setOnMouseReleased(this::onMouseReleased);
  }

  protected VertexBehaviourManager getVertexBehaviourManager() {
    return vertexBehaviourManager;
  }
  protected void setVertexPosition(final MouseEvent mouseEvent) {
    setVertexPosition(mouseEvent.getX(), mouseEvent.getY());
  }
  protected void setVertexPosition(final double x, final double y) {
    setLayoutX(x);
    setLayoutY(y);
  }
  protected void updateVertexPosition(final MouseEvent mouseEvent) {
    updateVertexPosition(mouseEvent.getX(), mouseEvent.getY());
  }
  protected void updateVertexPosition(final double x, final double y) {
    setLayoutX(getLayoutX() + x + getTranslateX());
    setLayoutY(getLayoutY() + y + getTranslateY());
  }
  protected void centerVertex() {
    final int byTwo = -2;
    final var width  = widthProperty().divide(byTwo);
    final var height = heightProperty().divide(byTwo);
    translateXProperty().bind(width);
    translateYProperty().bind(height);
  }

  private void onVertexDragged(final MouseEvent mouseEvent) {
    vertexBehaviourManager.performAllActions(VERTEX_DRAGGED_ACTIONS, mouseEvent);
  }
  private void onVertexDragDetected(final MouseEvent mouseEvent) {
    vertexBehaviourManager.performAllActions(VERTEX_DRAG_DETECTED_ACTIONS, mouseEvent);
  }
  private void onVertexPressed(final MouseEvent mouseEvent) {
    vertexBehaviourManager.performAllActions(VERTEX_PRESSED_ACTIONS, mouseEvent);
  }
  private void onMouseReleased(final MouseEvent mouseEvent) {
    vertexBehaviourManager.performAllActions(MOUSE_RELEASED_ACTIONS, mouseEvent);
  }
}
