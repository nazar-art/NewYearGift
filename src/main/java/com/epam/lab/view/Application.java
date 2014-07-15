package com.epam.lab.view;

import java.io.File;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.epam.lab.controller.GiftController;
import com.epam.lab.model.exceptions.CreateDocumentConfigurationException;
import com.epam.lab.model.exceptions.InvalidUserInputDataException;
import com.epam.lab.model.sweets.Sweets;

public class Application {

	private static final Logger LOG = Logger.getLogger(Application.class);

	private GiftController giftController;
	
	ArrayList<Sweets> sweets;
	
	public Application() throws CreateDocumentConfigurationException {
		giftController = new GiftController();
		sweets = new ArrayList<Sweets>();
	}

	public void start() throws CreateDocumentConfigurationException {
		// Custom button text
		Object[] options = { "Yes, please", "I have configuration xml file",
				"No, thanks!" };

		int n = JOptionPane.showOptionDialog(null,
				"Would you like to create new random New Year's Gift?",
				"A Silly Question", JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[2]);

		estimateUserInput(n);
	}

	private void estimateUserInput(int n) throws CreateDocumentConfigurationException {
		switch (n) {
		case 0:
			process();
			break;
		case 1:
			chooseFile();
			break;
		case 2:
			goodBye();
			break;
		}
	}

	private void chooseFile() {

		JFileChooser chooser = new JFileChooser();

		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File selectedFile = chooser.getSelectedFile();
			giftController.parseXmlFile(selectedFile);
		}
	}

	private void goodBye() {
		JOptionPane.showMessageDialog(null, "Good Bye!");
	}

	private void process() throws CreateDocumentConfigurationException {

		String[] input = divideUserInput();
		double start = 0, end = 0;

		if (input.length < 2) {
			errorMessage("Input error!");
			throw new InvalidUserInputDataException();
		}
		
		int quantity = Integer.parseInt(input[0]);
		start = Double.parseDouble(input[1]);
		end = Double.parseDouble(input[2]);

		// general overwiev
		giftController.showGiftContent(quantity);

		// sort by weight
		giftController.showSortedByWeight();

		// sort by sugar level
		giftController.showSortedBySugar();

		// show gift list with sugar limitation
		giftController.showExtractedSugar(start, end);

		// write to xml file
		giftController.writeToXmlFile();
	}

	private void errorMessage(String msg) {
		// custom title, error icon
		JOptionPane.showMessageDialog(null, msg, "Input error",
				JOptionPane.ERROR_MESSAGE);
	}

	private String[] divideUserInput() {
		return inputGiftInfo().trim().split("\\s");
	}

	private String inputGiftInfo() {

		String resultString = "";
		JTextField quantityField = new JTextField(5);
		JTextField minSugarField = new JTextField(5);
		JTextField maxSugarField = new JTextField(5);

		JPanel myPanel = new JPanel();

		myPanel.add(new JLabel("Quantity of New Year's Gift:"));
		myPanel.add(quantityField);
		myPanel.add(Box.createHorizontalStrut(15)); // a spacer

		myPanel.add(new JLabel("Min sugar level (form 0):"));
		myPanel.add(minSugarField);
		myPanel.add(Box.createHorizontalStrut(15)); // a spacer

		myPanel.add(new JLabel("Max sugar level (to 100):"));
		myPanel.add(maxSugarField);

		int result = JOptionPane.showConfirmDialog(null, myPanel,
				"Please Enter Droid configuration",
				JOptionPane.OK_CANCEL_OPTION);

		// create result string
		if (result == JOptionPane.OK_OPTION) {
			resultString = resultString + quantityField.getText() + " "
					+ minSugarField.getText() + " " + maxSugarField.getText();

			LOG.debug("the New Year's Gift is created, quantityty "
					+ quantityField.getText() + " with " + minSugarField
					+ " min and " + maxSugarField + " max sugar level level");
		}

		return resultString;
	}

/*	public static void main(String[] args) {
		try {
			new Application().start();
		} catch (Exception e) {
			LOG.error("Error ocurred", e);
			e.printStackTrace();
		}
	}*/

}
