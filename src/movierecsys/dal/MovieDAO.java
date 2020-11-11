/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.dal;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import movierecsys.be.Movie;

/**
 * @author Snoozy1995
 */
public class MovieDAO {

    private static final String MOVIE_SOURCE = "data/movie_titles.txt";
    private static final Path MOVIE = ;
    public List<Movie> moviesInMemory=null;

    //todo test functions below thoroughly


    /**
     * Gets a list of all movies in the persistence storage.
     *
     * @return List of movies.
     * @throws java.io.FileNotFoundException
     */
    public List<Movie> getAllMovies() throws FileNotFoundException, IOException {
        List<Movie> allMovies = new ArrayList<>();
        File file = new File(MOVIE_SOURCE);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    Movie mov = stringArrayToMovie(line);
                    allMovies.add(mov);
                } catch (Exception ex) {
                    System.out.println("Could not resolve stringline to movie, moving on to next line...\n["+line+"]");
                    //Do nothing we simply do not accept malformed lines of data.
                    //In a perfect world you should at least log the incident.
                }
            }
        }
        moviesInMemory=allMovies;
        return allMovies;
    }

    /**
     * Reads a movie from a , s
     *
     * @param t
     * @return
     * @throws NumberFormatException
     */
    private Movie stringArrayToMovie(String t) {
        String[] arrMovie = t.split(",");

        int id = Integer.parseInt(arrMovie[0]);
        int year = Integer.parseInt(arrMovie[1]);
        String title = arrMovie[2];
        if (arrMovie.length > 3) {
            for (int i = 3; i < arrMovie.length; i++) {
                title += "," + arrMovie[i];
            }
        }
        Movie mov = new Movie(id, year, title);
        return mov;
    }

    /**
     * Creates a movie in the persistence storage.
     *
     * @param releaseYear The release year of the movie
     * @param title       The title of the movie
     * @return The object representation of the movie added to the persistence
     * storage.
     */
    private Movie createMovie(int releaseYear, String title) {
        int id=getNewID();
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(MOVIE_SOURCE, true));
            writer.write(id+","+releaseYear+","+title);
            writer.close();
        }catch(Exception e){
            System.out.println("Problem saving to persistent storage, only saved in memory.");
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
    private void deleteMovie(Movie movie) {
        moviesInMemory.remove(movie);
        saveStorage();
    }

    /**
     * Updates the movie in the persistence storage to reflect the values in the
     * given Movie object.
     *
     * @param movie The updated movie.
     */
    private void updateMovie(Movie movie) {
        //todo check if it exists in the memory or add it...
        saveStorage();
    }

    /**
     * Gets a the movie with the given ID.
     *
     * @param id ID of the movie.
     * @return A Movie object.
     */
    private Movie getMovie(int id) {
        return moviesInMemory.stream().filter(a -> a.getId() == id).collect(Collectors.toList()).get(0);
    }

    private Integer getNewID(){
        int maxValue=-1;
        for(Movie movie:moviesInMemory){
            if(maxValue<movie.getId()){
                maxValue=movie.getId();
            }
        }
        return maxValue+1;
    }

    private void saveStorage(){
        try{
            File file = new File(MOVIE_SOURCE);
            List<String> out= new ArrayList<String>();
            for(Movie movie:moviesInMemory){
                out.add(movie.getId()+","+movie.getYear()+","+movie.getTitle());
            }
            Files.write(file.toPath(), out, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        }catch(Exception e){
            System.out.println("Problem saving to persistent storage, only saved in memory.")
        }
    }

}
