package org.openjfx.model;

public interface DataStructure<T> {
  boolean search(T value);
  boolean insert(T value);
  boolean remove(T value);
}