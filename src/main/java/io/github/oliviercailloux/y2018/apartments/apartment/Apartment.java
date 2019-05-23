package io.github.oliviercailloux.y2018.apartments.apartment;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;

import static com.google.common.base.Preconditions.checkArgument;



public class Apartment extends Object {
	
	private static Logger LOGGER = LoggerFactory.getLogger(Apartment.class);

	/**
	 * @param floorArea a real number it represents the floor area of the apartment in square meters.
	 * Has to be initialized, if not, the apartment can't be created without a floor area.
	 */
	private double floorArea; 

	/**
	 * @param address a String of characters that gives the full location of the apartment (number, street name, ZIP code, city, country), has to initialized
	 * Has to be initialized, if not, the apartment can't be created without an address.
	 */
	private String address; 

	/**
	 * @param nbBedrooms is an integer superior or equal to zero, it is the number of bedrooms available of use in the apartment.
	 * if not initialized, the parameter will have a default value of <code>0</code> which mean the apartment doesn't have any bedroom.
	 */
	private int nbBedrooms;

	/**
	 * @param nbSleepings is an integer superior or equal to zero corresponding of the accommodation capacity of the apartment (nb of people that can sleep in the apartment).
	 * If not initialized, the parameter will have a default value of <code>0</code> which mean the apartment doesn't have any place to sleep.
	 */
	private int nbSleeping; 

	/**
	 * @param nbBathrooms an integer superior or equal to zero which corresponds to the number of bathrooms.
	 * If not initialized, the parameter will have a default value set of <code>0</code> which mean the apartment doesn't have any bathroom.
	 */
	private int nbBathrooms;

	/**
	 * @param terrace a boolean (true/false) which indicates if there's a terrace or not can be interpreted as hasTerrace.
	 * If not initialized, the parameter will have a default value set of <code>false</code> which mean the apartment doesn't have a terrace.
	 */
	private boolean terrace;

	/**
	 * @param floorAreaTerrace is a real number superior or equal to zero it represents the floor area of the terrace of the apartment if there's any.
	 * If not initialized, the parameter will have a default value set of <code>0</code> which mean the apartment have a floor area of terrace of zero, whether it has or not a terrace.
	 */
	private double floorAreaTerrace; 

	/**
	 * @param description is a string of characters that describe the apartment and the offer (its accommodations).
	 * If not initialized, the parameter will have a default value set of <code>""</code> which mean the announcement doesn't have a description.
	 */
	private String description; 

	/**
	 * @param title is a string of characters that represents the title of the announcement.
	 * It has to be initialized, if not, the apartment can't be created without a <code>title</code>.
	 */
	private String title;

	/**
	 * @param wifi is a boolean (true/false) which indicates if there is wireless connection to Internet or not can be interpreted as hasWifi. 
	 * If not initialized, the parameter will have a default value of <code>false</code> which mean the apartment doesn't have wifi.
	 */
	private boolean wifi; 

	/**
	 * @param pricePerNight is a real number superior or equal to zero, how much it cost (before any fees) to stay per night in euros. 
	 * If not initialized, the parameter will have a default value of <code>0</code> which mean the apartment doesn't have a price per night.
	 */
	private double pricePerNight; 

	/**
	 * @param nbMinNight is an integer superior or equal to zero, indicates how long in nights the customer have to stay. 
	 * If not initialized, the parameter will have a default value of <code>0</code> which mean the apartment doesn't have a minimum of night to stay-in.
	 */
	private int nbMinNight; 

	/**
	 * @param tele is a boolean (true/false) which indicates if there's a television or not can be interpreted as hasTelevision. 
	 * If not initialized, the parameter will have a default set value of <code>false</code> which mean the apartment doesn't have a tele.
	 */
	private boolean tele; 



