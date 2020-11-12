/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.dal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import movierecsys.be.Movie;
import movierecsys.be.Rating;
import movierecsys.be.User;

/**
 *
 * @author pgn
 */
public class RatingDAO
{
    private static final String MOVIE_SOURCE = "data/movie_titles.txt";
    public List<Rating> ratingsInMemory=null;
    /**
     * Persists the given rating.
     * @param rating the rating to persist.
     */
    public void createRating(Rating rating)
    {
        //TODO Rate movie
    }
    
    /**
     * Updates the rating to reflect the given object.
     * @param rating The updated rating to persist.
     */
    public void updateRating(Rating rating)
    {
        //TODO Update rating
    }
    
    /**
     * Removes the given rating.
     * @param rating 
     */
    public void deleteRating(Rating rating)
    {
        //TODO Delete rating
    }
    
    /**
     * Gets all ratings from all users.
     * @return List of all ratings.
     */
    public List<Rating> getAllRatings()
    {
        if(ratingsInMemory!=null) return ratingsInMemory;
        List<Rating> allRatings = new ArrayList<>();
        File file = new File(MOVIE_SOURCE);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    Rating rating = stringArrayToRating(line);
                    allRatings.add(rating);
                } catch (Exception ex) {
                    System.out.println("Could not resolve string line to movie, moving on to next line...\n["+line+"]");
                    //Do nothing we simply do not accept malformed lines of data.
                    //In a perfect world you should at least log the incident.
                }
            }
        }catch(Exception e){
            //todo
        }
        ratingsInMemory=allRatings;
        return ratingsInMemory;
    }

    /**
     * Reads a movie from a , s
     *
     * @param t represents the line string.
     * @return Movie class object
     * @throws NumberFormatException
     */
    private Rating stringArrayToRating(String t) {
        String[] arrMovie = t.split(",");
        int movieId = Integer.parseInt(arrMovie[0]);
        Movie movie=MovieDAO.getAllMovies().stream().filter(a -> a.getId() == movieId).collect(Collectors.toList()).get(0);
        //...
        int userId = Integer.parseInt(arrMovie[1]);
        //User user=UserDAO.getAllUsers().stream().filter(a -> a.getId() == userId).collect(Collectors.toList()).get(0);
        //...
        int rating = Integer.parseInt(arrMovie[2]);
        //return new Rating(movie,user,rating);
        return null;
    }
    
    /**
     * Get all ratings from a specific user.
     * @param user The user 
     * @return The list of ratings.
     */
    public List<Rating> getRatings(User user)
    {
        //TODO Get user ratings.
        return null;
    }
    
}
