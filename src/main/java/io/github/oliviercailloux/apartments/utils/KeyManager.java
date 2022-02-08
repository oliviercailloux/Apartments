package io.github.oliviercailloux.apartments.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class KeyManager {

  public static String getApiKey() throws FileNotFoundException, IOException {

    String apiKey = System.getenv("APIKEY");

    if (apiKey == null) {
      String fichier = new File("API_KEY.txt").getAbsolutePath();
      apiKey = "";

      try (FileReader fr = new FileReader(fichier)) {
        int c = fr.read();

        while (c != -1) {
          apiKey += (char) c;
          c = fr.read();
        }
      }
    }

    return apiKey;
  }

  public static String getGeocodeApiKey() throws FileNotFoundException, IOException {

    String geocodeApiKey = System.getenv("GEOCODEAPIKEY");

    if (geocodeApiKey == null) {
      String fichier = new File("GEOCODE_API_KEY.txt").getAbsolutePath();

      geocodeApiKey = "";

      try (FileReader fr = new FileReader(fichier)) {
        int c = fr.read();

        while (c != -1) {
          geocodeApiKey += (char) c;
          c = fr.read();
        }
      }
    }

    return geocodeApiKey;
  }
}
