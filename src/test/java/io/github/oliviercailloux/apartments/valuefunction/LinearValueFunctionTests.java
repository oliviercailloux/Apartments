package io.github.oliviercailloux.apartments.valuefunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class LinearValueFunctionTests {

  @Test
  void testGetSubjectiveValue() {
    LinearValueFunction f = new LinearValueFunction(10, 50);
    assertEquals(0.25, f.getSubjectiveValue(20.0));
  }

  @Test
  void testSubjectiveValueLower() {
    LinearValueFunction f = new LinearValueFunction(10, 50);
    assertEquals(1, f.getSubjectiveValue(100.0));
  }

  @Test
  void testSubjectiveValueUpper() {
    LinearValueFunction f = new LinearValueFunction(10, 50);
    assertEquals(0, f.getSubjectiveValue(9.0));
  }

  @Test
  void testExceptionIllegalArgEquals() {
    assertThrows(IllegalArgumentException.class, () -> {
      @SuppressWarnings("unused")
      LinearValueFunction e = new LinearValueFunction(10, 10);
    });
  }
}
