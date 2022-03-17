package io.github.oliviercailloux.apartments.apartment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.oliviercailloux.apartments.apartment.Apartment.Builder;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import org.junit.jupiter.api.Test;

/** Test class for ApartmentFactory. */
class ApartmentFactoryTests {

  /**
   * Test GenerateApartment function tests the correspondence between the results sent and those
   * recorded.
   */
  @Test
  void testGenerateApartmentFromParameters() {
    double floorArea = 456.56;
    String address = "A Random Address";
    int nbBedrooms = 5;
    int nbSleeping = 9;
    int nbBathrooms = 3;
    boolean hasTerrace = true;
    double floorAreaTerrace = 25.32;
    String description = "A Random Description for A Random House in A Random Address";
    String title = "A Random House";
    boolean wifi = true;
    double pricePerNight = 45.95;
    int nbMinNight = 60;
    boolean tele = false;
    Builder apartBuilder = new Builder();
    Apartment apart = apartBuilder.setFloorArea(floorArea).setAddress(address)
        .setNbBedrooms(nbBedrooms).setNbSleeping(nbSleeping).setNbBathrooms(nbBathrooms)
        .setTerrace(hasTerrace).setFloorAreaTerrace(floorAreaTerrace).setDescription(description)
        .setTitle(title).setWifi(wifi).setPricePerNight(pricePerNight).setNbMinNight(nbMinNight)
        .setTele(tele).build();

    assertEquals(floorArea, apart.getFloorArea());
    assertEquals(address, apart.getAddress());
    assertEquals(nbBedrooms, apart.getNbBedrooms());
    assertEquals(nbSleeping, apart.getNbSleeping());
    assertEquals(nbBathrooms, apart.getNbBathrooms());
    assertEquals(hasTerrace, apart.getTerrace());
    assertEquals(floorAreaTerrace, apart.getFloorAreaTerrace());
    assertEquals(description, apart.getDescription());
    assertEquals(title, apart.getTitle());
    assertEquals(wifi, apart.getWifi());
    assertEquals(pricePerNight, apart.getPricePerNight());
    assertEquals(nbMinNight, apart.getNbMinNight());
    assertEquals(tele, apart.getTele());
  }

  /**
   * This function allow us to test if we have an exception thrown when the json path given in
   * argument of the method is wrong.
   */
  @Test
  public void testGenerateApartmentFromJsonException() {
    assertThrows(IOException.class,
        () -> ApartmentFactory.generateApartmentsFromJsonPath(Path.of("abc")));
  }

  /** Make an API call and verify that the return format of the API has not changed */
  @Test
  public void testRandomAddress() {
    String lontitude = "2.2712946";
    String latitude = "48.869962";
    Client client = ClientBuilder.newClient();
    try {
      Optional<String> address =
          ApartmentFactory.tryToGetOnlineRandomAddress(client, lontitude, latitude);
      assertTrue(address.isPresent(), "We are supposed to retrieve an address");
      assertEquals("2 Chemin des Lacs à la Porte Dauphine 75016 Paris", address.get(),
          "Call to the address retrieval API with a fixed longitude and attitude need to return a"
              + " good result");
    } finally {
      client.close();
    }
  }
}
