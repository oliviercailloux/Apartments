package io.github.oliviercailloux.y2018.apartments.gui;

import io.github.oliviercailloux.y2018.apartments.apartment.Apartment;
import io.github.oliviercailloux.y2018.apartments.apartment.Apartment.Builder;
import io.github.oliviercailloux.y2018.apartments.apartment.xml.XMLProperties;
import java.io.File;
import java.io.FileOutputStream;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** This class enables the user to create a form for creating/modifying an apartment. */
public class FormApartmentGUI {
  protected static Display display = new Display();
  protected static Shell shell = new Shell(display);
  protected Text title;
  protected Text address;
  protected Text floorArea;
  protected Text nbBedrooms;
  protected Text nbSleeping;
  protected Text nbBathrooms;
  protected Button terrace;
  protected Text floorAreaTerrace;
  protected Text pricePerNight;
  protected Text nbMinNight;
  protected Text description;
  protected Button wifi;
  protected Button tele;
  protected File file;
  protected Composite information;
  protected Apartment apart;

  public Color okColor = new Color(display, 119, 197, 110);
  public Color alertColor = new Color(display, 255, 200, 200);
  public Color normalColor = new Color(display, 255, 255, 255);

  protected String titlePanel;

  private static final Logger LOGGER = LoggerFactory.getLogger(FormApartmentGUI.class);

  /** Creates the form in adding different composites for inserting Apartment information. */
  protected void createForm() {
    title = createFormFieldComposite("Title of the apartment*: ");
    address = createFormFieldComposite("Address*: ");
    floorArea = createFormFieldComposite("Floor Area*:");
    nbBedrooms = createFormFieldComposite("Number of bedrooms: ");
    nbSleeping = createFormFieldComposite("Sleeping capacity: ");
    nbBathrooms = createFormFieldComposite("Number of bathrooms: ");
    terrace = createCheckboxComposite("Terrace: ");
    floorAreaTerrace = createFormFieldComposite("Floor area terrace: ");
    pricePerNight = createFormFieldComposite("Price per night: ");
    nbMinNight = createFormFieldComposite("Minimum nights to stay: ");
    wifi = createCheckboxComposite("WiFi: ");
    tele = createCheckboxComposite("Television: ");
    description = createFormFieldComposite("Description:");
    information = createCompositeInformation();
    terrace.addListener(SWT.Selection, new Listener() {
      @Override
      public void handleEvent(Event e) {
        Button b = (Button) e.widget;
        if (b.getSelection()) {
          floorAreaTerrace.setEditable(true);
          floorAreaTerrace.setVisible(true);
        } else {
          floorAreaTerrace.setEditable(false);
          floorAreaTerrace.setVisible(false);
        }
        informationToFile();
      }
    });
    validationField();
  }

  /**
   * validationREquiredField initialize the listener for every component (Checkbox and Text) and
   * execute the informationToFile routine in order to verify if every textField respect its own
   * format.
   */
  private void validationField() {
    Listener textVerification = new Listener() {
      @Override
      public void handleEvent(Event e) {
        Text t = (Text) e.widget;
        if (!t.getText().isEmpty()) {
          t.setBackground(new Color(display, 255, 255, 255));
        } else {
          Label l = (Label) t.getParent().getChildren()[0];
          if (l.getText().contains("*"))
            t.setBackground(new Color(display, 255, 200, 200));
        }
        informationToFile();
      }
    };

    Listener selectionListener = new Listener() {
      @Override
      public void handleEvent(Event arg0) {
        informationToFile();
      }
    };

    title.addListener(SWT.KeyUp, textVerification);
    address.addListener(SWT.KeyUp, textVerification);
    floorArea.addListener(SWT.KeyUp, textVerification);
    floorAreaTerrace.addListener(SWT.KeyUp, textVerification);
    nbBedrooms.addListener(SWT.KeyUp, textVerification);
    nbSleeping.addListener(SWT.KeyUp, textVerification);
    nbBathrooms.addListener(SWT.KeyUp, textVerification);
    pricePerNight.addListener(SWT.KeyUp, textVerification);
    nbMinNight.addListener(SWT.KeyUp, textVerification);
    wifi.addListener(SWT.Selection, selectionListener);
    tele.addListener(SWT.Selection, selectionListener);
    description.addListener(SWT.KeyUp, textVerification);
  }

