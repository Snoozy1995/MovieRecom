package movierecsys.dal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import movierecsys.be.Movie;
import movierecsys.be.Rating;
import movierecsys.be.User;

/**
 * @author Snoozy1995
 */
public class RatingDAO {
    private static final String FILE_SOURCE = "data/ratings.txt";
    private static String SQL_SOURCE="ratings";
    public static List<Rating> ratingsInMemory=null;
    public static HashMap<User,List<Rating>> userListHashMap=new HashMap<>();
    public static HashMap<Movie,List<Rating>> movieListHashMap=new HashMap<>();
    /**
     * Persists the given rating.
     * @param rating the rating to persist.
     */
    public static Rating createRating(Movie movie, User user, int rating){
        if(!DAOConfiguration.useSQL){
            FileDAO.appendLineToFile(FILE_SOURCE,movie.getId()+","+user.getId()+","+rating);
        }else{
            SQLDAO.insertToTable(SQL_SOURCE,"movie,user,rating",movie.getId()+","+user.getId()+","+rating);
        }
        Rating rate=new Rating(movie,user,rating);
        ratingsInMemory.add(rate);
        return rate;
    }
    
    /**
     * Updates the rating to reflect the given object.
     * @param rating The updated rating to persist.
     */
    public void updateRating(Rating rating){
        if(getAllRatings().stream().filter(a -> a==rating).collect(Collectors.toList()).get(0)==null){
            ratingsInMemory.add(rating);
            if(!DAOConfiguration.useSQL) saveStorage();
            else SQLDAO.insertToTable(SQL_SOURCE,"movie,user,rating",rating.getMovie().getId()+","+rating.getUser().getId()+","+rating);
        }else{
            if(!DAOConfiguration.useSQL) saveStorage();
            else SQLDAO.updateToTable(SQL_SOURCE,"rating="+rating.getRating(),"movie="+rating.getMovie().getId()+" AND user="+rating.getUser().getId());
        }
    }
    
    /**
     * Removes the given rating.
     * @param rating The rating to be deleted.
     */
    public void deleteRating(Rating rating){
        ratingsInMemory.remove(rating);
        if(!DAOConfiguration.useSQL) saveStorage();
        else SQLDAO.deleteFromTable(SQL_SOURCE,"movie="+rating.getMovie().getId()+" AND user="+rating.getUser().getId());
    }
    
    /**
     * Gets all ratings from all users.
     * @return List of all ratings.
     */
    public static List<Rating> getAllRatings() {
        if(ratingsInMemory!=null) return ratingsInMemory;
        List<String> array=new ArrayList<>();
        if(!DAOConfiguration.useSQL) {
            array = FileDAO.readFileToList(FILE_SOURCE);
        }else{
            array=SQLDAO.selectToStringList(SQL_SOURCE,"movie,user,rating");
        }
        List<Rating> allRatings = new ArrayList<>();
        for(String line: array){
            try {
                allRatings.add(stringArrayToRating(line));
            } catch (Exception ex) {
                System.out.println("["+line+"]\nCould not resolve string line to rating object, moving on to next line...");
            }
        }
        ratingsInMemory=allRatings;
        return ratingsInMemory;
    }

    /**
     * Reads a movie from a , s
     *
     * @param t represents the line string.
     * @return Rating class object
     * @throws NumberFormatException Due to invalid parseInts.
     */
    private static Rating stringArrayToRating(String t) {
        int first=t.indexOf(',');
        int last=t.lastIndexOf(',');

        Movie movie=MovieDAO.moviesHashMap.get(Integer.parseInt(t.substring(0,first)));
        User user=UserDAO.usersHashMap.get(Integer.parseInt(t.substring(first+1,last)));
        Rating rating=new Rating(movie,user,Integer.parseInt(t.substring(last+1)));
        if(movieListHashMap.get(movie)==null){
            movieListHashMap.put(movie,new ArrayList<>());
        }
        if(userListHashMap.get(user)==null){
            userListHashMap.put(user,new ArrayList<>());
        }
        movieListHashMap.get(movie).add(rating);
        userListHashMap.get(user).add(rating);
        return rating;
    }
    
    /**
     * Get all ratings from a specific user.
     * @param user The user 
     * @return The list of ratings.
     */
    public static List<Rating> getRatings(User user){
        return getAllRatings().parallelStream().filter(a -> a.getUser() == user).collect(Collectors.toList());
    }

    private static void saveStorage(){
        if(!DAOConfiguration.useSQL) return;
        List<String> out= new ArrayList<>();
        for(Rating rating:ratingsInMemory){
            out.add(rating.getMovie().getId()+","+rating.getUser().getId()+","+rating.getRating());
        }
        FileDAO.saveListToFile(FILE_SOURCE, out);
    }
}
