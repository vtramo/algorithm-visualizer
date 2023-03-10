package org.openjfx.ui;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import lombok.Setter;
import org.openjfx.model.ObservablePositionValueDataStructure;
import org.openjfx.model.Position;
import org.openjfx.model.ObservablePositionValue;
import org.openjfx.utils.Sleep;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Graph extends Pane {
  @Setter
  private long delay = 250L;
  private final Map<Position, Vertex> vertexByPositionValue = new HashMap<>();
  private final Map<VertexCouple, Edge> edges = new HashMap<>();
  private Vertex lastVertex;

  private record VertexCouple(Vertex vertex1, Vertex vertex2) {
    @Override public boolean equals(Object o) {
      return o instanceof VertexCouple other &&
        this.vertex1.equals(other.vertex1) &&
        this.vertex2.equals(other.vertex2);
    }
  }

  public Graph(final ObservablePositionValueDataStructure<Integer> observableDataStructure) {
    observableDataStructure.addStepListener(this::onStepDone);
    observableDataStructure.addOnValueFoundListener(this::onValueFound);
    observableDataStructure.addOnValueInsertedListener(this::onValueInserted);
    observableDataStructure.addOnValueRemovedListener(this::onValueRemoved);

    setPrefWidth(1280);
    setPrefHeight(720);
    setStyle("-fx-background-color:lightblue;");
  }

  private void onStepDone(final ObservablePositionValue<Integer> positionValue) {
    final var vertex = vertexByPositionValue.get(positionValue.getPosition());
    vertex.addStyle(Style.VertexStyle.RED_TRANSPARENT_VERTEX);
    Sleep.sleep(delay);
    vertex.removeStyle(Style.VertexStyle.RED_TRANSPARENT_VERTEX);
  }

  private void onValueFound(final ObservablePositionValue<Integer> positionValue) {
    final var vertex = vertexByPositionValue.get(positionValue.getPosition());
    vertex.addStyle(Style.VertexStyle.GREEN_VERTEX);
    Sleep.sleep(delay);
    vertex.removeStyle(Style.VertexStyle.GREEN_VERTEX);
  }

  private void onValueInserted(ObservablePositionValue<Integer> positionValue) {
    final var vertex = addVertex(positionValue);
    if (lastVertex != null && !lastVertex.equals(vertex)) linkVertexes(lastVertex, vertex);
    lastVertex = vertex;
  }

  private void onValueRemoved(final ObservablePositionValue<Integer> positionValue) {
    final var vertex = vertexByPositionValue.get(positionValue.getPosition());
    vertex.addStyle(Style.VertexStyle.GREEN_VERTEX);
    Sleep.sleep(delay);
    vertex.removeStyle(Style.VertexStyle.GREEN_VERTEX);
    removeVertex(vertex);
  }

  private void removeVertex(final Vertex vertex) {
    final var prev = vertex.getPrev();
    final var next = vertex.getNext();

    if (prev != null) {
      if (next != null) {
        removeEdge(vertex, next);
        linkVertexes(prev, next);
      } else {
        prev.setNext(null);
        lastVertex = prev;
      }
      removeEdge(prev, vertex);
    } else if (next != null) {
      next.setPrev(null);
      removeEdge(vertex, next);
    } else {
      lastVertex = null;
    }

    Platform.runLater(() -> getChildren().remove(vertex));
    vertexByPositionValue.remove(vertex.getPosition());

    rollback(vertex.getNext(), vertex.getPosition());

    vertex.setNext(null);
    vertex.setPrev(null);
  }

  private Vertex addVertex(final ObservablePositionValue<Integer> positionValue) {
    positionValue.addOnPositionChangedListener(this::onVertexPositionChanged);
    final var position = positionValue.getPosition();
    final var vertex = new Vertex(positionValue.getValue(), position);
    vertexByPositionValue.put(position, vertex);
    getChildren().add(vertex);
    return vertex;
  }

  private void onVertexPositionChanged(final Position oldPosition, final ObservablePositionValue<Integer> positionValue) {
    final var vertexToUpdate = Optional.of(vertexByPositionValue.remove(oldPosition));
    vertexToUpdate.ifPresent(vertex -> {
      final var newVertexPosition = positionValue.getPosition();
      vertexByPositionValue.put(newVertexPosition, vertex);
    });
  }

  private void rollback(final Vertex vertex, Position backPosition) {
    var curr = vertex;
    while (curr != null) {
      final var tmpPosition = curr.getPosition();
      curr.setVertexPositionTimelineAnimation(backPosition, delay);
      vertexByPositionValue.remove(tmpPosition);
      vertexByPositionValue.put(backPosition, curr);
      backPosition = tmpPosition;
      curr = curr.getNext();
    }
  }

  private void removeEdge(final Vertex vertex1, final Vertex vertex2) {
    final var edgeToRemove = edges.remove(new VertexCouple(vertex1, vertex2));
    Platform.runLater(() -> getChildren().remove(edgeToRemove));
  }

  private Edge linkVertexes(final Vertex vertex1, final Vertex vertex2) {
    final var edge = new Edge(vertex1, vertex2);
    edges.put(new VertexCouple(vertex1, vertex2), edge);
    Platform.runLater(() -> getChildren().add(edge));
    return edge;
  }
}