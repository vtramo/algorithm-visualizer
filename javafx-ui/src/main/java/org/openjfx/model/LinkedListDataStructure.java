package org.openjfx.model;

import java.util.LinkedList;
import java.util.function.Consumer;

public class LinkedListDataStructure<T> extends LinkedList<T> implements DataStructure<T> {
  private Consumer<T> searchStepAction;
  @Override
  public void setSearchStepAction(final Consumer<T> searchStepAction) {
    this.searchStepAction = searchStepAction;
  }
  @Override
  public boolean search(final T value) {
    for (final T v: this) {
      if (v == value) return true;
      searchStepAction.accept(v);
    }
    return false;
  }
  @Override
  public boolean insert(final T value) {
    return add(value);
  }
  @Override
  public boolean remove(final Object value) {
    return false;
  }
}
