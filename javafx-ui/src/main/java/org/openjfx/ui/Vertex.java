package org.openjfx.ui;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import org.openjfx.model.Position;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

import static org.openjfx.ui.Style.*;

public class Vertex extends Button {
  @Setter @Getter private Vertex prev, next;
  @Setter @Getter private Integer value;
  @Getter private Position position;

  private final ObservableList<String> vertexStyleClass = getStyleClass();
  private final List<BiConsumer<Double, Double>> onXChangedListeners = new LinkedList<>();
  private final List<BiConsumer<Double, Double>> onYChangedListeners = new LinkedList<>();

  Vertex(final int value, final Position position) {
    initVertex(value, position);
  }

  private void initVertex(final Integer value, final Position position) {
    this.value = value;
    this.position = position;

    setText(value.toString());
    addStyle(VertexStyle.VERTEX_DEFAULT_STYLE);

    setVertexPosition(position);
    centerVertex();

    layoutXProperty().addListener((observableValue, oldNumber, newNumber) ->
      onXChangedListeners.forEach(biConsumer ->
        biConsumer.accept(
          oldNumber.doubleValue(),
          newNumber.doubleValue()
        )
      )
    );

    layoutYProperty().addListener((observableValue, oldNumber, newNumber) ->
      onYChangedListeners.forEach(biConsumer ->
        biConsumer.accept(
          oldNumber.doubleValue(),
          newNumber.doubleValue()
        )
      )
    );
  }

  protected void setVertexPosition(final Position position) {
    this.position = position;
    setLayoutX(position.x());
    setLayoutY(position.y());
  }

  protected void setVertexPositionTimelineAnimation(final Position position, final long msDuration) {
    this.position = position;
    final var timeline = new Timeline();
    final var xKeyValue = new KeyValue(layoutXProperty(), position.x());
    final var yKeyValue = new KeyValue(layoutYProperty(), position.y());
    final var xKeyFrame = new KeyFrame(Duration.millis(msDuration), xKeyValue);
    final var yKeyFrame = new KeyFrame(Duration.millis(msDuration), yKeyValue);
    timeline.getKeyFrames().add(xKeyFrame);
    timeline.getKeyFrames().add(yKeyFrame);
    timeline.play();
  }

  protected void centerVertex() {
    final int byTwo = -2;
    final var width  = widthProperty().divide(byTwo);
    final var height = heightProperty().divide(byTwo);
    translateXProperty().bind(width);
    translateYProperty().bind(height);
  }

  protected void addStyle(final VertexStyle style) {
    vertexStyleClass.add(style.getStyleName());
  }

  protected void removeStyle(final VertexStyle style) {
    vertexStyleClass.remove(style.getStyleName());
  }

  protected void addOnXChangedListener(final BiConsumer<Double, Double> biConsumer) {
    onXChangedListeners.add(biConsumer);
  }

  protected void addOnYChangedListener(final BiConsumer<Double, Double> biConsumer) {
    onYChangedListeners.add(biConsumer);
  }
}
