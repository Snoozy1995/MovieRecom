package movierecsys.gui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import movierecsys.be.Movie;
import movierecsys.be.Rating;
import movierecsys.bll.util.LoginManager;
import movierecsys.bll.util.MovieManager;
import movierecsys.bll.util.MovieRecommender;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class MovieController implements Initializable {
    public ObservableList<String> moviesList= FXCollections.observableArrayList();
    public ObservableList<String> recommendedList= FXCollections.observableArrayList();
    public ObservableList<String> watchedList= FXCollections.observableArrayList();
    public MovieManager movieManager=new MovieManager();
    public MovieRecommender movieRecommender=new MovieRecommender();
    private Movie currentMovie=null;
    @FXML
    private Pane movieRatingPane;
    @FXML
    private Button deleteMovieRatingsButton;
    @FXML
    private Button deleteMovieButton;
    @FXML
    private TextField txtMovieSearch;
    @FXML
    private ListView<String> lstMovies;
    @FXML
    private ListView<String> lstRecommended;
    @FXML
    private ListView<String> lstWatched;
    @FXML
    private Text txtMovieTitle;
    @FXML
    private Text txtMovieYear;
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        long startTime = new Date().getTime();
        setMovieViewVisibility(false);
        lstMovies.setItems(moviesList);
        lstRecommended.setItems(recommendedList);
        lstWatched.setItems(watchedList);
        System.out.println("movieManager.getAllRatings()");
        List<Rating> allRatings=movieManager.getAllRatings();
        System.out.println("allRatings size:"+allRatings.size());
        System.out.println("movieManager.getRatingsByUser(LoginManager.getLoggedInUser())");
        List<Rating> ownRatings=movieManager.getRatingsByUser(LoginManager.getLoggedInUser());
        System.out.println("movieRecommender.getSimilarRecommendations()");
        List<Movie> recommendedMovies=movieRecommender.getSimilarRecommendations(allRatings,ownRatings);
        System.out.println("movieManager.toListString()");
        recommendedList.setAll(movieManager.toListString(recommendedMovies));
        watchedList.setAll(movieManager.toListStringRating(ownRatings));

        lstMovies.setOnMouseClicked(click ->selectMovie(click,lstMovies));
        lstRecommended.setOnMouseClicked(click ->selectMovie(click,lstRecommended));
        lstWatched.setOnMouseClicked(click ->selectMovie(click,lstWatched));

        long endTime = new Date().getTime();
        long difference = endTime - startTime;
        System.out.println("Elapsed time in milliseconds: " + difference);
        txtMovieSearch.textProperty().addListener((obs, oldText, newText) -> moviesList.setAll(movieManager.toListString(movieManager.searchMovie(newText))));
    }

    private void selectMovie(MouseEvent click,ListView<String> lst) {
        if (click.getClickCount() != 2) return;
        int id=Integer.parseInt(lst.getSelectionModel().getSelectedItem().split("]")[0].replace("[",""));
        selectMovie(movieManager.getMovie(id));
    }

    @FXML
    private void onDeleteMovie(ActionEvent e){
        if(currentMovie==null) return;
        //@todo delete movie
    }

    void selectMovie(Movie movie){
        if(movie==null) {
            currentMovie=null;
            txtMovieTitle.setText("Title: N/A");
            txtMovieYear.setText("Release Year: N/A");
            setMovieViewVisibility(false);
            return;
        }
        currentMovie=movie;
        String title=movie.getTitle();
        if(title.length()>25){ title=title.substring(0,24); } //@todo see for other ways to handle..
        txtMovieTitle.setText("Title: "+title);
        txtMovieYear.setText("Release Year: "+movie.getYear());
        setMovieViewVisibility(true);
        //Reset rating UI @todo
    }

    void setMovieViewVisibility(boolean show){
        deleteMovieButton.setVisible(show);
        deleteMovieRatingsButton.setVisible(show);
        for( Node node: movieRatingPane.getChildren()) {
            node.setVisible(show);
        }
    }
}
