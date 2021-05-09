package io.github.oliviercailloux.y2018.apartments.valuefunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.common.base.VerifyException;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import io.github.oliviercailloux.y2018.apartments.localize.Localizer;
import io.github.oliviercailloux.y2018.apartments.utils.KeyManager;
import java.io.IOException;
import java.util.HashSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class DistanceValueFunctionTest {

  private String apiKey;
  private HashSet<LatLng> interestLocations;
  private String apart;
  private PartialValueFunction<Double> durationValueFunction;

  @BeforeEach
  @Disabled("Do not run because there isn't the API KEY in the depository")
  void initEach() throws Exception {
    this.apiKey = KeyManager.getApiKey();
    apart = "Place du Maréchal de Lattre de Tassigny, 75016 Paris";
    interestLocations = new HashSet<>();
    interestLocations
        .add(Localizer.getGeometryLocation("Place Charles de Gaulle, 75116 Paris, France", apiKey));
    interestLocations.add(Localizer
        .getGeometryLocation("1 Rue Benouville, 75116 Paris 16e Arrondissement, France", apiKey));
    interestLocations
        .add(Localizer.getGeometryLocation("19 Rue Surcouf, 75007 Paris, France", apiKey));
    interestLocations.add(Localizer
        .getGeometryLocation("20 Boulevard Jules Guesde, 94500 Champigny-sur-Marne", apiKey));

  }

  @Test
  @Disabled("Do not run because there isn't the API KEY in the depository")
  void testSubjectiveValue() throws Exception {
    DistanceValueFunction distanceVF =
        DistanceValueFunction.withDefaultDurationValueFunction(apiKey, interestLocations);
    LatLng apartCoordinates = Localizer.getGeometryLocation(apart, apiKey);
    double subjectiveValue = distanceVF.getSubjectiveValue(apartCoordinates);
    assertEquals(0.9, subjectiveValue, 0.01);
  }

  @Test
  @Disabled("Do not run because there isn't the API KEY in the depository")
  void testSubjectiveValueLinear() throws Exception {
    durationValueFunction = new ReversedLinearValueFunction(0d, 36000d);
    DistanceValueFunction distanceVF =
        DistanceValueFunction.given(apiKey, interestLocations, durationValueFunction);
    LatLng apartCoordinates = Localizer.getGeometryLocation(apart, apiKey);
    double subjectiveValue = distanceVF.getSubjectiveValue(apartCoordinates);
    assertEquals(0.94, subjectiveValue, 0.01);
  }

  /**
   * This test is supposed to fail because it can only do subway distance calculations.
   * 
   * @throws ApiException
   * @throws InterruptedException
   * @throws IOException
   */
  @Test
  @Disabled("Do not run because there isn't the API KEY in the depository")
  void testSubjectiveValueFail() throws Exception {
    LatLng adress = Localizer.getGeometryLocation("Borsod-Abaúj-Zemplén, Hongrie", apiKey);
    interestLocations.add(adress);
    DistanceValueFunction distanceVF =
        DistanceValueFunction.withDefaultDurationValueFunction(apiKey, interestLocations);
    LatLng apartCoordinates = Localizer.getGeometryLocation(apart, apiKey);
    assertThrows(VerifyException.class,
        () -> distanceVF.getSubjectiveValue(apartCoordinates));
  }

}
