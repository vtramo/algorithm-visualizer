package org.openjfx.utils;

public class Sleep {
  public static void sleep(final long ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
