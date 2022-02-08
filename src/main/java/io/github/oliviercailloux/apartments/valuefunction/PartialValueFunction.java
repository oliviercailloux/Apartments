package io.github.oliviercailloux.apartments.valuefunction;

import java.util.function.Function;

/**
 * <code>PartialValueFunction</code> is an object that enables to compute the subjective value of a
 * parameter
 *
 * @param <T> the type of the parameter
 */
public interface PartialValueFunction<T> extends Function<T, Double> {

  /**
   * This function enables to get the subjective value of an objective data
   *
   * @param objectiveData is the parameter from which we want to get the subjective value
   * @return A double (between 0 and 1) that represent the subjective value of the parameter
   * @throws IllegalArgumentException
   */
  public double getSubjectiveValue(T objectiveData) throws IllegalArgumentException;
}
