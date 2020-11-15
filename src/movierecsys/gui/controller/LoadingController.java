package movierecsys.gui.controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import movierecsys.bll.util.LoginManager;
import movierecsys.bll.util.UserManager;

import java.net.URL;
import java.util.ResourceBundle;

public class LoadingController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle rb){
        try {
            Parent root1 = new FXMLLoader(getClass().getResource("../view/Default.fxml")).load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.setTitle("Movie Recommendation Application");
            stage.show();
            //current_stage.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
