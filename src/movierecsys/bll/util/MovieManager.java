package movierecsys.bll.util;

import movierecsys.be.Movie;
import movierecsys.dal.MovieDAO;
import movierecsys.dal.RatingDAO;
import movierecsys.dal.UserDAO;

import java.util.ArrayList;
import java.util.List;

public class MovieManager {
    private final MovieSearcher movSearcher=new MovieSearcher();

    public MovieManager(){
        MovieDAO.getAllMovies();
        UserDAO.getAllUsers();
        RatingDAO.getAllRatings();
    }

    public List<Movie> searchMovie(String query){
        return movSearcher.search(MovieDAO.getAllMovies(),query);
    }

    public List<String> toListString(List<Movie> moviesList){
        List<String> moviesString=new ArrayList<>();
        for(Movie movie:moviesList){
            moviesString.add("["+movie.getId()+"] "+movie.getTitle()+" ("+movie.getYear()+")");
        }
        return moviesString;
    }
}
