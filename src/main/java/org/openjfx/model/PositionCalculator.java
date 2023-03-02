package org.openjfx.model;

import lombok.Builder;
import lombok.Getter;

import java.util.LinkedList;

public class PositionCalculator {
  @Getter private final double xInitialPosition, yInitialPosition;
  @Getter private double xPositionIncrementFactor, yPositionIncrementFactor;
  @Getter private final double maxX, maxY, minX, minY;
  @Getter private boolean limitReached;
  private double x, y;
  private LinkedList<Position> historyPositions = new LinkedList<>();

  @Builder
  public PositionCalculator(
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
    historyPositions.add(new Position(xInitialPosition, yInitialPosition));
  }

  public Position goAhead() {
    System.out.println("x: " + x + ", y: " + y);
    if (limitReached) return getActualPosition();

    final var nextX = x + xPositionIncrementFactor;
    final var nextY = y + yPositionIncrementFactor;

    if (nextX < maxX && nextX > minX) {
      x += xPositionIncrementFactor;
    } else {
      if (nextY >= maxY) {
        limitReached = true;
        return getActualPosition();
      } else {
        System.out.println("OH SHIT");
        y += yPositionIncrementFactor;
        xPositionIncrementFactor = -xPositionIncrementFactor;
      }
    }

    System.out.println("AFTER x: " + x + ", y: " + y);
    historyPositions.add(new Position(x, y));
    return getActualPosition();
  }

  public Position goBack() {
    if (limitReached) {
      limitReached = false;
      return getActualPosition();
    }

    final var totalPositions = historyPositions.size();
    if (totalPositions - 1 <= 0) return getActualPosition();

    historyPositions.removeLast();

    final var lastPosition = historyPositions.getLast();
    if (lastPosition.y() != y) xPositionIncrementFactor = -xPositionIncrementFactor;
    x = lastPosition.x();
    y = lastPosition.y();

    limitReached = false;
    return getActualPosition();
  }

  public Position getActualPosition() {
    return new Position(x, y);
  }

  public Position reset() {
    limitReached = false;
    historyPositions = new LinkedList<>();
    x = xInitialPosition;
    y = yInitialPosition;
    xPositionIncrementFactor = Math.abs(xPositionIncrementFactor);
    historyPositions.add(new Position(x, y));
    return getActualPosition();
  }
}
