package org.openjfx;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import org.openjfx.model.LinkedListDataStructure;
import org.openjfx.model.ObservableDataStructure;
import org.openjfx.ui.Graph;

public class Controller {
  private final ObservableDataStructure<Integer> observableDataStructure =
    new ObservableDataStructure<>(new LinkedListDataStructure<>());

  @FXML private BorderPane borderPane;
  @FXML private Slider algorithmDisplaySpeedSlider;
  @FXML public Spinner<Integer> searchValueSpinner;
  @FXML public Spinner<Integer> removeValueSpinner;
  @FXML public Spinner<Integer> addValueSpinner;

  @FXML protected void initialize() {
    buildIntegerSpinner(searchValueSpinner);
    buildIntegerSpinner(removeValueSpinner);
    buildIntegerSpinner(addValueSpinner);
    final var graph = new Graph(observableDataStructure);
    borderPane.setRight(graph);
  }
  @FXML protected void onAddClicked(final MouseEvent __) {
    final var valueToAdd = addValueSpinner.getValue();
    observableDataStructure.insert(valueToAdd);
  }
  @FXML protected void onSearchButtonClicked(final MouseEvent __) {
    final var valueToSearch = searchValueSpinner.getValue();
    Thread.startVirtualThread(() -> observableDataStructure.search(valueToSearch));
  }
  @FXML protected void onRemoveButtonClicked(MouseEvent __) {
    /*final var valueToSearch = removeValueSpinner.getValue();
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
    });*/
  }
  @FXML protected void onAlgorithmDisplaySpeedSliderValueChanged(final MouseEvent __) {

  }

  private void buildIntegerSpinner(final Spinner<Integer> spinner) {
    final var spinnerValueFactory = new SpinnerValueFactory
      .IntegerSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE);
    spinnerValueFactory.setValue(0);
    spinner.setValueFactory(spinnerValueFactory);
    spinner.setEditable(true);
  }
}