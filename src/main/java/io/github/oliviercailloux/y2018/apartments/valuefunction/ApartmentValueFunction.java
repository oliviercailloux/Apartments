package io.github.oliviercailloux.y2018.apartments.valuefunction;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.collect.ImmutableMap;
import io.github.oliviercailloux.y2018.apartments.apartment.Apartment;
import io.github.oliviercailloux.y2018.apartments.utils.RandomRange;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The public class ApartmentValueFunction enables to compute the subjective values of apartments.
 * This is provided by the creation of an object ApartmentValueFunction which contains for each
 * valuable attribute of an apartment : An object of and an associated weight.
 */
public class ApartmentValueFunction {

  /**
   * The two nexts arguments are the objects used to compute the value function of the
   * characteristics of an apartment
   */
  private EnumMap<Criterion, PartialValueFunction<Boolean>> booleanValueFunctions;

  private EnumMap<Criterion, PartialValueFunction<Double>> doubleValueFunctions;

  /**
   * The next argument gives the weight of an apartment characteristic subjective value in the
   * calculation of the Apartment total subjective value
   */
  private EnumMap<Criterion, Double> weight;

  private static final Logger LOGGER = LoggerFactory.getLogger(ApartmentValueFunction.class);

  /**
   * Constructor of the object. By default, all the objects are ConstantValueFunction objects. By
   * default, all the weights have the same value and their sum is 1. The setters functions enable
   * to set those two.
   */
  public ApartmentValueFunction() {
    this.booleanValueFunctions = new EnumMap<>(Criterion.class);
    this.doubleValueFunctions = new EnumMap<>(Criterion.class);
    Arrays.stream(Criterion.values()).forEach(criterion -> {
      if (criterion.hasBooleanDomain()) {
        setInternalBooleanValueFunction(criterion, new ConstantValueFunction<>(0.0d));
      } else {
        setInternalDoubleValueFunction(criterion, new ConstantValueFunction<>(0.0d));
      }
    });
    this.weight = new EnumMap<>(Criterion.class);
    Arrays.stream(Criterion.values())
        .forEach(criterion -> setWeightSubjectiveValue(criterion, 0.1d));
  }

  /**
   * Used to retrieve a partial boolean value function according to the <code>criterion</code>
   * passed as a parameter
   *
   * @param criterion the criterion associated with the <code>PartialValueFunction</code> that we
   *        want to obtain
   * @return the partial boolean value function associated with the <code>criterion</code> parameter
   */
  private PartialValueFunction<Boolean> getInternalBooleanValueFunction(Criterion criterion) {
    checkNotNull(criterion);
    checkArgument(criterion.hasBooleanDomain());
    return checkNotNull(this.booleanValueFunctions.get(criterion));
  }

  /**
   * Used to retrieve a partial double value function according to the <code>criterion</code> passed
   * as a parameter
   *
   * @param criterion the criterion associated with the <code>PartialValueFunction</code> that we
   *        want to obtain
   * @return the partial double value function associated with the <code>criterion</code> parameter
   */
  private PartialValueFunction<Double> getInternalDoubleValueFunction(Criterion criterion) {
    checkNotNull(criterion);
    checkArgument(criterion.hasDoubleDomain());
    return checkNotNull(this.doubleValueFunctions.get(criterion));
  }

  /**
   * Allows you to update the Double <code>PartialValueFunction p</code> according to the <code>
   * criterion</code>
   *
   * @param criterion the criterion associated with the <code>PartialValueFunction</code> that we
   *        want to set
   * @param p <code>PartialValueFunction</code> to update
   */
  private void setInternalDoubleValueFunction(Criterion criterion, PartialValueFunction<Double> p) {
    checkNotNull(p);
    checkNotNull(criterion);
    checkArgument(criterion.hasDoubleDomain());
    this.doubleValueFunctions.put(criterion, p);
  }

  /**
   * Allows you to update the Boolean <code>PartialValueFunction p</code> according to the <code>
   * criterion</code>
   *
   * @param criterion the criterion associated with the <code>PartialValueFunction</code> that we
   *        want to obtain
   * @param p <code>PartialValueFunction</code> to update
   */
  private void setInternalBooleanValueFunction(Criterion criterion,
      PartialValueFunction<Boolean> p) {
    checkNotNull(p);
    checkNotNull(criterion);
    checkArgument(criterion.hasBooleanDomain());
    this.booleanValueFunctions.put(criterion, p);
  }

