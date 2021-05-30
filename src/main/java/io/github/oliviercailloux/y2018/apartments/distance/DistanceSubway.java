package io.github.oliviercailloux.y2018.apartments.distance;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.LatLng;
import com.google.maps.model.TransitMode;
import com.google.maps.model.TravelMode;
import io.github.oliviercailloux.y2018.apartments.valuefunction.DistanceMode;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class enables the user to calculate the distance in seconds between two points using the
 * subway transport. This class uses Google Maps API. Inspiration from
 * https://www.programcreek.com/java-api-examples/index.php?api=com.google.maps.model.DistanceMatrix
 */
public class DistanceSubway {

  private String startPoint;
  private String endPoint;
  private GeoApiContext dist;
  private LatLng startCoordinate;
  private LatLng endCoordinate;

  private static final Logger LOGGER = LoggerFactory.getLogger(DistanceSubway.class);

  /**
   * Create an Object DistanceSubway in order to calculate a distance between two points using the
   * metro transport.
   *
   * @param startPoint the start point of the path
   * @param endPoint the end point of the path
   * @param apiKey String which corresponds to the API Key
   * @throws IOException
   * @throws InterruptedException
   * @throws ApiException
   */
  public DistanceSubway(String startPoint, String endPoint, String apiKey) {
    if (startPoint == null || endPoint == null)
      throw new IllegalArgumentException("Address is not a valid object");
    if (startPoint.length() == 0 || endPoint.length() == 0)
      throw new IllegalArgumentException("Address is empty");

    this.endPoint = endPoint;
    this.startPoint = startPoint;

    try {
      this.dist = new GeoApiContext.Builder().apiKey(apiKey).build();
    } catch (IllegalStateException e) {

      LOGGER.error("The api key is not valid" + e.getMessage());
      throw new IllegalStateException(
          "ERROR : The api key is not valid, please be sure you have a valid key" + e.getMessage());
    }
  }

  /**
   * Constructor which builds a DistanceSubway object.
   *
   * @param startCoordinate LatLng the start coordinate
   * @param endCoordinate LatLng the end coordinate
   */
  public DistanceSubway(LatLng startCoordinate, LatLng endCoordinate, String apiKey) {
    this.startCoordinate = startCoordinate;
    this.endCoordinate = endCoordinate;
    try {
      this.dist = new GeoApiContext.Builder().apiKey(apiKey).build();
    } catch (IllegalStateException e) {
      LOGGER.error("The api key is not valid" + e.getMessage());
      throw new IllegalStateException(
          "ERROR : The api key is not valid, please be sure you have a valid key" + e.getMessage());
    }
  }

  /**
   * This method enables the user to calculate a distance between two points using Google Maps API.
   * The method uses DistanceMatrix of Google Maps library. The API key can be found here :
   * <a href= "https://developers.google.com/maps/documentation/geocoding/start?hl=fr#get-a-key">Get
   * your api key </a> The request return an Object DistanceMatrix with three properties ;
   * desitnation_addresses, origin_addresses and rows. Rows is an Array of elements. In our case
   * there is only one element. In this element, there is the properties distance and duration, and
   * both have the properties Text (human reading) and value (double).
   *
   * @param distanceMode is a enum type, allow the user to choose between address mode (by the name)
   *        or by coordinate mode.
   * @return distance in hours between the two points given in the constructor.
   * @throws Exception
   */
  public double calculateDistanceAddress(DistanceMode distancemode) throws Exception {

    DistanceMatrixApiRequest request = DistanceMatrixApi.newRequest(dist);

    DistanceMatrix result = null;

    switch (distancemode) {
      case ADDRESS:
        result = request.origins(startPoint).destinations(endPoint).mode(TravelMode.TRANSIT)
            .transitModes(TransitMode.SUBWAY).language("fr-FR").await();
        break;
      case COORDINATE:
        result = request.origins(startCoordinate).destinations(endCoordinate)
            .mode(TravelMode.TRANSIT).transitModes(TransitMode.SUBWAY).language("fr-FR").await();
        break;
      default:
        throw new Exception("The distance mode specified is not correct.");
    }

    return result.rows[0].elements[0].duration.inSeconds;
  }
}
