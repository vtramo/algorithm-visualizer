package org.openjfx.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

@AllArgsConstructor
public class ObservablePositionValue<T> {
  @Getter @Setter private T value;
  @Getter private Position position;

  private final List<BiConsumer<Position, ObservablePositionValue<T>>> onPositionChangedListeners = new LinkedList<>();

  public void addOnPositionChangedListener(final BiConsumer<Position, ObservablePositionValue<T>> consumer) {
    onPositionChangedListeners.add(consumer);
  }

  public void setPosition(final Position position) {
    Objects.requireNonNull(position);
    final var oldPosition = this.position;
    this.position = position;
    onPositionChangedListeners.forEach(consumer -> consumer.accept(oldPosition, this));
  }
}
