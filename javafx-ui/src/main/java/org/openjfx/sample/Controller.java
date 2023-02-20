package org.openjfx.sample;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import static org.openjfx.sample.VertexBehavior.IGNORE_KEY;

public class Controller {
  @FXML
  private AnchorPane graph;
  private final Vertex[] lastVertexCreated = new Vertex[1];

  protected Vertex getLastVertexCreated() {
    return lastVertexCreated[0];
  }
  protected void setLastVertexCreated(final Vertex vertex) { lastVertexCreated[0] = vertex; }
  protected void onGraphPressed(final MouseEvent mouseEvent) {
    if (!mouseEvent.isPrimaryButtonDown()) return;
    lastVertexCreated[0] = spawnVertexOnGraph(mouseEvent.getX(), mouseEvent.getY());
  }
  protected void onGraphDragDetected(final MouseEvent mouseEvent) {
    if (!mouseEvent.isPrimaryButtonDown()) return;
    final var penultimateVertex = lastVertexCreated[0];
    lastVertexCreated[0] = spawnVertexOnGraph(mouseEvent.getX(), mouseEvent.getY());
    spawnArrowOnGraph(penultimateVertex, lastVertexCreated[0]);
  }
  protected void onGraphDragged(final MouseEvent mouseEvent) {
    if (!mouseEvent.isPrimaryButtonDown()) return;
    if (lastVertexCreated[0] != null) {
      lastVertexCreated[0].setVertexPosition(mouseEvent.getX(), mouseEvent.getY());
    }
  }
  protected void onMouseReleased(MouseEvent __) {
    lastVertexCreated[0] = null;
  }
  protected Vertex spawnVertexOnGraph(final double x, final double y) {
    final Vertex vertex = new Vertex(x, y);

    final var vertexBehaviourManager = vertex.getVertexBehaviourManager();
    final var vertexDragDetectedArrowCreatorBehavior = new VertexDragDetectedArrowCreatorBehavior(this, vertexBehaviourManager);
    final var vertexDeletionBehavior = new VertexDeletionBehavior(this, vertexBehaviourManager);
    vertexBehaviourManager.addOnVertexDragDetectedAction(vertexDragDetectedArrowCreatorBehavior, IGNORE_KEY);
    vertexBehaviourManager.addOnVertexPressedAction(vertexDeletionBehavior, IGNORE_KEY);

    addVertex(vertex);
    return vertex;
  }
  protected boolean spawnArrowOnGraph(final Vertex v1, final Vertex v2) {
    final var arrow = new Arrow(v1.getLayoutX(), v1.getLayoutY(), v2.getLayoutX(), v2.getLayoutY());
    arrow.x1Property().bind(v1.layoutXProperty());
    arrow.y1Property().bind(v1.layoutYProperty());
    arrow.x2Property().bind(v2.layoutXProperty());
    arrow.y2Property().bind(v2.layoutYProperty());
    return graph.getChildren().add(arrow);
  }
  protected boolean addVertex(final Vertex v) {
    final var graphChildren = graph.getChildren();
    return graphChildren.add(v);
  }
  protected boolean removeVertex(final Vertex v) {
    final var graphChildren = graph.getChildren();
    return graphChildren.remove(v);
  }
}