	/**
	 * @param floorArea is a real number superior or equal to zero, it represents the floor area of the apartment in square meters
	 * @param address is a string of characters that gives the full location of the apartment
	 * @param title a string of characters that represents the title of the announcement
	 */
	public Apartment (double floorArea, String address, String title) {	
		this.floorArea = floorArea;
		this.address = address;
		this.nbBedrooms = 0 ;
		this.nbSleeping = 0 ;
		this.nbBathrooms = 0;
		this.terrace = false ;
		this.floorAreaTerrace = 0 ;
		this.description = "";
		this.title = title;
		this.wifi = false ;
		this.pricePerNight = 0;
		this.nbMinNight = 0 ;
		this.tele = false ;
		checkArgument(floorArea>=0,"The floor area of the apartment cannot be negative");
		checkArgument(address !="","The address of the apartment must be specified");
		checkArgument(title !="","The title of the apartment must be specified");
		LOGGER.info("the apartment has been created with success");
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Apartment))
			return false;
		Apartment apart = (Apartment) obj;
		return apart.floorArea == floorArea && apart.address.equals(address) && apart.nbBedrooms == nbBedrooms && apart.nbSleeping == nbSleeping && apart.nbBathrooms == nbBathrooms && apart.terrace == terrace && apart.floorAreaTerrace == floorAreaTerrace && apart.description.equals(description) && apart.title.equals(title) && apart.wifi == wifi && apart.pricePerNight == pricePerNight && apart.nbMinNight == nbMinNight &&  apart.tele == tele ;
		}
	
	
	/**
	 * A toString meant to be used while in development for the testing
	 *  @return only the essential information of an apartment, which are the floor area, its address and his title
	 */
	// public String toString() {
		//String floorAreaTS = "\nFloor area : " + Double.toString(floorArea) + " square meters";
		//String addressTS = "\nAddress : " + address ;
		//String titleTS = "\nTitle : " + title ;
		//return floorAreaTS + addressTS + titleTS;
	//}

	public String toString(){
		ToStringHelper floorAreas = MoreObjects.toStringHelper(this);
		ToStringHelper adressTS = MoreObjects.toStringHelper(this);
		ToStringHelper titleTS = MoreObjects.toStringHelper(this);
		adressTS.add(this.address, true);
		floorAreas.add(Double.toString(this.floorArea), true);
		titleTS.add(this.title, true);
		return floorAreas.toString() + adressTS.toString() + titleTS.toString(); 
		
	}

	/**
	 * A toString based method meant to be used after the development for regular use
	 *  @return all information of an apartment
	 */
	public String toFullString() {
		String dispTitle ;
		String dispAddress ;
		String dispFloorArea ;
		String dispNbBedrooms ;
		String dispNbSleeping ;
		String dispNbBathrooms ;
		String dispTerrace ;
		String dispFloorAreaTerrace ;
		String dispDescription ;
		String dispWifi ;
		String dispPricePerNight ;
		String dispNbMinNight ;
		String dispTele ;

		dispTitle = "\nTitle : " + title ;
		dispAddress = "\nAddress : " + address ;
		dispFloorArea = "\nFloor area : " + Double.toString(floorArea) + " square meters";
		dispNbBedrooms = "\nNumber of bedrooms : " + ((nbBedrooms == 0) ? "N/A" : Integer.toString(nbBedrooms)+" bedroom(s)") ;
		dispNbSleeping = "\nNumber of sleeping (capacity) : " + ((nbSleeping == 0) ? "N/A" : Integer.toString(nbSleeping)+" person(s)") ;
		dispNbBathrooms = "\nNumber of bathrooms : " + ((nbBathrooms == 0) ? "N/A" : Integer.toString(nbBathrooms)+" bathroom(s)") ;
		dispTerrace = "\nTerrace : " + ((terrace == true) ? "Yes" : "No") ;
		dispFloorAreaTerrace = ((terrace == false) ? "" : "\nTerrace floor area : " + ((floorAreaTerrace == 0) ? "N/A" : Double.toString(floorAreaTerrace)+" square meters")) ;
		dispDescription = "\nDescription : " + ((description == "") ? "N/A" : description) ;
		dispWifi = "\nWifi : " + ((wifi == true) ? "Yes" : "No") ;
		dispTele = "\nTelevision : " + ((tele == true) ? "Yes" : "No") ;
		dispPricePerNight = "\nPrice per night : " + ((pricePerNight == 0) ? "N/A" : Double.toString(pricePerNight)+"€") ;
		dispNbMinNight = "\nNumber of night minimum to rent this apartment : " + ((nbMinNight == 0) ? "N/A" : Integer.toString(nbMinNight)+" night(s)") ;
		
		return dispTitle + dispAddress + dispFloorArea  +dispNbBedrooms + dispNbSleeping + dispNbBathrooms + dispTerrace + dispFloorAreaTerrace + dispDescription  +dispWifi + dispPricePerNight + dispNbMinNight + dispTele ; 
	}

	/**
	 * get the value of the floor area
	 * @return a double positive or equal to zero
	 */
	public double getFloorArea() {
		return floorArea;
	}

	/**
	 * get the value of the address
	 * @return a String of characters
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * get the number of bedrooms, 0 if missing
	 * @return an integer positive or equal to zero
	 */
	public int getNbBedrooms() {
		return nbBedrooms;
	}

	/**
	 * get the number of sleeping, 0 if missing
	 * @return an integer positive or equal to zero
	 */
	public int getNbSleeping() {
		return nbSleeping;
	}

	/**
	 * get the number of bathrooms, 0 if missing
	 * @return an integer positive or equal to zero
	 */
	public int getNbBathrooms() {
		return nbBathrooms;
	}

	/**
	 * get the presence (or absence) of a terrace
	 * @return a boolean
	 */
	public boolean getTerrace() {
		return terrace;
	}

	/**
	 * get the value of the floor area of the terrace (0 if there is no terrace)
	 * @return a double positive or equal to zero
	 */
	public double getFloorAreaTerrace() {
		return floorAreaTerrace;
	}

	/**
	 * get the description of the apartment, "" if missing
	 * @return a string of characters
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * get the title of the announcement 
	 * @return a string of characters
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * get the presence (or absence) of WiFi 
	 * @return a boolean
	 */
	public boolean getWifi() {
		return wifi;
	}

	/**
	 * get the price for one night, 0 if missing
	 * @return a double positive or equal to zero
	 */
	public double getPricePerNight() {
		return pricePerNight;
	}

	/**
	 * get the minimum number of nights, 0 if missing
	 * @return an integer positive or equal to zero
	 */
	public int getNbMinNight() {
		return nbMinNight;
	}

	/**
	 * get the presence (or absence) of a TV
	 * @return a boolean
	 */
	public boolean getTele() {
		return tele;
	}
	

	/**
	 * @param floorArea is a real number superior or equal to zero
	 * @return 
	 */
	public void setFloorArea(double floorArea) {
		checkArgument(floorArea >= 0,"The floor area should not be negative");
		this.floorArea = floorArea;
		LOGGER.info("The floor area has been set to "+ floorArea);
	}


	/**
	 * @param address is a string of characters 
	 */
	public void setAddress(String address) {
		checkArgument(address !="","The address should not be empty");
		this.address = address;
		LOGGER.info("The address has been set to "+ address);
	}

	/**
	 * @param nbBedrooms is an integer superior or equal to zero
	 */
	public void setNbBedrooms(int nbBedrooms) {
		checkArgument(nbBedrooms >= 0,"The number of Bedrooms can not be negative");
		this.nbBedrooms = nbBedrooms;
		LOGGER.info("The number of bathrooms has been set to "+ nbBedrooms);
	}

	/**
	 * @param nbSleepings is an integer superior or equal to zero
	 */
	public void setNbSleeping(int nbSleeping) {
		checkArgument(nbSleeping >= 0,"The accomodation capacity can not be negative");
		this.nbSleeping = nbSleeping ;
		LOGGER.info("The number of sleepings has been set to "+ nbSleeping);
	}

	/**
	 * @param nbBathrooms is an integer superior or equal to zero 
	 */
	public void setNbBathrooms(int nbBathrooms) {
		checkArgument(nbBathrooms >= 0,"The number of bathrooms can not be negative");
		this.nbBathrooms = nbBathrooms ;
		LOGGER.info("The number of bathrooms has been set to "+ nbBathrooms);
	}

	/**
	 * @param terrace is a boolean (true/false)
	 */
	public void setTerrace(boolean terrace) {
		this.terrace = terrace ;
		LOGGER.info("terrace has been set to "+ terrace);
	}

	/**
	 * @param floorAreaTerrace is a real number superior or equal to zero, it only works if terrace = true (use setTerrace)
	 */
	public void setFloorAreaTerrace(double floorAreaTerrace) {
		checkArgument((this.terrace == false && floorAreaTerrace == 0) ||( this.terrace == true),"The terrace can not have a floor area if it doesn't exists");
		checkArgument(floorAreaTerrace >= 0,"The floor area of the terrace can not be negative");
		this.floorAreaTerrace = floorAreaTerrace ;
		LOGGER.info("The floor area of the terrace has been set to "+ floorAreaTerrace);
	}

	/**
	 * @param description is a string of characters
	 */
	public void setDescription(String description) {
		this.description = description ;
		LOGGER.info("The description has been set to " + description);
	}

	/**
	 * @param title is a string of characters 
	 */
	public void setTitle(String title) {
		checkArgument(title !="","The title should not be empty");
		this.title = title ;
		LOGGER.info("The title has been set to "+ floorArea);
	}

	/**
	 * @param wifi is a boolean (true/false)
	 */
	public void setWifi(boolean wifi) {
		this.wifi = wifi ;
		LOGGER.info("The wifi has been set to "+ wifi);
	}

	/**
	 * @param pricePerNight is a real number superior or equal to zero
	 */
	public void setPricePerNight(double pricePerNight) {
		checkArgument(pricePerNight >= 0,"The price per night can not be negative");
		this.pricePerNight = pricePerNight ;
		LOGGER.info("The price per night has been set to "+ pricePerNight);
	}

	/**
	 * @param nbMinNight is an integer superior or equal to zero
	 */
	public void setNbMinNight(int nbMinNight) {
		checkArgument(nbMinNight >= 0,"The minimum number of nights can not be negative");
		this.nbMinNight = nbMinNight ;
		LOGGER.info("The number minimum of night has been set to "+ nbMinNight);
	}

	/**
	 * @param tele is a boolean (true/false)
	 */
	public void setTele(boolean tele) {
		this.tele = tele ;
		LOGGER.info("The tele has been set to "+ tele);
	}

	@Override
	public int hashCode() {
		return Objects.hash(address, floorArea, nbBedrooms, nbSleeping, nbBathrooms, terrace, floorAreaTerrace, description, title, wifi, pricePerNight, nbMinNight, tele);
	}

}
