package org.openjfx.ui;

import javafx.scene.input.MouseEvent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;

import static org.openjfx.ui.DefaultVertexBehaviors.ON_PRIMARY_BUTTON_CLICKED_UPDATE_VERTEX_POSITION_KEY;
import static org.openjfx.ui.DefaultVertexBehaviors.ON_VERTEX_DRAG_DETECTED_SET_VERTEX_TO_FRONT_KEY;

class VertexBehaviorManager {
  protected enum ActionType {
    VERTEX_DRAGGED_ACTIONS,
    VERTEX_DRAG_DETECTED_ACTIONS,
    VERTEX_PRESSED_ACTIONS,
    MOUSE_RELEASED_ACTIONS
  }

  final private Vertex vertex;
  VertexBehaviorManager(Vertex vertex) {
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
  final private ConcurrentMap<Object, BiConsumer<Vertex, MouseEvent>> onMouseReleasedActions = new ConcurrentHashMap<>();

  protected void performAllActions(ActionType actionType, MouseEvent mouseEvent) {
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

  protected void addOnVertexDraggedAction(final BiConsumer<Vertex, MouseEvent> action, final Object key) {
    onVertexDraggedActions.put(key, action);
  }
  protected void addOnVertexDragDetectedAction(final BiConsumer<Vertex, MouseEvent> action, final Object key) {
    onVertexDragDetectedActions.put(key, action);
  }
  protected void addOnVertexPressedAction(final BiConsumer<Vertex, MouseEvent> action, final Object key) {
    onVertexPressedActions.put(key, action);
  }
  protected void addOnMouseReleasedAction(final BiConsumer<Vertex, MouseEvent> action, final Object key) {
    onMouseReleasedActions.put(key, action);
  }

  protected boolean removeOnMouseReleasedAction(final Object key) {
    return onMouseReleasedActions.remove(key) != null;
  }
  protected boolean removeOnVertexDraggedAction(final Object key) {
    return onVertexDraggedActions.remove(key) != null;
  }
  protected boolean removeOnVertexDragDetectedAction(final Object key) {
    return onVertexDragDetectedActions.remove(key) != null;
  }
  protected boolean removeOnVertexPressedAction(final Object key) {
    return onVertexPressedActions.remove(key) != null;
  }
}
