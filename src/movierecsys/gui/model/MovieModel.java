package movierecsys.gui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import movierecsys.be.Movie;
import movierecsys.bll.util.MovieSearcher;
import movierecsys.dal.MovieDAO;

import java.io.IOException;

public class MovieModel {
    public ObservableList<Movie> moviesList= FXCollections.observableArrayList();

    private MovieSearcher movSearcher=new MovieSearcher();

    void MovieModel(){
        //System.out.println("test constructor1");
    }
    public void searchInputChange(String query){
        moviesList.setAll(movSearcher.search(null,query));
    }
}
