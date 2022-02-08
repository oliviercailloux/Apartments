package io.github.oliviercailloux.apartments.valuefunction.profile;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.BoundType;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Range;
import io.github.oliviercailloux.apartments.valuefunction.ApartementVF;
import io.github.oliviercailloux.apartments.valuefunction.Criterion;
import java.util.Arrays;
import java.util.EnumMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Profile {

  private static final Logger LOGGER = LoggerFactory.getLogger(Profile.class);
  private ApartementVF linearAvf;
  private QuestionPriceArea questionPriceArea;

  /**
   * rangesMap is a map of criterion containing the ranges of all the different range for these
   * criterion
   */
  private EnumMap<Criterion, Range<Double>> rangesMap;

  private Profile() {
    ApartementVF.Builder blavf =
        new ApartementVF.Builder().setTeleValueFunction(true).setTerraceValueFunction(true)
            .setWifiValueFunction(true).setFloorAreaTerraceValueFunction(1d, 50d)
            .setFloorAreaValueFunction(9d, 200d).setNbBathroomsValueFunction(1, 10)
            .setNbBedroomsValueFunction(0, 30).setNbSleepingValueFunction(1, 30)
            .setNbMinNightValueFunction(1, 31).setPricePerNightValueFunction(30d, 200d);
    for (Criterion c : Criterion.values()) {
      blavf.setWeight(c, 0d);
    }
    this.linearAvf = blavf.build();

    this.rangesMap = new EnumMap<>(Criterion.class);
    for (Criterion c : Criterion.values()) {
      this.rangesMap.put(c, Range.closed(0.0d, 0.0d));
    }
  }

  private Profile(EnumMap<Criterion, Range<Double>> rangesMap, ApartementVF linearAvf) {
    this.linearAvf = checkNotNull(linearAvf);
    checkNotNull(rangesMap);
    checkArgument(rangesMap.keySet().containsAll(Arrays.asList(Criterion.values())),
        "rangesMap must have all Criterion");
    this.rangesMap = rangesMap;
  }

  /**
   * Getter for the QuestionPriceArea
   *
   * @return the Question for the price and the floor area
   */
  public QuestionPriceArea getQuestionPriceArea() {
    return questionPriceArea;
  }

  public static Profile create(EnumMap<Criterion, Range<Double>> rangesMap, ApartementVF linearAvf) {
    return new Profile(rangesMap, linearAvf);
  }

  /**
   * Gives the linearAVF of a Profile
   *
   * @return the LinearAVF of the Profile
   */
  public ApartementVF getLinearAVF() {
    return this.linearAvf;
  }

  /**
   * Gives the subjective value weight of a criterion
   *
   * @param crit the criterion we want to know the value
   * @return the range of the subjective value weight
   */
  public Range<Double> getWeightRange(Criterion crit) {
    checkArgument(this.rangesMap.containsKey(crit));
    return this.rangesMap.get(crit);
  }

  /**
   * Allows you to retrieve an <code>ImmutableMap</code> containing all the <code>Range</code>
   * (subjective value) associated with each <code>Criterion</code>
   *
   * @return An <code>ImmutableMap</code>, which for each <code>Criterion</code> associates its
   *         subjective value
   */
  public ImmutableMap<Criterion, Range<Double>> getWeightsRange() {
    return Maps.immutableEnumMap(this.rangesMap);
  }

  /**
   * Get the middle of the range of the given criterion
   *
   * @param crit the Criterion
   * @return the middle of the Range of the given Criterion
   */
  double getMiddleOfRange(Criterion crit) {
    return (this.getWeightRange(crit).upperEndpoint() + this.getWeightRange(crit).lowerEndpoint())
        / 2;
  }

  /**
   * Sets the linearAVF of a <code>Profile</code> It's a clone of the current <code>Profile</code>,
   * changing the <code>Profile</code> to <code> newLinearAvf </code>.
   *
   * @param newLinearAvf recreate a Profile based on <code>newLinearAvf</code>.
   * @return Profile with its LinearAVF set
   */
  public Profile withLinearAVF(ApartementVF newLinearAvf) {
    Arrays.stream(Criterion.values()).forEach(c -> this.checkWeightInRange(c, newLinearAvf));
    return Profile.create(this.rangesMap, newLinearAvf);
  }

  /**
   * Check if the weight of the LinearAVF of the given criterion is in the profile range for this
   * criterion.
   *
   * @param crit the criterion to check the weight with the range
   * @param linearAVF the LinearAVF of check if its weight is in the range
   * @throws IllegalArgumentException when the weight of the LinearAVF is not in the range
   */
  public void checkWeightInRange(Criterion crit, ApartementVF linearAVF) {
    Range<Double> range = this.getWeightRange(crit);
    Double weight = linearAVF.getWeight(crit);
    checkNotNull(range);
    checkNotNull(weight);
    checkArgument(range.contains(weight),
        "A weight in the linear AVF is not in the targeted range");
  }

  /**
   * Sets the subjective value weight of a criterion
   *
   * @param crit the criterion we want to know the value
   * @param value range for the weight
   */
  private void setWeightRange(Criterion crit, Range<Double> value) {
    checkArgument(this.rangesMap.containsKey(crit));
    checkRangeValidity(value);
    this.rangesMap.put(crit, value);
    this.linearAvf = this.linearAvf.withWeight(crit, getMiddleOfRange(crit));
    LOGGER.debug("The {} weight has been set to {}", crit, value);
  }

  /**
   * check if the given range is valid for this context
   *
   * @param value the Range to check
   * @throws IllegalArgumentException when doubles contained are < 0 or if there are only 1 value in
   *         the range
   */
  private void checkRangeValidity(Range<Double> value) {
    checkNotNull(value, "The rage cannot be null");
    checkArgument(value.hasLowerBound() && value.hasUpperBound(), "The given range is not valid");
    checkArgument(
        value.lowerBoundType().equals(BoundType.CLOSED)
            && value.upperBoundType().equals(BoundType.CLOSED),
        "The given range must have closed bounds");
    checkArgument(value.lowerEndpoint() >= 0 && value.upperEndpoint() > 0,
        "The weight of the range cannot be negative");
  }

  public static class Builder {
    private Profile toBuild;

    public Builder() {
      toBuild = new Profile();
    }

    public Profile build() {
      checkNotNull(toBuild.linearAvf);
      for (Criterion c : Criterion.values()) {
        checkNotNull(toBuild.getWeightRange(c), c.name() + " is null");
        this.toBuild.checkWeightInRange(c, toBuild.linearAvf);
      }

      checkNotNull(toBuild.getQuestionPriceArea());
      return toBuild;
    }

    public Builder setLinearAVF(ApartementVF newLinearAvf) {
      this.toBuild.linearAvf = newLinearAvf;
      return this;
    }

    /**
     * Set the range of weight for the criterion given in parameters.
     *
     * @param crit the criterion concerned
     * @param lowerValue the minimal value possible for this weight
     * @param upperValue the maximal value possible for this weight
     * @return the current instance of Builder
     */
    public Builder setWeightRange(Criterion crit, double lowerValue, double upperValue) {
      this.toBuild.setWeightRange(crit, Range.closed(lowerValue, upperValue));
      return this;
    }

    public Builder setWeightRange(Criterion crit, Range<Double> value) {
      this.toBuild.setWeightRange(crit, value);
      return this;
    }

    public Builder setQuestionPriceArea(QuestionPriceArea q) {
      this.toBuild.questionPriceArea = q;
      return this;
    }
  }
}
