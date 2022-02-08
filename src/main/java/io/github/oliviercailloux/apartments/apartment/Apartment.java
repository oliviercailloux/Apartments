package io.github.oliviercailloux.apartments.apartment;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Apartment extends Object {

  private static final Logger LOGGER = LoggerFactory.getLogger(Apartment.class);

  /**
   * A real number representing the floor area of the apartment in square meters. Has to be
   * initialized
   */
  private double floorArea;

  /**
   * A String giving the full location of the apartment (number, street name, ZIP code, city,
   * country). Has to be initialized
   */
  private String address;

  /** An integer >= 0 representing the number of bedrooms available default : 0 */
  private int nbBedrooms;

  /**
   * An integer >= 0 corresponding of the accommodation capacity of the apartment (nb of people that
   * can sleep in the apartment) default : 0
   */
  private int nbSleeping;

  /** An integer >= 0 corresponding to the number of bathrooms. default : 0 */
  private int nbBathrooms;

  /** A boolean which indicates if there's a terrace or not. default : false */
  private boolean hasTerrace;

  /**
   * A real number >= 0 representing the floor area of the terrace of the apartment if there's any.
   * default : 0
   */
  private double floorAreaTerrace;

  /** A String describing the apartment and the offer default : "" */
  private String description;

  /** A String representing the title of the announcement. Has to be initialized */
  private String title;

  /**
   * A boolean which indicates if there is wireless connection to Internet or not default : false
   */
  private boolean wifi;

  /**
   * A real number >= 0 how much it costs (before any fees) to stay per night in euros. default : 0
   */
  private double pricePerNight;

  /** An integer >= 0, indicates how long in nights the customer have to stay. default : 0 */
  private int nbMinNight;

  /** A boolean which indicates if there's a television or not can default : false */
  private boolean tele;

  /** Constructor by default which will be used by Apartment.Builder */
  private Apartment() {
    this.floorArea = 0;
    this.address = null;
    this.title = null;
    this.nbBedrooms = 0;
    this.nbSleeping = 0;
    this.nbBathrooms = 0;
    this.hasTerrace = false;
    this.floorAreaTerrace = 0;
    this.description = "";
    this.wifi = false;
    this.pricePerNight = 0;
    this.nbMinNight = 0;
    this.tele = false;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Apartment))
      return false;
    Apartment apart = (Apartment) obj;
    return apart.floorArea == this.floorArea && apart.address.equals(this.address)
        && apart.nbBedrooms == this.nbBedrooms && apart.nbSleeping == this.nbSleeping
        && apart.nbBathrooms == this.nbBathrooms && apart.hasTerrace == this.hasTerrace
        && apart.floorAreaTerrace == this.floorAreaTerrace
        && apart.description.equals(this.description) && apart.title.equals(this.title)
        && apart.wifi == this.wifi && apart.pricePerNight == this.pricePerNight
        && apart.nbMinNight == this.nbMinNight && apart.tele == this.tele;
  }

  @Override
  /**
   * A toString meant to be used while in development for the testing
   *
   * @return only the mandatory information of an apartment
   */
  public String toString() {
    String floorAreaTS = String.format("%nFloor area : %f square meters", this.floorArea);
    String addressTS = "\nAddress : " + this.address;
    String titleTS = "\nTitle : " + this.title;
    return floorAreaTS + addressTS + titleTS;
  }

  /**
   * A toString based method meant to be used after development for regular use
   *
   * @return all information of an apartment
   */
  public String toFullString() {
    String dispTitle;
    String dispAddress;
    String dispFloorArea;
    String dispNbBedrooms;
    String dispNbSleeping;
    String dispNbBathrooms;
    String dispTerrace;
    String dispFloorAreaTerrace;
    String dispDescription;
    String dispWifi;
    String dispPricePerNight;
    String dispNbMinNight;
    String dispTele;

    dispTitle = "\nTitle : " + this.title;
    dispAddress = "\nAddress : " + this.address;
    dispFloorArea = "\nFloor area : " + Double.toString(this.floorArea) + " square meters";
    dispNbBedrooms = "\nNumber of bedrooms : "
        + ((this.nbBedrooms == 0) ? "N/A" : Integer.toString(this.nbBedrooms) + " bedroom(s)");
    dispNbSleeping = "\nNumber of sleeping (capacity) : "
        + ((this.nbSleeping == 0) ? "N/A" : Integer.toString(this.nbSleeping) + " person(s)");
    dispNbBathrooms = "\nNumber of bathrooms : "
        + ((this.nbBathrooms == 0) ? "N/A" : Integer.toString(this.nbBathrooms) + " bathroom(s)");
    dispTerrace = "\nTerrace : " + (this.hasTerrace ? "Yes" : "No");
    dispFloorAreaTerrace =
        new StringBuilder().append(!this.hasTerrace ? "" : "\nTerrace floor area : ")
            .append((floorAreaTerrace == 0) ? "N/A"
                : Double.toString(this.floorAreaTerrace) + " square meters")
            .toString();
    dispDescription =
        "\nDescription : " + (Objects.equals(this.description, "") ? "N/A" : this.description);
    dispWifi = "\nWifi : " + (this.wifi ? "Yes" : "No");
    dispTele = "\nTelevision : " + (this.tele ? "Yes" : "No");
    dispPricePerNight = "\nPrice per night : "
        + ((this.pricePerNight == 0) ? "N/A" : Double.toString(this.pricePerNight) + "€");
    dispNbMinNight = "\nNumber of night minimum to rent this apartment : "
        + ((this.nbMinNight == 0) ? "N/A" : Integer.toString(this.nbMinNight) + " night(s)");

    return dispTitle + dispAddress + dispFloorArea + dispNbBedrooms + dispNbSleeping
        + dispNbBathrooms + dispTerrace + dispFloorAreaTerrace + dispDescription + dispWifi
        + dispPricePerNight + dispNbMinNight + dispTele;
  }

  /**
   * Gets the value of the floor area
   *
   * @return a double >= 0
   */
  public double getFloorArea() {
    return this.floorArea;
  }

  /**
   * Gets the address
   *
   * @return a String
   */
  public String getAddress() {
    return this.address;
  }

  /**
   * Gets the number of bedrooms
   *
   * @return an integer >= 0
   */
  public int getNbBedrooms() {
    return this.nbBedrooms;
  }

  /**
   * Gets the number of sleeping
   *
   * @return an integer >= 0
   */
  public int getNbSleeping() {
    return this.nbSleeping;
  }

  /**
   * Gets the number of bathrooms
   *
   * @return an integer >= 0
   */
  public int getNbBathrooms() {
    return this.nbBathrooms;
  }

  /**
   * Gets the presence (or absence) of a terrace
   *
   * @return a boolean
   */
  public boolean getTerrace() {
    return this.hasTerrace;
  }

  /**
   * Gets the value of the floor area of the terrace (0 if there is no terrace)
   *
   * @return a double >= 0
   */
  public double getFloorAreaTerrace() {
    return this.floorAreaTerrace;
  }

  /**
   * Gets the description of the apartment, "" if missing
   *
   * @return a String
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * Gets the title of the announcement
   *
   * @return a String
   */
  public String getTitle() {
    return this.title;
  }

  /**
   * Gets the presence (or absence) of WiFi
   *
   * @return a boolean
   */
  public boolean getWifi() {
    return this.wifi;
  }

  /**
   * Gets the price for one night
   *
   * @return a double >= 0
   */
  public double getPricePerNight() {
    return this.pricePerNight;
  }

  /**
   * Gets the minimum number of nights
   *
   * @return an integer >= 0
   */
  public int getNbMinNight() {
    return this.nbMinNight;
  }

  /**
   * Gets the presence (or absence) of a TV
   *
   * @return a boolean
   */
  public boolean getTele() {
    return this.tele;
  }

  /** @param floorArea a real number >= 0 */
  private void setFloorArea(double floorArea) {
    checkArgument(floorArea > 0, "The floor area should not be <= 0");
    this.floorArea = floorArea;
    LOGGER.info("The floor area has been set to {}", floorArea);
  }

  /** @param address a String */
  private void setAddress(String address) {
    checkArgument(!address.isEmpty(), "The address should not be empty");
    this.address = checkNotNull(address, "The address should not be null");
    LOGGER.info("The address has been set to {}", address);
  }

  /** @param nbBedrooms an integer >= 0 */
  private void setNbBedrooms(int nbBedrooms) {
    checkArgument(nbBedrooms >= 0, "The number of Bedrooms can not be negative");
    this.nbBedrooms = nbBedrooms;
    LOGGER.info("The number of bathrooms has been set to {}", nbBedrooms);
  }

  /** @param nbSleeping an integer >= 0 */
  private void setNbSleeping(int nbSleeping) {
    checkArgument(nbSleeping >= 0, "The accomodation capacity can not be negative");
    this.nbSleeping = nbSleeping;
    LOGGER.info("The number of sleepings has been set to {}", nbSleeping);
  }

  /** @param nbBathrooms an integer >= 0 */
  private void setNbBathrooms(int nbBathrooms) {
    checkArgument(nbBathrooms >= 0, "The number of bathrooms can not be negative");
    this.nbBathrooms = nbBathrooms;
    LOGGER.info("The number of bathrooms has been set to {}", nbBathrooms);
  }

  /** @param terrace a boolean */
  private void setTerrace(boolean terrace) {
    this.hasTerrace = terrace;
    LOGGER.info("terrace has been set to {}", terrace);
  }

  /**
   * @param floorAreaTerrace a real number >= 0, it only works if terrace = true (use setTerrace)
   */
  private void setFloorAreaTerrace(double floorAreaTerrace) {
    checkArgument(floorAreaTerrace >= 0, "The floor area of the terrace cannot be negative");
    this.floorAreaTerrace = floorAreaTerrace;
    LOGGER.info("The floor area of the terrace has been set to {}", floorAreaTerrace);
  }

  /** @param description a String */
  private void setDescription(String description) {
    this.description = checkNotNull(description, "The description should not be null");
    LOGGER.info("The description has been set to {}", description);
  }

  /** @param title a String */
  private void setTitle(String title) {
    checkArgument(!title.isEmpty(), "The title should not be empty");
    this.title = checkNotNull(title, "The title should not be null");
    LOGGER.info("The title has been set to {}", floorArea);
  }

  /** @param wifi a boolean */
  private void setWifi(boolean wifi) {
    this.wifi = wifi;
    LOGGER.info("The wifi has been set to {}", wifi);
  }

  /** @param pricePerNight a real number >= 0 */
  private void setPricePerNight(double pricePerNight) {
    checkArgument(pricePerNight >= 0, "The price per night cannot be negative");
    this.pricePerNight = pricePerNight;
    LOGGER.info("The price per night has been set to {}", pricePerNight);
  }

  /** @param nbMinNight an integer >= 0 */
  private void setNbMinNight(int nbMinNight) {
    checkArgument(nbMinNight >= 0, "The minimum number of nights cannot be negative");
    this.nbMinNight = nbMinNight;
    LOGGER.info("The number minimum of night has been set to {}", nbMinNight);
  }

  /** @param tele a boolean */
  private void setTele(boolean tele) {
    this.tele = tele;
    LOGGER.info("The tele has been set to {}", tele);
  }

  @Override
  public int hashCode() {
    return Objects.hash(address, floorArea, nbBedrooms, nbSleeping, nbBathrooms, hasTerrace,
        floorAreaTerrace, description, title, wifi, pricePerNight, nbMinNight, tele);
  }

  /**
   * Code inspired from :
   * https://codereview.stackexchange.com/questions/127391/simple-builder-pattern-implementation-for-building-immutable-objects/127509#127509
   */
  public static class Builder {

    private Apartment apartmentToBuild;
    private boolean wifiKnown;
    private boolean teleKnown;
    private boolean hasTerraceKnown;

    public Builder() {
      apartmentToBuild = new Apartment();
      this.hasTerraceKnown = false;
      this.teleKnown = false;
      this.wifiKnown = false;
    }

    public Apartment build() {
      if (apartmentToBuild.getFloorArea() < 0) {
        throw new IllegalStateException("The floor area of the apartment cannot be negative");
      } else if (apartmentToBuild.getAddress() == null) {
        throw new IllegalStateException("The address of the apartment must be specified");
      } else if (apartmentToBuild.getTitle() == null) {
        throw new IllegalStateException("The title of the apartment must be specified");
      } else if (!this.hasTerraceKnown) {
        throw new IllegalStateException("The terrace must be specified");
      } else if (!this.teleKnown) {
        throw new IllegalStateException("The presence of TV must be specified");
      } else if (!this.wifiKnown) {
        throw new IllegalStateException("The presence of wifi must be specified");
      } else if (!(apartmentToBuild.getTerrace()) && apartmentToBuild.getFloorAreaTerrace() > 0) {
        throw new IllegalStateException(
            "The terrace can not have a floor area if it doesn't exists");
      } else if (apartmentToBuild.getTerrace() && apartmentToBuild.getFloorAreaTerrace() <= 0) {
        throw new IllegalStateException("The existing terrace can not have a floor area  <= 0");
      }
      return this.apartmentToBuild;
    }

    public Builder setFloorArea(double floorArea) {
      this.apartmentToBuild.setFloorArea(floorArea);
      return this;
    }

    public Builder setAddress(String address) {
      this.apartmentToBuild.setAddress(address);
      return this;
    }

    public Builder setTitle(String title) {
      this.apartmentToBuild.setTitle(title);
      return this;
    }

    public Builder setNbBedrooms(int nbBedrooms) {
      this.apartmentToBuild.setNbBedrooms(nbBedrooms);
      return this;
    }

    public Builder setNbSleeping(int nbSleeping) {
      this.apartmentToBuild.setNbSleeping(nbSleeping);
      return this;
    }

    public Builder setNbBathrooms(int nbBathrooms) {
      this.apartmentToBuild.setNbBathrooms(nbBathrooms);
      return this;
    }

    public Builder setFloorAreaTerrace(double floorAreaTerrace) {
      this.apartmentToBuild.setFloorAreaTerrace(floorAreaTerrace);
      return this;
    }

    public Builder setPricePerNight(double pricePerNight) {
      this.apartmentToBuild.setPricePerNight(pricePerNight);
      return this;
    }

    public Builder setNbMinNight(int nbMinNight) {
      this.apartmentToBuild.setNbMinNight(nbMinNight);
      return this;
    }

    public Builder setTerrace(boolean terrace) {
      this.hasTerraceKnown = true;
      this.apartmentToBuild.setTerrace(terrace);
      return this;
    }

    public Builder setDescription(String description) {
      this.apartmentToBuild.setDescription(description);
      return this;
    }

    public Builder setWifi(boolean wifi) {
      this.wifiKnown = true;
      this.apartmentToBuild.setWifi(wifi);
      return this;
    }

    public Builder setTele(boolean tele) {
      this.teleKnown = true;
      this.apartmentToBuild.setTele(tele);
      return this;
    }
  }
}
