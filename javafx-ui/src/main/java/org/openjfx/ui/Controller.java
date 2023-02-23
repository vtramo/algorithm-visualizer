package org.openjfx.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.LinkedList;
import java.util.function.Consumer;

import static org.openjfx.ui.VertexBehavior.IGNORE_KEY;
import static org.openjfx.ui.style.VertexStyles.GREEN_VERTEX;
import static org.openjfx.ui.style.VertexStyles.RED_TRANSPARENT_VERTEX;
import static org.openjfx.utils.Sleep.sleep;

public class Controller {
  private static final long ALGORITHM_DISPLAY_DELAY = 250L;
  private static final long ALGORITHM_DISPLAY_DELAY_FOUND = 1500L;
  private long actualAlgorithmDisplaySpeed = 50;

  @FXML private Pane graph;
  @FXML private Slider algorithmDisplaySpeedSlider;
  @FXML public Spinner<Integer> searchValueSpinner;
  @FXML public Spinner<Integer> removeValueSpinner;
  @FXML public Spinner<Integer> addValueSpinner;

  private final LinkedList<Vertex> vertices = new LinkedList<>() {
    @Override
    public Vertex getLast() {
      return size() == 0 ? null : super.getLast();
    }
  };
  private NextVertexPositionCalculator nextVertexPositionCalculator;

  @FXML
  protected void initialize() {
    buildIntegerSpinner(searchValueSpinner);
    buildIntegerSpinner(removeValueSpinner);
    buildIntegerSpinner(addValueSpinner);
  }
  private void buildIntegerSpinner(final Spinner<Integer> spinner) {
    final var spinnerValueFactory = new SpinnerValueFactory
      .IntegerSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE);
    spinnerValueFactory.setValue(0);
    spinner.setValueFactory(spinnerValueFactory);
    spinner.setEditable(true);
  }

  @FXML
  protected void onGraphPressed(final MouseEvent __) {
    return;
  }
  @FXML
  protected void onAddClicked(final MouseEvent __) {
    if (nextVertexPositionCalculator == null) buildNextNodePositionCalculator();
    if (nextVertexPositionCalculator.isLimitReached()) {
      // TODO: Limit Reached
    } else {
      final var actualPosition = nextVertexPositionCalculator.getActualPosition();

      final var newVertexValue = addValueSpinner.getValue();
      final var lastVertex = vertices.getLast();
      final var newVertex = spawnVertex(newVertexValue, actualPosition[0], actualPosition[1]);
      if (lastVertex != null) linkVertexes(lastVertex, newVertex);

      nextVertexPositionCalculator.goAhead();
    }
  }
  @FXML
  protected void onSearchButtonClicked(final MouseEvent __) {
    final var valueToSearch = searchValueSpinner.getValue();
    searchVertexValue(valueToSearch, v -> {});
  }
  @FXML
  protected void onRemoveButtonClicked(MouseEvent __) {
    final var valueToSearch = removeValueSpinner.getValue();
    searchVertexValue(valueToSearch, vertex -> {
      final var prev = vertex.getPrev();
      final var next = vertex.getNext();
      Platform.runLater(() -> {
        if (prev != null && next != null) linkVertexes(prev, next);
        if (next != null && prev == null) next.setPrev(null);
        if (prev != null && next == null) prev.setNext(null);
        if (vertices.getLast() == vertex) nextVertexPositionCalculator.goBack();
        this.removeVertex(vertex);
      });
    });
  }
  @FXML
  protected void onAlgorithmDisplaySpeedSliderValueChanged(final MouseEvent __) {
    actualAlgorithmDisplaySpeed = (long) algorithmDisplaySpeedSlider.getValue();
  }
  private void searchVertexValue(final int valueToSearch, final Consumer<Vertex> action) {
    Thread.startVirtualThread(() -> {
      for (final var vertex: vertices) {
        final var vertexStyleClass = vertex.getStyleClass();
        String style;
        long delay;
        if (Integer.valueOf(vertex.getText()).equals(valueToSearch)) {
          style = GREEN_VERTEX;
          delay = ALGORITHM_DISPLAY_DELAY_FOUND;
        } else {
          style = RED_TRANSPARENT_VERTEX;
          delay = ALGORITHM_DISPLAY_DELAY - actualAlgorithmDisplaySpeed;
        }
        vertexStyleClass.add(style);
        sleep(delay);
        vertexStyleClass.remove(style);
        if (style.equals(GREEN_VERTEX)) {
          action.accept(vertex);
          break;
        }
      }
    });
  }
  protected Vertex spawnVertex(final Integer value, final double x, final double y) {
    final Vertex prev = vertices.getLast();
    final Vertex vertex = new Vertex(value, x, y);
    if (prev != null) {
      prev.setNext(vertex);
      vertex.setPrev(prev);
    }
    vertices.add(vertex);

    final var vertexBehaviorManager = vertex.getVertexBehaviourManager();
    final var vertexDragDetectedArrowCreatorBehavior = new VertexDragDetectedArrowCreatorBehavior(this, vertexBehaviorManager);
    final var vertexDeletionBehavior = new VertexDeletionBehavior(this, vertexBehaviorManager);
    vertexBehaviorManager.addOnVertexDragDetectedAction(vertexDragDetectedArrowCreatorBehavior, IGNORE_KEY);
    vertexBehaviorManager.addOnVertexPressedAction(vertexDeletionBehavior, IGNORE_KEY);

    addVertex(vertex);
    return vertex;
  }
  protected Arrow linkVertexes(final Vertex vertex1, final Vertex vertex2) {
    final var arrow = new Arrow(vertex1, vertex2);
    addNode(arrow);
    return arrow;
  }
  protected boolean addVertex(final Vertex vertex) {
    final var graphChildren = graph.getChildren();
    return graphChildren.add(vertex);
  }
  protected boolean removeVertex(final Vertex vertex) {
    final var graphChildren = graph.getChildren();
    vertex.forAllEdges(this::removeNode);
    vertices.remove(vertex);
    return graphChildren.remove(vertex);
  }
  protected boolean addNode(final Node node) {
    final var graphChildren = graph.getChildren();
    return graphChildren.add(node);
  }
  protected boolean removeNode(final Node node) {
    final var graphChildren = graph.getChildren();
    return graphChildren.remove(node);
  }
  private void buildNextNodePositionCalculator() {
    final var graphLayoutBounds = graph.getLayoutBounds();
    nextVertexPositionCalculator = NextVertexPositionCalculator.builder()
      .xInitialPosition(50)
      .yInitialPosition(50)
      .xPositionIncrementFactor(100)
      .yPositionIncrementFactor(100)
      .maxX(graphLayoutBounds.getMaxX())
      .maxY(graphLayoutBounds.getMaxY())
      .build();
  }
}