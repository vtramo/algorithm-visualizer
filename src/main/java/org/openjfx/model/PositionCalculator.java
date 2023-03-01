package org.openjfx.model;

import lombok.Builder;
import lombok.Getter;

import java.util.LinkedList;
import java.util.Optional;

public class PositionCalculator {
  private final double xInitialPosition, yInitialPosition;
  private double xPositionIncrementFactor, yPositionIncrementFactor;
  private final double maxX, maxY, minX, minY;
  @Getter
  private boolean limitReached;
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

  public Optional<Position> goAhead() {
    final var nextX = x + xPositionIncrementFactor;

    if (nextX < maxX && nextX > minX) {
      x += xPositionIncrementFactor;
    } else {
      y += yPositionIncrementFactor;
      final var nextY = y + yPositionIncrementFactor;

      if (nextY > maxY) {
        limitReached = true;
        return Optional.empty();
      } else {
        xPositionIncrementFactor = -xPositionIncrementFactor;
      }
    }

    historyPositions.add(new Position(x, y));
    return Optional.of(getActualPosition());
  }

  public Optional<Position> goBack() {
    final var totalPositions = historyPositions.size();
    if (totalPositions - 1 <= 0) return Optional.empty();

    final var lastPosition = historyPositions.get(totalPositions - 2);
    x = lastPosition.x();
    y = lastPosition.y();

    historyPositions.removeLast();
    limitReached = false;

    return Optional.of(getActualPosition());
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
