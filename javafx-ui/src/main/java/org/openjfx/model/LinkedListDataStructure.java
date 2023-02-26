package org.openjfx.model;

import java.util.LinkedList;
import java.util.List;

public class LinkedListDataStructure<T> extends ObservableStepDataStructure<T> {
  private final List<T> list = new LinkedList<>();

  @Override
  public boolean search(final T value) {
    for (final T v: list) {
      if (v == value) return true;
      stepObservers.forEach(consumer -> consumer.accept(v));
    }
    return false;
  }

  @Override
  public boolean insert(final T value) {
    return list.add(value);
  }

  @Override
  public boolean remove(final Object value) {
    for (final T v: list) {
      if (v == value) {
        list.remove(v);
        return true;
      }
      stepObservers.forEach(consumer -> consumer.accept(v));
    }
    return false;
  }
}
