package org.openjfx.model;

import lombok.Getter;
import org.openjfx.utils.Constants;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ObservableDataStructure<T> extends ObservableStepDataStructure<T> {
  private final ObservableStepDataStructure<T> dataStructure;

  private final List<Consumer<Position>> onValueFoundListeners = new LinkedList<>();
  private final List<Consumer<Position>> onValueRemovedListeners = new LinkedList<>();
  private final List<BiConsumer<T, Position>> onValueInsertedListeners = new LinkedList<>();

  @Getter private final PositionCalculator lastPositionCalculator = buildPositionCalculator();
  @Getter private final PositionCalculator currentPositionCalculator = buildPositionCalculator();

  public ObservableDataStructure(final ObservableStepDataStructure<T> dataStructure) {
    this.dataStructure = dataStructure;
  }

  public void addStepListener(final Consumer<Position> searchStepAction) {
    dataStructure.addStepObserver(value -> {
      final var position = currentPositionCalculator.getActualPosition();
      searchStepAction.accept(position);
      currentPositionCalculator.goAhead();
    });
  }

  public void addOnValueFoundListener(final Consumer<Position> onValueFoundListener) {
    onValueFoundListeners.add(onValueFoundListener);}

  public void addOnValueInsertedListener(final BiConsumer<T, Position> onValueInsertedListener) {
    onValueInsertedListeners.add(onValueInsertedListener);
  }

  public void addOnValueRemovedListener(final Consumer<Position> onValueRemovedListener) {
    onValueRemovedListeners.add(onValueRemovedListener);
  }

  private PositionCalculator buildPositionCalculator() {
    return PositionCalculator.builder()
      .xInitialPosition(25)
      .yInitialPosition(25)
      .xPositionIncrementFactor(75)
      .yPositionIncrementFactor(60)
      .maxX(Constants.SCREEN_WIDTH)
      .maxY(Constants.SCREEN_HEIGHT)
      .build();
  }

  @Override
  public boolean search(T value) {
    final boolean found = dataStructure.search(value);
    final var position = currentPositionCalculator.getActualPosition();
    if (found) onValueFoundListeners.forEach(listener -> listener.accept(position));
    currentPositionCalculator.reset();
    return found;
  }

  @Override
  public boolean remove(T value) {
    final boolean removed = dataStructure.remove(value);
    final var position = currentPositionCalculator.getActualPosition();
    if (removed) {
      onValueRemovedListeners.forEach(listener -> listener.accept(position));
      lastPositionCalculator.goBack();
    }
    currentPositionCalculator.reset();
    return removed;
  }

  @Override
  public boolean insert(T value) {
    if (lastPositionCalculator.isLimitReached()) return false;
    final Position position = lastPositionCalculator.getActualPosition();
    final boolean inserted = dataStructure.insert(value);
    if (inserted) onValueInsertedListeners.forEach(listener -> listener.accept(value, position));
    lastPositionCalculator.goAhead();
    return inserted;
  }
}