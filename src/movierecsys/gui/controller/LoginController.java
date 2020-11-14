package movierecsys.gui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import movierecsys.bll.util.LoginManager;
import movierecsys.bll.util.UserManager;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public ObservableList<String> usersList= FXCollections.observableArrayList();
    @FXML
    private ListView<String> lstUsers;
    @FXML
    private TextField loginInput;
    @FXML
    private void closeButtonAction() {
        System.exit(0);
    }

    @FXML
    private void loginButtonAction(ActionEvent event) {
        String text=loginInput.getText();
        if(text==null) return;
        if(text.length()==0) return;
        LoginManager.login(text);
        loggedInProcessWindow((Stage)((Node)event.getSource()).getScene().getWindow());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        lstUsers.setItems(usersList);
        usersList.setAll(UserManager.getUsersListView());
        lstUsers.setOnMouseClicked(click -> {

            if (click.getClickCount() == 2) {
                int id=Integer.parseInt(lstUsers.getSelectionModel().getSelectedItem().split("]")[0].replace("[",""));
                LoginManager.login(id);
                loggedInProcessWindow((Stage) ((Node)click.getSource()).getScene().getWindow());
            }
        });
    }

    private void loggedInProcessWindow(Stage current_stage){
        if(!LoginManager.isLoggedIn()) return;
        try {
            Parent root1 = new FXMLLoader(getClass().getResource("../view/MovieView.fxml")).load();
            current_stage.close();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.setTitle("Movie Recommendation Application");
            stage.setResizable(false);
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
