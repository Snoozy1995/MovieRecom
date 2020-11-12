/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.dal;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import movierecsys.be.Movie;
import movierecsys.be.Rating;
import movierecsys.be.User;

/**
 *
 * @author Snoozy1995
 */


//todo test functions below thoroughly



public class RatingDAO
{
    private static final String MOVIE_SOURCE = "data/movie_titles.txt";
    public static List<Rating> ratingsInMemory=null;
    /**
     * Persists the given rating.
     * @param rating the rating to persist.
     */
    public static Rating createRating(Movie movie, User user, int rating){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(MOVIE_SOURCE, true));
            writer.write(movie.getId()+","+user.getId()+","+rating);
            writer.close();
        }catch(Exception e){
            System.out.println("Problem saving to persistent storage, only saved in memory.");
        }
        Rating rate=new Rating(movie,user,rating);
        ratingsInMemory.add(rate);
        return rate;
    }
    
    /**
     * Updates the rating to reflect the given object.
     * @param rating The updated rating to persist.
     */
    public void updateRating(Rating rating)
    {
        if(ratingsInMemory.stream().filter(a -> a==rating).collect(Collectors.toList()).get(0)==null){
            ratingsInMemory.add(rating);
        }
        saveStorage();
    }
    
    /**
     * Removes the given rating.
     * @param rating 
     */
    public void deleteRating(Rating rating)
    {
        ratingsInMemory.remove(rating);
        saveStorage();
    }
    
    /**
     * Gets all ratings from all users.
     * @return List of all ratings.
     */
    public static List<Rating> getAllRatings()
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
    private static Rating stringArrayToRating(String t) {
        String[] arrMovie = t.split(",");
        int movieId = Integer.parseInt(arrMovie[0]);
        Movie movie=MovieDAO.getAllMovies().stream().filter(a -> a.getId() == movieId).collect(Collectors.toList()).get(0);
        //...
        int userId = Integer.parseInt(arrMovie[1]);
        User user=UserDAO.getAllUsers().stream().filter(a -> a.getId() == userId).collect(Collectors.toList()).get(0);
        //...
        int rating = Integer.parseInt(arrMovie[2]);
        return new Rating(movie,user,rating);
    }
    
    /**
     * Get all ratings from a specific user.
     * @param user The user 
     * @return The list of ratings.
     */
    public static List<Rating> getRatings(User user){
        return ratingsInMemory.stream().filter(a -> a.getUser() == user).collect(Collectors.toList());
    }

    private static void saveStorage(){
        try{
            File file = new File(MOVIE_SOURCE);
            List<String> out= new ArrayList<>();
            for(Rating rating:ratingsInMemory){
                out.add(rating.getMovie().getId()+","+rating.getUser().getId()+","+rating.getRating());
            }
            Files.write(file.toPath(), out, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        }catch(Exception e){
            System.out.println("[RatingDAO] Problem saving to persistent storage, only saved in memory.");
        }
    }
    
}
