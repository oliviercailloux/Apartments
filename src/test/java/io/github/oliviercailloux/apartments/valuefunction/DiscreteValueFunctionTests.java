package io.github.oliviercailloux.apartments.valuefunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.oliviercailloux.apartments.valuefunction.DiscreteValueFunction;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class DiscreteValueFunctionTests {

  @Test
  void testGetSubjectiveValue() {
    DiscreteValueFunction<String> f = new DiscreteValueFunction<>("Bad", "Medium", "Good");
    assertEquals(0.5, f.getSubjectiveValue("Medium"));
  }

  @Test
  void testConstructorValue() {
    Map<Integer, Double> discreteMapTest = new HashMap<>();
    discreteMapTest.put(5, 0.0);
    discreteMapTest.put(10, 0.25);
    discreteMapTest.put(99, 1.0);
    DiscreteValueFunction<Integer> f = new DiscreteValueFunction<>(discreteMapTest);
    assertEquals(1.0, f.getSubjectiveValue(99));
  }

  @Test
  void testException() {
    assertThrows(IllegalArgumentException.class, () -> {
      Map<Integer, Double> discreteMapTest = new HashMap<>();
      discreteMapTest.put(5, 0.0);
      discreteMapTest.put(10, 0.25);
      discreteMapTest.put(99, 1.2);
      DiscreteValueFunction<Integer> f = new DiscreteValueFunction<>(discreteMapTest);
      assertEquals(1.2, f.getSubjectiveValue(99));
    });
  }

  @Test
  void testExceptionDiffArgsString() {
    assertThrows(IllegalArgumentException.class, () -> {
      @SuppressWarnings("unused")
      DiscreteValueFunction<String> vF = new DiscreteValueFunction<>("Unique", "Unique", "Toto");
    });
  }
}
