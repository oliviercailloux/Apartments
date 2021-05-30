package io.github.oliviercailloux.y2018.apartments.apartment.xml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.oliviercailloux.y2018.apartments.apartment.Apartment;
import io.github.oliviercailloux.y2018.apartments.apartment.Apartment.Builder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import org.junit.jupiter.api.Test;

class XMLPropertiesTests {

  @Test
  void testReadApartment() throws Exception {
    XMLProperties j = new XMLProperties();

    Apartment a = new Builder().setFloorArea(1182118.48).setAddress("118 rue du père noel 77480")
        .setNbBedrooms(5).setNbSleeping(10).setNbBathrooms(1).setTerrace(true)
        .setFloorAreaTerrace(8.6)
        .setDescription("Un igloo tout mignon en compagnie du père noël et de la mère noël")
        .setTitle("Grand Igloo").setWifi(true).setPricePerNight(404).setNbMinNight(1).setTele(false)
        .build();

    File f = new File(
        "src/test/resources/io/github/oliviercailloux/y2018/apartments/apartment/xml/xmlfileTest.xml");
    try (FileOutputStream s = new FileOutputStream(f.getAbsolutePath())) {
      j.toXML(a, s);
    }

    ReadApartmentsXMLFormat r = new ReadApartmentsXMLFormat();

    try (InputStream f1 = ReadApartmentsXMLFormat.class.getResourceAsStream("xmlfileTest.xml")) {

      Apartment a1 = r.readApartment(f1);

      assertEquals("118 rue du père noel 77480", a1.getAddress());
      assertEquals("Grand Igloo", a1.getTitle());
      assertEquals("Un igloo tout mignon en compagnie du père noël et de la mère noël",
          a1.getDescription());
      assertEquals(10, a1.getNbSleeping());
      assertEquals(1, a1.getNbBathrooms());
      assertEquals(5, a1.getNbBedrooms());
      assertEquals(1182118.48, a1.getFloorArea());
      assertEquals(8.6, a1.getFloorAreaTerrace());
      assertEquals(404.0, a1.getPricePerNight());
      assertEquals(1, a1.getNbMinNight());
      assertFalse(a1.getTele());
      assertTrue(a1.getTerrace());
      assertTrue(a1.getWifi());
    }

    ReadApartmentsXMLFormat r1 = new ReadApartmentsXMLFormat();
    try (InputStream f1 = ReadApartmentsXMLFormat.class.getResourceAsStream("xmlfileTest.xml")) {
      Apartment a1 = r1.readApartment(f1);
      System.out.println(a1);
    }
  }
}
