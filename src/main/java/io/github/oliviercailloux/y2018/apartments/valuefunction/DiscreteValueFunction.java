package io.github.oliviercailloux.y2018.apartments.valuefunction;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.Verify;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Class which enables the user to get the subjective value of an element given in argument. */
public class DiscreteValueFunction<T> implements PartialValueFunction<T> {

  private ImmutableMap<T, Double> subjective;
  private static final Logger LOGGER = LoggerFactory.getLogger(DiscreteValueFunction.class);

  /**
   * Create a map with 3 discrete values of the same type which are associated to their respective
   * subjective values.
   *
   * @param s1 first key not null which corresponds to the subjective value 0
   * @param s2 second key not null which corresponds to the subjective value 0.5
   * @param s3 third key not null which corresponds to the subjective value 1
   */
  public DiscreteValueFunction(T s1, T s2, T s3) {
    checkArgument(!Objects.equals(s1, s2) && !Objects.equals(s1, s3) && !Objects.equals(s2, s3),
        "The elements have to be different");
    subjective = ImmutableMap.of(s1, 0.0, s2, 0.5, s3, 1.0);
    LOGGER.info("The Map with the three elements have been set with success");
  }

  /**
   * Create a map with 2 discrete values of the same type which are associated to their respective
   * subjective values.
   *
   * @param s1 first key not null which corresponds to the subjective value 0
   * @param s2 second key not null of type which corresponds to the subjective value 1
   */
  public DiscreteValueFunction(T s1, T s2) {
    checkArgument(!(Objects.equals(s1, s2)), "The elements have to be different");
    subjective = ImmutableMap.of(s1, 0.0, s2, 1.0);
    LOGGER.info("The Map with the two elements have been set with success");
  }

  /**
   * Create a map that match the not null keys and the subjective values associated
   *
   * @param subjective a Map<T, Double> where the key is of type <b>T</b> and the associated value,
   *        a double between 0 and 1, defines its subjective value.
   */
  public DiscreteValueFunction(Map<T, Double> subjective) {

    Stream<Entry<T, Double>> erreur = subjective.entrySet().stream()
        .filter((entry) -> entry.getValue() < 0 || entry.getValue() > 1);
    Map<T, Double> mapError =
        erreur.collect(Collectors.toMap((entry) -> entry.getKey(), (entry) -> entry.getValue()));
    if (!(mapError.isEmpty())) {
      throw new IllegalArgumentException(
          "The subjective values must be between 0 and 1. The wrong values are : " + mapError);
    }

    this.subjective = ImmutableMap.copyOf(subjective);
  }

  @Override
  public Double apply(T objectiveData) {
    return getSubjectiveValue(objectiveData);
  }

  @Override
  public double getSubjectiveValue(T objectiveData) {
    return subjective.get(objectiveData);
  }

  /**
   * Factory method
   *
   * @param min the minimum required for the random number
   * @param max the maximum required for the random number
   * @return DiscreteValueFunction with a map of utilities from min to max (it is not set by default
   *         to uniform, that way it is not equivalent to a LinearValue Function)
   */
  public static DiscreteValueFunction<Double> discreteValueFunctionBeetween(int min, int max) {

    Verify.verify(min < max);

    Random random = new Random();
    int var = random.nextInt(max - min + 1) + min;

    double newSubjectiveValue = 0;
    double oldSubjectiveValue = 0;
    HashMap<Double, Double> varMap = new HashMap<>();
    for (double i = 0; i < var; ++i) {
      oldSubjectiveValue = newSubjectiveValue;
      newSubjectiveValue = i / var - oldSubjectiveValue;
      newSubjectiveValue = random.nextDouble() * newSubjectiveValue + oldSubjectiveValue;
      varMap.put(i, newSubjectiveValue);
    }

    return new DiscreteValueFunction<>(varMap);
  }
}
