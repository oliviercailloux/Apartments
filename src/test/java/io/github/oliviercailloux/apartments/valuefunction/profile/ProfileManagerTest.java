package io.github.oliviercailloux.apartments.valuefunction.profile;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

public class ProfileManagerTest {

  /** Function to test the ProfileManager implementation */
  @Test
  void testProfileManager() {
    assertDoesNotThrow(() -> ProfileManager.getInstance());
  }
}
