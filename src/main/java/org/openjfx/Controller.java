package org.openjfx;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import org.openjfx.model.LinkedListPositionValueDataStructure;
import org.openjfx.model.ObservablePositionValueDataStructure;
import org.openjfx.ui.Graph;
import org.openjfx.ui.SpinnerFactory;

public class Controller {
  private final ObservablePositionValueDataStructure<Integer> observableDataStructure =
    new ObservablePositionValueDataStructure<>(new LinkedListPositionValueDataStructure<>());

  private Graph graph;
  @FXML private BorderPane borderPane;
  @FXML private Slider algorithmDisplaySpeedSlider;
  @FXML private Spinner<Integer> searchValueSpinner, removeValueSpinner, addValueSpinner;

  @FXML protected void initialize() {
    SpinnerFactory.createIntegerSpinner(searchValueSpinner);
    SpinnerFactory.createIntegerSpinner(removeValueSpinner);
    SpinnerFactory.createIntegerSpinner(addValueSpinner);

    graph = new Graph(observableDataStructure);
    borderPane.setBottom(graph);
  }

  @FXML protected void onAddClicked(final MouseEvent __) {
    final int valueToAdd = addValueSpinner.getValue();
    observableDataStructure.insert(valueToAdd);
  }

  @FXML protected void onSearchButtonClicked(final MouseEvent __) {
    final int valueToSearch = searchValueSpinner.getValue();
    Thread.startVirtualThread(() -> observableDataStructure.search(valueToSearch));
  }

  @FXML protected void onRemoveButtonClicked(MouseEvent __) {
    final int valueToRemove = removeValueSpinner.getValue();
    Thread.startVirtualThread(() -> observableDataStructure.remove(valueToRemove));
  }

  @FXML protected void onAlgorithmDisplaySpeedSliderValueChanged(final MouseEvent __) {
    final long delay = (long) algorithmDisplaySpeedSlider.getValue();
    graph.setDelay(delay);
  }
}