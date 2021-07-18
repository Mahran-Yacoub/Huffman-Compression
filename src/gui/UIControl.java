package gui;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import Huffman.CompressMethods;
import Huffman.DecompressMethods;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import main.Driver;

public class UIControl implements Initializable {

	/** This list will store all objects of row type to fill tableView */
	private ObservableList<Row> listView;

	@FXML
	private RadioButton encodingRadioButton;

	@FXML
	private RadioButton decodingRadioButton;

	@FXML
	private TextField sourceFilePath;

	@FXML
	private TextField destinationFilePath;

	@FXML
	private Label sourceFileInformation;

	@FXML
	private Label codedFileInformation;

	@FXML
	private TableView<Row> huffmanTable;

	@FXML
	private TableColumn<Row, Character> characterColumn;

	@FXML
	private TableColumn<Row, String> huffmanCodeColumn;

	@FXML
	private TableColumn<Row, Integer> lengthColumn;

	@FXML
	private TableColumn<Row, Integer> frequencyColumn;

	@FXML
	private TextArea encodedOfHeader;

	@FXML
	private TextArea numberOfCharacterInOriginalFile;

	@FXML
	private TextArea extensionOfOriginalFile;

	private File source;

	private File destination;

	private ToggleGroup radioButtons = new ToggleGroup();

	private CompressMethods compressTools;

