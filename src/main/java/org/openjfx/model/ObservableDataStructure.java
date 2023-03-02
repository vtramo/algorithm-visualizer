package org.openjfx.model;

import lombok.Getter;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ObservableDataStructure<T> extends ObservableStepDataStructure<T> {
  private final ObservableStepDataStructure<T> observableStepDataStructure;

  private final List<Consumer<Position>> onValueFoundListeners = new LinkedList<>();
  private final List<Consumer<Position>> onValueRemovedListeners = new LinkedList<>();
  private final List<BiConsumer<T, Position>> onValueInsertedListeners = new LinkedList<>();

  @Getter private final PositionCalculator lastPositionCalculator = buildPositionCalculator();
  @Getter private final PositionCalculator currentPositionCalculator = buildPositionCalculator();

  public ObservableDataStructure(final ObservableStepDataStructure<T> observableStepDataStructure) {
    this.observableStepDataStructure = observableStepDataStructure;
  }

  public void addStepListener(final Consumer<Position> searchStepAction) {
    observableStepDataStructure.addStepObserver(value -> {
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
      .maxX(1280)
      .maxY(720)
      .build();
  }

  @Override
  public boolean search(T value) {
    final boolean found = observableStepDataStructure.search(value);
    final var actualPosition = currentPositionCalculator.getActualPosition();

    if (found) onValueFoundListeners.forEach(listener -> listener.accept(actualPosition));

    currentPositionCalculator.reset();
    return found;
  }

  @Override
  public boolean remove(T value) {
    final boolean removed = observableStepDataStructure.remove(value);
    final var actualPosition = currentPositionCalculator.getActualPosition();

    if (removed) {
      onValueRemovedListeners.forEach(listener -> listener.accept(actualPosition));
      if (size() == 0) lastPositionCalculator.reset();
      else             lastPositionCalculator.goBack();
    }

    currentPositionCalculator.reset();
    return removed;
  }

  @Override
  public boolean insert(T value) {
    if (lastPositionCalculator.isLimitReached()) return false;

    final var lastPosition = lastPositionCalculator.getActualPosition();
    final boolean inserted = observableStepDataStructure.insert(value);

    if (inserted) onValueInsertedListeners.forEach(listener -> listener.accept(value, lastPosition));

    lastPositionCalculator.goAhead();
    return inserted;
  }

  @Override
  public int size() {
    return observableStepDataStructure.size();
  }
}