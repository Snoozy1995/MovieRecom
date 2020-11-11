package movierecsys.bll.util;

import movierecsys.be.Movie;
import movierecsys.dal.MovieDAO;

import java.util.List;

public class MovieManager {
    private MovieDAO movDAO=new MovieDAO(); // @todo move, shouldnt be here...
    private MovieSearcher movSearcher=new MovieSearcher();
    public List<Movie> movies=null;

    void MovieManager(){
        try {
            movies = movDAO.getAllMovies();
        }
        catch(Exception e){

        }
    }
    public void searchMovie(String query){
        movSearcher.search(movies,query);
    }
}
