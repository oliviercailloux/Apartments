package io.github.oliviercailloux.apartments.apartment.xml;

import io.github.oliviercailloux.apartments.apartment.Apartment;
import io.github.oliviercailloux.apartments.apartment.Apartment.Builder;
import java.io.IOException;
import java.io.InputStream;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class enables the user to load an XML file for an apartment and transform it to an apartment
 * object.
 */
public class ReadApartmentsXMLFormat {

  private final Properties prop;

  private static final Logger LOGGER = LoggerFactory.getLogger(ReadApartmentsXMLFormat.class);

  public ReadApartmentsXMLFormat() {
    prop = new Properties();
  }

  /**
   * This method enables to read a XML file and store the informations into an apartment object. The
   * XML file must contain at list a value for floorArea address and title. The key of each
   * parameters in the XML file is respectively the name of the parameter.
   *
   * @param input is the path of XML file
   * @return an apartment object with values for each parameters found in the XML files and default
   *         values for the other parameters.
   * @throws IOException, NumberFormatException, InvalidPropertiesFormatException
   */
  public Apartment readApartment(InputStream input) throws IOException {
    LOGGER.info("Enter readApartment method");

    prop.loadFromXML(input);

    LOGGER.info("XML Files loaded with success");

    if (!prop.containsKey("floorArea") || !prop.containsKey("address")
        || !prop.containsKey("title")) {
      LOGGER.error(
          "Impossible to create an apartment if a floor Area, a title or an address is missing.");
      throw new InvalidPropertiesFormatException(
          "Capital information left for the creation of an Apartment Object");
    }

    Builder apartBuilder = setObligatoryValues(new Builder());

    if (prop.containsKey("floorAreaTerrace")) {
      apartBuilder.setFloorAreaTerrace(Double.parseDouble(prop.getProperty("floorAreaTerrace")));
    }
    if (prop.containsKey("pricePerNight")) {
      apartBuilder.setPricePerNight(Double.parseDouble(prop.getProperty("pricePerNight")));
    }
    if (prop.containsKey("description")) {
      apartBuilder.setDescription(prop.getProperty("description"));
    }
    if (prop.containsKey("nbBedrooms")) {
      apartBuilder.setNbBedrooms(Integer.parseInt(prop.getProperty("nbBedrooms")));
    }
    if (prop.containsKey("nbSleeping")) {
      apartBuilder.setNbSleeping(Integer.parseInt(prop.getProperty("nbSleeping")));
    }
    if (prop.containsKey("nbBathrooms")) {
      apartBuilder.setNbBathrooms(Integer.parseInt(prop.getProperty("nbBathrooms")));
    }
    if (prop.containsKey("nbMinNight")) {
      apartBuilder.setNbMinNight(Integer.parseInt(prop.getProperty("nbMinNight")));
    }

    apartBuilder = setBooleanValues(apartBuilder);
    Apartment apartment = apartBuilder.build();

    LOGGER.info("Parameters inserted with success in the Apartment Object");
    LOGGER.info("Leave readApartment method");

    return apartment;
  }

  public Builder setObligatoryValues(Builder apartBuilder) {
    if (prop.containsKey("address")) {
      apartBuilder.setAddress(prop.getProperty("address"));
    }
    if (prop.containsKey("title")) {
      apartBuilder.setTitle(prop.getProperty("title"));
    }
    if (prop.containsKey("floorArea")) {
      apartBuilder.setFloorArea(Double.parseDouble(prop.getProperty("floorArea")));
    }
    return apartBuilder;
  }

  public Builder setBooleanValues(Builder apartBuilder) {
    if (prop.containsKey("terrace")) {
      apartBuilder.setTerrace(Boolean.parseBoolean(prop.getProperty("terrace")));
    }
    if (prop.containsKey("wifi")) {
      apartBuilder.setWifi(Boolean.parseBoolean(prop.getProperty("wifi")));
    }
    if (prop.containsKey("tele")) {
      apartBuilder.setTele(Boolean.parseBoolean(prop.getProperty("tele")));
    }
    return apartBuilder;
  }
}