  /**
   * informationToFile is a method which verifies first the required fields first before writing
   * into the xml file. In a second hand, it gets the checkbox status and verify every text field
   * who must have a integer or a decimal number. If the user doesn't respect the format of a field,
   * it won't be written into the file, the field is clear and highlight in red.
   */
  protected void informationToFile() {
    if (!verificationText(floorArea, TypeButtonText.DOUBLE)
        || !verificationText(floorArea, TypeButtonText.REQUIRED)
        || !verificationText(title, TypeButtonText.REQUIRED)
        || !verificationText(address, TypeButtonText.REQUIRED)) {
      loadMessage(MessageInfo.REQUIRED, "Title, Address and Floor Area are required");
    } else if (!verificationText(nbBedrooms, TypeButtonText.INT)
        || !verificationText(nbMinNight, TypeButtonText.INT)
        || !verificationText(nbSleeping, TypeButtonText.INT)
        || !verificationText(nbBathrooms, TypeButtonText.INT)
        || !verificationText(pricePerNight, TypeButtonText.DOUBLE)) {
      loadMessage(MessageInfo.ERROR, "There is an error with the informations given !");
    } else if ((terrace.getSelection()
        && !verificationText(floorAreaTerrace, TypeButtonText.DOUBLE))) {
      loadMessage(MessageInfo.ERROR, "Floor Area Terrace should not be empty !");
    } else {
      Builder apartBuilder = new Builder();
      apart = apartBuilder.setFloorArea(Double.parseDouble(floorArea.getText()))
          .setAddress(address.getText()).setNbBedrooms(Integer.parseInt(nbBedrooms.getText()))
          .setNbSleeping(Integer.parseInt(nbSleeping.getText()))
          .setNbBathrooms(Integer.parseInt(nbBathrooms.getText()))
          .setTerrace(terrace.getSelection())
          .setFloorAreaTerrace(Double.parseDouble(floorAreaTerrace.getText()))
          .setDescription(description.getText()).setTitle(title.getText())
          .setWifi(wifi.getSelection())
          .setPricePerNight(Double.parseDouble(pricePerNight.getText()))
          .setTele(tele.getSelection()).build();
      write(apart);
      loadMessage(MessageInfo.SAVED, "Apartment have been saved !");
      System.out.println("saved");
    }
  }

  /**
   * This method attributes a color to the information window and depends on the MessageInfo type.
   *
   * @param type MessageInfo defined in the class MessageInfo for the alert types of a message
   * @param message String which corresponds to the message linked with the MessageInfo object
   */
  protected void loadMessage(MessageInfo type, String message) {
    Color color = alertColor;
    switch (type) {
      case REQUIRED:
        color = alertColor;
        break;
      case SAVED:
        color = okColor;
        break;
      case LOAD:
        color = okColor;
        break;
      case ERROR:
        color = alertColor;
        break;
      default:
        break;
    }
    information.setBackground(color);
    Label l = (Label) information.getChildren()[0];
    l.setBackground(color);
    l.setText(message);
  }

  /**
   * Creates a composite (in the GUI) which contains a Text box.
   *
   * @param label String which corresponds to the the title of the Text box.
   * @return Text containing the information of the Text box.
   */
  private Text createFormFieldComposite(String label) {
    Composite c = new Composite(shell, SWT.PUSH);

    GridLayout f = new GridLayout(2, false);
    c.setLayout(f);
    GridData a = new GridData(SWT.FILL, SWT.NONE, true, false);
    a.minimumWidth = SWT.FILL;
    a.horizontalAlignment = SWT.NONE;
    a.widthHint = 200;
    Label lb = new Label(c, SWT.FILL);
    lb.setText(label);
    lb.setLayoutData(a);
    Text t = new Text(c, SWT.FILL);
    t.setText("");
    t.setLayoutData(a);

    if (label.equalsIgnoreCase("Floor area terrace: "))
      t.setEditable(false);
    if (label.contains("*") && (t.getText().equals(" ") || !t.getText().isEmpty()))
      t.setBackground(alertColor);

    shell.pack();
    LOGGER.info("The Composite " + label + " was created.");
    return t;
  }

