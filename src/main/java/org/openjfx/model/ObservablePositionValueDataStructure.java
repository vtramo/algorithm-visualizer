package org.openjfx.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class ObservablePositionValueDataStructure<T> extends ObservableStepPositionValueDataStructure<T> {
  private final ObservableStepPositionValueDataStructure<T> observableStepDataStructure;

  private final List<Consumer<ObservablePositionValue<T>>> onValueFoundListeners = new LinkedList<>();
  private final List<Consumer<ObservablePositionValue<T>>> onValueRemovedListeners = new LinkedList<>();
  private final List<Consumer<ObservablePositionValue<T>>> onValueInsertedListeners = new LinkedList<>();

  public ObservablePositionValueDataStructure(final ObservableStepPositionValueDataStructure<T> observableStepDataStructure) {
    this.observableStepDataStructure = observableStepDataStructure;
  }

  public void addStepListener(final Consumer<ObservablePositionValue<T>> searchStepAction) {
    observableStepDataStructure.addStepObserver(searchStepAction);
  }

  public void addOnValueFoundListener(final Consumer<ObservablePositionValue<T>> onValueFoundListener) {
    onValueFoundListeners.add(onValueFoundListener);}

  public void addOnValueInsertedListener(final Consumer<ObservablePositionValue<T>> onValueInsertedListener) {
    onValueInsertedListeners.add(onValueInsertedListener);
  }

  public void addOnValueRemovedListener(final Consumer<ObservablePositionValue<T>> onValueRemovedListener) {
    onValueRemovedListeners.add(onValueRemovedListener);
  }

  @Override
  public Optional<ObservablePositionValue<T>> search(T value) {
    final var searchedPositionValue = observableStepDataStructure.search(value);

    searchedPositionValue.ifPresent(positionValue ->
      onValueFoundListeners.forEach(listener -> listener.accept(searchedPositionValue.get()))
    );

    return searchedPositionValue;
  }

  @Override
  public Optional<ObservablePositionValue<T>> remove(T value) {
    final var removedPositionValue = observableStepDataStructure.remove(value);

    removedPositionValue.ifPresent(positionValue ->
      onValueRemovedListeners.forEach(listener -> listener.accept(positionValue))
    );

    return removedPositionValue;
  }

  @Override
  public Optional<ObservablePositionValue<T>> insert(T value) {
    final var insertedPositionValue = observableStepDataStructure.insert(value);

    insertedPositionValue.ifPresent(positionValue ->
      onValueInsertedListeners.forEach(listener -> listener.accept(positionValue))
    );

    return insertedPositionValue;
  }

  @Override
  public int size() {
    return observableStepDataStructure.size();
  }
}