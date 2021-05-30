package io.github.oliviercailloux.y2018.apartments.gui;

import io.github.oliviercailloux.y2018.apartments.apartment.Apartment;
import io.github.oliviercailloux.y2018.apartments.apartment.json.JsonConvert;
import io.github.oliviercailloux.y2018.apartments.valuefunction.LinearAVF;
import java.util.ArrayList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** this class displays a list of apartments sorted according to the user's utilities */
public class LayoutApartmentGUI {

  private static final Logger LOGGER = LoggerFactory.getLogger(LayoutApartmentGUI.class);

  java.util.List<Apartment> listApp;
  LinearAVF linearAVF;

  Display display = new Display();
  Shell shell = new Shell(display);

  /** add objects of apartments in a listShell */
  final List listShell = new List(shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);

  public LayoutApartmentGUI(LinearAVF newLinearAVF) {
    this.linearAVF = newLinearAVF;
    this.listApp = getListSorted(linearAVF);
  }

  /**
   * function permitting to run the GUI
   *
   * @param lavf LinearAVF to use to sort apartments
   */
  public static void process(LinearAVF lavf) {
    LayoutApartmentGUI gui = new LayoutApartmentGUI(lavf);
    gui.displayAppart();
  }

  /**
   * Method that creates a list of random apartments and tries them according to the utility of the
   * user
   *
   * @param linearAVF a way to rate the apartments
   */
  private static java.util.List<Apartment> getListSorted(LinearAVF linearAVF) {
    java.util.List<Apartment> listApartment = JsonConvert.getDefaultApartments();
    java.util.List<Apartment> appart = new ArrayList<>();

    for (int i = 0; i < 49; i++) {
      appart.add(listApartment.get(i));
    }
    appart.sort((Apartment c, Apartment d) -> -Double.compare(linearAVF.getSubjectiveValue(c),
        linearAVF.getSubjectiveValue(d)));

    return appart;
  }

