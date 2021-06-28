package io.github.oliviercailloux.y2018.apartments.gui;

import io.github.oliviercailloux.y2018.apartments.valuefunction.ApartementVF;
import io.github.oliviercailloux.y2018.apartments.valuefunction.profile.Profile;
import io.github.oliviercailloux.y2018.apartments.valuefunction.profile.ProfileManager;
import io.github.oliviercailloux.y2018.apartments.valuefunction.profile.ProfileType;
import io.github.oliviercailloux.y2018.apartments.valuefunction.profile.QuestionPriceArea;
import java.io.IOException;
import java.io.InputStream;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProfileQuestionGUI {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProfileQuestionGUI.class);

  String trueQuestionPriceArea;
  String falseQuestionPriceArea;
  Display display;
  Shell shell;

  /** This constructor initialize constant and create a display centered in the screen */
  public ProfileQuestionGUI() {
    this.trueQuestionPriceArea = "Yes";
    this.falseQuestionPriceArea = "No";
    this.display = new Display();
    this.shell = new Shell(display);
  }

  /**
   * JUST FOR TESTING MUST BE ADAPT This is the main function, it asks Questions , AdaptAnswers and
   * then displays the list of Apartments
   *
   * @param args arguments given to the program
   */
  public static void main(String[] args) {
    process(ProfileType.COUPLE);
  }

  /**
   * Run a new instance of the GUI, display the question of the given ProfileType and give the
   * generated linearAVF to LayoutApartmentGUI
   *
   * @param selected the given ProfileType
   */
  public static void process(ProfileType selected) {
    ProfileQuestionGUI prof = new ProfileQuestionGUI();
    ApartementVF newLinearAVF = prof.askQuestions(selected);
    LayoutApartmentGUI.process(newLinearAVF);
  }

  /**
   * This function will create and display the window (interface) to ask the user questions and to
   * determine later the weight of his choices
   *
   * @return
   */
  public ApartementVF askQuestions(ProfileType profileTypeSelected) {
    Profile profileSelected = ProfileManager.getInstance().getProfile(profileTypeSelected);
    String profileName = profileTypeSelected.name().substring(0, 1)
        + profileTypeSelected.name().toLowerCase().substring(1);

    shell.setText("Profile selection - Questions");
    shell.setLayout(new GridLayout());
    shell.setBounds(500, 500, 600, 500);

    // for the first questions about minimum
    final Group group = new Group(shell, SWT.NONE);
    group.setText("Profile selected : ");
    group.setLayout(new GridLayout(2, false));

    Label imageLabel = new Label(group, SWT.NONE);
    Label imageLabelText = new Label(group, SWT.NONE);

    try (InputStream f = ProfileQuestionGUI.class.getResourceAsStream(profileName + ".png")) {
      Image image =
          new Image(this.display, new Image(this.display, f).getImageData().scaledTo(70, 70));

      imageLabel.setImage(image);

    } catch (IOException e1) {
      throw new IllegalArgumentException(e1);
    }
    imageLabelText.setText(profileName);

    // question about the importance of an element for the user
    Group buttonGroup = new Group(shell, SWT.NONE);
    GridLayout gridLayout = new GridLayout();
    gridLayout.numColumns = 1;
    buttonGroup.setLayout(gridLayout);
    buttonGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    QuestionPriceArea question = profileSelected.getQuestionPriceArea();
    Label label = new Label(buttonGroup, SWT.NONE);
    label.setText(profileSelected.getQuestionPriceArea().getQuestion());

    // buttonchoix1 and buttonchoix2 are two radio buttons, to let the user chose
    // between two options
    Button buttonchoix1 = new Button(buttonGroup, SWT.RADIO);
    buttonchoix1.setText(trueQuestionPriceArea);
    buttonchoix1.setSelection(false);

    Button buttonchoix2 = new Button(buttonGroup, SWT.RADIO);
    buttonchoix2.setText(falseQuestionPriceArea);
    buttonchoix2.setSelection(false);
    final boolean[] result1 = new boolean[1];
    buttonchoix1.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent e) {
        Button source = (Button) e.widget;

        if (source.getSelection()) {
          result1[0] = true;
        }
      }
    });

    buttonchoix2.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        Button source = (Button) e.getSource();
        if (source.getSelection()) {
          result1[0] = false;
        }
      }
    });
    // the listener when we click on finish
    Listener finishlistener = event -> {
      shell.close();
      question.resolve(profileSelected, result1[0]);
      LOGGER.info("ProfileQuestionGUI closed");
    };

    // This is a submit button, it will close the shell when the user click on Submit
    final Button finish = new Button(shell, SWT.PUSH);
    finish.setText("Submit");
    finish.pack();
    finish.addListener(SWT.Selection, finishlistener);
    this.centerShellInWindow();
    // open the window
    shell.open();
    while (!shell.isDisposed()) {

      if (!display.readAndDispatch()) {

        display.sleep();
      }
    }
    display.dispose();
    LOGGER.info("The screen was closed with success.");

    return profileSelected.getLinearAVF();
  }

  /** Permit to center the GUI in the screen */
  private void centerShellInWindow() {
    int x = (display.getClientArea().width - shell.getSize().x) / 2;
    int y = (display.getClientArea().height - shell.getSize().y) / 2;
    shell.setLocation(x, y);
  }
}
