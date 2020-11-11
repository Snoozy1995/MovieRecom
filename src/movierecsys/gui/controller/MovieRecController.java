/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import movierecsys.bll.util.MovieManager;

/**
 *
 * @author Snoozy1995
 */
public class MovieRecController implements Initializable
{
    public ObservableList<String> moviesList= FXCollections.observableArrayList();
    public MovieManager movieManager=new MovieManager();
    @FXML
    private TextField txtMovieSearch;
    @FXML
    private ListView<String> lstMovies;
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        lstMovies.setItems(moviesList);
        txtMovieSearch.textProperty().addListener((obs, oldText, newText) -> moviesList.setAll(movieManager.toListString(movieManager.searchMovie(newText))));
    }

}