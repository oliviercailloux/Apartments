package io.github.oliviercailloux.y2018.apartments.gui;

import io.github.oliviercailloux.y2018.apartments.valuefunction.profile.ProfileManager;
import io.github.oliviercailloux.y2018.apartments.valuefunction.profile.ProfileType;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is a class that will allow us to demonstrate. We ask a few questions to the user at
 * the beginning, we adapt his utility then we show him a list of apartments according to his
 * preferences
 */
public class ProfileGUI {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProfileGUI.class);
  private static final int MARGIN_HORIZONTAL = 50;
  private static final int MARGIN_VERTICAL = 50;
  private static final int MARGIN_IMAGES = 50;
  private static final int IMAGES_WIDTH = 200;
  private static final int IMAGES_HEIGHT = 250;
  private static final List<ProfileType> profileTypesAvailable =
      ProfileManager.getInstance().getAvailableProfileTypes();
  Display display;
  Shell shell;
  private ProfileType selectedProfile = null;

  /** Constuctor initializing a centered Shell */
  public ProfileGUI() {
    this.display = new Display();
    this.shell = new Shell(display);
    shell.setText("Profile selection - Questions");
    shell.setLayout(new GridLayout());
  }

  /**
   * function permitting to run the GUI
   *
   * @throws IOException if ressources images are not found
   */
  public static void process() throws IOException {
    ProfileGUI gui = new ProfileGUI();
    gui.askForProfile();
    if (gui.selectedProfile == null) {
      LOGGER.info("L'Opération a été annulée. \n Fin du ProfileGUI");
      return;
    }
    ProfileQuestionGUI.process(gui.selectedProfile);
  }

  /**
   * This is the main function, it asks Questions , AdaptAnswers and then displays the list of
   * Apartments
   *
   * @param args arguments given to the program
   */
  public static void main(String[] args) throws IOException {
    ProfileGUI.process();
  }

  /**
   * This function will create and display the window (interface) to ask the user questions and to
   * determine later the weight of his choices
   */
  public void askForProfile() throws IOException {
    RowLayout rowLayout = new RowLayout(SWT.HORIZONTAL);
    rowLayout.center = true;
    rowLayout.marginLeft = MARGIN_HORIZONTAL;
    rowLayout.marginRight = MARGIN_HORIZONTAL;
    rowLayout.marginTop = MARGIN_VERTICAL;
    rowLayout.marginBottom = MARGIN_VERTICAL;
    rowLayout.spacing = MARGIN_IMAGES;
    shell.setLayout(rowLayout);
    for (int i = 0; i < profileTypesAvailable.size(); i++) {
      final int index = i;
      final String profileName = profileTypesAvailable.get(i).name().substring(0, 1).toUpperCase()
          + profileTypesAvailable.get(i).name().substring(1).toUpperCase();
      try (InputStream f = ProfileGUI.class.getResourceAsStream(profileName + "Subtitle.png")) {
        Image image = new Image(this.display,
            new Image(this.display, f).getImageData().scaledTo(IMAGES_WIDTH, IMAGES_HEIGHT));

        Button b = new Button(this.shell, SWT.PUSH);
        b.setBackground(new Color(display, new RGB(220, 220, 220), 0));
        b.setImage(image);

        Listener selectionlistener = event -> {
          shell.close();
          selectedProfile = profileTypesAvailable.get(index);
          LOGGER.info("Open Question GUI with Profile {}", profileName);
        };
        b.addListener(SWT.Selection, selectionlistener);
      }
    }
    shell.setBackground(new Color(display, new RGB(180, 180, 180), 0));
    shell.pack();
    this.centerShellInWindow();
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
    display.dispose();
    LOGGER.info("The screen was closed with success.");
  }

  /** Permit to center the GUI in the screen */
  private void centerShellInWindow() {
    int x = (display.getClientArea().width - shell.getSize().x) / 2;
    int y = (display.getClientArea().height - shell.getSize().y) / 2;
    shell.setLocation(x, y);
  }
}
