package org.openjfx.model;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public abstract class ObservableStepPositionValueDataStructure<T> implements PositionValueDataStructure<T> {
  protected final List<Consumer<ObservablePositionValue<T>>> stepObservers = new LinkedList<>();

  public void addStepObserver(final Consumer<ObservablePositionValue<T>> stepObserver) {
    stepObservers.add(stepObserver);
  }

  protected void notifyStepObservers(final ObservablePositionValue<T> positionValue) {
    stepObservers.forEach(consumer -> consumer.accept(positionValue));
  }
}