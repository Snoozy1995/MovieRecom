package movierecsys.dal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import movierecsys.be.Movie;

/**
 * @author Snoozy1995
 */
public class MovieDAO {
    private static final String FILE_SOURCE = "data/movie_titles.txt";
    private static final String SQL_SOURCE="movies";
    public static List<Movie> moviesInMemory=null;
    public static HashMap<Integer,Movie> moviesHashMap=new HashMap<>();

    /**
     * Gets a list of all movies in the persistence storage.
     *
     * @return List of movies.
     */
    public static List<Movie> getAllMovies(){
        if(moviesInMemory!=null) return moviesInMemory;
        List<String> array;
        if(!DAOConfiguration.useSQL) {
            array = FileDAO.readFileToList(FILE_SOURCE);
        }else{
            array=SQLDAO.selectToStringList(SQL_SOURCE,"id,releaseYear,title");
        }
        List<Movie> allMovies= new ArrayList<>();
        for(String line: array){
            try {
                Movie movie=stringArrayToMovie(line);
                moviesHashMap.put(movie.getId(),movie);
                allMovies.add(movie);
            } catch (Exception ex) {
                System.out.println("["+line+"]\nCould not resolve string line to movie object, moving on to next line...");
            }
        }
        moviesInMemory=allMovies;
        return allMovies;
    }

    /**
     * Reads a movie from a , s
     *
     * @param t represents the line string.
     * @return Movie class object
     * @throws NumberFormatException Due to invalid parseInts.
     */
    private static Movie stringArrayToMovie(String t) {
        String[] arrMovie = t.split(",");
        int year=0;
        if(!arrMovie[1].equals("NULL")){ year=Integer.parseInt(arrMovie[1]); }
        StringBuilder title = new StringBuilder(arrMovie[2]);
        if (arrMovie.length > 3) {
            for (int i = 3; i < arrMovie.length; i++) {
                title.append(",").append(arrMovie[i]);
            }
        }
        return new Movie(Integer.parseInt(arrMovie[0]),year, title.toString());
    }

    /**
     * Creates a movie in the persistence storage.
     *
     * @param releaseYear The release year of the movie
     * @param title       The title of the movie
     * @return The object representation of the movie added to the persistence
     * storage.
     */
    private static Movie createMovie(int releaseYear, String title) {
        int id=getNewID();
        if(!DAOConfiguration.useSQL){
            FileDAO.appendLineToFile(FILE_SOURCE,id+","+releaseYear+","+title);
        }else{
            SQLDAO.insertToTable(SQL_SOURCE,"id,releaseYear,title",id+","+releaseYear+","+title);
        }

        Movie movie=new Movie(id, releaseYear, title);
        moviesInMemory.add(movie);
        return movie;
    }

    /**
     * Deletes a movie from the persistence storage.
     *
     * @param movie The movie to delete.
     */
    private static void deleteMovie(Movie movie) {
        moviesInMemory.remove(movie);
        if(!DAOConfiguration.useSQL) saveStorage();
        else SQLDAO.deleteFromTable(SQL_SOURCE,"id="+movie.getId());
    }

    /**
     * Updates the movie in the persistence storage to reflect the values in the
     * given Movie object.
     *
     * @param movie The updated movie.
     */
    private static void updateMovie(Movie movie) {
        if(getMovie(movie.getId())==null){ moviesInMemory.add(movie); }
        if(!DAOConfiguration.useSQL) saveStorage();
        else SQLDAO.updateToTable(SQL_SOURCE,"releaseYear="+movie.getYear()+",title="+movie.getTitle(),"id="+movie.getId());
    }

    /**
     * Gets a the movie with the given ID.
     *
     * @param id ID of the movie.
     * @return A Movie object.
     */
    public static Movie getMovie(int id) {
        return getAllMovies().stream().filter(a -> a.getId() == id).collect(Collectors.toList()).get(0);
    }

    /**
     * Gets a new ID available for a new addition(Movie).
     *
     * @return Integer representing the new ID to be used..
     */
    private static Integer getNewID(){
        int maxValue=-1;
        List<Movie> moviesList=getAllMovies();
        for(Movie movie:moviesList){
            if(maxValue<movie.getId()){ maxValue=movie.getId(); }
        }
        return maxValue+1;
    }

    /**
     * Saves the persistent storage for the movies.
     */
    private static void saveStorage(){
        if(!DAOConfiguration.useSQL) return;
        List<String> out=new ArrayList<>();
        for(Movie movie:moviesInMemory){
            out.add(movie.getId()+","+movie.getYear()+","+movie.getTitle());
        }
        FileDAO.saveListToFile(FILE_SOURCE, out);
    }
}
