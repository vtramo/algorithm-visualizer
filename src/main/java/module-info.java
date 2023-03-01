module org.openjfx {
  requires javafx.controls;
  requires javafx.fxml;
  requires lombok;
  exports org.openjfx;
  exports org.openjfx.ui;
  opens org.openjfx.ui to javafx.fxml;
  opens org.openjfx to javafx.fxml;
  exports org.openjfx.utils;
  opens org.openjfx.utils to javafx.fxml;
}