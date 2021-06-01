package io.github.oliviercailloux.y2018.apartments.apartment.json;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.VerifyException;
import io.github.oliviercailloux.y2018.apartments.apartment.Apartment;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.adapter.JsonbAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class JsonConvert contains all function to transform Apartment object to JSON and vice-versa.
 *
 * @author Etienne CARTIER & Morgane FIOT
 */
public abstract class JsonConvert {

  private static final Logger LOGGER = LoggerFactory.getLogger(JsonConvert.class);

  private JsonConvert() {}

  /**
   * The path to export a list of apartments in a JSON file.
   *
   * @return <i>Path</i> of the JSON file at the root of the project
   */
  private static Path getUniqueExportPath() {
    return Path.of("exportApartments" + System.currentTimeMillis() + ".json");
  }

  /**
   * The method return a default JSON file read by jsonToApartments.
   *
   * @return <i>Path</i> where jsonToApartments will read.
   */
  private static Path startApartments() {
    try {
      URI ressource = JsonConvert.class.getResource("defaultApartments.json").toURI();
      return Path.of(ressource);
    } catch (URISyntaxException e) {
      throw new VerifyException(e);
    }
  }

  /**
   * Converts a JSON expression to a list of Apartments.
   *
   * @return <i>List</i> the list of Apartments created
   */
  public static List<Apartment> getDefaultApartments() {
    Path apartPath = startApartments();
    try {
      return jsonToApartments(apartPath);
    } catch (IOException io) {
      throw new IllegalStateException(
          "We are reading a resource file, it should not have an Exception", io);
    }
  }

  /**
   * Converts a JSON expression to a list of Apartments.
   *
   * @param jsonPath {@link Path} the JSON expression to convert into a list of Apartments
   * @return the list of Apartments created
   * @throws IOException if the file doesn't exists
   */
  @SuppressWarnings("serial")
  public static List<Apartment> jsonToApartments(Path jsonPath) throws IOException {
    JsonbConfig config = new JsonbConfig().withAdapters(JsonConvert.getAdapter());
    String jsonString = Files.readString(jsonPath);
    List<Apartment> apartments = new ArrayList<>();
    LOGGER.info("Create ArrayList of Apartment");
    try (Jsonb jsonb = JsonbBuilder.create(config)) {
      LOGGER.info("Create Json builder");
      apartments = jsonb.fromJson(jsonString, new ArrayList<Apartment>() {
        /**/
      }.getClass().getGenericSuperclass());
    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    }
    return apartments;
  }

  /** A method that implement a {@link JsonbAdapter} to avoid the use of ApartmentBuilder Code */
  public static JsonbAdapter<Apartment, Apartment.Builder> getAdapter() {
    return new JsonbAdapter<>() {
      @Override
      public Apartment.Builder adaptToJson(Apartment obj) {
        throw new UnsupportedOperationException("This function should not be called");
      }

      /**
       * Build an Apartment with Apartment.Builder
       *
       * @param obj the Apartment to build
       * @return the Apartment build from <code>obj</code>
       */
      @Override
      public Apartment adaptFromJson(Apartment.Builder obj) {
        checkNotNull(obj, "The Apartment.Builder 'obj' can't be null");
        return obj.build();
      }
    };
  }

  /**
   * Converts a list of Apartments to a JSON file with the default path APARTMENT_PATH_JSON.
   *
   * @param listApartments <code>{@link List}</code> object to convert into JSON
   * @return <i>Path</i> of the created file
   */
  public static Path apartmentsToJson(List<Apartment> listApartments) {
    Path defaultPath = getUniqueExportPath();
    try {
      apartmentsToJson(listApartments, defaultPath);
    } catch (IOException io) {
      throw new IllegalStateException("We write in the current file, the Path exists", io);
    }
    return defaultPath.toAbsolutePath();
  }

  /**
   * Converts a list of Apartments to a JSON file. If you call the function with the same path, the
   * data in the file will be overwritten.
   *
   * @param listApartments <code>{@link List}</code> object to convert into JSON
   *        (<b>listApartments</b> could contain only one apartment)
   * @param jsonPath <i>String</i> the path where to create the JSON file
   * @throws IOException if the JSON file can't be created.
   */
  public static void apartmentsToJson(List<Apartment> listApartments, Path jsonPath)
      throws IOException {
    Files.writeString(jsonPath, apartmentsToJsonString(listApartments));
    LOGGER.info("Apartment have been converted with success");
  }

  /**
   * Converts a list of Apartments to a JSON file.
   *
   * @param listApartments <code>{@link List}</code> object to convert into JSON
   *        (<b>listApartments</b> could contain only one apartment)
   * @return <i>String</i> containing the list of Apartments in JSON format
   */
  public static String apartmentsToJsonString(List<Apartment> listApartments) {
    try (Jsonb jsonb = JsonbBuilder.create()) {
      return jsonb.toJson(listApartments);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
}
