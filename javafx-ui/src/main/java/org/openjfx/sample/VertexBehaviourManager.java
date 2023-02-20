package org.openjfx.sample;

import javafx.scene.input.MouseEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;

import static org.openjfx.sample.DefaultVertexBehaviors.ON_VERTEX_DRAG_DETECTED_SET_VERTEX_TO_FRONT_KEY;
import static org.openjfx.sample.DefaultVertexBehaviors.ON_PRIMARY_BUTTON_CLICKED_UPDATE_VERTEX_POSITION_KEY;

public class VertexBehaviourManager {
  protected enum ActionType {
    VERTEX_DRAGGED_ACTIONS,
    VERTEX_DRAG_DETECTED_ACTIONS,
    VERTEX_PRESSED_ACTIONS,
    MOUSE_RELEASED_ACTIONS
  }

  final private Vertex vertex;
  public VertexBehaviourManager(Vertex vertex) {
    this.vertex = vertex;
  }

  final private ConcurrentMap<Object, BiConsumer<Vertex, MouseEvent>> onVertexDraggedActions = new ConcurrentHashMap<>() {
    {
      put(
        ON_PRIMARY_BUTTON_CLICKED_UPDATE_VERTEX_POSITION_KEY,
        DefaultVertexBehaviors::onPrimaryButtonClickedUpdateVertexPosition
      );
    }
  };
  final private ConcurrentMap<Object, BiConsumer<Vertex, MouseEvent>> onVertexDragDetectedActions = new ConcurrentHashMap<>() {
    {
      put(
        ON_VERTEX_DRAG_DETECTED_SET_VERTEX_TO_FRONT_KEY,
        DefaultVertexBehaviors::onVertexDragDetectedSetVertexToFront
      );
    }
  };
  final private ConcurrentMap<Object, BiConsumer<Vertex, MouseEvent>> onVertexPressedActions = new ConcurrentHashMap<>();
  final private Map<Object, BiConsumer<Vertex, MouseEvent>> onMouseReleasedActions = new ConcurrentHashMap<>();

  public void performAllActions(ActionType actionType, MouseEvent mouseEvent) {
    switch (actionType) {
      case VERTEX_DRAGGED_ACTIONS ->
        onVertexDraggedActions.values().forEach(action -> action.accept(vertex, mouseEvent));
      case VERTEX_DRAG_DETECTED_ACTIONS ->
        onVertexDragDetectedActions.values().forEach(action -> action.accept(vertex, mouseEvent));
      case VERTEX_PRESSED_ACTIONS ->
        onVertexPressedActions.values().forEach(action -> action.accept(vertex, mouseEvent));
      case MOUSE_RELEASED_ACTIONS ->
        onMouseReleasedActions.values().forEach(action -> action.accept(vertex, mouseEvent));
    }
  }

  public void addOnVertexDraggedAction(final BiConsumer<Vertex, MouseEvent> action, final Object key) {
    onVertexDraggedActions.put(key, action);
  }
  public void addOnVertexDragDetectedAction(final BiConsumer<Vertex, MouseEvent> action, final Object key) {
    onVertexDragDetectedActions.put(key, action);
  }
  public void addOnVertexPressedAction(final BiConsumer<Vertex, MouseEvent> action, final Object key) {
    onVertexPressedActions.put(key, action);
  }
  public void addOnMouseReleasedAction(final BiConsumer<Vertex, MouseEvent> action, final Object key) {
    onMouseReleasedActions.put(key, action);
  }

  public boolean removeOnMouseReleasedAction(final int key) {
    return onMouseReleasedActions.remove(key) != null;
  }
  public boolean removeOnVertexDraggedAction(final int key) {
    return onVertexDraggedActions.remove(key) != null;
  }
  public boolean removeOnVertexDragDetectedAction(final int key) {
    return onVertexDragDetectedActions.remove(key) != null;
  }
  public boolean removeOnVertexPressedAction(final BiConsumer<Vertex, MouseEvent> action, final Object key) {
    return onVertexPressedActions.remove(key) != null;
  }
}
