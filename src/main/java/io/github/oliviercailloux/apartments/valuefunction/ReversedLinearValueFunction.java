package io.github.oliviercailloux.apartments.valuefunction;

import com.google.common.collect.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class which enables the user to get the subjective value of a double given in argument in the
 * case where the subjective value associated to 1 is the lower bound of the range.
 */
public class ReversedLinearValueFunction implements PartialValueFunction<Double> {

  private Range<Double> interval;
  private static final Logger LOGGER = LoggerFactory.getLogger(ReversedLinearValueFunction.class);

  /**
   * Create a guava interval between min and max.
   *
   * @param min lower bound of the interval associated to the subjective value 1
   * @param max upper bound of the interval associated to the subjective value 0
   */
  public ReversedLinearValueFunction(double min, double max) {
    if (min == max) {
      LOGGER.error("The upper bound is equal to the lower bound in the constructor of the"
          + " LinearValueFunction.");
      throw new IllegalArgumentException("The upper bound can't be equal to the lower bound.");
    }
    interval = Range.closed(min, max);
    LOGGER.info("The interval [{}, {}] has been set with success in the LinearValueFunction class.",
        min, max);
  }

  public Range<Double> getInterval() {
    return this.interval;
  }

  @Override
  public double getSubjectiveValue(Double objectiveData) throws IllegalArgumentException {
    if (interval.lowerEndpoint() > objectiveData) {
      return 1;
    } else if (interval.upperEndpoint() < objectiveData) {
      return 0;
    } else {
      return 1 - (objectiveData - interval.lowerEndpoint())
          / (interval.upperEndpoint() - interval.lowerEndpoint());
    }
  }

  @Override
  public Double apply(Double objectiveData) {
    return getSubjectiveValue(objectiveData);
  }
}
