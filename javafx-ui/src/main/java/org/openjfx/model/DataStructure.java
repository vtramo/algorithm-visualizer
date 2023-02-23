package org.openjfx.model;

import java.util.function.Consumer;

public interface DataStructure<T> {
  void setSearchStepAction(Consumer<T> searchStepAction);
  boolean search(T value);
  boolean insert(T value);
  boolean remove(T value);
}
