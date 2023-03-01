package org.openjfx;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openjfx.model.Position;
import org.openjfx.model.PositionCalculator;

import java.util.Optional;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isEmpty;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@DisplayName("A PositionCalculator")
class PositionCalculatorTest {

  PositionCalculator positionCalculator;

  @BeforeEach
  void createPositionCalculator() {
    positionCalculator = PositionCalculator.builder()
      .xInitialPosition(25)
      .yInitialPosition(25)
      .xPositionIncrementFactor(75)
      .yPositionIncrementFactor(60)
      .maxX(1280)
      .maxY(720)
      .build();
  }

  @Test
  @DisplayName("When created is not null")
  void isNotNull() {
    assertThat(positionCalculator, notNullValue());
  }

  @Nested
  @DisplayName("When it reaches the max number of positions")
  class when_it_reaches_the_max_number_of_positions {

    @BeforeEach
    void reachTheMaximumNumberOfPositions() {
      while (!positionCalculator.isLimitReached()) {
        positionCalculator.goAhead();
      }
    }

    @Test
    @DisplayName("Then it shouldn't create any more positions")
    void noAnyMorePositions() {
      final Optional<Position> nextPosition = positionCalculator.goAhead();
      assertThat(nextPosition, isEmpty());
    }
  }
}