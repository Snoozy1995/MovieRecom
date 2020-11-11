/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import movierecsys.be.Movie;
import movierecsys.bll.util.MovieSearcher;
import movierecsys.dal.MovieDAO;

/**
 *
 * @author pgn
 */
public class MovieRecController implements Initializable
{

    /**
     * The TextField containing the URL of the targeted website.
     */
    @FXML
    private TextField txtMovieSearch;

    /**
     * The TextField containing the query word.
     */
    @FXML
    private ListView<Movie> lstMovies;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        System.out.println("test constructor1");
        //ObservableList<Movie> moviesList= FXCollections.observableArrayList();
        MovieSearcher movSearcher=new MovieSearcher();
        MovieDAO movDAO=new MovieDAO(); // @todo move, shouldnt be here...
        //Listener for changes in textfield
        txtMovieSearch.textProperty().addListener((obs, oldText, newText) -> {
            try { // fix ioexception in search to remove ioexception here @todo
                lstMovies.setItems(FXCollections.observableArrayList(movSearcher.search(movDAO.getAllMovies(),newText)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}