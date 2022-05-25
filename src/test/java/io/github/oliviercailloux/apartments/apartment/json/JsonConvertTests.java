package io.github.oliviercailloux.apartments.apartment.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.oliviercailloux.apartments.apartment.Apartment;
import io.github.oliviercailloux.apartments.apartment.Apartment.Builder;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import jakarta.json.bind.adapter.JsonbAdapter;
import org.junit.jupiter.api.Test;

/**
 * Test class for JsonConvert
 *
 * @author Etienne CARTIER & Morgane FIOT
 */
public class JsonConvertTests {
  Path jsonTestPath;

  public JsonConvertTests() throws Exception {
    URI ressource = JsonConvertTests.class.getResource("jsonApartments.json").toURI();
    this.jsonTestPath = Path.of(ressource);
  }

  /**
   * Tests apartmentsToJson function. Verifies if the JSON file created by the function corresponds
   * to the expected file.
   *
   * @throws IOException if the file doesn't exist
   */
  @Test
  void testApartmentsToJson() throws Exception {
    Builder apartBuilder = new Apartment.Builder();
    ArrayList<Apartment> apartments = new ArrayList<>();
    apartments.add(apartBuilder.setAddress("118 rue du père noel 77480").setFloorArea(1182118.48)
        .setTitle("Grand Igloo").setTerrace(false).setWifi(false).setTele(false).build());
    apartBuilder = new Apartment.Builder();
    apartments.add(apartBuilder.setAddress("123 rue du soleil").setFloorArea(1234567.89)
        .setTitle("Maison Test").setTerrace(false).setWifi(false).setTele(false).build());

    String expectedApartmentsJsonString =
        Files.readString(Path.of("expectedApartmentsJsonString.json"));
    assertEquals(expectedApartmentsJsonString, JsonConvert.apartmentsToJsonString(apartments));
  }

  /**
   * Tests jsonToApartments function. Verifies if the <i>ArrayList</i> of Apartment created by the
   * function corresponds to the expected <i>ArrayList</i>.
   *
   * @throws URISyntaxException if the file doesn't exists.
   * @throws IOException if the file can't be convert into JSON format.
   */
  @Test
  void testJsonToApartments() throws Exception {
    Builder apartBuilder = new Apartment.Builder();
    List<Apartment> apartmentsRef = new ArrayList<>();
    apartmentsRef.add(apartBuilder.setAddress("118 rue du père noel 77480").setFloorArea(1182118.48)
        .setTitle("Grand Igloo").setTerrace(false).setWifi(false).setTele(false).build());
    apartBuilder = new Apartment.Builder();
    apartmentsRef.add(apartBuilder.setAddress("123 rue du soleil").setFloorArea(1234567.89)
        .setTitle("Maison Test").setTerrace(false).setWifi(false).setTele(false).build());

    List<Apartment> apartmentsTest = JsonConvert.jsonToApartments(this.jsonTestPath);

    assertEquals(apartmentsRef.get(0).hashCode(), apartmentsTest.get(0).hashCode());
    assertEquals(apartmentsRef.get(1).hashCode(), apartmentsTest.get(1).hashCode());
  }

  /** Test if the adapter method works */
  @Test
  void testGetAdapter() throws Exception {
    JsonbAdapter<Apartment, Apartment.Builder> adapter = JsonConvert.getAdapter();
    Apartment.Builder builder1 =
        new Apartment.Builder().setAddress("118 rue du père noel 77480").setFloorArea(1182118.48)
            .setTitle("Grand Igloo").setTerrace(false).setWifi(false).setTele(false);
    Apartment.Builder builder2 =
        new Apartment.Builder().setAddress("118 rue du père noel 77480").setFloorArea(1182118.48)
            .setTitle("Grand Igloo").setTerrace(false).setWifi(false).setTele(false);
    Apartment a = adapter.adaptFromJson(builder1);
    Apartment b = builder2.build();
    assertEquals(a, b);
  }
}
