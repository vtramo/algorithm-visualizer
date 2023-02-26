package org.openjfx.ui;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.shape.Polyline;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.List;

class Edge extends Group {
  private static final double EDGE_SCALAR = 20.0;
  private static final double EDGE_HEAD_SCALAR = Math.toRadians(20.0);
  private static final double EDGE_HEAD_LENGTH = 10;

  private final Polyline mainLine = new Polyline();
  private final Polyline arrowHead = new Polyline();
  @Getter(AccessLevel.PROTECTED) private Vertex vertex1, vertex2;
  private final SimpleDoubleProperty
    x1 = new SimpleDoubleProperty(),
    y1 = new SimpleDoubleProperty(),
    x2 = new SimpleDoubleProperty(),
    y2 = new SimpleDoubleProperty();

  protected Edge(final Vertex vertex1, final Vertex vertex2) {
    this(vertex1.getLayoutX(), vertex1.getLayoutY(), vertex2.getLayoutX(), vertex2.getLayoutY());
    bindVertices(vertex1, vertex2);
  }

  protected Edge(final double x1, final double y1, final double x2, final double y2) {
    this.x1.set(x1);
    this.x2.set(x2);
    this.y1.set(y1);
    this.y2.set(y2);
    setEdgeStyle();
    createMainLine();
  }

  private void update() {
    final double[] start = scale(x1.get(), y1.get(), x2.get(), y2.get());
    final double[] end = scale(x2.get(), y2.get(), x1.get(), y1.get());
    final ObservableList<Double> points = mainLine.getPoints();
    points.setAll(start[0], start[1], end[0], end[1]);
    setEdgeHead(start[0], start[1], end[0], end[1]);
  }

  private void createMainLine() {
    getChildren().addAll(mainLine, arrowHead);
    update();
    for (var simpleDoubleProperty: List.of(x1, y1, x2, y2)) {
      simpleDoubleProperty.addListener((__, ___, ____) -> update());
    }
  }

  private void setEdgeStyle() {
    mainLine.getStyleClass().setAll(Style.EdgeStyle.EDGE_STYLE.getStyleName());
    arrowHead.getStyleClass().setAll(Style.EdgeStyle.EDGE_HEAD_STYLE.getStyleName());
  }

  private void setEdgeHead(final double x1, final double y1, final double x2, final double y2) {
    final double theta = calculateAtan2(y2 - y1, x2 - x1);
    arrowHead.getPoints()
      .setAll(
        x2 - (Math.cos(theta + EDGE_HEAD_SCALAR) * EDGE_HEAD_LENGTH),
        y2 - (Math.sin(theta + EDGE_HEAD_SCALAR) * EDGE_HEAD_LENGTH),
        x2, y2
      );
    arrowHead.getPoints()
      .addAll(
        x2 - (Math.cos(theta - EDGE_HEAD_SCALAR) * EDGE_HEAD_LENGTH),
        y2 - (Math.sin(theta - EDGE_HEAD_SCALAR) * EDGE_HEAD_LENGTH)
      );
  }

  protected void bindVertices(final Vertex vertex1, final Vertex vertex2) {
    this.vertex1 = vertex1; this.vertex2 = vertex2;
    vertex1.setNext(vertex2); vertex2.setPrev(vertex1);
    x1.bind(vertex1.layoutXProperty());
    y1.bind(vertex1.layoutYProperty());
    x2.bind(vertex2.layoutXProperty());
    y2.bind(vertex2.layoutYProperty());
  }

  private double[] scale(final double x1, final double y1, final double x2, final double y2) {
    final double theta = calculateAtan2(y2 - y1, x2 - x1);
    return new double[] {
      x1 + Math.cos(theta) * EDGE_SCALAR,
      y1 + Math.sin(theta) * EDGE_SCALAR
    };
  }

  private double calculateAtan2(final double y, final double x) {
    return Math.atan2(y, x);
  }
}