package org.openjfx.ui;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import lombok.Setter;
import org.openjfx.model.ObservableDataStructure;
import org.openjfx.model.Position;
import org.openjfx.utils.Sleep;

import java.util.HashMap;
import java.util.Map;

import static org.openjfx.utils.Constants.*;

public class Graph extends Pane {
  @Setter
  private long delay = 250L;
  private final Map<Position, Vertex> vertexByPosition = new HashMap<>();
  private final Map<VertexCouple, Edge> edges = new HashMap<>();
  private Vertex lastVertex;

  private record VertexCouple(Vertex vertex1, Vertex vertex2) {
    @Override public boolean equals(Object o) {
      return o instanceof VertexCouple other &&
        this.vertex1.equals(other.vertex1) &&
        this.vertex2.equals(other.vertex2);
    }
  }

  public Graph(final ObservableDataStructure<Integer> observableDataStructure) {
    observableDataStructure.addStepListener(this::onStepDone);
    observableDataStructure.addOnValueFoundListener(this::onValueFound);
    observableDataStructure.addOnValueInsertedListener(this::onValueInserted);
    observableDataStructure.addOnValueRemovedListener(this::onValueRemoved);

    setPrefWidth(SCREEN_WIDTH);
    setPrefHeight(SCREEN_HEIGHT);
    setStyle("-fx-background-color:lightblue;");
  }

  private void onStepDone(final Position position) {
    final var vertex = vertexByPosition.get(position);
    System.out.println("STEP - Vertex: " + vertex + " Position: " + position);
    vertex.addStyle(Style.VertexStyle.RED_TRANSPARENT_VERTEX);
    Sleep.sleep(delay);
    vertex.removeStyle(Style.VertexStyle.RED_TRANSPARENT_VERTEX);
  }

  private void onValueFound(final Position position) {
    final var vertex = vertexByPosition.get(position);
    System.out.println("VALUE FOUND - Vertex: " + vertex + " Position: " + position);
    vertex.addStyle(Style.VertexStyle.GREEN_VERTEX);
    Sleep.sleep(delay);
    vertex.removeStyle(Style.VertexStyle.GREEN_VERTEX);
  }

  private void onValueInserted(final Integer value, final Position position) {
    final var vertex = addVertex(value, position);
    System.out.println("VALUE INSERTED - Vertex: " + vertex + " Position: " + position);
    if (lastVertex != null && !lastVertex.equals(vertex)) linkVertexes(lastVertex, vertex);
    lastVertex = vertex;
  }

  private void onValueRemoved(final Position position) {
    final var vertex = vertexByPosition.get(position);
    System.out.println("VALUE REMOVED - Vertex: " + vertex + " Position: " + position);
    removeVertex(vertex);
  }

  private Vertex addVertex(final Integer value, final Position position) {
    final var vertex = new Vertex(value, position);
    vertexByPosition.put(position, vertex);
    getChildren().add(vertex);
    return vertex;
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
    vertexByPosition.remove(vertex.getPosition());

    rollback(vertex.getNext(), vertex.getPosition());

    vertex.setNext(null);
    vertex.setPrev(null);
  }

  private void rollback(final Vertex vertex, Position backPosition) {
    System.out.println("ROLLBACK - Vertex: " + vertex + " Back Position: " + backPosition);
    var curr = vertex;
    while (curr != null) {
      final var tmpPosition = curr.getPosition();
      curr.setVertexPosition(backPosition);
      vertexByPosition.remove(tmpPosition);
      vertexByPosition.put(backPosition, curr);
      backPosition = tmpPosition;
      curr = curr.getNext();
    }
  }

  private Edge linkVertexes(final Vertex vertex1, final Vertex vertex2) {
    final var edge = new Edge(vertex1, vertex2);
    System.out.println("EDGE INSERTED - Vertex1: " + vertex1 + " Vertex2: " + vertex2);
    edges.put(new VertexCouple(vertex1, vertex2), edge);
    Platform.runLater(() -> getChildren().add(edge));
    return edge;
  }

  private void removeEdge(final Vertex vertex1, final Vertex vertex2) {
    final var edgeToRemove = edges.remove(new VertexCouple(vertex1, vertex2));
    System.out.println("EDGE REMOVED - Vertex1: " + vertex1 + " Vertex2: " + vertex2);
    Platform.runLater(() -> getChildren().remove(edgeToRemove));
  }
}