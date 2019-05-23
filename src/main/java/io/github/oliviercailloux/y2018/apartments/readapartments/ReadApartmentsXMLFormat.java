package io.github.oliviercailloux.y2018.apartments.readapartments;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.oliviercailloux.y2018.apartments.apartment.Apartment;

/**
 *
 * This class enables the user to load an XML file for an apartment and transform it to an apartment object.
 *
 */
public class ReadApartmentsXMLFormat {

	private Properties prop;

	private final static Logger LOGGER = LoggerFactory.getLogger(ReadApartmentsXMLFormat.class);

	public ReadApartmentsXMLFormat() {

		prop = new Properties();
	}


	/**
	 * This method enables to read a XML file and store the informations into an apartment object. The XML file must contain at list a value for floorArea address and title. The key of each parameters in the XML file is respectively the name of the parameter.
	 * @param input is the path of XML file
	 * @return an apartment object with values for each parameters found in the XML files and default values for the other parameters.
	 * @throws IOException, NumberFormatException, InvalidPropertiesFormatException
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public Apartment readApartment(InputStream input) throws IOException, InvalidPropertiesFormatException, IllegalArgumentException, IllegalAccessException{

		LOGGER.info("Enter readApartment method");

		prop.loadFromXML(input);

		LOGGER.info("XML Files loaded with success");

		if (prop.containsKey("floorArea")==false || prop.containsKey("address")==false || prop.containsKey("title")==false) {
			LOGGER.error("Impossible to create an apartment if a floor Area, a title or an address is missing.");
			throw new InvalidPropertiesFormatException("Capital information left for the creation of an Apartment Object");
		}

		Apartment apartment = new Apartment(Double.parseDouble(prop.getProperty("floorArea")),prop.getProperty("address"),prop.getProperty("title"));

		Field fields[] = apartment.getClass().getDeclaredFields();
    	for(Field f : fields) {
        	if(prop.containsKey(f.getName())){
            	f.setAccessible(true);
            	f.set(apartment, prop.get(f.getName()));
        	}
    	}
		/*
		if (prop.containsKey("description"))
			apartment.setDescription(prop.getProperty("description"));
		if (prop.containsKey("nbBathrooms"))
			apartment.setNbBathrooms(Integer.parseInt(prop.getProperty("nbBathrooms")));
		if (prop.containsKey("terrace"))
			apartment.setTerrace(Boolean.valueOf(prop.getProperty("terrace")));
		if (prop.containsKey("floorAreaTerrace"))
			apartment.setFloorAreaTerrace(Double.parseDouble(prop.getProperty("floorAreaTerrace")));
		if (prop.containsKey("wifi"))
			apartment.setWifi(Boolean.valueOf(prop.getProperty("wifi")));
		if (prop.containsKey("tele"))
			apartment.setTele(Boolean.valueOf(prop.getProperty("tele")));
		if (prop.containsKey("nbSleeping"))
			apartment.setNbSleeping(Integer.parseInt(prop.getProperty("nbSleeping")));
		if (prop.containsKey("nbBedrooms"))
			apartment.setNbBedrooms(Integer.parseInt(prop.getProperty("nbBedrooms")));
		if (prop.containsKey("pricePerNight"))
			apartment.setPricePerNight(Double.parseDouble(prop.getProperty("pricePerNight")));
		if (prop.containsKey("nbMinNight"))
			apartment.setNbMinNight(Integer.parseInt(prop.getProperty("nbMinNight")));
		*/

		LOGGER.info("Parameters inserted with success in the Apartment Object");
		LOGGER.info("Leave readApartment method");

		return apartment;
	}

}
