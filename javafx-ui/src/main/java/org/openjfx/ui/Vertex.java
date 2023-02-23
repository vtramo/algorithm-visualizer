package org.openjfx.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;

import static org.openjfx.ui.VertexBehaviorManager.ActionType.*;
import static org.openjfx.ui.style.VertexStyles.VERTEX_DEFAULT_STYLE;

class Vertex extends Button {
  final private VertexBehaviorManager vertexBehaviorManager = new VertexBehaviorManager(this);
  final private ObservableList<String> vertexStyleClass = getStyleClass();
  final private ObservableList<Arrow> edges = FXCollections.observableArrayList();
  @Setter @Getter private Vertex prev, next;
  @Setter @Getter private Integer value;

  Vertex(final int value, final double x, final double y) {
    initVertex(value, x, y);
  }

  private void initVertex(final Integer value, final double x, final double y) {
    this.value = value;
    setText(value.toString());
    vertexStyleClass.add(VERTEX_DEFAULT_STYLE);

    setVertexPosition(x, y);
    centerVertex();

    setOnMouseDragged(this::onVertexDragged);
    setOnDragDetected(this::onVertexDragDetected);
    setOnMousePressed(this::onVertexPressed);
    setOnMouseReleased(this::onMouseReleased);
  }
  protected VertexBehaviorManager getVertexBehaviourManager() {
    return vertexBehaviorManager;
  }
  protected boolean addEdge(final Arrow edge) {
    return edges.add(edge);
  }
  protected void forAllEdges(final Consumer<Arrow> arrowConsumer) {
    edges.forEach(arrowConsumer);
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
    vertexBehaviorManager.performAllActions(VERTEX_DRAGGED_ACTIONS, mouseEvent);
  }
  private void onVertexDragDetected(final MouseEvent mouseEvent) {
    vertexBehaviorManager.performAllActions(VERTEX_DRAG_DETECTED_ACTIONS, mouseEvent);
  }
  private void onVertexPressed(final MouseEvent mouseEvent) {
    vertexBehaviorManager.performAllActions(VERTEX_PRESSED_ACTIONS, mouseEvent);
  }
  private void onMouseReleased(final MouseEvent mouseEvent) {
    vertexBehaviorManager.performAllActions(MOUSE_RELEASED_ACTIONS, mouseEvent);
  }
}
