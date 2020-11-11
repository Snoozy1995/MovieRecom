package movierecsys.bll.util;

import movierecsys.be.Movie;
import movierecsys.dal.MovieDAO;

import java.util.ArrayList;
import java.util.List;

public class MovieManager {
    private MovieDAO movDAO=new MovieDAO(); // @todo move, shouldnt be here...
    private MovieSearcher movSearcher=new MovieSearcher();

    public MovieManager(){
        try{ movDAO.getAllMovies(); }
        catch(Exception e){ }
    }

    public List<Movie> searchMovie(String query){
        return movSearcher.search(movDAO.moviesInMemory,query);
    }

    public List<String> toListString(List<Movie> moviesList){
        List<String> moviesString=new ArrayList<String>();
        for(Movie movie:moviesList){
            moviesString.add("["+movie.getId()+"] "+movie.getTitle()+" ("+movie.getYear()+")");
        }
        return moviesString;
    }
}
