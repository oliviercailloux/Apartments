package io.github.oliviercailloux.y2018.apartments.valuefunction;

import static org.junit.jupiter.api.Assertions.*;

import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import io.github.oliviercailloux.y2018.apartments.localize.Localizer;
import io.github.oliviercailloux.y2018.apartments.utils.KeyManager;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DistanceValueFunctionTest {

  private String apiKey;
  private HashSet<String> interestLocations;
  private String apart;
  private PartialValueFunction<Double> durationValueFunction;

  @BeforeEach
  void initEach() throws FileNotFoundException, IOException {
    this.apiKey = KeyManager.getApiKey();
    apart = "Place du Maréchal de Lattre de Tassigny, 75016 Paris";
    interestLocations = new HashSet<>();
    interestLocations.add("Place Charles de Gaulle, 75116 Paris, France");
    interestLocations.add("1 Rue Benouville, 75116 Paris 16e Arrondissement, France");
    interestLocations.add("19 Rue Surcouf, 75007 Paris, France");
    interestLocations.add("20 Boulevard Jules Guesde, 94500 Champigny-sur-Marne");
    durationValueFunction = new LinearValueFunction(0d, 36000d);
  }

  @Test
  void testSubjectiveValue() throws ApiException, InterruptedException, IOException {
    DistanceValueFunction distanceVF =
        DistanceValueFunction.getDistanceCriteria(apiKey, interestLocations);
    LatLng apartCoordinates = Localizer.getGeometryLocation(apart, apiKey);
    double subjectiveValue = distanceVF.getSubjectiveValue(apartCoordinates);
    assertEquals(0.60, subjectiveValue, 0.1);
  }

  @Test
  void testSubjectiveValueLinear() throws ApiException, InterruptedException, IOException {
    DistanceValueFunction distanceVF =
        DistanceValueFunction.getDistanceCriteria(apiKey, interestLocations, durationValueFunction);
    LatLng apartCoordinates = Localizer.getGeometryLocation(apart, apiKey);
    double subjectiveValue = distanceVF.getSubjectiveValue(apartCoordinates);
    assertEquals(0.94, subjectiveValue, 0.1);
  }

  @Test
  void testSubjectiveValueFail() throws ApiException, InterruptedException, IOException {
    interestLocations.add("Borsod-Abaúj-Zemplén, Hongrie");
    DistanceValueFunction distanceVF =
        DistanceValueFunction.getDistanceCriteria(apiKey, interestLocations);
    LatLng apartCoordinates = Localizer.getGeometryLocation(apart, apiKey);
    assertThrows(IllegalStateException.class, () -> distanceVF.getSubjectiveValue(apartCoordinates));
  }

}