	private DecompressMethods decompressTools;

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * This method will be used to choose a file to be decompressed
	 * 
	 * @param event
	 */
	@FXML
	void browseDestinationFile(ActionEvent event) {

		try {

			FileChooser chose = new FileChooser();
			destination = chose.showOpenDialog(null);
			destinationFilePath.setText(destination.getAbsolutePath());

		} catch (Exception e) {

			System.out.println(e.getMessage());
		}

	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This method will be used to choose a file to be compressed
	 * 
	 * @param event
	 */

	@FXML
	void browseSourceFile(ActionEvent event) {

		try {

			FileChooser chose = new FileChooser();
			source = chose.showOpenDialog(null);
			sourceFilePath.setText(source.getAbsolutePath());

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This method will compress or decompress a choosen file , Depends of radio
	 * button that selected
	 * 
	 * @param event
	 */
	@FXML
	void compressOrDecompressAction(ActionEvent event) {

		if (radioButtons.getSelectedToggle() == this.encodingRadioButton) { // It want to Compress a source File

			encodeOption();

		} else if (radioButtons.getSelectedToggle() == this.decodingRadioButton) { // It want to Decompress a source
																					// File
			decodeOption();

		} else {

			// Error message will be Display if there is no choice selected
			ShowError choiceError = new ShowError();
			choiceError.showError("Enter Ecode or Decode Please");
		}

	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This method will close a stage
	 * 
	 * @param event
	 */
	@FXML
	void cancel(ActionEvent event) {

		Driver.mainStage.close();

	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This method will clear all input data
	 * 
	 * @param event
	 */
	@FXML
	void clear(ActionEvent event) {

		this.source = null;
		this.destination = null;
		this.sourceFileInformation.setText("");
		this.codedFileInformation.setText("");
		this.sourceFilePath.clear();
		this.destinationFilePath.clear();
		this.listView.clear();
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This method will be used to initialize and setup some data fields
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		/*
		 * Adding radio Buttons to One Group , so You can choose one radio button at a
		 * time
		 */
		encodingRadioButton.setToggleGroup(radioButtons);
		decodingRadioButton.setToggleGroup(radioButtons);

		// Set Columns and add them to tableView
		characterColumn.setCellValueFactory(new PropertyValueFactory<Row, Character>("character"));
		huffmanCodeColumn.setCellValueFactory(new PropertyValueFactory<Row, String>("huffmanCode"));
		lengthColumn.setCellValueFactory(new PropertyValueFactory<Row, Integer>("length"));
		frequencyColumn.setCellValueFactory(new PropertyValueFactory<Row, Integer>("frequency"));

	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This method will invoke to compress source File that will be choosen
	 */
	private void encodeOption() {

		if (source != null) {

			/*
			 * Get source File to Compress it with compressed file with the same name and
			 * .huff extension and in the same directory
			 */
			String pathWithExtension = source.getParent() + "\\" + source.getName();
			int indexOfDotExtension = pathWithExtension.lastIndexOf('.');
			String pathWithOutExtension = pathWithExtension.substring(0, indexOfDotExtension);
			File destination = new File(pathWithOutExtension + ".huff");

			// Start The compression Process
			compressTools = new CompressMethods();
			compressTools.compress(source, destination);

			printSourceInformation();

			fillTap3WhileEncoding();
			
			listView = FXCollections.observableArrayList(compressTools.getRows());
			huffmanTable.setItems(listView);

			ShowError doneEncoding = new ShowError();
			doneEncoding.showError("Encoding Finished");

		} else {

			ShowError errorNotChooseSourceFile = new ShowError();
			errorNotChooseSourceFile.showError("Choose Source File Please");
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This method will invoke to decompress Destination File that will be choosen
	 */
	private void decodeOption() {

		if (destination != null) {

			
			decompressTools = new DecompressMethods(destination);
			
			/*
			 * Get source File to Compress it with compressed file with the same name and
			 * original extension and in the same directory
			 */
			String pathWithExtension = destination.getParent() + "\\" + destination.getName();
			int indexOfDotExtension = pathWithExtension.lastIndexOf('.');
			String pathWithOutExtension = pathWithExtension.substring(0, indexOfDotExtension);
			File original = new File(pathWithOutExtension + decompressTools.getExtensionOfOriginalFile()  ); 

			// This is a trivial solution for a rename problem
			if (original.exists()) {
				original = new File(pathWithOutExtension +"."+ decompressTools.getExtensionOfOriginalFile());
			}

			// Start The Decompression Process
			decompressTools.decompres(original);

			printDestinationInformation();

			fillTap3WhileDencoding();

			listView = FXCollections.observableArrayList(decompressTools.getRows());
			huffmanTable.setItems(listView);
			
			ShowError doneDecoding = new ShowError();
			doneDecoding.showError("Decoding Finished");

		} else {

			ShowError errorNotChooseDestinationFile = new ShowError();
			errorNotChooseDestinationFile.showError("Choose Destination File Please");
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This Method will print source file information on GUI
	 */
	private void printSourceInformation() {

		// Fill Information about source file in GUI
		String sourceInformation = "File Length :\t" + compressTools.getCountNumberOfCharInOriginalFile() + "\n\n"
				+ "# of Distinguished Characters:  " + compressTools.getCountDistinct();
		this.sourceFileInformation.setText(sourceInformation);

		// Fill Information about Destination/compressed file in GUI
		int NumberOfCharInHeader = compressTools.getCountNumberOfCharInHeader() / 8 ;
		if(NumberOfCharInHeader%8>0) {
			NumberOfCharInHeader++;
		}
		int NumberOfCharInEncodedMessage = compressTools.getNumberOfCharInEncodedMessage() ;
		
		float ratioOfCompression = ((1 - (float) (NumberOfCharInHeader + NumberOfCharInEncodedMessage)
				/ compressTools.getCountNumberOfCharInOriginalFile()) * 100);

		ratioOfCompression = (int) (ratioOfCompression * 100) / 100f;

		String DestinationInformation = "File Head Length :\t" + NumberOfCharInHeader + "\n\n" + "Actual Data length:\t"
				+ NumberOfCharInEncodedMessage + "\n\nCompression Rate:\t\t" + ratioOfCompression + " %";

		this.codedFileInformation.setText(DestinationInformation);
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This Method will print destination file information on GUI
	 */
	private void printDestinationInformation() {

		// Fill Information about source file in GUI
		String sourceInformation = "File Length :\t" + decompressTools.getNumberOfCharInOriginalFile() + "\n\n"
				+ "# of Distinguished Characters:  " + decompressTools.getCountDistinctInOriginal();
		this.sourceFileInformation.setText(sourceInformation);

		// Fill Information about Destination/compressed file in GUI
		int NumberOfCharInHeader = decompressTools.getCountHeader()/8;
		if(NumberOfCharInHeader%8>0) {
			NumberOfCharInHeader++;
		}
		int NumberOfCharInEncodedMessage = decompressTools.getCountCharInCompressedFile()  ;

		float ratioOfCompression = ((1 - (float) (NumberOfCharInHeader + NumberOfCharInEncodedMessage)
				/ decompressTools.getNumberOfCharInOriginalFile()) * 100);

		ratioOfCompression = (int) (ratioOfCompression * 100) / 100f;

		String DestinationInformation = "File Head Length :\t" + NumberOfCharInHeader + "\n\n" + "Actual Data length:\t"
				+ NumberOfCharInEncodedMessage + "\n\nCompression Rate:\t\t" + ratioOfCompression + " %";

		this.codedFileInformation.setText(DestinationInformation);

	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void fillTap3WhileEncoding() {

		this.numberOfCharacterInOriginalFile.setText(compressTools.getCountNumberOfCharInOriginalFile() + "");
		this.extensionOfOriginalFile.setText(compressTools.getExtensionOfOriginalFile());
		this.encodedOfHeader.setText(compressTools.getEncodedOfHeader().toString());

	}

	private void fillTap3WhileDencoding() {

		this.numberOfCharacterInOriginalFile.setText(decompressTools.getNumberOfCharInOriginalFile() + "");
		this.extensionOfOriginalFile.setText(decompressTools.getExtensionOfOriginalFile());
		this.encodedOfHeader.setText(decompressTools.getEncodedOfHeader().toString());

	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
