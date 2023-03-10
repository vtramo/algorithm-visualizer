package org.openjfx.model;

import java.util.Optional;

public interface PositionValueDataStructure<T> {
  Optional<ObservablePositionValue<T>> search(T value);
  Optional<ObservablePositionValue<T>> insert(T value);
  Optional<ObservablePositionValue<T>> remove(T value);
  int size();
}