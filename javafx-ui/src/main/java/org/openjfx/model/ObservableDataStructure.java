package org.openjfx.model;

import lombok.Getter;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ObservableDataStructure<T> implements DataStructure<T> {
  @Getter private final PositionCalculator lastPositionCalculator = buildPositionCalculator();
  @Getter private final PositionCalculator currentPositionCalculator = buildPositionCalculator();
  private final DataStructure<T> dataStructure;
  private final List<Consumer<Position>> onValueFoundListeners = new LinkedList<>();
  private final List<Consumer<Position>> onValueRemovedListeners = new LinkedList<>();
  private final List<BiConsumer<T, Position>> onValueInsertedListeners = new LinkedList<>();

  public ObservableDataStructure(final DataStructure<T> dataStructure) {
    this.dataStructure = dataStructure;
  }

  public void addOnValueFoundListener(final Consumer<Position> onValueFoundListener) {
    onValueFoundListeners.add(onValueFoundListener);}
  public void addOnValueInsertedListener(final BiConsumer<T, Position> onValueInsertedListener) {
    onValueInsertedListeners.add(onValueInsertedListener);
  }

  @Override
  public void setSearchStepAction(final Consumer<T> searchStepAction) {
    dataStructure.setSearchStepAction(searchStepAction);
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
    final var position = currentPositionCalculator.getActualPosition();
    final boolean removed = dataStructure.remove(value);
    if (removed) onValueRemovedListeners.forEach(listener -> listener.accept(position));
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

  private PositionCalculator buildPositionCalculator() {
    return PositionCalculator.builder()
      .xInitialPosition(50)
      .yInitialPosition(50)
      .xPositionIncrementFactor(75)
      .yPositionIncrementFactor(60)
      .maxX(1280)
      .maxY(720)
      .build();
  }
}
