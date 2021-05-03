package io.github.oliviercailloux.y2018.apartments.valuefunction;

import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import io.github.oliviercailloux.y2018.apartments.distance.DistanceMode;
import io.github.oliviercailloux.y2018.apartments.distance.DistanceSubway;
import io.github.oliviercailloux.y2018.apartments.localize.Localizer;
import io.github.oliviercailloux.y2018.apartments.utils.KeyManager;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
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
   * Initialize an instance of <code>DistanceValueFunction</code>. If
   * <code> durationValuefunction</code> null, we use a <code>PieceWiseLinearValueFunction</code>.
   * If there isn't any interest places, it adds by default the center of Paris.
   * 
   * @param apiKey
   * @param interestLocations : a set containing the tenant's interest places.
   * @param durationValueFunction : valueFunction used in the subjective value calculation.
   * @throws IllegalArgumentException if the <code>apiKey</code> if <code> null </code>.
   */
  private DistanceValueFunction(String apiKey, Set<String> interestLocations,
      PartialValueFunction<Double> durationValueFunction) {
    if (apiKey.equals("")) {
      throw new IllegalArgumentException("The apikey is empty");
    }

    if (interestLocations.isEmpty()) {
      this.interestLocations = new HashSet<>();
      this.interestLocations.add("9 rue Lacuee, 75012 Paris, France");
      LOGGER.info(
          "The interest location (9 rue Lacuee, 75012 Paris, France) has been added to the set .");
    }
    this.apiKey = apiKey;
    this.interestLocations = interestLocations.get();

    for (String interest : this.interestLocations) {
      LOGGER.info("The interest location ({}) has been added to the set.", interest);
    }

    if (durationValueFunction.isEmpty()) {
      Map<Double, Double> map = new HashMap<>();
      map.put(0d, 1d);
      map.put(3600d, 0.8);
      map.put(36000d, 0d);
      this.durationValueFunction = new PieceWiseLinearValueFunction(map);
    } else {
      this.durationValueFunction = durationValueFunction.get();
    }
  }

  /**
   * @param apiKey
   * @param interestLocations
   * @param durationValueFunction
   * @return an instance of <code>DistanceValueFunction</code>.
   */
  public static DistanceValueFunction given(String apiKey, Set<String> interestLocations,
      PartialValueFunction<Double> durationValueFunction) {
    //initialiser tout ici
    return new DistanceValueFunction(Optional.ofNullable(apiKey),
        Optional.ofNullable(interestLocations), Optional.ofNullable(durationValueFunction));
  }

  /**
   * @param apiKey
   * @param interestLocations
   * @return an instance of <code>DistanceValueFunction</code>.
   */
  public static DistanceValueFunction withDefaultDurationValueFunction(String apiKey,
      Set<String> interestLocations) {
    return new DistanceValueFunction(Optional.ofNullable(apiKey),
        Optional.ofNullable(interestLocations), Optional.empty());
  }

  /**
   * Create coordinates from the interest localization address, and calculate the subjective value
   * of each one. It stores both in a <code>Map</code>.
   * 
   * @param apartmentLocalization
   * @return <code>Map</code> containing the subjective value of each interest localization.
   * @throws IOException
   * @throws InterruptedException
   * @throws ApiException
   * @throws Exception
   */
  private Map<LatLng, Double> addInterestLocations(LatLng apartmentLocalization)
      throws ApiException, InterruptedException, IOException {
    Map<LatLng, Double> interestLocationsSubjectiveValue = new HashMap<>();
    for (String localization : interestLocations) {
      LatLng localizationCoordinates = Localizer.getGeometryLocation(localization, apiKey);
      double distanceToApart =
          calculateDistanceLocation(localizationCoordinates, apartmentLocalization);
      // double localizationSubjectiveValue = 1 - setUtility(distanceToApart);
      double localizationSubjectiveValue = setUtility(distanceToApart);
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
   * @throws IOException
   * @throws InterruptedException
   * @throws ApiException
   * @throws Exception if the latitude and longitude does not have the good format
   *         (com.google.maps.model.LatLng)
   */
  private double calculateDistanceLocation(LatLng interestLocalization,
      LatLng apartmentLocalization) throws ApiException, InterruptedException, IOException {
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

  public static void main(String[] args)
      throws FileNotFoundException, IOException, ApiException, InterruptedException {
    String apiKey = KeyManager.getApiKey();
    String apart = "Place du Maréchal de Lattre de Tassigny, 75016 Paris";
    HashSet<String> interestLocations = new HashSet<>();
    interestLocations.add("Place Charles de Gaulle, 75116 Paris, France");
    interestLocations.add("1 Rue Benouville, 75116 Paris 16e Arrondissement, France");
    interestLocations.add("19 Rue Surcouf, 75007 Paris, France");
    interestLocations.add("20 Boulevard Jules Guesde, 94500 Champigny-sur-Marne");
    DistanceValueFunction distanceVF =
        DistanceValueFunction.withDefaultDurationValueFunction(apiKey, interestLocations);
    LatLng apartCoordinates = Localizer.getGeometryLocation(apart, apiKey);
    double subjectiveValue = distanceVF.getSubjectiveValue(apartCoordinates);
    System.out.println(subjectiveValue);
  }
}
