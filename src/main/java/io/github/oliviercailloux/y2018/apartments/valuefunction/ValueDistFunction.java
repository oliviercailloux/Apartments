package io.github.oliviercailloux.y2018.apartments.valuefunction;

import com.google.maps.model.LatLng;
import io.github.oliviercailloux.y2018.apartments.distance.DistanceMode;
import io.github.oliviercailloux.y2018.apartments.distance.DistanceSubway;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class enables the user to calculate the utility of a location by linear interpolation, to
 * have the maximum duration between the interest places.
 */
public class ValueDistFunction implements PartialValueFunction<LatLng> {

  private Map<LatLng, Double> interestlocation;
  private LatLng appartlocation;
  private double maxDuration;
  private String apiKey;
  private static final Logger LOGGER = LoggerFactory.getLogger(ValueDistFunction.class);

  /**
   * Initializes the different variables of the ValueDistFunction class.
   *
   * @param appartlocation Object LatLng which represents the apartment location.
   * @param apiKey string that represent the API Key used by the Google Maps Api to calculate the
   *     distances
   */
  public ValueDistFunction(LatLng appartlocation, String apiKey) {
    interestlocation = new HashMap<>();
    this.appartlocation = appartlocation;
    this.apiKey = apiKey;
    maxDuration = 0;
  }

  /**
   * Add the apartment location and its utility to the HashMap and update the variable maxDuration.
   *
   * @param interest Object LatLng of an interest place of the user.
   * @throws Exception
   */
  public void addInterestLocation(LatLng interest) throws Exception {
    double currentdistance = calculateDistanceLocation(interest);
    if (currentdistance > maxDuration) maxDuration = currentdistance;
    double utility = 1 - setUtility(currentdistance);
    interestlocation.put(interest, utility);
    LOGGER.info(
        "The interest location ({}) with the utility {} has been added with success in the Map.",
        interest,
        utility);
  }

  /**
   * @return a double which corresponds to the maximum of the duration between an interest place and
   *     the apartment.
   */
  public double getMaxDuration() {
    return maxDuration;
  }

  /**
   * @param interest
   * @return double number which corresponds to the distance (seconds) between the Location
   *     appartocation and the Location interest in parameter.
   * @throws Exception if the latitude and longitude does not have the good format
   *     (com.google.maps.model.LatLng)
   */
  public double calculateDistanceLocation(LatLng interest) throws Exception {
    DistanceSubway dist = new DistanceSubway(interest, appartlocation, apiKey);
    double currentdistance = dist.calculateDistanceAddress(DistanceMode.COORDINATE);
    LOGGER.debug(
        "The distance between {} and {} has been calculated and is equal to {}",
        interest,
        appartlocation,
        currentdistance);
    return currentdistance;
  }

  /**
   * @param currentdistance double distance in seconds.
   * @return a double corresponding to the utility of the distance.
   */
  public double setUtility(double currentdistance) {

    Map<Double, Double> map = new HashMap<>();
    map.put(0d, 0d);
    map.put(3600d, 0.8);
    map.put(36000d, 1d);

    PieceWiseLinearValueFunction f = new PieceWiseLinearValueFunction(map);
    return f.getSubjectiveValue(currentdistance);
  }

  @Override
  public double getSubjectiveValue(LatLng objectiveData) {
    if (!interestlocation.containsKey(objectiveData)) {
      throw new IllegalArgumentException("The map doestn't contain the key " + objectiveData);
    }
    return interestlocation.get(objectiveData);
  }

  @Override
  public Double apply(LatLng objectiveData) {
    return getSubjectiveValue(objectiveData);
  }
}
