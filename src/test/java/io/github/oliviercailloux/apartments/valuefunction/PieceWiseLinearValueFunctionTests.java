package io.github.oliviercailloux.apartments.valuefunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.oliviercailloux.apartments.valuefunction.PieceWiseLinearValueFunction;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;
import org.junit.jupiter.api.Test;

public class PieceWiseLinearValueFunctionTests {

  @Test
  public void testGetSubjectiveValueNormal() throws Exception {

    PieceWiseLinearValueFunction p = initializePieceWise();

    assertEquals(0.75, p.getSubjectiveValue(20d));
    assertEquals(0.25, p.getSubjectiveValue(5d));
  }

  @Test
  public void testGetUtilityWithParamAboveMax() throws Exception {

    PieceWiseLinearValueFunction p = initializePieceWise();

    assertEquals(1d, p.getSubjectiveValue(70d));
  }

  @Test
  public void testGetUtilityWithParamBelowMin() throws Exception {

    PieceWiseLinearValueFunction p = initializePieceWise();

    assertEquals(0, p.getSubjectiveValue(-10d));
  }

  private static PieceWiseLinearValueFunction initializePieceWise() {

    SortedMap<Double, Double> map = new ConcurrentSkipListMap<>();
    map.put(0d, 0d);
    map.put(10d, 0.5);
    map.put(30d, 1d);

    return new PieceWiseLinearValueFunction(map);
  }
}
