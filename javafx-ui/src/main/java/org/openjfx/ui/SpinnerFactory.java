package org.openjfx.ui;

import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public abstract class SpinnerFactory {

  public static void createIntegerSpinner(final Spinner<Integer> spinner) {
    final var spinnerValueFactory = new SpinnerValueFactory
      .IntegerSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE);
    spinnerValueFactory.setMax(999);
    spinnerValueFactory.setMin(-999);
    spinnerValueFactory.setValue(0);
    spinner.setValueFactory(spinnerValueFactory);
    spinner.setEditable(true);
  }
}
