package org.openjfx.ui;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.shape.Polyline;
import lombok.AccessLevel;
import lombok.Getter;

class Edge extends Group {
  private static final double EDGE_SCALAR = 20.0;
  private static final double EDGE_HEAD_SCALAR = Math.toRadians(EDGE_SCALAR);
  private static final double EDGE_HEAD_LENGTH = 10.0;

  @Getter(AccessLevel.PROTECTED) private final Vertex vertex1, vertex2;
  private double x1, y1, x2, y2;
  private final Polyline line = new Polyline();
  private final Polyline lineHead = new Polyline();

  protected Edge(final Vertex vertex1, final Vertex vertex2) {
    this.x1 = vertex1.getPosition().x();
    this.x2 = vertex2.getPosition().x();
    this.y1 = vertex1.getPosition().y();
    this.y2 = vertex2.getPosition().y();
    this.vertex1 = vertex1;
    this.vertex2 = vertex2;
    bindVertices(vertex1, vertex2);
    setEdgeStyle();
    createLine();
  }

  private void bindVertices(final Vertex vertex1, final Vertex vertex2) {
    vertex1.setNext(vertex2);
    vertex2.setPrev(vertex1);

    vertex1.addOnXChangedListener((oldValue, newValue) -> {
      x1 = newValue;
      updateLine();
    });

    vertex1.addOnYChangedListener((oldValue, newValue) -> {
      y1 = newValue;
      updateLine();
    });

    vertex2.addOnXChangedListener((oldValue, newValue) -> {
      x2 = newValue;
      updateLine();
    });

    vertex2.addOnYChangedListener((oldValue, newValue) -> {
      y2 = newValue;
      updateLine();
    });
  }

  private void createLine() {
    getChildren().addAll(line, lineHead);
    updateLine();
  }

  private void updateLine() {
    final double[] start = scale(x1, y1, x2, y2);
    final double[] end = scale(x2, y2, x1, y1);
    final ObservableList<Double> points = line.getPoints();
    points.setAll(start[0], start[1], end[0], end[1]);
    setEdgeHead(start[0], start[1], end[0], end[1]);
  }

  private void setEdgeStyle() {
    line.getStyleClass().setAll(Style.EdgeStyle.EDGE_STYLE.getStyleName());
    lineHead.getStyleClass().setAll(Style.EdgeStyle.EDGE_HEAD_STYLE.getStyleName());
  }

  private void setEdgeHead(final double x1, final double y1, final double x2, final double y2) {
    final double theta = calculateAtan2(y2 - y1, x2 - x1);

    lineHead.getPoints()
      .setAll(
        x2 - (Math.cos(theta + EDGE_HEAD_SCALAR) * EDGE_HEAD_LENGTH),
        y2 - (Math.sin(theta + EDGE_HEAD_SCALAR) * EDGE_HEAD_LENGTH),
        x2, y2
      );

    lineHead.getPoints()
      .addAll(
        x2 - (Math.cos(theta - EDGE_HEAD_SCALAR) * EDGE_HEAD_LENGTH),
        y2 - (Math.sin(theta - EDGE_HEAD_SCALAR) * EDGE_HEAD_LENGTH)
      );
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