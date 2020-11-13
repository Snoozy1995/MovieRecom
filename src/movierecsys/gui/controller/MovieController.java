package movierecsys.gui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import movierecsys.be.Movie;
import movierecsys.be.Rating;
import movierecsys.bll.util.LoginManager;
import movierecsys.bll.util.MovieManager;
import movierecsys.bll.util.MovieRecommender;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MovieController implements Initializable {
    public ObservableList<String> moviesList= FXCollections.observableArrayList();
    public ObservableList<String> recommendedList= FXCollections.observableArrayList();
    public MovieManager movieManager=new MovieManager();
    public MovieRecommender movieRecommender=new MovieRecommender();
    @FXML
    private TextField txtMovieSearch;
    @FXML
    private ListView<String> lstMovies;
    @FXML
    private ListView<String> lstRecommended;
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        lstMovies.setItems(moviesList);
        lstRecommended.setItems(recommendedList);
        System.out.println("movieManager.getAllRatings()");
        List<Rating> allRatings=movieManager.getAllRatings();
        System.out.println("allRatings size:"+allRatings.size());
        System.out.println("movieManager.getRatingsByUser(LoginManager.getLoggedInUser())");
        List<Rating> ownRatings=movieManager.getRatingsByUser(LoginManager.getLoggedInUser());
        System.out.println("movieRecommender.getSimilarRecommendations()");
        List<Movie> recommendedMovies=movieRecommender.getSimilarRecommendations(allRatings,ownRatings);
        System.out.println("movieManager.toListString()");
        recommendedList.setAll(movieManager.toListString(recommendedMovies));

        //recommendedList.setAll(movieManager.toListString(movieRecommender.getSimilarRecommendations(movieManager.getAllRatings(), movieManager.getRatingsByUser(LoginManager.getLoggedInUser()))));
        txtMovieSearch.textProperty().addListener((obs, oldText, newText) -> moviesList.setAll(movieManager.toListString(movieManager.searchMovie(newText))));
    }
}
