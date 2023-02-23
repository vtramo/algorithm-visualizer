package org.openjfx.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import lombok.Getter;
import lombok.Setter;
import org.openjfx.model.Position;

import java.util.function.Consumer;

public class Vertex extends Button {
  private final ObservableList<String> vertexStyleClass = getStyleClass();
  private final ObservableList<Edge> edges = FXCollections.observableArrayList();
  @Setter @Getter private Vertex prev, next;
  @Setter @Getter private Integer value;
  @Getter private Position position;

  Vertex(final int value, final Position position) {
    initVertex(value, position);
  }
  private void initVertex(final Integer value, final Position position) {
    this.value = value;
    this.position = position;
    setText(value.toString());
    addStyle(Style.VertexStyle.VERTEX_DEFAULT_STYLE);

    setVertexPosition(position);
    centerVertex();
  }

  public boolean addEdge(final Edge edge) {
    return edges.add(edge);
  }
  protected void forAllEdges(final Consumer<Edge> arrowConsumer) {
    edges.forEach(arrowConsumer);
  }
  protected void setVertexPosition(final Position position) {
    setLayoutX(position.x());
    setLayoutY(position.y());
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
  public void addStyle(Style.VertexStyle style) {
    vertexStyleClass.add(style.getStyleName());
  }
  public void removeStyle(Style.VertexStyle style) {
    vertexStyleClass.remove(style.getStyleName());
  }

  @Override public boolean equals(Object o) {
    return o instanceof Vertex other &&
      other.position == this.position;
  }
}
