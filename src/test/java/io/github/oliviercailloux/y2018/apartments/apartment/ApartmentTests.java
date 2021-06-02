package io.github.oliviercailloux.y2018.apartments.apartment;

import static org.junit.jupiter.api.Assertions.*;

import io.github.oliviercailloux.y2018.apartments.apartment.Apartment.Builder;
import org.junit.jupiter.api.Test;

class ApartmentTests {

  private double floorArea = 456.56;
  private String address = "A Random Address";
  private int nbBedrooms = 5;
  private int nbSleeping = 9;
  private int nbBathrooms = 3;
  private boolean hasTerrace = true;
  private double floorAreaTerrace = 25.32;
  private String description = "A Random Description for A Random House in A Random Address";
  private String title = "A Random House";
  private boolean wifi = true;
  private double pricePerNight = 45.95;
  private int nbMinNight = 60;
  private boolean tele = false;

  @Test
  void testEqualsTrue() {
    Builder apartBuilder = new Builder();
    Apartment a1 = apartBuilder.setFloorArea(floorArea).setAddress(address)
        .setNbBedrooms(nbBedrooms).setNbSleeping(nbSleeping).setNbBathrooms(nbBathrooms)
        .setTerrace(hasTerrace).setFloorAreaTerrace(floorAreaTerrace).setDescription(description)
        .setTitle(title).setWifi(wifi).setPricePerNight(pricePerNight).setNbMinNight(nbMinNight)
        .setTele(tele).build();
    apartBuilder = new Builder();
    Apartment a2 = apartBuilder.setFloorArea(floorArea).setAddress(address)
        .setNbBedrooms(nbBedrooms).setNbSleeping(nbSleeping).setNbBathrooms(nbBathrooms)
        .setTerrace(hasTerrace).setFloorAreaTerrace(floorAreaTerrace).setDescription(description)
        .setTitle(title).setWifi(wifi).setPricePerNight(pricePerNight).setNbMinNight(nbMinNight)
        .setTele(tele).build();

    assertTrue(a1.equals(a2));
  }

  @Test
  void testEqualsFalse() {
    Builder apartBuilder = new Builder();
    Apartment a1 = apartBuilder.setFloorArea(floorArea).setAddress("Chaville")
        .setNbBedrooms(nbBedrooms).setNbSleeping(nbSleeping).setNbBathrooms(nbBathrooms)
        .setTerrace(hasTerrace).setFloorAreaTerrace(floorAreaTerrace).setDescription(description)
        .setTitle("Marc's home").setWifi(wifi).setPricePerNight(pricePerNight)
        .setNbMinNight(nbMinNight).setTele(tele).build();
    apartBuilder = new Builder();
    Apartment a2 = apartBuilder.setFloorArea(floorArea).setAddress("Arras")
        .setNbBedrooms(nbBedrooms).setNbSleeping(nbSleeping).setNbBathrooms(nbBathrooms)
        .setTerrace(hasTerrace).setFloorAreaTerrace(floorAreaTerrace).setDescription(description)
        .setTitle("Micky's home").setWifi(wifi).setPricePerNight(pricePerNight)
        .setNbMinNight(nbMinNight).setTele(tele).build();

    assertFalse(a1.equals(a2));
  }

  @Test
  void testHashCodeTrue() {
    Builder apartBuilder = new Builder();
    Apartment a1 = apartBuilder.setFloorArea(floorArea).setAddress(address)
        .setNbBedrooms(nbBedrooms).setNbSleeping(nbSleeping).setNbBathrooms(nbBathrooms)
        .setTerrace(hasTerrace).setFloorAreaTerrace(floorAreaTerrace).setDescription(description)
        .setTitle(title).setWifi(wifi).setPricePerNight(pricePerNight).setNbMinNight(nbMinNight)
        .setTele(tele).build();
    apartBuilder = new Builder();
    Apartment a2 = apartBuilder.setFloorArea(floorArea).setAddress(address)
        .setNbBedrooms(nbBedrooms).setNbSleeping(nbSleeping).setNbBathrooms(nbBathrooms)
        .setTerrace(hasTerrace).setFloorAreaTerrace(floorAreaTerrace).setDescription(description)
        .setTitle(title).setWifi(wifi).setPricePerNight(pricePerNight).setNbMinNight(nbMinNight)
        .setTele(tele).build();

    assertEquals(a1.hashCode(), a2.hashCode());
  }

  @Test
  void testHashCodeFalse() {

    Builder apartBuilder = new Builder();
    Apartment a1 = apartBuilder.setFloorArea(floorArea).setAddress("Chaville")
        .setNbBedrooms(nbBedrooms).setNbSleeping(nbSleeping).setNbBathrooms(nbBathrooms)
        .setTerrace(hasTerrace).setFloorAreaTerrace(floorAreaTerrace).setDescription(description)
        .setTitle("Marc's home").setWifi(wifi).setPricePerNight(pricePerNight)
        .setNbMinNight(nbMinNight).setTele(tele).build();
    apartBuilder = new Builder();
    Apartment a2 = apartBuilder.setFloorArea(floorArea).setAddress("Arras")
        .setNbBedrooms(nbBedrooms).setNbSleeping(nbSleeping).setNbBathrooms(nbBathrooms)
        .setTerrace(hasTerrace).setFloorAreaTerrace(floorAreaTerrace).setDescription(description)
        .setTitle("Micky's home").setWifi(wifi).setPricePerNight(pricePerNight)
        .setNbMinNight(nbMinNight).setTele(tele).build();

    assertFalse(a1.hashCode() == a2.hashCode());
  }
}
