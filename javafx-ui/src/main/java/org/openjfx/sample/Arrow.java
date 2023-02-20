package org.openjfx.sample;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.shape.Polyline;

import java.util.List;

public class Arrow extends Group {
  private static final double ARROW_SCALAR = 20.0;
  private final Polyline mainLine = new Polyline();
  private final SimpleDoubleProperty
    x1 = new SimpleDoubleProperty(),
    y1 = new SimpleDoubleProperty(),
    x2 = new SimpleDoubleProperty(),
    y2 = new SimpleDoubleProperty();

  public Polyline getMainLine() {
    return mainLine;
  }
  public double getX1() {
    return x1.get();
  }
  public double getY1() {
    return y1.get();
  }
  public double getX2() {
    return x2.get();
  }
  public double getY2() {
    return y2.get();
  }
  public SimpleDoubleProperty x1Property() {
    return x1;
  }
  public SimpleDoubleProperty y1Property() {
    return y1;
  }
  public SimpleDoubleProperty x2Property() {
    return x2;
  }
  public SimpleDoubleProperty y2Property() {
    return y2;
  }

  public Arrow(double x1, double y1, double x2, double y2) {
    this.x1.set(x1);
    this.x2.set(x2);
    this.y1.set(y1);
    this.y2.set(y2);
    updateMainLine();
  }
  private void updateMainLine() {
    getChildren().addAll(mainLine);
    for (var simpleDoubleProperty: List.of(x1, y1, x2, y2)) {
      simpleDoubleProperty.addListener((__, ___, ____) -> update());
    }
  }
  private void update() {
    final double[] start = scale(x1.get(), y1.get(), x2.get(), y2.get());
    final double[] end = scale(x2.get(), y2.get(), x1.get(), y1.get());
    final ObservableList<Double> points = mainLine.getPoints();
    points.setAll(start[0], start[1], end[0], end[1]);
  }
  private double[] scale(final double x1, final double y1, final double x2, final double y2) {
    final double theta = Math.atan2(y2 - y1, x2 - x1);
    return new double[] {
      x1 + Math.cos(theta) * ARROW_SCALAR,
      y1 + Math.sin(theta) * ARROW_SCALAR
    };
  }
}