  /**
   * Set the function which will be used to calculate the subjective value of a boolean
   * <code> criterion</code> given.
   * 
   * @param criterion the criterion to set his value function
   * @param criterionValueFunction <code>PartialValueFunction</code> to set
   */
  public void setBooleanValueFunction(Criterion criterion,
      PartialValueFunction<Boolean> criterionValueFunction) {
    this.setInternalBooleanValueFunction(criterion, criterionValueFunction);
    LOGGER.info("The {} preferencies has been set", criterion);
  }

  /**
   * Set the function which will be used to calculate the subjective value of a double
   * <code> criterion</code> given.
   * 
   * @param criterion the criterion to set his value function
   * @param criterionValueFunction <code>PartialValueFunction</code> to set
   */
  public void setDoubleValueFunction(Criterion criterion,
      PartialValueFunction<Double> criterionValueFunction) {
    this.setInternalDoubleValueFunction(criterion, criterionValueFunction);
    LOGGER.info("The {} preferencies has been set", criterion);
  }

  /**
   * Set the weight of the <code>criterion</code> subjective value corresponding to his importance.
   * 
   * @param criterion to set the subjective value weight
   * @param subjectiveValueWeight >= 0
   */
  public void setCriterionSubjectiveValueWeight(Criterion criterion, double subjectiveValueWeight) {
    checkArgument(subjectiveValueWeight >= 0,
        "The weight of the criterion " + criterion + " cannot be negative");
    this.setWeightSubjectiveValue(criterion, subjectiveValueWeight);
    LOGGER.info("The {} weight has been set to {}", criterion, subjectiveValueWeight);
  }

  /**
   * Gives the subjective value weight of a criterion <code>criterion</code>
   *
   * @param criterion the criterion we want to know the value
   * @return the subjective value weight
   */
  double getWeightSubjectiveValue(Criterion criterion) {
    checkArgument(this.weight.containsKey(criterion));
    return this.weight.get(criterion);
  }

  /**
   * Update the subjective value weight of a criterion <code>criterion</code>
   *
   * @param criterion the criterion we want to set the value
   */
  public void setWeightSubjectiveValue(Criterion criterion, double value) {
    checkNotNull(criterion);
    this.weight.put(criterion, value);
  }

  /**
   * Get the PartialValueFunction used to compute the subjective value of the <code>criterion
   * </code> given in parameter. We assume that the criterion has a double domain when using it.
   * 
   * @param criterion
   * @return the criterion's value function
   */
  public PartialValueFunction<Double> getDoubleValueFunction(Criterion criterion) {
    return this.getInternalDoubleValueFunction(criterion);
  }

  /**
   * Get the PartialValueFunction used to compute the subjective value of the <code>criterion
   * </code> given in parameter. We assume that the criterion has a boolean domain when using it.
   * 
   * @param criterion
   * @return the criterion's value function
   */
  public PartialValueFunction<Boolean> getBooleanValueFunction(Criterion criterion) {
    return this.getInternalBooleanValueFunction(criterion);
  }

  /**
   * Sets the subjective value weight of a criterion
   *
   * @param criterion the criterion we want to set
   * @param value the value we want to assign at this criterion
   * @return an object ApartmentValueFunction with the modified criterion
   */
  public ApartmentValueFunction withSubjectiveValueWeight(final Criterion criterion,
      final double value) {
    ApartmentValueFunction avf = cloneAVF();
    avf.getWeightSubjectiveValue(criterion);
    avf.setWeightSubjectiveValue(criterion, value);
    return avf;
  }

  /**
   * We make the assumption (by casting), that the runtime PartialValueFunction associated to
   * criteria is a LinearValueFunction, even if in real life it would be a discrete criteria (e.g.
   * the number of bedrooms)
   *
   * <p>
   * The goal is to replace a LinearValueFunction's bound by a new bound Warning : The values of the
   * object should be instantiate before using this function or an error will appear
   *
   * @param criterion the criterion to adapt. This criterion should not be a boolean as TV for
   *        example.
   * @param newBound the new bound to define
   * @param lower true if we want to adapt the lower bound, false on the other case
   * @return an object ApartmentValueFunction
   */
  public ApartmentValueFunction adaptBounds(Criterion criterion, double newBound, boolean lower) {
    ApartmentValueFunction avf = this.cloneAVF();
    checkArgument(avf.getInternalDoubleValueFunction(criterion) instanceof LinearValueFunction);
    LinearValueFunction lvf = (LinearValueFunction) avf.getInternalDoubleValueFunction(criterion);
    avf.setInternalDoubleValueFunction(criterion, adaptLinearValueFunction(lvf, newBound, lower));
    return avf;
  }

