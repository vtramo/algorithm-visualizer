package org.openjfx.ui;

import lombok.Getter;

public class Style {
  private Style() {}

  public enum EdgeStyle {
    EDGE_STYLE("edge"),
    EDGE_HEAD_STYLE("edgeHead");
    @Getter private final String styleName;
    EdgeStyle(final String styleName) {
      this.styleName = styleName;
    }
  }

  public enum VertexStyle {
    RED_TRANSPARENT_VERTEX("redTransparentVertex"),
    GREEN_VERTEX("greenTransparentVertex"),
    VERTEX_DEFAULT_STYLE("vertex");

    @Getter private final String styleName;
    VertexStyle(final String styleName) {
      this.styleName = styleName;
    }
  }
}
