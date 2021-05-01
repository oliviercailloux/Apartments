package io.github.oliviercailloux.y2018.apartments.valuefunction;

import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import io.github.oliviercailloux.y2018.apartments.distance.DistanceMode;
import io.github.oliviercailloux.y2018.apartments.distance.DistanceSubway;
import io.github.oliviercailloux.y2018.apartments.localize.Localizer;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class allows to calculate the subjective value of the <code>DISTANCE</code>.
 */
public class DistanceValueFunction implements PartialValueFunction<LatLng> {

  private Set<String> interestLocations;
  private String apiKey;
  private PartialValueFunction<Double> durationValueFunction;
  private static final Logger LOGGER = LoggerFactory.getLogger(DistanceValueFunction.class);

  /**
   * Initialize an instance of <code>DistanceValueFunction</code>.
   * 
   * @param apiKey
   * @param interestLocations : a set containing the tenant's interest places.
   * @param durationValueFunction : valueFunction used in the subjective value calculation. By
   *        default, we use a <code>PieceWiseLinearValueFunction</code>.
   */
  private DistanceValueFunction(String apiKey, Set<String> interestLocations,
      PartialValueFunction<Double> durationValueFunction) {
    this.apiKey = apiKey;
    this.interestLocations = interestLocations;
    this.durationValueFunction = durationValueFunction;
    LOGGER.info("The DistanceValueFunction has been successfully instantiated.");
  }

  /**
   * Initialize an instance of <code>DistanceValueFunction</code>.
   * 
   * @param apiKey
   * @param interestLocations : a set containing the tenant's interest places.
   */
  private DistanceValueFunction(String apiKey, Set<String> interestLocations) {
    this.apiKey = apiKey;
    this.interestLocations = interestLocations;
    Map<Double, Double> map = new HashMap<>();
    map.put(0d, 0d);
    map.put(3600d, 0.8);
    map.put(36000d, 1d);
    this.durationValueFunction = new PieceWiseLinearValueFunction(map);
    LOGGER
        .info("The DistanceValueFunction has been successfully instantiated with the default PVF.");
  }

  /**
   * @param apiKey
   * @param interestLocations
   * @param durationValueFunction
   * @return an instance of <code>DistanceValueFunction</code>.
   */
  public static DistanceValueFunction getDistanceCriteria(String apiKey,
      Set<String> interestLocations, PartialValueFunction<Double> durationValueFunction) {
    return new DistanceValueFunction(apiKey, interestLocations, durationValueFunction);
  }

  /**
   * @param apiKey
   * @param interestLocations
   * @return an instance of <code>DistanceValueFunction</code>.
   */
  public static DistanceValueFunction getDistanceCriteria(String apiKey,
      Set<String> interestLocations) {
    return new DistanceValueFunction(apiKey, interestLocations);
  }

  /**
   * Create coordinates from the interest localization address, and calculate the subjective value
   * of each one. It stores both in a <code>Map</code>.
   * 
   * @param apartmentLocalization
   * @return <code>Map</code> containing the subjective value of each interest localization.
   * @throws Exception
   */
  private Map<LatLng, Double> addInterestLocations(LatLng apartmentLocalization) throws Exception {
    Map<LatLng, Double> interestLocationsSubjectiveValue = new HashMap<>();
    for (String localization : interestLocations) {
      LatLng localizationCoordinates = Localizer.getGeometryLocation(localization, apiKey);
      double distanceToApart =
          calculateDistanceLocation(localizationCoordinates, apartmentLocalization);
      double localizationSubjectiveValue = 1 - setUtility(distanceToApart);
      interestLocationsSubjectiveValue.put(localizationCoordinates, localizationSubjectiveValue);
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
   * @throws Exception if the latitude and longitude does not have the good format
   *         (com.google.maps.model.LatLng)
   */
  private double calculateDistanceLocation(LatLng interestLocalization,
      LatLng apartmentLocalization) throws Exception {
    DistanceSubway dist = new DistanceSubway(interestLocalization, apartmentLocalization, apiKey);
    double currentdistance = dist.calculateDistanceAddress(DistanceMode.COORDINATE);
    LOGGER.info("The distance between {} and {} has been calculated and is equal to {}",
        interestLocalization, apartmentLocalization, currentdistance);
    return currentdistance;
  }

  /**
   * Calculate the utility of distance given in parameter.
   * 
   * @param currentdistance double distance in seconds.
   * @return a double corresponding to the utility of the distance.
   */
  private double setUtility(double currentdistance) {
    return durationValueFunction.getSubjectiveValue(currentdistance);
  }

  @Override
  public double getSubjectiveValue(LatLng apartmentLocalization) throws IllegalArgumentException {
    try {
      Map<LatLng, Double> interestLocationsSubjectiveValue =
          addInterestLocations(apartmentLocalization);
      double subjectiveValue = 0;
      Collection<Double> listValues = interestLocationsSubjectiveValue.values();
      Iterator<Double> iterator = listValues.iterator();
      while (iterator.hasNext()) {
        Double value = iterator.next();
        subjectiveValue += value;
      }
      LOGGER.info("The distance's subjective value has been computed with success. Value : {}.",
          (subjectiveValue / interestLocationsSubjectiveValue.size()));
      return (subjectiveValue / interestLocationsSubjectiveValue.size());
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public Double apply(LatLng apartmentLocalization) {
    return this.getSubjectiveValue(apartmentLocalization);
  }
}
