package io.github.oliviercailloux.apartments.apartment;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import io.github.oliviercailloux.apartments.apartment.Apartment.Builder;
import io.github.oliviercailloux.apartments.apartment.json.JsonConvert;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** A factory for creating Apartment objects. */
public abstract class ApartmentFactory {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApartmentFactory.class);

  private static Random rand = new Random();

  /** The URL of the API address. */
  private static final String URL_API_ADDRESS = "https://api-adresse.data.gouv.fr/reverse/";

  /**
   * This function aims to generate a new apartment with random characteristics and a real address.
   *
   * @return An Apartment with random characteristics and a real address
   * @throws AddressApiException in case the API does not return a good address format
   * @throws ClientErrorException in case the API is unreachable (HTTP error)
   */
  public static Apartment generateRandomRealApartment()
      throws ClientErrorException, AddressApiException {
    return generateRandomApartment(true);
  }

  /**
   * This function aims to generate a new apartment with random characteristics and a real address
   * if the Address Api answer.
   *
   * @return An Apartment with random characteristics and a real address if the Address Api answer
   */
  public static Apartment generateRandomApartment() {
    try {
      return generateRandomApartment(false);
    } catch (ClientErrorException | AddressApiException e) {
      // We call generateRandomApartment() with the parameter false.
      // So we are not supposed to get Exceptions.
      // In the event that an exception is thrown it is a software error, hence the
      // IllegalStateException()
      throw new IllegalStateException(e);
    }
  }

  /**
   * This function aims to generate a new apartment with random characteristics.
   *
   * @param realAddress - True indicates that the address is necessarily real (may have an
   *        {@link AddressApiException}) <br>
   *        False indicates that if a real address cannot be returned we will have an unreal address
   * @see #getRandomAddress()
   * @return the apartment built
   * @throws AddressApiException in case the API does not return a good address format
   * @throws ClientErrorException in case the API is unreachable (HTTP error)
   */
  private static Apartment generateRandomApartment(boolean realAddress)
      throws ClientErrorException, AddressApiException {
    double floorArea = simulateRandomDraw(65d, 21d);
    String address = realAddress ? getOnlineRandomAddress() : getRandomAddress();
    int averageRoomArea = 10 + rand.nextInt(20);
    int nbBedrooms = Math.max(((int) (floorArea / averageRoomArea)) - 1, 1);
    int nbSleeping = (1 + rand.nextInt(4)) * nbBedrooms;
    int nbBathrooms = 1 + rand.nextInt(nbBedrooms);
    boolean hasTerrace = Math.random() >= 0.5;
    double floorAreaTerrace = (hasTerrace) ? simulateRandomDraw(15d, 2d) : 0;
    String title = "Location Apartement " + rand.nextInt(10000);
    String description = "This apartment has " + nbBedrooms + " bedrooms and a size of " + floorArea
        + "square meters";
    boolean wifi = Math.random() >= 0.5;
    double pricePerNight = floorArea * simulateRandomDraw(11d, 3d);
    boolean tele = Math.random() >= 0.5;
    int nbMinNight = rand.nextInt(700) + 1;
    Builder apartBuilder = new Builder();
    return apartBuilder.setFloorArea(floorArea).setAddress(address).setNbBedrooms(nbBedrooms)
        .setNbSleeping(nbSleeping).setNbBathrooms(nbBathrooms).setTerrace(hasTerrace)
        .setFloorAreaTerrace(floorAreaTerrace).setDescription(description).setTitle(title)
        .setWifi(wifi).setPricePerNight(pricePerNight).setNbMinNight(nbMinNight).setTele(tele)
        .build();
  }

  /**
   * This function aims to generate a list of random apartments with real address.
   *
   * @param nbApartment the number of apartments the list should contains
   * @return a list of random apartments of size nbApartment with real Address
   */
  public static List<Apartment> generateRandomApartments(int nbApartment) {
    try {
      return generateRandomApartmentList(nbApartment, false);
    } catch (ClientErrorException | AddressApiException e) {
      // We call generateRandomApartment() with the parameter false.
      // So we are not supposed to get Exceptions.
      // In the event that an exception is thrown it is a software error, hence the
      // IllegalStateException()
      throw new IllegalStateException(e);
    }
  }

  /**
   * This function aims to generate a list of random apartments with real address.
   *
   * @param nbApartment the number of apartments the list should contains
   * @return a List of random apartments of size nbApartment with real Address
   * @throws AddressApiException in case the API does not return a good address format
   * @throws ClientErrorException in case the API is unreachable (HTTP error)
   */
  public static List<Apartment> generateRandomRealApartments(int nbApartment)
      throws ClientErrorException, AddressApiException {
    return generateRandomApartmentList(nbApartment, true);
  }

  /**
   * This function aims to generate a list of random apartments.
   *
   * @param nbApartment the number of apartments the list should contains
   * @return a List of random apartments of size nbApartment
   * @throws AddressApiException in case the API does not return a good address format
   * @throws ClientErrorException in case the API is unreachable (HTTP error)
   */
  private static List<Apartment> generateRandomApartmentList(int nbApartment, boolean realAddress)
      throws ClientErrorException, AddressApiException {
    if (nbApartment <= 0) {
      throw new IllegalArgumentException("You must indicate a number of apartments > 0");
    }
    List<Apartment> listApartment = new ArrayList<>();
    for (int i = 0; i < nbApartment; i++) {
      listApartment.add(realAddress ? generateRandomRealApartment() : generateRandomApartment());
    }
    return listApartment;
  }

  /**
   * This function simulates a random draw of a variable, being given its expectation and its
   * deviation.
   *
   * @param mean gives the mathematical expectation of the variable
   * @param deviation is the standard deviation of the variable
   * @return an outcome of the randomized experiment
   */
  private static double simulateRandomDraw(double mean, double deviation) {
    double draw = Math.abs(rand.nextGaussian());
    return deviation * draw + mean;
  }

  /**
   * Call an API which generates a random address. This function aims at getting the random address
   * generated.
   *
   * <p>
   * So, we generate a random latitude and a longitude and try to retrive an address
   *
   * <p>
   * <b>Regarding the API call:</b> <br>
   *
   * <p>
   * The probability that this function will return an exception of type AddressApiException is
   * 0.032% <br>
   * Empirically, the probability that we get a possible failure of the application is around
   * 0.0533% The API used:
   *
   * <ul>
   * <li>Is maintained by the interdepartmental digital department
   * <li>Is free
   * <li>Virtually no constraint on the number of calls (so it doesn't matter if a call fails)
   * <li>Does not ask for authentication (API key or others)
   * <li>Does not depend on openstreetmap or GoogleMaps
   * </ul>
   *
   * <br>
   *
   * @see #getRandomAddress() <code>getRandomAddress()</code> calls <code>getOnlineRandomAddress()
   *     </code> but return an apipa address in case an Exception is thrown
   * @return the address generated.
   * @throws AddressApiException in case the API doesn't return a good format after a certain number
   *         of attempts
   * @throws ClientErrorException in case the JAX-RS call fails, for example because of no
   *         connection or an HTTP 500, 404 or others
   */
  private static String getOnlineRandomAddress() throws ClientErrorException, AddressApiException {
    /**
     * An API call has an average 20% chance of failing. Given this highprobability, we iterate
     * until we get a correct result (a good address). RETRY corresponds to the maximum test value
     * in case the API does not return the address field. In the case of an API error (for example
     * 500, or others), jax-rs throws an error of type <code>ClientErrorException</code> In the
     * event that we reach beyond the RETRY tests, and we still do not have a good address, we throw
     * an exception
     */
    final int RETRY = 5;
    Optional<String> address = Optional.empty();
    // Latitude and longitude for Ile de France
    final double LAT_IDF = 48.8_499_198d;
    final double LONG_IDF = 2.6_370_411d;
    // Open a new client JAX-RS
    final Client client = ClientBuilder.newClient();
    try {
      for (int i = 1; i <= RETRY; i++) {
        // Latitude and longitude generation
        double lat = Math.round(LAT_IDF * 10.0d) / 10.0d;
        double lng = Math.round(LONG_IDF * 10.0d) / 10.0d;
        String longitude =
            String.valueOf(lng) + String.valueOf(rand.nextInt((99_999 - 10000) + 1) + 10_000);
        String latitude =
            String.valueOf(lat) + String.valueOf(rand.nextInt((99_999 - 10000) + 1) + 10_000);
        // Call API
        address = tryToGetOnlineRandomAddress(client, longitude, latitude);
        if (address.isPresent()) {
          break;
        }
      }
    } finally {
      // We caught an exception that seems abnormal,
      // we close the client and throw the exception again
      client.close();
    }
    if (address.isPresent()) {
      return address.get();
    }
    // We were unable to retrieve a correct address
    throw new AddressApiException(
        "It appears that the API has failed to return a correct address.");
  }

  /**
   * Call an API which generates an existing random address. This function aims at getting the
   * random address generated. if the Address API cannot answer, it will generate a random fake
   * address.
   *
   * @return the random address
   */
  private static String getRandomAddress() {
    try {
      return ApartmentFactory.getOnlineRandomAddress();
    } catch (ClientErrorException | AddressApiException e) {
      // We return an apipa address
      LOGGER.error("Problem while getting random address {}", e.toString());
      StringBuilder sb = new StringBuilder();
      sb.append(ApartmentFactory.rand.nextInt(3000)).append(" rue de l'appel échoué ")
          .append(ApartmentFactory.rand.nextInt(19) + 75001).append(" Paris ");
      return sb.toString();
    }
  }

  /**
   * Generate a list of apartments from a JSON file.
   *
   * @param jsonFilePath the address where the JSON with all the apartments are
   * @return the list of apartments found in the JSON file.
   * @throws IOException if we cannot have access to the JSON file.
   */
  public static List<Apartment> generateApartmentsFromJsonPath(Path jsonFilePath)
      throws IOException {
    return JsonConvert.jsonToApartments(jsonFilePath);
  }

  /**
   * Generate apartment from a JSON file by default.
   *
   * @return the list of apartments found in the JSON file by default.
   */
  public static List<Apartment> getDefaultApartments() {
    return JsonConvert.getDefaultApartments();
  }

  /**
   * Make an HTTP call and check if the features.properties.label field is present in In case it is
   * not present, we return an empty <code>Optional</code>
   *
   * <p>
   * The case where the optional is empty is as follows: jsonString equals to <code>
   * {"type": "FeatureCollection", "version": "draft", "features": [], "attribution": "BAN", "licence": "ETALAB-2.0", "limit": 1}
   * </code>
   *
   * @see #getOnlineRandomAddress() For more information about the used API
   * @param client jax-rs to make the HTTP call. This function do not close the Client! In case an
   *        Exception is thrown, it will return to the caller
   * @param longitude corresponds to the longitude which will be passed in API parameter. Cannot be
   *        null
   * @param latitude corresponds to the latitude which will be passed in API parameter. Cannot be
   *        null
   * @return an optional which is empty if we have not managed to recover the field. Otherwise, it
   *         returns the address
   */
  static Optional<String> tryToGetOnlineRandomAddress(Client client, final String longitude,
      final String latitude) {
    checkNotNull(client, "client can't be null");
    checkNotNull(longitude, "longitude can't be null");
    checkNotNull(latitude, "latitude can't be null");
    Optional<String> address = Optional.empty();
    WebTarget target =
        client.target(URL_API_ADDRESS).queryParam("lon", longitude).queryParam("lat", latitude);
    LOGGER.info("Address API Call : {}", target.getUri().toString());
    String jsonString = target.request(MediaType.TEXT_PLAIN).get(String.class);
    checkNotNull(jsonString, "jsonString cannot be null");
    checkArgument(!jsonString.isBlank(), "jsonString cannot be blank");
    try (JsonReader jr = Json.createReader(new StringReader(jsonString))) {
      JsonObject json = jr.readObject();
      JsonArray features = json.get("features").asJsonArray();
      if (!features.isEmpty()) {
        JsonObject properties = features.get(0).asJsonObject().get("properties").asJsonObject();
        address = Optional.ofNullable(properties.getString("label"));
      }
    }
    return address;
  }
}
