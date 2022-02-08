package io.github.oliviercailloux.apartments.valuefunction;

/**
 * Class that associates the subjective value of a given parameter to 0.
 *
 * @param <T> the type of the parameter
 */
public class ConstantValueFunction<T> implements PartialValueFunction<T> {

  double subjectiveValue;

  public ConstantValueFunction(double d) {
    this.subjectiveValue = d;
  }

  @Override
  public Double apply(T objectiveData) {
    return getSubjectiveValue(objectiveData);
  }

  @Override
  public double getSubjectiveValue(T objectiveData) throws IllegalArgumentException {
    return this.subjectiveValue;
  }
}
