package io.github.oliviercailloux.y2018.apartments.gui;

import com.google.common.base.Preconditions;
import io.github.oliviercailloux.y2018.apartments.valuefunction.ApartmentValueFunction;
import io.github.oliviercailloux.y2018.apartments.valuefunction.Criterion;
import io.github.oliviercailloux.y2018.apartments.valuefunction.LinearValueFunction;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author SALAME & SAKHO */

/**
 * This class is a class that will allow us to demonstrate. We ask a few questions to the user at
 * the beginning, we adapt his utility then we show him a list of apartments according to his
 * preferences
 */
public class AskOpinionForUtility {

  private static final Logger LOGGER = LoggerFactory.getLogger(AskOpinionForUtility.class);

  /** To move for questions */
  int pointer = 0;

  /** Keeps the user’s answers (when he presses the button) */
  List<String> moreImportantAttributes;

  /** Stock the attribute that the user didn't answer (the unpressed button) */
  List<String> lessImportantAttributes;

  /**
   * Is composed of the attributes for the first button (buttonchoix1), we can add how much we want
   */
  List<Criterion> choix1;

  /** Is composed of the attributes for the second button (buttonchoix2) */
  List<Criterion> choix2;

  double surfaceMin;
  double nbBedMin;

  Display display;
  Shell shell;

  public AskOpinionForUtility() {
    this.moreImportantAttributes = new ArrayList<>();
    this.lessImportantAttributes = new ArrayList<>();
    this.choix1 = new ArrayList<>();
    this.choix2 = new ArrayList<>();
    this.surfaceMin = 0d;
    this.nbBedMin = 0d;
    this.pointer = 0;
    this.display = new Display();
    this.shell = new Shell(display);
  }

  /**
   * This is the main function, it asks Questions , AdaptAnswers and then displays the list of
   * Apartments
   *
   * @param args
   * @throws IllegalAccessException for the DisplayApps function
   * @throws IOException,IllegalAccessException
   */
  public static void main(String[] args) {

    AskOpinionForUtility asker = new AskOpinionForUtility();
    ApartmentValueFunction avf = new ApartmentValueFunction();
    asker.askQuestions();

    avf.setDoubleValueFunction(Criterion.FLOOR_AREA, new LinearValueFunction(0d, 300d));
    avf.setDoubleValueFunction(Criterion.NB_BEDROOMS, new LinearValueFunction(0d, 6d));
    avf.setDoubleValueFunction(Criterion.NB_SLEEPING, new LinearValueFunction(0d, 6d));
    avf.setDoubleValueFunction(Criterion.NB_BATHROOMS, new LinearValueFunction(0d, 6d));
    avf.setDoubleValueFunction(Criterion.FLOOR_AREA_TERRACE, new LinearValueFunction(0d, 100d));
    avf.setDoubleValueFunction(Criterion.PRICE_PER_NIGHT, new LinearValueFunction(0d, 80d));
    avf.setDoubleValueFunction(Criterion.NB_MIN_NIGHT, new LinearValueFunction(0d, 6d));

    avf = asker.adaptAnswers(avf);

    avf = avf.withSubjectiveValueWeight(Criterion.FLOOR_AREA, 0.5);
    avf = avf.withSubjectiveValueWeight(Criterion.FLOOR_AREA_TERRACE, 0);
    avf = avf.withSubjectiveValueWeight(Criterion.NB_BATHROOMS, 0);
    avf = avf.withSubjectiveValueWeight(Criterion.NB_BEDROOMS, 0.5);
    avf = avf.withSubjectiveValueWeight(Criterion.NB_MIN_NIGHT, 0);
    avf = avf.withSubjectiveValueWeight(Criterion.NB_SLEEPING, 0);
    avf = avf.withSubjectiveValueWeight(Criterion.PRICE_PER_NIGHT, 0);
    avf = avf.withSubjectiveValueWeight(Criterion.TELE, 0);
    avf = avf.withSubjectiveValueWeight(Criterion.TERRACE, 0);
    avf = avf.withSubjectiveValueWeight(Criterion.WIFI, 0);

    LOGGER.info("Begining the Layout.");
  }

