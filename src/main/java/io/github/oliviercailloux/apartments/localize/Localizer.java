package io.github.oliviercailloux.apartments.localize;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import java.io.IOException;

public class Localizer {

  /**
   * getGeometryLocation return, base on the full address of the location, the geocode of it.
   *
   * @param location is the full address of the location
   * @return a LatLng Object which contains the latitude and longitude of the location
   * @throws ApiException
   * @throws InterruptedException
   * @throws IOException
   */
  public static LatLng getGeometryLocation(String address, String apiKey)
      throws ApiException, InterruptedException, IOException {

    GeoApiContext context = new GeoApiContext.Builder().apiKey(apiKey).build();

    GeocodingResult[] res = GeocodingApi.newRequest(context).address(address).await();

    return res[0].geometry.location;
  }
}
