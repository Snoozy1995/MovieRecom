package movierecsys.gui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import movierecsys.be.Movie;

public class MovieModel {
    public ObservableList<Movie> moviesList= FXCollections.observableArrayList();
}