  /** General method which displays all the sorted apartment */
  public void displayAppart() {

    addAppinListShell();

    shell.setText("Filtered results");

    // create a gridLayout of 3 columns
    GridLayout gridLayout = new GridLayout();
    gridLayout.numColumns = 3;
    shell.setLayout(gridLayout);

    // create a griddata for a list of objects
    GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
    gridData.horizontalSpan = 2;

    // create a list label
    Label label = new Label(shell, SWT.NULL);
    label.setText("List of available apartments :");
    label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));

    // define griddata with a verticalspan : 3 rows
    gridData = new GridData(GridData.FILL_BOTH);
    gridData.verticalSpan = 3;

    gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
    gridData.verticalSpan = 4;
    gridData.heightHint = 400;
    listShell.setLayoutData(gridData);

    Group appartInfo = new Group(shell, SWT.NULL);
    appartInfo.setText("Details on selected apartment :");

    gridLayout = new GridLayout();
    gridLayout.numColumns = 2;
    appartInfo.setLayout(gridLayout);

    gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
    gridData.horizontalSpan = 2;
    appartInfo.setLayoutData(gridData);

    // beginning of creation of elements to display when we click on an apartment
    Label adresse;
    Label surface;
    Label prix;
    Label nbrChambres;
    Label wifi;
    Label tv;
    Label terrace;
    Label nbBathroom;

    Label wifiLabel;
    Label tvLabel;
    Label terraceLabel;
    Label nbBathroomLabel;

    GridData dataLabelAddress = new GridData(GridData.FILL_HORIZONTAL);
    dataLabelAddress.widthHint = 250;
    dataLabelAddress.heightHint = 45;

    GridData dataLabel = new GridData(GridData.FILL_HORIZONTAL);
    dataLabel.widthHint = 250;

    GridData dataLabelHide = new GridData(GridData.FILL_HORIZONTAL);
    dataLabelHide.widthHint = 250;

    new Label(appartInfo, SWT.NULL).setText("Address :");
    adresse = new Label(appartInfo, SWT.SINGLE | SWT.BORDER);
    adresse.setLayoutData(dataLabelAddress);

    new Label(appartInfo, SWT.NULL).setText("Surface :");
    surface = new Label(appartInfo, SWT.SINGLE | SWT.BORDER);
    surface.setLayoutData(dataLabel);

    new Label(appartInfo, SWT.NULL).setText("Price :");
    prix = new Label(appartInfo, SWT.SINGLE | SWT.BORDER);
    prix.setLayoutData(dataLabel);

    new Label(appartInfo, SWT.NULL).setText("Number of bedrooms :");
    nbrChambres = new Label(appartInfo, SWT.SINGLE | SWT.BORDER);
    nbrChambres.setLayoutData(dataLabel);

    nbBathroomLabel = new Label(appartInfo, SWT.NULL);
    nbBathroomLabel.setText("Number of bathrooms :");
    nbBathroomLabel.setVisible(false);
    nbBathroom = new Label(appartInfo, SWT.SINGLE | SWT.BORDER);
    nbBathroom.setLayoutData(dataLabelHide);
    nbBathroom.setVisible(false);

    wifiLabel = new Label(appartInfo, SWT.NULL);
    wifiLabel.setText("Wifi :");
    wifiLabel.setVisible(false);
    wifi = new Label(appartInfo, SWT.SINGLE | SWT.BORDER);
    wifi.setLayoutData(dataLabelHide);
    wifi.setVisible(false);

    tvLabel = new Label(appartInfo, SWT.NULL);
    tvLabel.setText("Tv :");
    tvLabel.setVisible(false);
    tv = new Label(appartInfo, SWT.SINGLE | SWT.BORDER);
    tv.setLayoutData(dataLabelHide);
    tv.setVisible(false);

    terraceLabel = new Label(appartInfo, SWT.NULL);
    terraceLabel.setText("Terrace :");
    terraceLabel.setVisible(false);
    terrace = new Label(appartInfo, SWT.SINGLE | SWT.BORDER);
    terrace.setLayoutData(dataLabelHide);
    terrace.setVisible(false);

    gridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
    gridData.horizontalSpan = 3;
    onClick(adresse, surface, prix, nbrChambres, nbBathroom, wifi, tv, terrace);

    Button button = new Button(shell, SWT.NONE);
    button.setText("View more");
    button.setSize(100, 25);
    button.addListener(SWT.Selection, event -> {
      if (button.getText().equals("View more")) {
        nbBathroomLabel.setVisible(true);
        wifiLabel.setVisible(true);
        tvLabel.setVisible(true);
        terraceLabel.setVisible(true);
        nbBathroom.setVisible(true);
        wifi.setVisible(true);
        tv.setVisible(true);
        terrace.setVisible(true);

        dataLabelHide.exclude = false;
        appartInfo.pack();

        button.setText("Hide fields");
      } else {
        nbBathroomLabel.setVisible(false);
        wifiLabel.setVisible(false);
        tvLabel.setVisible(false);
        terraceLabel.setVisible(false);
        nbBathroom.setVisible(false);
        wifi.setVisible(false);
        tv.setVisible(false);
        terrace.setVisible(false);

        dataLabelHide.exclude = true;

        button.setText("View more");
      }
    });

    shell.setSize(1180, 550);
    this.centerShellInWindow();
    shell.open();
    LOGGER.info("The Shell was opened with success.");

    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
    LOGGER.info("The screen was closed with success.");
  }

  /** Method that adds available apartments in the shell list to display */
  public void addAppinListShell() {
    for (Apartment a : listApp) {
      listShell.add("Title: " + a.getTitle() + "\t" + " Address : " + a.getAddress());
    }
  }

  /**
   * Method that defines the action to execute when clicking on an apartment, here we display some
   * elements of the apartment
   *
   * @param address the address label
   * @param floorArea the floorArea
   * @param price the price
   * @param nbBedrooms the number of bedrooms
   * @param nbBathroom the number of bathrooms
   * @param wifi the label for Wifi
   * @param tv the label for TV
   * @param terrace the label for Terrace
   */
  private void onClick(Label address, Label floorArea, Label price, Label nbBedrooms,
      Label nbBathroom, Label wifi, Label tv, Label terrace) {
    SelectionAdapter selectApp = new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent event) {
        int[] selectedItems = listShell.getSelectionIndices();

        for (int loopIndex = 0; loopIndex < selectedItems.length; loopIndex++) {
          address
              .setText(listApp.get(listShell.getSelectionIndex()).getAddress().replace(", ", "\n"));
          floorArea.setText(
              " " + Math.round(listApp.get(listShell.getSelectionIndex()).getFloorArea()) + "m²");
          price.setText(" "
              + Math.round(listApp.get(listShell.getSelectionIndex()).getPricePerNight()) + "€");
          nbBedrooms.setText(" " + listApp.get(listShell.getSelectionIndex()).getNbBedrooms());
          nbBathroom.setText(" " + listApp.get(listShell.getSelectionIndex()).getNbBathrooms());
          wifi.setText(" " + listApp.get(listShell.getSelectionIndex()).getWifi());
          tv.setText(" " + listApp.get(listShell.getSelectionIndex()).getTele());
          terrace.setText(" " + listApp.get(listShell.getSelectionIndex()).getTerrace());
        }
      }
    };
    listShell.addSelectionListener(selectApp);
  }

  /** Permit to center the GUI in the screen */
  private void centerShellInWindow() {
    int x = (display.getClientArea().width - shell.getSize().x) / 2;
    int y = (display.getClientArea().height - shell.getSize().y) / 2;
    shell.setLocation(x, y);
  }
}
