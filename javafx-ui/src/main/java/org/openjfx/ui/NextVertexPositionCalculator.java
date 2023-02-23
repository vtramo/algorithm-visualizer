package org.openjfx.ui;

import javafx.util.Pair;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.LinkedList;

class NextVertexPositionCalculator {
  private final double xInitialPosition, yInitialPosition;
  private double xPositionIncrementFactor, yPositionIncrementFactor;
  private final double maxX, maxY, minX, minY;
  @Getter(AccessLevel.PROTECTED)
  private boolean limitReached;
  private final LinkedList<Pair<Double, Double>> historyPositions = new LinkedList<>();
  private double x, y;

  @Builder
  protected NextVertexPositionCalculator(
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

  protected double[] goAhead() {
    final var nextX = x + xPositionIncrementFactor;
    final var nextY = y + yPositionIncrementFactor;
    if (nextX <= maxX && nextX >= minX) {
      x += xPositionIncrementFactor;
    } else if (nextY > maxY) {
      limitReached = true;
      return null;
    } else {
      y += yPositionIncrementFactor;
      xPositionIncrementFactor = -xPositionIncrementFactor;
    }
    historyPositions.add(new Pair<>(x, y));
    return getActualPosition();
  }
  protected double[] goBack() {
    final var totalPositions = historyPositions.size();
    if (totalPositions - 1 <= 0) throw new IllegalStateException();
    final var lastPosition = historyPositions.get(totalPositions - 2);
    x = lastPosition.getKey();
    y = lastPosition.getValue();
    historyPositions.removeLast();
    limitReached = false;
    return getActualPosition();
  }
  protected double[] getActualPosition() {
    return new double[] { x, y };
  }
}
