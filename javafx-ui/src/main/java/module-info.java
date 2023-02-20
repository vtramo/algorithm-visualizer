module org.openjfx {
  requires javafx.controls;
  requires javafx.fxml;
  exports org.openjfx;
  exports org.openjfx.sample;
  opens org.openjfx.sample to javafx.fxml;
}