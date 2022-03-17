package io.github.oliviercailloux.apartments.valuefunction.profile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.common.collect.Range;
import io.github.oliviercailloux.apartments.valuefunction.ApartementVF;
import io.github.oliviercailloux.apartments.valuefunction.Criterion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProfileTests {

  Profile profile;

  /** Inits the profile object before each tests */
  @BeforeEach
  void initEach() {
    Profile.Builder profileBuilder = new Profile.Builder();
    ApartementVF.Builder blavf =
        new ApartementVF.Builder().setTeleValueFunction(true).setTerraceValueFunction(true)
            .setWifiValueFunction(true).setFloorAreaTerraceValueFunction(10d, 20d)
            .setFloorAreaValueFunction(50d, 100d).setNbBathroomsValueFunction(2, 4)
            .setNbBedroomsValueFunction(2, 4).setNbSleepingValueFunction(2, 4)
            .setNbMinNightValueFunction(5, 10).setPricePerNightValueFunction(30d, 60d);
    for (Criterion c : Criterion.values()) {
      blavf.setWeight(c, 2d);
    }
    ApartementVF lavf = blavf.build();

    for (Criterion c : Criterion.values()) {
      profileBuilder.setWeightRange(c, Range.closed(0d, 10d));
    }

    profileBuilder.setLinearAVF(lavf);
    profileBuilder.setQuestionPriceArea(QuestionPriceArea.create(50, 100));
    profile = profileBuilder.build();
  }

  /** Function to test the basic Profile implementation */
  @Test
  void testProfile() {
    assertEquals(2d, profile.getLinearAVF().getWeight(Criterion.FLOOR_AREA), 0.0001);
    assertEquals(Range.closed(0d, 10d), profile.getWeightRange(Criterion.NB_BEDROOMS));
  }

  /** Function to test the Profile builder */
  @Test
  void testBuilder() {
    Profile.Builder profileBuilder = new Profile.Builder();
    assertThrows(IllegalArgumentException.class,
        () -> profileBuilder.setWeightRange(Criterion.NB_SLEEPING, 10d, 0d));
  }
}
