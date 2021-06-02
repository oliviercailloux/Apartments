package io.github.oliviercailloux.y2018.apartments.apartment;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.google.common.math.DoubleMath;
import com.google.common.math.Stats;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import io.github.oliviercailloux.y2018.apartments.apartment.json.JsonConvert;


public class ApartmentStatitics {

  private static final Logger LOGGER = LoggerFactory.getLogger(Apartment.class);
  
  private static final HashSet<String> listOfFeatures = new HashSet<>(Arrays.asList("address", "description", "floorArea", "floorAreaTerrace", "nbBathrooms", "nbBedrooms", "nbMinNight","nbSleeping", "pricePerNight", "tele", "terrace", "title", "wifi"));
  private static final HashSet<String> listOfContinousFeatures = new HashSet<>(Arrays.asList("floorArea", "floorAreaTerrace","pricePerNight"));
  private static final HashSet<String> listOfDiscreteFeatures = new HashSet<>(Arrays.asList("nbBathrooms", "nbBedrooms", "nbMinNight","nbSleeping"));
  private static final HashSet<String> listOfBooleanFeatures = new HashSet<>(Arrays.asList("tele", "terrace", "wifi"));
  private static final HashSet<String> listOfStringFeatures = new HashSet<>(Arrays.asList("address","description","title"));
  
  private static List<Apartment> listOfApartments = JsonConvert.getDefaultApartments();
  
  /**
   * <p>
   * This method is able to return the statistics of all types of criteria (boolean, numeric, string, etc.)
   * </p>
   * <p>
   * <em>Note </em>: the first character of the criteria must be in lower case.
   * </p>
   * 
   * @param featureName : The name of the criteria thats we want to extract statistics.
   * @return The statistics of the given criteria 
   */
  
  public static String getStatisticsOf(String featureName) {
    
    checkNotNull(featureName);

    if (listOfContinousFeatures.contains(featureName) || listOfDiscreteFeatures.contains(featureName)) {
      
      LOGGER.info("{} is part of our Numeric criteria", featureName);
      HashMap<String, ArrayList<Double>> criteriaStats = getNumericData(featureName);
      String writtenStats = displayNumericStatistics(criteriaStats);
      return writtenStats;
    
    } else if (listOfBooleanFeatures.contains(featureName)) {
      
      LOGGER.info("{} is part of our Boolean criteria", featureName);
      ImmutableMap<String, Integer> criteriaStats = getBooleanData(featureName);
      String writtenStats = displayBooleanStatistics(criteriaStats);
      return writtenStats;

    } else if (listOfStringFeatures.contains(featureName)) {
      
      LOGGER.info("{} is part of our String criteria", featureName);
      return("No statistics are available for String criterias like "+featureName);
      
    } else {
      
      throw new IllegalArgumentException(featureName+" isn't one of our criteria");
      
    }
    
  }
  
  /**
   * <p>
   * Create a HashMap able to store the name of a criteria and its data in a ArrayList.
   * </p>
   * <p>
   * <em>Note </em>: the first character of the criteria must be in lower case.
   * </p>
   * 
   * @param featureName : The name of the criteria thats we want to extract statistics.
   * @return A HashMap (size 1) that has the name of the feature as its entry, and its data
   *         through a ArrayList as values of the Map.
   */
  
  public static HashMap<String, ArrayList<Double>> getNumericData(String featureName) {

    HashMap<String, ArrayList<Double>> criteriaStats = new HashMap<>(1);

    switch (featureName) {

      case "floorArea": {
        ArrayList<Double> floorAreaStats = new ArrayList<>();
        for (int i = 0; i < listOfApartments.size(); i++) {
          floorAreaStats.add(Math.round(listOfApartments.get(i).getFloorArea() * 100.0) / 100.0);
        }
        criteriaStats.put(featureName, floorAreaStats);
        LOGGER.info("Floor area statistics are available :\n");
        return criteriaStats;
      }

      case "floorAreaTerrace": {
        ArrayList<Double> floorAreaTerraceStats = new ArrayList<>();
        for (int i = 0; i < listOfApartments.size(); i++) {
          floorAreaTerraceStats.add(Math.round(listOfApartments.get(i).getFloorAreaTerrace() * 100.0) / 100.0);
        }
        criteriaStats.put(featureName, floorAreaTerraceStats);
        LOGGER.info("Floor area terrace statistics are available :\n");
        return criteriaStats;
      }

      case "nbBathrooms": {
        ArrayList<Double> nbBathroomsStats = new ArrayList<>();
        for (int i = 0; i < listOfApartments.size(); i++) {
          nbBathroomsStats.add(Double.valueOf(listOfApartments.get(i).getNbBathrooms()));
        }
        criteriaStats.put(featureName, nbBathroomsStats);
        LOGGER.info("Floor area terrace statistics are available :\n");
        return criteriaStats;
      }

      case "nbBedrooms": {
        ArrayList<Double> nbBedroomsStats = new ArrayList<>();
        for (int i = 0; i < listOfApartments.size(); i++) {
          nbBedroomsStats.add(Double.valueOf(listOfApartments.get(i).getNbBedrooms()));
        }
        criteriaStats.put(featureName, nbBedroomsStats);
        LOGGER.info("Number of bedrooms statistics are available  :\n");
        return criteriaStats;
      }

      case "nbMinNight": {
        ArrayList<Double> nbMinNightStats = new ArrayList<>();
        for (int i = 0; i < listOfApartments.size(); i++) {
          nbMinNightStats.add(Double.valueOf(listOfApartments.get(i).getNbMinNight()));
        }
        LOGGER.info("The requested minimum number of night statistics are available :\n");
        criteriaStats.put(featureName, nbMinNightStats);
        return criteriaStats;
      }

      case "nbSleeping": {
        ArrayList<Double> nbSleepingStats = new ArrayList<>();
        for (int i = 0; i < listOfApartments.size(); i++) {
          nbSleepingStats.add(Double.valueOf(listOfApartments.get(i).getNbSleeping()));
        }
        criteriaStats.put(featureName, nbSleepingStats);
        LOGGER.info("The number of sleepings statistics are available :\n");
        return criteriaStats;
      }

      case "pricePerNight": {
        ArrayList<Double> pricePerNightStats = new ArrayList<>();
        for (int i = 0; i < listOfApartments.size(); i++) {
          pricePerNightStats
              .add(Math.round(listOfApartments.get(i).getPricePerNight() * 100.0) / 100.0);
        }
        criteriaStats.put(featureName, pricePerNightStats);
        LOGGER.info("The price per night statistics are available :\n");
        return criteriaStats;
      }

      default:
        throw new IllegalArgumentException(featureName+" isn't a Apartment feature");
    }

  }

