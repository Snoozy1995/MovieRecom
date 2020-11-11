/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import movierecsys.be.Movie;
import movierecsys.bll.util.MovieSearcher;
import movierecsys.dal.MovieDAO;
import movierecsys.gui.model.MovieModel;

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

    private MovieModel movieModel;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        movieModel=new MovieModel();
        lstMovies.setItems(movieModel.moviesList);
        //Listener for changes in textfield
        txtMovieSearch.textProperty().addListener((obs, oldText, newText) -> movieModel.searchInputChange(newText));
    }

}