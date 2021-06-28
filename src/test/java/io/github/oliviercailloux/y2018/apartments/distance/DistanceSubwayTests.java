package io.github.oliviercailloux.y2018.apartments.distance;

import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.oliviercailloux.y2018.apartments.utils.KeyManager;

/**
 * Inspiration for reading .txt file :
 * https://www.ukonline.be/programmation/java/tutoriel/chapitre12/page3.php
 */
class DistanceSubwayTests {

  /**
   * The test check if the distance (in second) between Ville d'Avray and Paris is below 2h (6300
   * seconds) and above 30 minutes (1800 seconds) This test fail in unnatural conditions of
   * circulation (strike, etc...) This test does not run due to the absence of the file API_KEY.txt
   * of Google Maps
   *
   * @throws Exception
   */
  void testCalculateDistanceAddress() throws Exception {

    DistanceSubway dist = new DistanceSubway("Paris", "Ville d'Avray", KeyManager.getApiKey());
    double time = dist.calculateDistanceAddress(DistanceMode.ADDRESS);
    assertTrue(7200 > time && 1800 < time);
  }
}