  /**
   * <p>
   * Create the Map that counts the occurrence of each boolean
   * </p>
   * 
   * @param featureName : The name of the criteria thats we want to extract data.
   */
  
  public static ImmutableMap<String, Integer> getBooleanData(String featureName) {

    HashMap<String, Integer> results = new HashMap<>();
    int yes = 0;
    int no = 0;

    switch (featureName) {

      case "tele": {
        for (int i = 0; i < listOfApartments.size(); i++) {
          if (listOfApartments.get(i).getTele()) {
            yes++;
          } else {
            no++;
          }
        }
        LOGGER.info("The tele presence statistics are available :\n");
      }
        break;
      
      case "terrace": {
        for (int i = 0; i < listOfApartments.size(); i++) {
          if (listOfApartments.get(i).getTerrace()) {
            yes++;
          } else {
            no++;
          }
        }
        LOGGER.info("The terrace presence statistics are available :\n");
      }
        break;
      
      case "wifi": {
        for (int i = 0; i < listOfApartments.size(); i++) {
          if (listOfApartments.get(i).getTerrace()) {
            yes++;
          } else {
            no++;
          }
        }
        LOGGER.info("The wifi presence statistics are available :\n");
      }
        break;

      default:
        throw new IllegalArgumentException(featureName+" isn't a Apartment feature");
    
    }
    results.put(featureName, yes);
    results.put("No "+featureName, no);
    return ImmutableMap.copyOf(results);

  }

  /**
   * <p>
   * Return the following statistics : the number of instances, its minimum, average, maximum and standard deviation.
   * </p>
   * 
   * @param A Map (size 1) that has the name of the feature as its entry, and its statistics through
   *        a ArrayList as values of the Map.
   * 
   * @return a short string describing this instance.
   */
  
  public static String displayNumericStatistics(HashMap<String, ArrayList<Double>> dataMap) {

    checkNotNull(dataMap);
    Entry<String, ArrayList<Double>> firstEntry = dataMap.entrySet().iterator().next();
    String feature = firstEntry.getKey();
    List<Double> listOfDoubles = firstEntry.getValue();
    Stats stats = Stats.of(listOfDoubles);

    LOGGER.info("\n\nSome statistics about {} :", feature);

    return ("\nNumber of apartments : " + stats.count() + 
            "\nMinimum : " + stats.min()+
            "\nMaximum : " + stats.max()+
            "\nAverage : " + Math.round(stats.mean() * 100) / 100d +
            "\nStandard deviation : "+Math.round(stats.populationStandardDeviation() * 100) / 100d +
            "\n");
  }

  /**
   * <p>
   * Return the total occurrence of the criteria for all apartments.
   * </p>
   * 
   * @param A ImmutableMap (size 1) that has the name of the feature as its entry, and its statistics through
   *        a DescriptiveStatistics as values of the Map.
   * 
   * @return a short string describing this instance.
   */
  
  public static String displayBooleanStatistics(ImmutableMap<String, Integer> criteriaStats) {

    checkNotNull(criteriaStats);
    List<String> modalities = new ArrayList<>();
    List<Integer> numbers = new ArrayList<>();
    
    for (Entry<String, Integer> s: criteriaStats.entrySet()) {
      modalities.add(s.getKey());
      numbers.add(s.getValue());
      }
    return ("\n"+modalities.get(0)+" : " + numbers.get(0) + 
            "\n"+modalities.get(1)+" : " + numbers.get(1) +
            "\n");
  
  }

  public static void main(String[] args) {

    HashMap<String, ArrayList<Double>> nbBedroomsStats = getNumericData("nbBedrooms");
    ImmutableMap<String, Integer> teleStats = getBooleanData("tele");

    System.out.println(displayBooleanStatistics(teleStats));
    System.out.println(displayNumericStatistics(nbBedroomsStats));
    System.out.println(displayNumericStatistics(getNumericData("nbMinNight")));
    System.out.println(getStatisticsOf("nbMinNight"));
    System.out.println(getStatisticsOf("pricePerNight"));
    System.out.println(getStatisticsOf("terrace"));

    
  }
}
