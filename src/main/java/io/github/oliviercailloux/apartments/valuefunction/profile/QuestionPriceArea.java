package io.github.oliviercailloux.apartments.valuefunction.profile;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Range;
import io.github.oliviercailloux.apartments.valuefunction.Criterion;
import java.util.EnumMap;

public class QuestionPriceArea {
  private int price;
  private int surface;
  private final String question = "Would you pay %d€ more for %d m2 more?";

  /**
   * Instantiates a new question price/area.
   *
   * @param price the price shown to the user according to his/her profile
   * @param surface the surface shown to the user according to his/her profile
   */
  private QuestionPriceArea(int price, int surface) {
    this.price = price;
    this.surface = surface;
  }

  /**
   * Creates the object question.
   *
   * @param price the price
   * @param surface the surface
   * @return the question price area
   */
  public static QuestionPriceArea create(int price, int surface) {
    return new QuestionPriceArea(price, surface);
  }

  /**
   * Getter for attribute price Price unit: euro (€)
   *
   * @return the price: question parameter
   */
  public int getPrice() {
    return this.price;
  }

  /**
   * Getter for attribute surface Surface unit: square meter
   *
   * @return the surface : question parameter
   */
  public int getSurface() {
    return this.surface;
  }

  /**
   * Build the question according to parameters emitted during class creation
   *
   * @return the question constructed
   */
  public String getQuestion() {
    return String.format(this.question, this.getPrice(), this.getSurface());
  }

  /**
   * Adapt the interval of weight contained in a profile depending on his response
   *
   * @param p the profile we need to modify
   * @param response the response of the user to the question
   * @return Profile containing the old profile with the Floor Area modified weight
   */
  public Profile resolve(Profile p, boolean response) {
    checkNotNull(p);
    double rate;
    Range<Double> closedRange;
    EnumMap<Criterion, Range<Double>> profileWeights = new EnumMap<>(p.getWeightsRange());

    /** In this part we compute the different rates of increasment the question is proposing. */
    Range<Double> surfaceRange =
        p.getLinearAVF().getInternalLinearValueFunction(Criterion.FLOOR_AREA).getInterval();
    double surfaceR = this.surface
        / (surfaceRange.upperEndpoint().doubleValue() - surfaceRange.lowerEndpoint().doubleValue());

    Range<Double> priceRange = p.getLinearAVF()
        .getInternalReversedLinearValueFunction(Criterion.PRICE_PER_NIGHT).getInterval();
    double priceR = this.price
        / (priceRange.upperEndpoint().doubleValue() - priceRange.lowerEndpoint().doubleValue());

    if (response) {
      rate = priceR / surfaceR;
      closedRange =
          Range.closed(Math.min(rate, p.getWeightRange(Criterion.FLOOR_AREA).upperEndpoint()),
              p.getWeightRange(Criterion.FLOOR_AREA).upperEndpoint());
    } else {
      rate = surfaceR / priceR;
      closedRange = Range.closed(p.getWeightRange(Criterion.FLOOR_AREA).lowerEndpoint(),
          Math.max(rate, p.getWeightRange(Criterion.FLOOR_AREA).lowerEndpoint()));
    }
    profileWeights.put(Criterion.FLOOR_AREA, closedRange);
    return Profile.create(profileWeights, p.getLinearAVF());
  }
}