  /**
   * Adapt linear value function by defining a new lower or upper bound
   *
   * @param oldLVF the old linear value function used
   * @param newBound the new lower or upper bound
   * @param lower used to say whether we change the lower or upper bound
   * @return an new object LinearValueFunction set with new bound
   */
  private static LinearValueFunction adaptLinearValueFunction(LinearValueFunction oldLVF,
      double newBound, boolean lower) {
    checkNotNull(oldLVF);
    if (lower) {
      return new LinearValueFunction(newBound, oldLVF.getInterval().upperEndpoint());
    }
    return new LinearValueFunction(oldLVF.getInterval().lowerEndpoint(), newBound);
  }

  /**
   * This method assumes that the preference between true and false is known but doesn't matter.
   *
   * @param moreImportant is the criterion that is to be prioritized in this object of
   *        ApartmentValueFunction
   * @param lessImportant is the criterion that is to be less important in this object of
   *        ApartmentValueFunction
   * @return an object ApartmentValueFunction
   */
  public ApartmentValueFunction adaptWeight(Criterion moreImportant, Criterion lessImportant) {
    checkNotNull(lessImportant, "This criterion cannot be null");
    checkNotNull(moreImportant, "This criterion cannot be null");
    checkArgument(!Objects.equals(moreImportant, lessImportant), "Both fields are the same.");
    ApartmentValueFunction avf = cloneAVF();
    double weightSum =
        avf.getWeightSubjectiveValue(moreImportant) + avf.getWeightSubjectiveValue(lessImportant);

    avf = avf.withSubjectiveValueWeight(moreImportant, 9 * weightSum / 10);
    avf = avf.withSubjectiveValueWeight(lessImportant, weightSum / 10);

    return avf;
  }

  /**
   * This function allows the user to clone an object ApartmentValueFunction
   *
   * @return an object ApartmentValueFunction
   */
  private ApartmentValueFunction cloneAVF() {

    ApartmentValueFunction avf = new ApartmentValueFunction();
    Arrays.stream(Criterion.values()).forEach(criterion -> {
      if (criterion.hasBooleanDomain()) {
        avf.booleanValueFunctions.put(criterion, this.booleanValueFunctions.get(criterion));
      } else {
        avf.doubleValueFunctions.put(criterion, this.doubleValueFunctions.get(criterion));
      }
    });

    Arrays.stream(Criterion.values())
        .forEach(criterion -> avf.weight.put(criterion, this.weight.get(criterion)));
    return avf;
  }

