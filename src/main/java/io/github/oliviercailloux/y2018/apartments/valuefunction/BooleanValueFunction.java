package io.github.oliviercailloux.y2018.apartments.valuefunction;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Class which enables the user to get the subjective value of a Boolean given in argument. */
public class BooleanValueFunction implements PartialValueFunction<Boolean> {
  private Map<Boolean, Double> subjective;
  private static final Logger LOGGER = LoggerFactory.getLogger(BooleanValueFunction.class);

  /**
   * Create a map which associate a subjective value to true and a subjective value to false. This
   * subjective value will take the value of 1 for the boolean that represent the "good value" and 0
   * for the other.
   *
   * @param isPrefered a boolean that indicates whether true or false represent the "good" value for
   *        the corresponding attribute
   */
  public BooleanValueFunction(boolean isPrefered) {
    subjective = new HashMap<>();
    if (isPrefered) {
      subjective.put(true, 1d);
      subjective.put(false, 0d);
    } else {
      subjective.put(false, 1d);
      subjective.put(true, 0d);
    }

    LOGGER.info(
        "The Map with the subjective values for true and false has been set with success in the"
            + " BooleanValueFunction class.");
  }

  @Override
  public Double apply(Boolean objectiveData) {
    return this.getSubjectiveValue(objectiveData);
  }

  @Override
  public double getSubjectiveValue(Boolean objectiveData) throws IllegalArgumentException {
    return subjective.get(objectiveData);
  }
}
