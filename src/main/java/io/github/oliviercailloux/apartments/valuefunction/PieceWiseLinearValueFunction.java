package io.github.oliviercailloux.apartments.valuefunction;

import com.google.common.base.Verify;
import com.google.common.collect.ImmutableSortedMap;
import java.util.Map;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class that allows the user to determinate the subjective value of a double given in argument,
 * according to two or more known values.
 */
public class PieceWiseLinearValueFunction implements PartialValueFunction<Double> {

  /**
   * The map is composed of all known utilities. For each entry, the Key represents the value taken
   * by the attribute and the Value is the grade associated.
   */
  private ImmutableSortedMap<Double, Double> utilities;

  private static final Logger LOGGER = LoggerFactory.getLogger(PieceWiseLinearValueFunction.class);

  /**
   * Builder of the PieceWiseLinearValueFunction
   *
   * @param utilParameters is the dictionary of all values along with the grades associated to them.
   *        There has to be a value associated to the grade 0, and another value associated to the
   *        grade 1.
   */
  public PieceWiseLinearValueFunction(Map<Double, Double> utilParameters) {

    if (!utilParameters.containsValue(0d) || !utilParameters.containsValue(1d)) {
      throw new IllegalArgumentException("The value associated to the grade 0 or 1 is missing.");
    }

    Stream<Double> error = utilParameters.values().stream().filter(v -> v > 1d || v < 0d);
    if (error.count() > 0) {
      throw new IllegalArgumentException("The grades have to be between 0 and 1.");
    }

    utilities = ImmutableSortedMap.copyOf(utilParameters);
    LOGGER.info("The map of data has been successfully instantiated.");
  }

  @Override
  public double getSubjectiveValue(Double objectiveData) {

    Verify.verify(utilities.size() >= 2);

    if (objectiveData <= utilities.firstKey()) {
      return 0d;
    }
    if (objectiveData >= utilities.lastKey()) {
      return 1d;
    }

    Map.Entry<Double, Double> minEntry = utilities.floorEntry(objectiveData);
    Map.Entry<Double, Double> maxEntry = utilities.ceilingEntry(objectiveData);
    Double minKey = minEntry.getKey();
    Double maxKey = maxEntry.getKey();
    Double minValue = minEntry.getValue();
    Double maxValue = maxEntry.getValue();
    return ((objectiveData - minKey) * (maxValue - minValue) / (maxKey - minKey)) + minValue;
  }

  @Override
  public Double apply(Double t) {
    return getSubjectiveValue(t);
  }
}
