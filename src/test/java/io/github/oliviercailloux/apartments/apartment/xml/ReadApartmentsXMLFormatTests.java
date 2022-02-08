package io.github.oliviercailloux.apartments.apartment.xml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.oliviercailloux.apartments.apartment.Apartment;
import io.github.oliviercailloux.apartments.apartment.xml.ReadApartmentsXMLFormat;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Test class for ReadApartmentsXMLFormat class. */
class ReadApartmentsXMLFormatTests {

  /**
   * Read an apartment from an XML file which is in the resources
   *
   * @throws Exception we catch all types of Exceptions
   */
  @Test
  void testReadApartmentFromResource() throws Exception {
    // Read the resource file
    ReadApartmentsXMLFormat r = new ReadApartmentsXMLFormat();
    // Construct the apartment
    Apartment a = r.readApartment(
        ReadApartmentsXMLFormatTests.class.getResourceAsStream("start-apartment-classpath.xml"));
    assertEquals("5 avenue Roger Salengro 92370 Chaville France", a.getAddress());
    assertEquals("Villa à louer", a.getTitle());
    assertEquals("", a.getDescription());
    assertEquals(5, a.getNbSleeping());
    assertEquals(0, a.getNbBathrooms());
    assertEquals(0, a.getNbBedrooms());
    assertEquals(150.0d, a.getFloorArea());
    assertEquals(160.0d, a.getFloorAreaTerrace());
    assertEquals(3, a.getPricePerNight());
    assertEquals(0, a.getNbMinNight());
    assertTrue(a.getTele());
    assertTrue(a.getTerrace());
    assertTrue(a.getWifi());
    // Test equals apartments
    Apartment.Builder apartBuilder = new Apartment.Builder();
    Apartment apartmentEqual =
        apartBuilder.setAddress("5 avenue Roger Salengro 92370 Chaville France")
            .setTitle("Villa à louer").setFloorArea(150.0d).setNbSleeping(5).setTele(true)
            .setWifi(true).setTerrace(true).setFloorAreaTerrace(160).setPricePerNight(3).build();
    assertEquals(apartmentEqual, a);
  }

  /**
   * Read an apartment from an XML file which is in the root of the directory
   *
   * @throws Exception we catch all types of Exceptions
   */
  @Test
  void testReadApartmentFromRoot() throws Exception {
    // Read the file which is at the root of the project
    ReadApartmentsXMLFormat r = new ReadApartmentsXMLFormat();
    // Construct the apartment
    Apartment a = r.readApartment(Files.newInputStream(Path.of("start-apartment.xml")));
    assertEquals("5 rue de la Ronce 92410 Ville d'Avray France", a.getAddress());
    assertEquals("hotel", a.getTitle());
    assertEquals(15.0d, a.getFloorArea());
    assertEquals(16, a.getNbBathrooms());
    assertFalse(a.getWifi());
    assertFalse(a.getTerrace());
    assertFalse(a.getTele());
    // Test equals apartments
    Apartment.Builder apartBuilder = new Apartment.Builder();
    Apartment apartmentEqual = apartBuilder.setTitle("hotel")
        .setAddress("5 rue de la Ronce 92410 Ville d'Avray France").setFloorArea(15)
        .setNbBathrooms(16).setWifi(false).setTerrace(false).setTele(false).build();
    assertEquals(apartmentEqual, a);
  }
}
