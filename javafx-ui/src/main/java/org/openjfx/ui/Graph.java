package org.openjfx.ui;

import javafx.scene.layout.Pane;
import org.openjfx.model.ObservableDataStructure;
import org.openjfx.model.Position;
import org.openjfx.utils.Sleep;

import java.util.HashMap;
import java.util.Map;

public class Graph extends Pane {
  private final long delay = 250L;
  private final ObservableDataStructure<Integer> observableDataStructure;
  private final Map<Position, Vertex> vertexMap = new HashMap<>();
  private Vertex lastVertex;

  public Graph(final ObservableDataStructure<Integer> observableDataStructure) {
    this.observableDataStructure = observableDataStructure;
    observableDataStructure.setSearchStepAction(this::onSearchStep);
    observableDataStructure.addOnValueFoundListener(this::onValueFound);
    observableDataStructure.addOnValueInsertedListener(this::onValueInserted);
    setPrefWidth(1280);
    setPrefHeight(720);
    setStyle("-fx-background-color:lightblue;");
  }

  private void onSearchStep(final Integer __) {
    final var currentPositionCalculator = observableDataStructure.getCurrentPositionCalculator();
    final var position = currentPositionCalculator.getActualPosition();
    final var vertex = vertexMap.get(position);
    vertex.addStyle(Style.VertexStyle.RED_TRANSPARENT_VERTEX);
    Sleep.sleep(delay);
    vertex.removeStyle(Style.VertexStyle.RED_TRANSPARENT_VERTEX);
    currentPositionCalculator.goAhead();
  }
  private void onValueFound(final Position position) {
    final var vertex = vertexMap.get(position);
    vertex.addStyle(Style.VertexStyle.GREEN_VERTEX);
    Sleep.sleep(delay);
    vertex.removeStyle(Style.VertexStyle.GREEN_VERTEX);
  }
  private void onValueInserted(final Integer value, final Position position) {
    final var vertex = addVertex(value, position);
    if (lastVertex != null) linkVertexes(lastVertex, vertex);
    lastVertex = vertex;
  }

  private Vertex addVertex(final Integer value, final Position position) {
    final var vertex = new Vertex(value, position);
    vertexMap.put(position, vertex);
    getChildren().add(vertex);
    return vertex;
  }
  private Edge linkVertexes(final Vertex vertex1, final Vertex vertex2) {
    final var arrow = new Edge(vertex1, vertex2);
    getChildren().add(arrow);
    return arrow;
  }
}