package view;

import java.util.ResourceBundle;

import controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import resources.Resources;

/**
 * Main class wich launch the application.
 * 
 * @author Lhomme Lucien
 */
public class Main extends Application {
	
	@Override
	public void start(Stage stage) throws Exception {
		
		ResourceBundle bundle = Resources.getBundle("strings");
		
		FXMLLoader fxmlLoader = new FXMLLoader(Resources.getURL("views/MyView.fxml"), bundle);
		Parent root = fxmlLoader.load();
		
		Controller controller = (Controller)fxmlLoader.getController();
		controller.setStage(stage);
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add(Resources.getURL("views/application.css").toExternalForm());
		
		scene.setOnKeyReleased(ev -> {
			if (ev.getCode() == KeyCode.F11)
				controller.fullScreen();
		});
		
		stage.getIcons().add(Resources.getImage("icon.png"));
		stage.setTitle(bundle.getString("window.title"));
		stage.setScene(scene);
		stage.show();
		
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}
