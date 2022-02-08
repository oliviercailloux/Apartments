package io.github.oliviercailloux.apartments.valuefunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.oliviercailloux.apartments.valuefunction.BooleanValueFunction;
import org.junit.jupiter.api.Test;

class BooleanValueFunctionTests {

  @Test
  void testGetSubjectiveValueOfTrue() {
    BooleanValueFunction b = new BooleanValueFunction(true);
    assertEquals(1, b.getSubjectiveValue(true));
    assertEquals(0, b.getSubjectiveValue(false));
  }

  @Test
  void testGetSubjectiveValueOfFalse() {
    BooleanValueFunction bo = new BooleanValueFunction(false);
    assertEquals(0, bo.getSubjectiveValue(true));
    assertEquals(1, bo.getSubjectiveValue(false));
  }
}
