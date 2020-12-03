package movierecsys.bll.util;

import java.util.*;
import java.util.stream.Collectors;

import movierecsys.be.Movie;
import movierecsys.be.Rating;
import movierecsys.be.User;
import movierecsys.dal.RatingDAO;

/**
 *
 * @author Snoozy1995
 */
public class MovieRecommender
{
    /**
     * Returns a list of movie recommendations based on the highest total recommendations. Excluding already rated movies from the list of results.
     * @param allRatings List of all users ratings.
     * @param excludeRatings List of Ratings (aka. movies) to exclude.
     * @return Sorted list of movies recommended to the caller. Sorted in descending order.
     */
    public List<Movie> highAverageRecommendations(List<Rating> allRatings, List<Rating> excludeRatings)
    {
        if(badAllRatingsParameter(allRatings)) return null;
        allRatings=excludeFromRatingIfSameMovie(allRatings,excludeRatings);

        //Using a hashmap and java stream -> count and sort ratings...
        HashMap<Movie,Integer> mapOfObjects = new HashMap<>();
        for(Rating rate:allRatings){
            mapOfObjects.merge(rate.getMovie(), rate.getRating(), Integer::sum);
        }

        return new ArrayList<>(sortHashMap(mapOfObjects).keySet());
    }


    public List<Movie> averageRecommendations(List<Rating> allRatings, List<Rating> excludeRatings)
    {
        if(badAllRatingsParameter(allRatings)) return null;
        allRatings=excludeFromRatingIfSameMovie(allRatings,excludeRatings);

        //Using a hashmap and java stream -> count and sort ratings...
        HashMap<Movie,Integer> mapOfObjectsSum = new HashMap<>();
        HashMap<Movie,Integer> mapOfObjectsCounter = new HashMap<>();
        for(Rating rate:allRatings){
            mapOfObjectsSum.merge(rate.getMovie(), rate.getRating(), Integer::sum);
            mapOfObjectsCounter.merge(rate.getMovie(),1,Integer::sum);
        }

        HashMap<Movie,Integer> mapOfObjectsFinal = new HashMap<>();

        for (Map.Entry<Movie,Integer> me : mapOfObjectsSum.entrySet()) {
            mapOfObjectsFinal.put(me.getKey(),me.getValue()/mapOfObjectsCounter.get(me.getKey()));
        }

        return new ArrayList<>(sortHashMap(mapOfObjectsFinal).keySet());
    }

    private HashMap<Movie,Integer> sortHashMap(HashMap<Movie,Integer> hashmap){
        return hashmap.entrySet().stream()
                .sorted(Map.Entry.<Movie,Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }

    /**
     * Returns a list of movie recommendations based on weighted recommendations. Excluding already rated movies from the list of results. 
     * @param allRatings List of all users ratings.
     * @param excludeRatings List of Ratings (aka. movies) to exclude. 
     * @return Sorted list of movies recommended to the caller. Sorted in descending order.
     */
    public List<Movie> weightedRecommendations(List<Rating> allRatings, List<Rating> excludeRatings)
    {
        //TODO Weighted recommender
        return null;
    }

    /**
     * Returns a list of movie recommendations based on two factors: Looking at movies rated yourself and taking other users similar into account:
     * Then high average of that. Excluding already rated movies from the list of results.
     * @param allRatings List of all users ratings.
     * @param ownRatings List of Ratings (aka. movies) that the user rated themselves.
     * @return Sorted list of movies recommended to the caller. Sorted in descending order.
     */
    public List<Movie> getSimilarRecommendations(List<Rating> allRatings, List<Rating> ownRatings){
        if(badAllRatingsParameter(allRatings)) return null;
        //Retrieve all ratings from movies that also you rated yourself...
        System.out.println("Step 1 ownRatings size:"+ownRatings.size());
        List<Rating> sameMovieRatings=allRatings.parallelStream().filter((rating)->{
            if(rating.getRating()==0) return false;
            for(Rating rate:ownRatings){
                int similarityBias=5; //When shouldnt we trust a person. This number seems a bit arbitrary, it doesnt actual equal 5 in terms of rating scale, not sure
                if(rating==rate) return false; // If rating same as rate then dont include.
                else if(rating.getRating()*rate.getRating()<similarityBias) return false; //@todo maybe remove, but for now keep. removes any bad similarity rating.
                else if(rating.getMovie().getId()==rate.getMovie().getId()) return true; //If same movie, include.
            }
            return false; // Dont include.
        }).collect(Collectors.toList());

        HashMap<User,Integer> similarityMap= new HashMap<>();
        HashMap<User,Integer> similarityMapValue=new HashMap<>();
        HashMap<User,Integer> similarityMapCounter=new HashMap<>();
        System.out.println("Step 2 sameMovieRatings:"+sameMovieRatings.size());
        //Getting values and the counts for each user to be averaged in next for loop...
        for(Rating rate:sameMovieRatings){
            for(Rating ownRate:ownRatings){
                if(rate.getMovie()==ownRate.getMovie()){
                    similarityMapValue.merge(rate.getUser(), rate.getRating()*ownRate.getRating(), Integer::sum);
                    similarityMapCounter.merge(rate.getUser(),1,Integer::sum);
                }
            }
        }

        System.out.println("Step 3 similarityFactorAveraging");
        List<Rating> userRatings=new ArrayList<>();
        //Averaging for the similarity factor:
        for (Map.Entry<User,Integer> me : similarityMapValue.entrySet()) {
            similarityMap.put(me.getKey(), me.getValue()/similarityMapCounter.get(me.getKey()));
            userRatings.addAll(excludeFromRatingIfSameMovie(RatingDAO.userListHashMap.get(me.getKey()),ownRatings)); //Note: excludeFromRatingIfSameMovie happens here, so any movie that's already been rated by the ownRatings, wont be included
        }

        System.out.println("Step 4 userRating size:"+userRatings.size());
        //Applying similarityFactor to values to be sorted.
        HashMap<Movie,Integer> moviesHashMap=new HashMap<>();
        for(Rating userRating: userRatings){
            moviesHashMap.merge(userRating.getMovie(),userRating.getRating()*similarityMap.get(userRating.getUser()),Integer::sum);
        }

        System.out.println("Step 5");
        // Sorts and returns List<Movie> accordingly.
        return new ArrayList<>(sortHashMap(moviesHashMap).keySet());
    }

    private boolean badAllRatingsParameter(List<Rating> allRatings){
        return (allRatings == null || allRatings.size() == 0);
    }

    private List<Rating> excludeFromRatingIfSameMovie(List<Rating> allRatings, List<Rating> excludeRatings){
        if(excludeRatings==null||excludeRatings.size()==0) return allRatings;
        //Sorting -- remove videos already rated...
        return allRatings.parallelStream().filter((rating)->{
            for(Rating rate:excludeRatings){
                if(rating.getMovie()==rate.getMovie()) return false;
            }
            return true;
        }).collect(Collectors.toList());
    }
    
}
