/*
package org.openjfx;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openjfx.model.Position;

import static org.hamcrest.CoreMatchers.*;
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
  @DisplayName("When created then goBack should return the initial position")
  void emptyPosition() {
    final var initialPosition = positionCalculator.getActualPosition();

    final var position = positionCalculator.goBack();

    assertThat(position, is(equalTo(initialPosition)));
  }

  @Test
  @DisplayName("When created then is not null")
  void isNotNull() {
    assertThat(positionCalculator, notNullValue());
  }

  @Nested
  @DisplayName("When it reaches the max number of positions")
  class when_it_reaches_the_max_number_of_positions {

    Position lastPosition;

    @BeforeEach
    void reachTheMaximumNumberOfPositions() {
      while (!positionCalculator.isLimitReached()) {
        lastPosition = positionCalculator.goAhead();
      }
    }

    @Test
    @DisplayName("Then it shouldn't create any more positions")
    void noAnyMorePositions() {
      final Position nextPosition = positionCalculator.goAhead();

      assertThat(nextPosition, is(equalTo(lastPosition)));
    }

    @Test
    @DisplayName("Then the next position should overstep at least one of the boundaries")
    void nextPositionOverstepAtLeastOneOfTheBoundaries() {
      final Position actualPosition = positionCalculator.getActualPosition();

      final Position nextPosition = calculateNextPosition(actualPosition);
      final var isLimitXExceeded = nextPosition.x() > positionCalculator.getMaxX();
      final var isLimitYExceeded = nextPosition.y() > positionCalculator.getMaxY();

      assertThat(isLimitXExceeded || isLimitYExceeded, is(true));
    }

    @Nested
    @DisplayName("When moving back one position")
    class when_moving_back_one_position {

      @BeforeEach
      void goBack() {
        positionCalculator.goBack();
      }

      @Test
      @DisplayName("Then exactly one position becomes free")
      void onePositionBecomesFree() {
        final var nextPosition = positionCalculator.goAhead();
        final var nextNextPosition = positionCalculator.goAhead();

        assertThat(nextPosition, is(notNullValue()));
        assertThat(positionCalculator.isLimitReached(), is(true));
        assertThat(nextNextPosition, is(equalTo(nextPosition)));
      }

      @Test
      @DisplayName("Then going forward should return to the position after which one of the boundaries is crossed")
      void returnToThePositionAfterWhichOneOfTheBoundariesIsCrossed() {
        final Position expectedNextPosition = lastPosition;

        final Position nextPosition = positionCalculator.goAhead();

        assertThat(nextPosition, is(equalTo(expectedNextPosition)));
      }
    }

  }

  Position calculateNextPosition(Position actualPosition) {
    final var xPositionIncrementFactor = positionCalculator.getXPositionIncrementFactor();
    final var yPositionIncrementFactor = positionCalculator.getYPositionIncrementFactor();

    final var nextX = actualPosition.x() + xPositionIncrementFactor;
    final var nextY = actualPosition.y() + yPositionIncrementFactor;

    return new Position(nextX, nextY);
  }
}*/
