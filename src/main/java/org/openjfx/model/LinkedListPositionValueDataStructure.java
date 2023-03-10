package org.openjfx.model;

import lombok.Getter;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;

public class LinkedListPositionValueDataStructure<T> extends ObservableStepPositionValueDataStructure<T> {
  private final LinkedList<ObservablePositionValue<T>> list = new LinkedList<>();
  private final PositionCalculator lastPositionCalculator = new PositionCalculator();

  @Override
  public Optional<ObservablePositionValue<T>> search(final T value) {
    for (final ObservablePositionValue<T> positionValue: list) {
      if (positionValue.getValue() == value) return Optional.of(positionValue);
      stepObservers.forEach(consumer -> consumer.accept(positionValue));
    }
    return Optional.empty();
  }

  @Override
  public Optional<ObservablePositionValue<T>> insert(final T value) {
    if (lastPositionCalculator.isLimitReached()) return Optional.empty();
    final var nextPosition = lastPositionCalculator.goAhead();
    final var insertedPositionValue = new ObservablePositionValue<>(value, nextPosition);
    list.add(insertedPositionValue);
    return Optional.of(insertedPositionValue);
  }

  @Override
  public Optional<ObservablePositionValue<T>> remove(final Object value) {
    final var listIterator = list.iterator();
    while (listIterator.hasNext()) {
      final var positionValue = listIterator.next();
      if (positionValue.getValue() == value) {
        lastPositionCalculator.goBack();
        listIterator.remove();
        rollback(positionValue, listIterator);
        return Optional.of(positionValue);
      }
      stepObservers.forEach(consumer -> consumer.accept(positionValue));
    }
    return Optional.empty();
  }

  private void rollback(ObservablePositionValue<T> positionValue, Iterator<ObservablePositionValue<T>> iterator) {
    var currentPositionValue = positionValue;
    while (iterator.hasNext()) {
      final var nextPositionValue = iterator.next();
      final var newPosition = currentPositionValue.getPosition();
      nextPositionValue.setPosition(newPosition);
      currentPositionValue = nextPositionValue;
    }
  }

  @Override
  public int size() {
    return list.size();
  }

  class PositionCalculator {
    @Getter private final double xInitialPosition = 25, yInitialPosition = 25;
    @Getter private double xPositionIncrementFactor = 75;
    @Getter private final double yPositionIncrementFactor = 60;
    @Getter private final double maxX = 1280, maxY = 720, minX = 0, minY = 0;
    @Getter private boolean limitReached;
    private double x, y;

    public Position goAhead() {
      if (limitReached) throw new IndexOutOfBoundsException("Limit reached!");

      if (list.isEmpty()) {
        x = xInitialPosition; y = yInitialPosition;
      } else {
        final var nextX = x + xPositionIncrementFactor;

        if (nextX < maxX && nextX > minX) {
          x += xPositionIncrementFactor;
        } else {
          y += yPositionIncrementFactor;
          xPositionIncrementFactor = -xPositionIncrementFactor;
        }
      }

      checkIfItIsTheLastPossiblePosition();
      return getActualPosition();
    }

    private void checkIfItIsTheLastPossiblePosition() {
      final var nextX = x + xPositionIncrementFactor;
      final var nextY = y + yPositionIncrementFactor;
      limitReached = (nextY >= maxY && (nextX <= minX || nextX >= maxX));
    }

    public Position goBack() {
      final var totalPositions = list.size();
      if (totalPositions - 1 <= 0) throw new IndexOutOfBoundsException("Limit reached!");

      final var penultimatePositionValue = list.get(totalPositions - 2);
      final var penultimatePosition = penultimatePositionValue.getPosition();
      if (y != penultimatePosition.y()) xPositionIncrementFactor = -xPositionIncrementFactor;
      x = penultimatePosition.x();
      y = penultimatePosition.y();

      limitReached = false;
      return getActualPosition();
    }

    public Position getActualPosition() {
      return new Position(x, y);
    }
  }
}
