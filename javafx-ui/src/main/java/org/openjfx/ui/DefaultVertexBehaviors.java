package org.openjfx.ui;

import javafx.scene.input.MouseEvent;

class DefaultVertexBehaviors {

  private DefaultVertexBehaviors() {}
  protected static final Object ON_PRIMARY_BUTTON_CLICKED_UPDATE_VERTEX_POSITION_KEY = new Object();
  protected static final Object ON_VERTEX_DRAG_DETECTED_SET_VERTEX_TO_FRONT_KEY = new Object();

  protected static void onPrimaryButtonClickedUpdateVertexPosition(final Vertex vertex, final MouseEvent mouseEvent) {
    if (!mouseEvent.isPrimaryButtonDown()) return;
    vertex.updateVertexPosition(mouseEvent);
  }
  protected static void onVertexDragDetectedSetVertexToFront(final Vertex vertex, final MouseEvent __) {
    vertex.toFront();
  }
}
