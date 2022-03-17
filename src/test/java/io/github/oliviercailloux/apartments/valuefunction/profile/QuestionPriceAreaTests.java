package io.github.oliviercailloux.apartments.valuefunction.profile;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.collect.Range;
import io.github.oliviercailloux.apartments.valuefunction.Criterion;
import org.junit.jupiter.api.Test;

public class QuestionPriceAreaTests {

  /** Verifying the initialization of the object QuestionPriceArea. */
  @Test
  void testInitialization() {
    QuestionPriceArea q = QuestionPriceArea.create(100, 10);
    assertEquals(100, q.getPrice());
    assertEquals(10, q.getSurface());
    assertEquals("Would you pay 100€ more for 10 m2 more?", q.getQuestion());
  }

  /**
   * Test if the resolve method works well. The code in comment needs the class Profile first to be
   * merge to work correctly
   */
  @Test
  void testResolve() {
    QuestionPriceArea question = QuestionPriceArea.create(15, 5);
    Profile profile = ProfileManager.getInstance().getProfile(ProfileType.STUDENT);
    Profile profile1 = question.resolve(profile, false);
    assertEquals(0.1234d, profile1.getWeightRange(Criterion.FLOOR_AREA).upperEndpoint(), 0.0001);
    assertEquals(0.0d, profile1.getWeightRange(Criterion.FLOOR_AREA).lowerEndpoint());
    Profile profile2 = question.resolve(profile, true);
    assertEquals(Range.closed(8.1d, 15.0d), profile2.getWeightRange(Criterion.FLOOR_AREA));
    assertEquals(15.0d, profile2.getWeightRange(Criterion.FLOOR_AREA).upperEndpoint(), 0.0001);
    assertEquals(8.1d, profile2.getWeightRange(Criterion.FLOOR_AREA).lowerEndpoint(), 0.0001);
  }
}
