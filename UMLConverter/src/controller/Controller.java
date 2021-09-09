package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.IndexRange;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import model.UMLConverter;
import resources.Resources;

/**
 * Controller class which takes care of functionalities.
 * 
 * @author Lhomme Lucien
 */
public class Controller implements Initializable {

	@FXML
	private ResourceBundle bundle;

	@FXML
	private Menu menuFile;
	@FXML
	private MenuItem menuNew;
	@FXML
	private MenuItem menuOpen;
	@FXML
	private Menu menuOpenRecent;
	@FXML
	private MenuItem menuClearHistory;
	@FXML
	private MenuItem menuClose;
	@FXML
	private MenuItem menuSave;
	@FXML
	private MenuItem menuSaveAs;
	@FXML
	private MenuItem menuRevert;
	@FXML
	private MenuItem menuExportAs;
	@FXML
	private MenuItem menuPrint;
	@FXML
	private MenuItem menuPreferences;
	@FXML
	private MenuItem menuQuit;

	@FXML
	private Menu menuEdit;
	@FXML
	private MenuItem menuUndo;
	@FXML
	private MenuItem menuRedo;
	@FXML
	private MenuItem menuCut;
	@FXML
	private MenuItem menuCopy;
	@FXML
	private MenuItem menuPaste;
	@FXML
	private MenuItem menuDelete;
	@FXML
	private MenuItem menuSelectAll;
	@FXML
	private MenuItem menuUnselectAll;

	@FXML
	private Menu menuWindow;
	@FXML
	private MenuItem menuFullScreen;
	@FXML
	private ImageView imageViewFullScreen;
	@FXML
	private Menu menuLanguages;
	@FXML
	private MenuItem menuEnglish;
	@FXML
	private MenuItem menuFrench;
	@FXML
	private MenuItem menuSpanish;
	@FXML
	private MenuItem menuItaly;
	@FXML
	private MenuItem menuGerman;

	@FXML
	private Menu menuHelp;
	@FXML
	private MenuItem menuAbout;

	@FXML
	private Button btnConvert;
	@FXML
	private Button btnFastCopy;

	@FXML
	private TextArea textAreaIn;
	@FXML
	private TextArea textAreaOut;

	private Stage primaryStage;

	private FileChooser fileChooser = new FileChooser();

	private File fileCurrent;

	private List<File> fileHistory = new ArrayList<File>();
	
	private final Clipboard systemClipboard = Clipboard.getSystemClipboard();

	public void setStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	@FXML
	public void newFile() {
		fileCurrent = null;
		textAreaIn.clear();
	}

	@FXML
	public void open() {
		fileChooser.setTitle("Ouvrir un .rpg");
		if (fileCurrent != null) {
			fileChooser.setInitialDirectory(fileCurrent.getParentFile());
		}
		File tmp = fileChooser.showOpenDialog(primaryStage);
		if (tmp != null) {
			menuClearHistory.setDisable(false);
			MenuItem menuItem = new MenuItem(tmp.getName());
			menuItem.setOnAction(ev -> {
				chargeFile(tmp);
			});
			menuOpenRecent.getItems().add(0, menuItem);
			fileHistory.add(tmp);
			chargeFile(tmp);
		}
	}

	private void chargeFile(File file) {
		textAreaIn.setText(Resources.chargeStringFile(fileCurrent = file));
	}

	@FXML
	public void save() {
		if (fileCurrent == null) {
			saveAs();
		} else {
			saveFile(fileCurrent, textAreaIn);
		}
	}

	@FXML
	public void saveAs() {
		fileChooser.setTitle(bundle.getString("menu.saveAs"));
		if (fileCurrent != null) {
			fileChooser.setInitialDirectory(fileCurrent.getParentFile());
		}
		File tmp = fileChooser.showSaveDialog(primaryStage);
		if (tmp != null) {
			saveFile(fileCurrent = tmp, textAreaIn);
		}
	}