  /**
   * This function will create and display the window (interface) to ask the user questions and to
   * determine later the weight of his choices
   */
  public void askQuestions() {

    // we add the other two criteria
    choix1.add(Criterion.TELE);
    choix2.add(Criterion.PRICE_PER_NIGHT);

    shell.setText("Profile selection - Questions");
    shell.setLayout(new GridLayout());
    shell.setBounds(500, 500, 600, 500);

    // for the first questions about minimum
    final Group group = new Group(shell, SWT.NONE);
    group.setText("Combien il vous faut pour accepter un apartement?");
    group.setLayout(new GridLayout(2, false));

    Label label1 = new Label(group, SWT.NONE);
    label1.setText("Surface minimum?");

    Text text1 = new Text(group, SWT.LEAD | SWT.BORDER);
    text1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

    Label label2 = new Label(group, SWT.NONE);
    label2.setText("Nombre de chambre minimum?");

    Text text2 = new Text(group, SWT.LEAD | SWT.BORDER);
    text2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

    // question about the importance of an element for the user
    Group buttonGroup = new Group(shell, SWT.NONE);
    GridLayout gridLayout = new GridLayout();
    gridLayout.numColumns = 1;
    buttonGroup.setLayout(gridLayout);
    buttonGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    buttonGroup.setText("Qu'est ce qui a le plus d'importance pour vous");

    // the listener when we click on finish
    Listener finishlistener = new Listener() {
      @Override
      public void handleEvent(Event event) {

        Preconditions.checkArgument(!text1.getText().equals(""), "il faut saisir un chiffre :(");
        Preconditions.checkArgument(!text2.getText().equals(""), "il faut saisir un texte :(");

        surfaceMin = Double.parseDouble(text1.getText());
        nbBedMin = Double.parseDouble(text2.getText());

        shell.close();

        LOGGER.info("Les attributs importants sont : ");
        for (int j = 0; j < moreImportantAttributes.size(); j++) {
          LOGGER.info(moreImportantAttributes.get(j));
        }

        LOGGER.info("Les attribut moins importants sont : ");
        for (int j = 0; j < lessImportantAttributes.size(); j++) {
          LOGGER.info(lessImportantAttributes.get(j));
        }
        LOGGER.info("la valeur minmum de la surface est " + surfaceMin
            + "\nLa valeur minimum du nbre de chambre est " + nbBedMin);
      }
    };

    // This is a submit button, it will close the shell when the user click on
    // Terminer
    final Button finish = new Button(shell, SWT.PUSH);
    finish.setText("Terminé");
    finish.pack();
    finish.addListener(SWT.Selection, finishlistener);

    // buttonchoix1 and buttonchoix2 are two radio buttons, to let the user chose
    // between two options
    Button buttonchoix1 = new Button(buttonGroup, SWT.RADIO);
    buttonchoix1.setText("WIFI");

    Button buttonchoix2 = new Button(buttonGroup, SWT.RADIO);
    buttonchoix2.setText("TERRACE");

    // when cliquing on theses buttons
    clickOnButton(buttonchoix1, buttonchoix2);
    clickOnButton(buttonchoix2, buttonchoix1);

    // open the window
    shell.open();
    LOGGER.info("The Shell was opened with success.");
    while (!shell.isDisposed()) {

      if (!display.readAndDispatch()) {

        display.sleep();
      }
    }
    display.dispose();
    LOGGER.info("The screen was closed with success.");
  }

  /**
   * Method that responds when we click on a button. It needs to retrieve the two buttons that
   * correspond to the items to choose
   *
   * @param pressedButton the choice of the user
   * @param unPressedButton the other choice not selected
   */
  public void clickOnButton(Button pressedButton, Button unPressedButton) {
    Label boo = new Label(shell, SWT.NULL);
    pressedButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        Button source1 = (Button) e.widget;

        if (source1.getSelection() && !moreImportantAttributes.contains(source1.getText())) {
          moreImportantAttributes.add(source1.getText());
          lessImportantAttributes.add(unPressedButton.getText());
        }

        if (pointer == choix1.size() || pointer == choix2.size()) {

          pressedButton.setEnabled(false);
          unPressedButton.setEnabled(false);

          boo.setText("\"Merci pour vos choix, nous allons chercher pour vous les meilleurs"
              + " apartements :) ");
          boo.pack();
        } else {

          pressedButton.setText(choix1.get(pointer).toString());
          unPressedButton.setText(choix2.get(pointer).toString());
          pointer++;
          /**
           * this will change the text of the buttons by choosing the content of the arrays
           * attributImportant and attributPasImportant
           */
        }

        /** in order not to be selected by default in the question in the next iteration */
        pressedButton.setSelection(false);

        pressedButton.pack();
        unPressedButton.pack();
      }
    });
  }

  /** This function will adapt the utility of the user using ApartmentValueFunction */
  public ApartmentValueFunction adaptAnswers(ApartmentValueFunction avf) {

    // we collect the answers on the minimums and we adapt the utility of the user
    avf.adaptBounds(Criterion.NB_BEDROOMS, nbBedMin, true);
    avf.adaptBounds(Criterion.FLOOR_AREA, surfaceMin, true);

    // we collect the answer of the first Question and adapt the utility of the user
    if (moreImportantAttributes.get(0).equals("WIFI")
        && lessImportantAttributes.get(0).equals("TERRACE")) {
      avf.adaptWeight(Criterion.WIFI, Criterion.TERRACE);
    } else {
      avf.adaptWeight(Criterion.TERRACE, Criterion.WIFI);
    }

    // we collect the answer of the second Question and we adapt the utility of the
    // user
    if (moreImportantAttributes.get(1).equals("TELE")
        && lessImportantAttributes.get(1).equals("PRICE_PER_NIGHT low")) {
      avf.adaptWeight(Criterion.TELE, Criterion.PRICE_PER_NIGHT);
    } else {
      avf.adaptWeight(Criterion.PRICE_PER_NIGHT, Criterion.TELE);
    }

    return avf;
  }
}
