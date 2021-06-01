package io.github.oliviercailloux.y2018.apartments.gui;

import io.github.oliviercailloux.y2018.apartments.iconDisplay.DisplayIcon;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class which enables the user to enter information about an apartment and to record it in a XML
 * File (name given by the user in the constructor) thanks to a GUI.
 */
public class CreateApartmentGUI extends FormApartmentGUI {

  private static final Logger LOGGER = LoggerFactory.getLogger(CreateApartmentGUI.class);

  /**
   * Initializes the constructor of the class.
   *
   * @param fileCompleteName String : name of the file the user wants to put Apartment information.
   */
  public CreateApartmentGUI(String fileCompleteName) {
    super();
    this.file = new File(fileCompleteName);
    this.titlePanel = "Create an Apartment";
    LOGGER.info("The GUI was initialized with success.");
  }

  /**
   * @param args must contains as first parameter the complete name of the file (Full Path).
   * @throws IOException
   */
  public static void main(String args[]) throws IOException {
    CreateApartmentGUI c;
    if (args[0] == null)
      c = new CreateApartmentGUI("GUITest");
    else
      c = new CreateApartmentGUI(args[0]);
    c.screenDisplay();
  }

  /**
   * General method which displays all the element of the GUI.
   *
   * @throws IOException if the logo doesn't load well.
   */
  protected void screenDisplay() throws IOException {
    try (InputStream f = DisplayIcon.class.getResourceAsStream("logo.png")) {

      LOGGER.info("The logo has been loaded with success.");
      FillLayout r = new FillLayout();
      r.type = SWT.VERTICAL;
      shell.setLayout(r);
      Image i = new Image(display, f);
      shell.setImage(i);
      shell.setText("Apartments");

      createPageTitle();
      createForm();

      shell.pack();
      shell.setMinimumSize(600, 700);
      shell.setSize(600, 700);

      shell.open();
      LOGGER.info("The Shell was opened with success.");

      while (!shell.isDisposed()) {
        if (!display.readAndDispatch())
          display.sleep();
      }
      i.dispose();
      display.dispose();
      LOGGER.info("The screen was closed with success.");
    }
  }
}
