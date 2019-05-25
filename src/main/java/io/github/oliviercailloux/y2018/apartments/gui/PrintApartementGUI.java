package io.github.oliviercailloux.y2018.apartments.gui;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import io.github.oliviercailloux.y2018.apartments.apartment.Apartment;
import io.github.oliviercailloux.y2018.apartments.readapartments.ReadApartmentsXMLFormat;

import javax.imageio.IIOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author AITALIBRAHAM & SAKHO
 *  
 */
public class PrintApartementGUI {
	/**
	 *  This class aims to print an apartment to the users 
	 */
	private final static Logger LOGGER = LoggerFactory.getLogger(CreateApartmentGUI.class);
	
	/**
	 * @param filename : the file describing apartments should be given in parameters "XML format"
	 * @throws IOException
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	
	public Apartment appar;
	protected static Display display = new Display();
	protected static Shell shell = new Shell(display, SWT.BORDER);
	
	public PrintApartementGUI() throws IOException, IllegalArgumentException, IllegalAccessException {
		this.appar = new Apartment(20.0 , "20 rue des consé" , "Test Apartment" );

	}
	
	public PrintApartementGUI(String fileName) throws IOException, IllegalArgumentException, IllegalAccessException {
		ReadApartmentsXMLFormat xmlReader= new ReadApartmentsXMLFormat();
		FileInputStream fileinputstream = new FileInputStream(fileName); 
		this.appar = xmlReader.readApartment(fileinputstream);
		
		LOGGER.info("Apratement has been loaded ");
		//lecture d'un appartement au format xml
		
		
		
	}
	
	/**
	 * @param args
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	public static void main(String args[]) throws IllegalArgumentException, IllegalAccessException, IOException{
		PrintApartementGUI prtApp = new PrintApartementGUI();
		LOGGER.info("Test Apartment has been created");
		Label lbl = new Label(shell, SWT.CENTER);
		lbl.setText("this isa lable test");
		lbl.pack();
		
		shell.setText("Apartments");
		shell.setSize(500, 700);
		shell.pack();
		shell.open();
		lbl.setSize(500, 700);
		
		while(!shell.isDisposed( )){
			if(!display.readAndDispatch( ))
				display.sleep( );
		}
		lbl.dispose();
		display.dispose();
		
	}
	
}
