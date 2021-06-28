package io.github.oliviercailloux.y2018.apartments.valuefunction.profile;

import static com.google.common.base.Preconditions.checkArgument;

import io.github.oliviercailloux.y2018.apartments.valuefunction.Criterion;
import io.github.oliviercailloux.y2018.apartments.valuefunction.ApartementVF;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProfileManager {

  private static ProfileManager instance = null;
  HashMap<ProfileType, Profile> mapProfile;

  private ProfileManager() {
    this.mapProfile = new HashMap<>();
    ApartementVF studentLavf = new ApartementVF.Builder().setTeleValueFunction(true)
        .setTerraceValueFunction(false).setWifiValueFunction(false)
        .setFloorAreaTerraceValueFunction(0d, 10d).setFloorAreaValueFunction(16d, 70d)
        .setNbBathroomsValueFunction(1, 2).setNbBedroomsValueFunction(1, 2)
        .setNbSleepingValueFunction(1, 2).setNbMinNightValueFunction(5, 14)
        .setPricePerNightValueFunction(40d, 60d).setWeight(Criterion.PRICE_PER_NIGHT, 25d)
        .setWeight(Criterion.WIFI, 25d).setWeight(Criterion.TERRACE, 7.5d)
        .setWeight(Criterion.TELE, 7.5d).setWeight(Criterion.NB_SLEEPING, 7.5d)
        .setWeight(Criterion.FLOOR_AREA, 7.5d).setWeight(Criterion.NB_BEDROOMS, 5d)
        .setWeight(Criterion.NB_BATHROOMS, 5d).setWeight(Criterion.NB_MIN_NIGHT, 5d)
        .setWeight(Criterion.FLOOR_AREA_TERRACE, 5d).build();
    Profile student = new Profile.Builder().setWeightRange(Criterion.PRICE_PER_NIGHT, 15d, 35d)
        .setWeightRange(Criterion.WIFI, 15d, 35d).setWeightRange(Criterion.TERRACE, 0d, 15d)
        .setWeightRange(Criterion.TELE, 0d, 15d).setWeightRange(Criterion.NB_SLEEPING, 0d, 15d)
        .setWeightRange(Criterion.FLOOR_AREA, 0d, 15d)
        .setWeightRange(Criterion.NB_BEDROOMS, 0d, 10d)
        .setWeightRange(Criterion.NB_BATHROOMS, 0d, 10d)
        .setWeightRange(Criterion.NB_MIN_NIGHT, 0d, 10d)
        .setWeightRange(Criterion.FLOOR_AREA_TERRACE, 0d, 10d)
        .setQuestionPriceArea(QuestionPriceArea.create(15, 5)).setLinearAVF(studentLavf).build();

    ApartementVF familyLavf = new ApartementVF.Builder().setTeleValueFunction(true)
        .setTerraceValueFunction(true).setWifiValueFunction(true)
        .setFloorAreaTerraceValueFunction(10d, 50d).setFloorAreaValueFunction(60d, 200d)
        .setNbBathroomsValueFunction(2, 3).setNbBedroomsValueFunction(2, 6)
        .setNbSleepingValueFunction(5, 7).setNbMinNightValueFunction(7, 28)
        .setPricePerNightValueFunction(200d, 400d).setWeight(Criterion.PRICE_PER_NIGHT, 10d)
        .setWeight(Criterion.WIFI, 10d).setWeight(Criterion.TERRACE, 10d)
        .setWeight(Criterion.TELE, 10d).setWeight(Criterion.NB_SLEEPING, 12.5d)
        .setWeight(Criterion.FLOOR_AREA, 12.5d).setWeight(Criterion.NB_BEDROOMS, 12.5d)
        .setWeight(Criterion.NB_BATHROOMS, 12.5d).setWeight(Criterion.NB_MIN_NIGHT, 5d)
        .setWeight(Criterion.FLOOR_AREA_TERRACE, 5d).build();
    Profile family = new Profile.Builder().setWeightRange(Criterion.PRICE_PER_NIGHT, 0d, 20d)
        .setWeightRange(Criterion.WIFI, 0d, 20d).setWeightRange(Criterion.TERRACE, 0d, 20d)
        .setWeightRange(Criterion.TELE, 0d, 20d).setWeightRange(Criterion.NB_SLEEPING, 2.5d, 22.5d)
        .setWeightRange(Criterion.FLOOR_AREA, 2.5d, 22.5d)
        .setWeightRange(Criterion.NB_BEDROOMS, 2.5d, 22.5d)
        .setWeightRange(Criterion.NB_BATHROOMS, 2.5d, 22.5d)
        .setWeightRange(Criterion.NB_MIN_NIGHT, 0d, 10d)
        .setWeightRange(Criterion.FLOOR_AREA_TERRACE, 0d, 10d)
        .setQuestionPriceArea(QuestionPriceArea.create(100, 10)).setLinearAVF(familyLavf).build();

    ApartementVF coupleLavf = new ApartementVF.Builder().setTeleValueFunction(true)
        .setTerraceValueFunction(false).setWifiValueFunction(true)
        .setFloorAreaTerraceValueFunction(0d, 10d).setFloorAreaValueFunction(30d, 100d)
        .setNbBathroomsValueFunction(1, 2).setNbBedroomsValueFunction(1, 3)
        .setNbSleepingValueFunction(2, 3).setNbMinNightValueFunction(2, 21)
        .setPricePerNightValueFunction(50d, 150d).setWeight(Criterion.PRICE_PER_NIGHT, 16d)
        .setWeight(Criterion.WIFI, 16d).setWeight(Criterion.TERRACE, 16d)
        .setWeight(Criterion.TELE, 10d).setWeight(Criterion.NB_SLEEPING, 5.5d)
        .setWeight(Criterion.FLOOR_AREA, 10d).setWeight(Criterion.NB_BEDROOMS, 10d)
        .setWeight(Criterion.NB_BATHROOMS, 5.5d).setWeight(Criterion.NB_MIN_NIGHT, 5.5d)
        .setWeight(Criterion.FLOOR_AREA_TERRACE, 5.5d).build();
    Profile couple = new Profile.Builder().setWeightRange(Criterion.PRICE_PER_NIGHT, 6d, 26d)
        .setWeightRange(Criterion.WIFI, 6d, 26d).setWeightRange(Criterion.TERRACE, 6d, 26d)
        .setWeightRange(Criterion.TELE, 0d, 20d).setWeightRange(Criterion.NB_SLEEPING, 0d, 11d)
        .setWeightRange(Criterion.FLOOR_AREA, 0d, 20d)
        .setWeightRange(Criterion.NB_BEDROOMS, 0d, 20d)
        .setWeightRange(Criterion.NB_BATHROOMS, 0d, 11d)
        .setWeightRange(Criterion.NB_MIN_NIGHT, 0d, 11d)
        .setWeightRange(Criterion.FLOOR_AREA_TERRACE, 0d, 11d)
        .setQuestionPriceArea(QuestionPriceArea.create(90, 15)).setLinearAVF(coupleLavf).build();

    this.mapProfile.put(ProfileType.STUDENT, student);
    this.mapProfile.put(ProfileType.FAMILY, family);
    this.mapProfile.put(ProfileType.COUPLE, couple);
  }

  public static ProfileManager getInstance() {
    if (instance == null) {
      instance = new ProfileManager();
    }

    return instance;
  }

  public Profile getProfile(ProfileType profType) {
    checkArgument(mapProfile.containsKey(profType));
    return getInstance().mapProfile.get(profType);
  }

  public List<ProfileType> getAvailableProfileTypes() {
    return new ArrayList<>(mapProfile.keySet());
  }
}