	private void saveFile(File file, TextArea textArea) {
		try {
			FileWriter fstream = new FileWriter(file);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(textArea.getText());
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void exportAs() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(bundle.getString("menu.exportAs"));

		fileChooser.getExtensionFilters().add(new ExtensionFilter("txt files (*.txt)", "*.txt", "*.TXT"));

		File file = fileChooser.showSaveDialog(primaryStage);
		if (file != null) {
			saveFile(file, textAreaOut);
		}
	}

	@FXML
	public void print() {

	}

	@FXML
	public void preferences() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(Resources.getURL("views/PreferencesView.fxml"), bundle);
			
			Parent root = fxmlLoader.load();
			
			final Stage dialog = new Stage();
			dialog.initModality(Modality.APPLICATION_MODAL);
			dialog.setTitle(bundle.getString("menu.preferences"));
			dialog.getIcons().setAll(primaryStage.getIcons());
			dialog.initOwner(primaryStage);
			Scene dialogScene = new Scene(root);
			dialog.setScene(dialogScene);
			dialog.show();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void clearHistory() {
		fileHistory.clear();
		menuOpenRecent.getItems().remove(0, menuOpenRecent.getItems().size() - 2);
		menuClearHistory.setDisable(true);
	}

	@FXML
	public void quit() {
		System.exit(0);
	}

	private TextInputControl getFocusedTextInputControl() {
		TextInputControl[] tics = new TextInputControl[]{textAreaIn, textAreaOut};
		for (TextInputControl tic : tics)
			if (tic.isFocused())
				return tic;
		return null;
	}
	
	private String newTextWithoutRange(String origText, IndexRange range) {
		String firstPart = origText.substring(0, range.getStart());
		String lastPart = origText.substring(range.getEnd(), origText.length());
		return firstPart + lastPart;
	}
	
	private void putContentClipboard(String text) {
		ClipboardContent content = new ClipboardContent();
		content.putString(text);
		systemClipboard.setContent(content);
	}

	@FXML
	public void cut() {
		TextInputControl focusedTIC = getFocusedTextInputControl();
		if (focusedTIC != null) {
			IndexRange range = focusedTIC.getSelection();
			if (range.getLength() != 0) {
				String text = focusedTIC.getSelectedText();
				putContentClipboard(text);
				String origText = focusedTIC.getText();
				focusedTIC.setText(newTextWithoutRange(origText, range));
				focusedTIC.positionCaret(range.getStart());
			}
		}
	}

	@FXML
	public void copy() {
		TextInputControl focusedTIC = getFocusedTextInputControl();
		if (focusedTIC != null) {
			String text = focusedTIC.getSelectedText();
			if (!text.isEmpty()) {
				putContentClipboard(text);
			}
		}
	}

	@FXML
	public void paste() {
		if (!systemClipboard.hasContent(DataFormat.PLAIN_TEXT))
			return;
		String clipboardText = systemClipboard.getString();
		TextInputControl focusedTIC = getFocusedTextInputControl();
		if (focusedTIC != null) {
			IndexRange range = focusedTIC.getSelection();
			String origText = focusedTIC.getText();
			int endPos = 0;
			String updatedText = "";
			String firstPart = origText.substring(0, range.getStart());
			String lastPart = origText.substring(range.getEnd(), origText.length());
			updatedText = firstPart + clipboardText + lastPart;
			if (range.getStart() == range.getEnd()) 
				endPos = range.getEnd() + clipboardText.length();
			else
				endPos = range.getStart() + clipboardText.length();
			focusedTIC.setText(updatedText);
			focusedTIC.positionCaret(endPos);
		}
	}
	
	@FXML
	public void suppr() {
		TextInputControl focusedTIC = getFocusedTextInputControl();
		if (focusedTIC != null) {
			IndexRange range = focusedTIC.getSelection();
			String origText = focusedTIC.getText();
			focusedTIC.setText(newTextWithoutRange(origText, range));
			focusedTIC.positionCaret(range.getStart());
		}
	}
	
	@FXML
	public void selectAll() {
		TextInputControl focusedTIC = getFocusedTextInputControl();
		if (focusedTIC != null) {
			focusedTIC.selectAll();
		}
	}
	
	@FXML
	public void unSelectAll() {
		TextInputControl focusedTIC = getFocusedTextInputControl();
		if (focusedTIC != null) {
			focusedTIC.deselect();
		}
	}

	@FXML
	public void fullScreen() {
		primaryStage.setFullScreen(!primaryStage.isFullScreen());
	}
	
	@FXML
	public void about() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(Resources.getURL("views/AboutView.fxml"), bundle);
			
			Parent root = fxmlLoader.load();
			
			final Stage dialog = new Stage();
			dialog.initModality(Modality.APPLICATION_MODAL);
			dialog.setTitle(bundle.getString("menu.about"));
			dialog.getIcons().setAll(primaryStage.getIcons());
			dialog.initOwner(primaryStage);
			Scene dialogScene = new Scene(root);
			dialog.setScene(dialogScene);
			dialog.show();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void convert() {
		textAreaOut.setText(UMLConverter.convertUML(textAreaIn.getText().split("\n")));
	}
	
	@FXML
	public void fastCopy() {
		putContentClipboard(textAreaOut.getText());
	}

	@FXML
	public void switchLanguageEN() {
		switchLanguage(new Locale("en", "EN"));
	}

	@FXML
	public void switchLanguageFR() {
		switchLanguage(new Locale("fr", "FR"));
	}

	@FXML
	public void switchLanguageES() {
		switchLanguage(new Locale("es", "ES"));
	}

	@FXML
	public void switchLanguageIT() {
		switchLanguage(new Locale("it", "IT"));
	}

	@FXML
	public void switchLanguageDE() {
		switchLanguage(new Locale("de", "DE"));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		bundle = resources;
		fileChooser.getExtensionFilters().add(new ExtensionFilter("txt files (*.txt)", "*.txt", "*.TXT"));

	}

	private void switchLanguage(Locale locale) {
		bundle = Resources.getBundle("strings", locale);

		primaryStage.setTitle(bundle.getString("window.title"));

		menuFile.setText(bundle.getString("menu.file"));
		menuNew.setText(bundle.getString("menu.new"));
		menuOpen.setText(bundle.getString("menu.open"));
		menuOpenRecent.setText(bundle.getString("menu.openRecent"));
		menuClearHistory.setText(bundle.getString("menu.clearHistory"));
		menuClose.setText(bundle.getString("menu.close"));
		menuSave.setText(bundle.getString("menu.save"));
		menuSaveAs.setText(bundle.getString("menu.saveAs"));
		menuRevert.setText(bundle.getString("menu.revert"));
		menuExportAs.setText(bundle.getString("menu.exportAs"));
		menuPrint.setText(bundle.getString("menu.print"));
		menuPreferences.setText(bundle.getString("menu.preferences"));
		menuQuit.setText(bundle.getString("menu.quit"));

		menuEdit.setText(bundle.getString("menu.edit"));
		menuUndo.setText(bundle.getString("menu.undo"));
		menuRedo.setText(bundle.getString("menu.redo"));
		menuCut.setText(bundle.getString("menu.cut"));
		menuCopy.setText(bundle.getString("menu.copy"));
		menuPaste.setText(bundle.getString("menu.paste"));
		menuDelete.setText(bundle.getString("menu.delete"));
		menuSelectAll.setText(bundle.getString("menu.selectAll"));
		menuUnselectAll.setText(bundle.getString("menu.unselectAll"));

		menuWindow.setText(bundle.getString("menu.window"));
		menuFullScreen.setText(bundle.getString("menu.fullScreen"));
		menuLanguages.setText(bundle.getString("menu.languages"));
		menuEnglish.setText(bundle.getString("menu.english"));
		menuFrench.setText(bundle.getString("menu.french"));
		menuSpanish.setText(bundle.getString("menu.spanish"));
		menuItaly.setText(bundle.getString("menu.italy"));
		menuGerman.setText(bundle.getString("menu.german"));

		menuHelp.setText(bundle.getString("menu.help"));
		menuAbout.setText(bundle.getString("menu.about"));

		btnConvert.setText(bundle.getString("btn.convert"));
		btnFastCopy.setText(bundle.getString("btn.fastCopy"));

	}
}
