package io.github.oliviercailloux.apartments.valuefunction;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import io.github.oliviercailloux.apartments.distance.DistanceMode;
import io.github.oliviercailloux.apartments.distance.DistanceSubway;
import io.github.oliviercailloux.apartments.localize.Localizer;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class allows to calculate the subjective value of the <code>DISTANCE</code>.
 */
public class DistanceValueFunction implements PartialValueFunction<LatLng> {

  private Set<LatLng> interestLocations;
  private String apiKey;
  private PartialValueFunction<Double> durationValueFunction;
  private static final Logger LOGGER = LoggerFactory.getLogger(DistanceValueFunction.class);

  /**
   * Initialize an instance of <code>DistanceValueFunction</code>.
   * 
   * @param apiKey
   * @param interestLocations : a set containing the tenant's interest places. Cannot be empty.
   * @param durationValueFunction : valueFunction used in the subjective value calculation.
   */
  private DistanceValueFunction(String apiKey, Set<LatLng> interestLocations,
      PartialValueFunction<Double> durationValueFunction) {
    checkNotNull(apiKey);
    checkNotNull(interestLocations);
    checkNotNull(durationValueFunction);
    checkArgument(!interestLocations.isEmpty());
    this.apiKey = apiKey;
    this.interestLocations = interestLocations;
    this.durationValueFunction = durationValueFunction;
    for (LatLng interest : this.interestLocations) {
      LOGGER.info("The interest location ({}) has been added to the set.", interest);
    }
  }

  /**
   * Initialize an instance of <code>DistanceValueFunction</code>. By default, the
   * <code>PieceWiseLinearValueFunction</code> is used to calculate the subjective value. If there
   * isn't any interest places, it adds by default the center of Paris.
   * 
   * @param apiKey
   * @param interestLocations
   * @param durationValueFunction a decreasing value function.
   * @return an instance of <code>DistanceValueFunction</code>.
   * @throws IOException
   * @throws InterruptedException
   * @throws ApiException
   * @throws Exception
   */
  public static DistanceValueFunction given(String apiKey, Set<LatLng> interestLocations,
      PartialValueFunction<Double> durationValueFunction)
      throws ApiException, InterruptedException, IOException {
    if (apiKey.equals("")) {
      throw new IllegalArgumentException("The apikey is empty");
    }
    if (interestLocations.isEmpty()) {
      LatLng address = Localizer.getGeometryLocation("Place Dauphine, 75001 Paris, France", apiKey);
      interestLocations.add(address);
    }
    return new DistanceValueFunction(apiKey, interestLocations, durationValueFunction);
  }

  /**
   * Initialize an instance of <code>DistanceValueFunction</code>. By default, the
   * <code>PieceWiseLinearValueFunction</code> is used to calculate the subjective value. If there
   * isn't any interest places, it adds by default the center of Paris.
   * 
   * @param apiKey
   * @param interestLocations
   * @return an instance of <code>DistanceValueFunction</code>.
   * @throws IOException
   * @throws InterruptedException
   * @throws ApiException
   * @throws Exception
   */
  public static DistanceValueFunction withDefaultDurationValueFunction(String apiKey,
      Set<LatLng> interestLocations) throws ApiException, InterruptedException, IOException {
    Map<Double, Double> map = new HashMap<>();
    map.put(0d, 1d);
    map.put(3600d, 0.8);
    map.put(36000d, 0d);
    PartialValueFunction<Double> pvf = new PieceWiseLinearValueFunction(map);
    return DistanceValueFunction.given(apiKey, interestLocations, pvf);
  }

  /**
   * Calculate the subjective value of each interest location.
   * 
   * @param apartmentLocalization
   * @return <code>Map</code> containing the subjective value of each interest localization.
   * @throws IOException
   * @throws InterruptedException
   * @throws ApiException
   */
  private Map<LatLng, Double> calculateSubjectiveValueInterestLocations(
      LatLng apartmentLocalization) throws ApiException, InterruptedException, IOException {
    Map<LatLng, Double> interestLocationsSubjectiveValue = new HashMap<>();
    for (LatLng localization : interestLocations) {
      double distanceToApart = getDistance(localization, apartmentLocalization);
      double localizationSubjectiveValue =
          durationValueFunction.getSubjectiveValue(distanceToApart);
      interestLocationsSubjectiveValue.put(localization, localizationSubjectiveValue);
      LOGGER.info(
          "The interest location ({}) with the utility {} has been added with success in the Map.",
          localization, localizationSubjectiveValue);
    }
    return interestLocationsSubjectiveValue;
  }

  /**
   * Calculate the distance (seconds) between the apartment localization and the interest place.
   * 
   * @param interestLocalization the interest localization coordinates
   * @param apartmentLocalization the apartment coordinates
   * @return double representing the distance
   * @throws IOException
   * @throws InterruptedException
   * @throws ApiException
   */
  private double getDistance(LatLng interestLocalization, LatLng apartmentLocalization)
      throws ApiException, InterruptedException, IOException {
    DistanceSubway dist = new DistanceSubway(interestLocalization, apartmentLocalization, apiKey);
    double currentdistance = dist.calculateDistanceAddress(DistanceMode.COORDINATE);
    LOGGER.info("The distance between {} and {} has been calculated and is equal to {}",
        interestLocalization, apartmentLocalization, currentdistance);
    return currentdistance;
  }

  @Override
  public double getSubjectiveValue(LatLng apartmentLocalization) {
    Map<LatLng, Double> interestLocationsSubjectiveValue;
    try {
      interestLocationsSubjectiveValue =
          calculateSubjectiveValueInterestLocations(apartmentLocalization);
    } catch (ApiException | InterruptedException | IOException | NullPointerException e) {
      throw new IllegalArgumentException(
          "Error during the interest locations subjective value computation");
    }
    double subjectiveValue = 0;
    Collection<Double> listValues = interestLocationsSubjectiveValue.values();
    Iterator<Double> iterator = listValues.iterator();
    while (iterator.hasNext()) {
      Double value = iterator.next();
      subjectiveValue += value;
    }
    double distanceSubjectiveValue = subjectiveValue / interestLocationsSubjectiveValue.size();
    LOGGER.info("The distance's subjective value has been computed with success. Value : {}.",
        distanceSubjectiveValue);
    return distanceSubjectiveValue;

  }

  @Override
  public Double apply(LatLng apartmentLocalization) {
    return this.getSubjectiveValue(apartmentLocalization);
  }
}
