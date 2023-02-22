package org.openjfx.ui;

import javafx.util.Pair;
import lombok.Builder;

import java.util.LinkedList;

public class NextVertexPositionCalculator {
  private final double xInitialPosition, yInitialPosition;
  private double xPositionIncrementFactor, yPositionIncrementFactor;
  private final double maxX, maxY, minX, minY;
  private final LinkedList<Pair<Double, Double>> historyPositions = new LinkedList<>();
  private double x, y;

  @Builder
  public NextVertexPositionCalculator(
    double xInitialPosition,
    double yInitialPosition,
    double xPositionIncrementFactor,
    double yPositionIncrementFactor,
    double maxX,
    double maxY,
    double minX,
    double minY
  ) {
    this.xInitialPosition = x = xInitialPosition;
    this.yInitialPosition = y = yInitialPosition;
    this.xPositionIncrementFactor = xPositionIncrementFactor;
    this.yPositionIncrementFactor = yPositionIncrementFactor;
    this.maxX = maxX;
    this.maxY = maxY;
    this.minX = minX;
    this.minY = minY;
    historyPositions.add(new Pair<>(xInitialPosition, yInitialPosition));
  }

  public double[] goAhead() {
    final var nextX = x + xPositionIncrementFactor;
    if (nextX > maxX) {
      xPositionIncrementFactor = -xPositionIncrementFactor;
      y += yPositionIncrementFactor;
    } else if (nextX < minX) {
      xPositionIncrementFactor = -xPositionIncrementFactor;
      y += yPositionIncrementFactor;
    } else {
      x += xPositionIncrementFactor;
    }
    historyPositions.add(new Pair<>(x, y));
    return getActualPosition();
  }
  public double[] goBack() {
    final var totalPositions = historyPositions.size();
    if (totalPositions - 1 <= 0) throw new IllegalStateException();
    final var lastPosition = historyPositions.get(totalPositions - 2);
    x = lastPosition.getKey();
    y = lastPosition.getValue();
    historyPositions.removeLast();
    return getActualPosition();
  }
  public double[] getActualPosition() {
    return new double[] { x, y };
  }
}
