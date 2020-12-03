package movierecsys.bll.util;

import movierecsys.be.Movie;
import movierecsys.be.Rating;
import movierecsys.be.User;
import movierecsys.dal.MovieDAO;
import movierecsys.dal.RatingDAO;

import java.util.ArrayList;
import java.util.List;

public class MovieManager {
    private final MovieSearcher movSearcher=new MovieSearcher();

    public MovieManager(){
        MovieDAO.getAllMovies();
    }

    public List<Rating> getAllRatings(){
        return RatingDAO.getAllRatings();
    }

    public Movie getMovie(int id){
        return MovieDAO.getMovie(id);
    }

    public void deleteMovie(Movie movie){
        MovieDAO.deleteMovie(movie);
    }

    public List<Rating> getRatingsByUser(User user){
        return RatingDAO.getRatings(user);
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

    public List<String> toListStringRating(List<Rating> ratingsList){
        List<String> moviesString=new ArrayList<>();
        for(Rating rating:ratingsList){
            moviesString.add("["+rating.getMovie().getId()+"] "+rating.getMovie().getTitle()+" Rating:"+rating.getRating());
        }
        return moviesString;
    }
}
