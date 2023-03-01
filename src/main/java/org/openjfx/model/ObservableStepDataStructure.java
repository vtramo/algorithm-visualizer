package org.openjfx.model;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public abstract class ObservableStepDataStructure<T> implements DataStructure<T> {
  protected final List<Consumer<T>> stepObservers = new LinkedList<>();

  public void addStepObserver(final Consumer<T> stepObserver) {
    stepObservers.add(stepObserver);
  }
}