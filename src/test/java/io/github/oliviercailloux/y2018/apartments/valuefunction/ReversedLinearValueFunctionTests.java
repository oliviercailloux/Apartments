package io.github.oliviercailloux.y2018.apartments.valuefunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ReversedLinearValueFunctionTests {

  @Test
  void testGetSubjectiveValue() {
    ReversedLinearValueFunction f = new ReversedLinearValueFunction(10, 50);
    assertEquals(0.25, f.getSubjectiveValue(40.0));
  }

  @Test
  void testGetSubjectiveValueLower() {
    ReversedLinearValueFunction f = new ReversedLinearValueFunction(10, 50);
    assertEquals(1, f.getSubjectiveValue(5.0));
  }

  @Test
  void testGetSubjectiveValueUpper() {
    ReversedLinearValueFunction f = new ReversedLinearValueFunction(10, 50);
    assertEquals(0, f.getSubjectiveValue(55.0));
  }

  @Test
  void testApply() {
    ReversedLinearValueFunction f = new ReversedLinearValueFunction(10, 50);
    assertEquals(0.25, f.getSubjectiveValue(40.0));
  }

  @Test
  void testExceptionIllegalArgEquals() {
    assertThrows(IllegalArgumentException.class, () -> {
      @SuppressWarnings("unused")
      ReversedLinearValueFunction e = new ReversedLinearValueFunction(10, 10);
    });
  }
}