  /**
   * This function return the subjective value of the Apartment in parameter. For each valuable
   * attribute of this apartment, the subjective value is computed by the associated
   * PartialValueFunction object. The weighted sum of these subjective values is returned by the
   * function. When the PartialValueFunction object of an attribute hasn't been set, the subjective
   * value given to the corresponding attribute will be 0.
   *
   * @param apart an object of type Apartment
   * @return a double : the weighted sum of the apartment attributes subjective values
   */
  public double getSubjectiveValue(Apartment apart) {
    checkNotNull(apart);
    ImmutableMap<Criterion, Double> subjectiveValue = new ImmutableMap.Builder<Criterion, Double>()
        .put(Criterion.FLOOR_AREA,
            this.doubleValueFunctions.get(Criterion.FLOOR_AREA)
                .getSubjectiveValue(apart.getFloorArea()))
        .put(Criterion.NB_BEDROOMS,
            this.doubleValueFunctions.get(Criterion.NB_BEDROOMS)
                .getSubjectiveValue((double) apart.getNbBedrooms()))
        .put(Criterion.NB_SLEEPING,
            this.doubleValueFunctions.get(Criterion.NB_SLEEPING)
                .getSubjectiveValue((double) apart.getNbSleeping()))
        .put(Criterion.NB_BATHROOMS,
            this.doubleValueFunctions.get(Criterion.NB_BATHROOMS)
                .getSubjectiveValue((double) apart.getNbBathrooms()))
        .put(Criterion.TERRACE,
            this.booleanValueFunctions.get(Criterion.TERRACE)
                .getSubjectiveValue(apart.getTerrace()))
        .put(Criterion.FLOOR_AREA_TERRACE,
            this.doubleValueFunctions.get(Criterion.FLOOR_AREA_TERRACE)
                .getSubjectiveValue(apart.getFloorAreaTerrace()))
        .put(Criterion.WIFI,
            this.booleanValueFunctions.get(Criterion.WIFI).getSubjectiveValue(apart.getWifi()))
        .put(Criterion.PRICE_PER_NIGHT,
            this.doubleValueFunctions.get(Criterion.PRICE_PER_NIGHT)
                .getSubjectiveValue(apart.getPricePerNight()))
        .put(Criterion.NB_MIN_NIGHT,
            this.doubleValueFunctions.get(Criterion.NB_MIN_NIGHT)
                .getSubjectiveValue((double) apart.getNbMinNight()))
        .put(Criterion.TELE,
            this.booleanValueFunctions.get(Criterion.TELE).getSubjectiveValue(apart.getTele()))
        .build();

    // Check that the subjective values ​​do have a value between 0 and 1
    subjectiveValue.entrySet().stream().filter(c -> c.getValue() < 0 || c.getValue() > 1)
        .forEach(c -> checkState(false,
            "The subjective value of " + c.getKey().name() + "must be between 0 and 1"));

    double sum = Arrays.stream(Criterion.values())
        .map(c -> this.weight.get(c) * subjectiveValue.get(c)).reduce(0.0d, Double::sum);
    double division = this.weight.values().stream().reduce(0.0d, Double::sum);
    return sum / division;
  }

  /**
   * Allows us to create a ApartmentValueFunction object with random values
   *
   * @return a randomized instance of an ApartmentValueFunction
   */
  public static ApartmentValueFunction getRandomApartmentValueFunction() {
    ApartmentValueFunction apartValueFunction = new ApartmentValueFunction();
    Random random = new Random();
    int endBoundTmp;

    for (Criterion c : Criterion.values()) {
      switch (c) {
        case NB_BEDROOMS:
        case NB_SLEEPING:
          apartValueFunction.setInternalDoubleValueFunction(c,
              DiscreteValueFunction.discreteValueFunctionBeetween(4, 6));
          break;
        case NB_BATHROOMS:
          apartValueFunction.setInternalDoubleValueFunction(c,
              DiscreteValueFunction.discreteValueFunctionBeetween(1, 3));
          break;
        case FLOOR_AREA:
        case FLOOR_AREA_TERRACE:
          endBoundTmp = random.nextInt(80) + 21;
          apartValueFunction.setInternalDoubleValueFunction(c,
              new LinearValueFunction(random.nextInt(endBoundTmp), endBoundTmp));
          break;
        case NB_MIN_NIGHT:
          endBoundTmp = random.nextInt(7) + 3;
          apartValueFunction.setInternalDoubleValueFunction(c,
              new ReversedLinearValueFunction(random.nextInt(endBoundTmp), endBoundTmp));
          break;
        case PRICE_PER_NIGHT:
          endBoundTmp = random.nextInt(180) + 21;
          apartValueFunction.setInternalDoubleValueFunction(c,
              new ReversedLinearValueFunction(random.nextInt(endBoundTmp), endBoundTmp));
          break;
        case TELE:
        case TERRACE:
        case WIFI:
          apartValueFunction.setInternalBooleanValueFunction(c,
              new BooleanValueFunction(random.nextBoolean()));
          break;
        default:
          throw new IllegalStateException("A criterion was not treated!");
      }
    }

    final List<Double> weightRange = RandomRange.weightRangeOfSum(1.0d, Criterion.values().length);
    LOGGER.info("Weight has been set to : {}", weightRange);

    // thanks https://stackoverflow.com/a/38515097
    final Iterator<Criterion> keyIter = Arrays.asList(Criterion.values()).iterator();
    final Iterator<Double> valIter = weightRange.iterator();
    IntStream.range(0, Criterion.values().length).boxed()
        .collect(Collectors.toMap(i -> keyIter.next(), i -> valIter.next()))
        .forEach(apartValueFunction::setWeightSubjectiveValue);
    return apartValueFunction;
  }

}