  /**
   * Verifies that the data in parameters are in the good type. Change the background color of the
   * Text Box if the type is not correct.
   *
   * @param text Text (data to analyze)
   * @param type TypeButtonText (type required for the @param text)
   * @return a boolean (true if the type is correct) false if wrong or empty
   */
  private boolean verificationText(Text text, TypeButtonText type) {

    switch (type) {
      case INT:
        if (!text.getText().trim().equals("")) {
          try {
            Integer.parseInt(text.getText());
            text.setBackground(normalColor);
            return true;
          } catch (NumberFormatException e) {
            text.setText("");
            text.setBackground(alertColor);
            LOGGER.error("The argument set is not valid " + e.getMessage());
          }
        }
        break;
      case DOUBLE:
        if (!text.getText().equals("")) {
          try {
            Double.parseDouble(text.getText());
            text.setBackground(normalColor);
            return true;
          } catch (NumberFormatException e) {
            text.setText("");
            Label l = (Label) text.getParent().getChildren()[0];
            loadMessage(MessageInfo.ERROR, "Error : "
                + l.getText().substring(0, l.getText().length() - 2) + "have not a valid format");
            LOGGER.error("The argument set is not valid " + e.getMessage());
          }
        }
        break;
      case REQUIRED:
        if (!text.getText().isEmpty()) {
          text.setBackground(normalColor);
          return true;
        }
        text.setBackground(alertColor);
        break;
      default:
        break;
    }

    return false;
  }

  /**
   * Creates a composite (in the GUI) which contains a check box.
   *
   * @param label String which corresponds to the the title of the check box.
   * @return Button for the validation of data.
   */
  private Button createCheckboxComposite(String label) {
    Composite c = new Composite(shell, SWT.PUSH);
    GridLayout f = new GridLayout(2, false);
    c.setLayout(f);
    GridData a = new GridData(SWT.FILL, SWT.NONE, true, false);
    a.minimumWidth = SWT.FILL;
    a.horizontalAlignment = SWT.NONE;
    a.widthHint = 200;
    Label lb = new Label(c, SWT.FILL);
    lb.setText(label);
    lb.setLayoutData(a);
    Button t = new Button(c, SWT.CHECK);
    t.setText("");
    t.setLayoutData(a);

    shell.pack();
    LOGGER.info("The Composite " + label + " was created.");
    return t;
  }

  /**
   * Insert Apartment data inserted in the GUI, in a XML File. The name of the file is in the
   * parameters of the class.
   *
   * @param a Object Apartment
   */
  private void write(Apartment a) {
    XMLProperties xmlFile = new XMLProperties();
    try (FileOutputStream s = new FileOutputStream(file.getAbsolutePath())) {
      xmlFile.toXML(a, s);
    } catch (Exception e) {
      MessageDialog.openError(shell, "Error",
          "Insertion Problem in the XML File\n\nTry to restart the app");
      LOGGER.error("Error while inserting data into XML File" + e.getMessage());
      throw new IllegalStateException(e);
    }
  }

  /** Initializes the top title of the GUI instance. */
  protected void createPageTitle() {
    Composite compoForTitle = new Composite(shell, SWT.NONE);
    GridLayout gl = new GridLayout(1, true);
    compoForTitle.setLayout(gl);
    Label titleLb = new Label(compoForTitle, SWT.FILL | SWT.NONE);
    titleLb.setText(titlePanel);
    GridData a = new GridData(SWT.FILL, SWT.NONE, true, false);
    a.minimumWidth = SWT.FILL;
    a.horizontalAlignment = SWT.NONE;
    a.widthHint = 200;
    titleLb.setLayoutData(a);
    LOGGER.info("The Composite of the header was created.");
  }

  /**
   * This method builds a composite object which contains the validation information of the form.
   *
   * @return a Composite Object of the information window.
   */
  private Composite createCompositeInformation() {
    Composite compo = new Composite(shell, SWT.NONE);

    GridLayout f = new GridLayout(1, false);
    compo.setLayout(f);
    compo.setSize(200, 200);
    compo.setBackground(okColor);
    GridData a = new GridData(SWT.FILL, SWT.NONE, true, false);
    a.minimumWidth = SWT.FILL;
    a.horizontalAlignment = SWT.NONE;
    a.widthHint = 300;
    Label lb = new Label(compo, SWT.FILL);
    lb.setText("Everything is loaded !");
    lb.setBackground(okColor);
    lb.setLayoutData(a);
    shell.pack();
    return compo;
  }
}
