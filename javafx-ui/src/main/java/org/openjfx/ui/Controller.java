package org.openjfx.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

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

  @FXML private AnchorPane graph;
  @FXML private Slider algorithmDisplaySpeedSlider;
  @FXML public Spinner<Integer> searchValueSpinner;
  @FXML public Spinner<Integer> removeValueSpinner;

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
  }
  private void buildIntegerSpinner(final Spinner<Integer> spinner) {
    final var spinnerValueFactory = new SpinnerValueFactory
      .IntegerSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE);
    spinnerValueFactory.setValue(0);
    spinner.setValueFactory(spinnerValueFactory);
    spinner.setEditable(true);
  }

  @FXML
  protected void onGraphPressed(final MouseEvent mouseEvent) {
    if (!mouseEvent.isPrimaryButtonDown()) return;
    spawnVertex(mouseEvent.getX(), mouseEvent.getY());
  }
  @FXML
  protected void onAddClicked(final MouseEvent __) {
    if (nextVertexPositionCalculator == null) buildNextNodePositionCalculator();
    final var actualPosition = nextVertexPositionCalculator.getActualPosition();

    final var lastVertex = vertices.getLast();
    final var vertex = spawnVertex(actualPosition[0], actualPosition[1]);
    if (lastVertex != null) linkVertexes(lastVertex, vertex);

    nextVertexPositionCalculator.goAhead();
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
      Platform.runLater(() -> this.removeVertex(vertex));
      if (vertices.getLast().equals(vertex)) nextVertexPositionCalculator.goBack();
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
  protected Vertex spawnVertex(final double x, final double y) {
    final Vertex vertex = new Vertex(x, y);
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