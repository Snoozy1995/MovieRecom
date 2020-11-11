package movierecsys.gui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import movierecsys.be.Movie;
import movierecsys.bll.util.MovieManager;
import movierecsys.bll.util.MovieSearcher;
import movierecsys.dal.MovieDAO;

import java.io.IOException;

public class MovieModel {
    public ObservableList<Movie> moviesList= FXCollections.observableArrayList();
    public MovieManager movieManager=new MovieManager();

    public void searchInputChange(String query){
        movieManager.searchMovie(query);
        if(movieManager.movies!=null){
            moviesList.setAll(movieManager.movies);
        }
    }
}
