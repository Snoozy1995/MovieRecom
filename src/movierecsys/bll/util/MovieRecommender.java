package movierecsys.bll.util;

import java.util.*;
import java.util.stream.Collectors;

import movierecsys.be.Movie;
import movierecsys.be.Rating;
import movierecsys.be.User;

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
        //if(badAllRatingsParameter(allRatings)) return null;
        //allRatings=excludeFromRatingIfSameMovie(allRatings,excludeRatings);
/*
        //Using a hashmap and java stream -> count and sort ratings...
        Map<Movie,Integer> mapOfObjects = new HashMap<>();
        for(Rating rate:allRatings){
            mapOfObjects.merge(rate.getMovie(), rate.getRating(), Integer::sum);
        }
        Map<Movie,Integer> sortedMap=mapOfObjects.entrySet().stream()
                .sorted(Map.Entry.<Movie,Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));*/

        //return new ArrayList<>(sortedMap.keySet());
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
        //Do either a highAverageRecommendation or weighted recommendation. For now highAverageRecommendation:...
        return processRecommendationsDefault(allRatings,ownRatings);
    }

    private List<Movie> processRecommendationsDefault(List<Rating> allRatings,List<Rating> ownRatings){
        if(badAllRatingsParameter(allRatings)) return null;
        //Retrieve all ratings from movies that also you rated yourself...
        List<Rating> sameMovieRatings=allRatings.stream().filter((rating)->{
            for(Rating rate:ownRatings){
                if(rating==rate) return false; // If rating same as rate then dont include.
                if(rating.getMovie()==rate.getMovie()) return true; //If same movie then include.
            }
            return false; // Dont include.
        }).collect(Collectors.toList());

        //Calculate similarity factor
        HashMap<User,Integer> similarityMap= new HashMap<>();
        HashMap<User,Integer> similarityMapValue=new HashMap<>();
        HashMap<User,Integer> similarityMapCounter=new HashMap<>();
        for(Rating rate:allRatings){
            for(Rating ownRate:ownRatings){
                similarityMapValue.merge(rate.getUser(), ownRate.getRating()*rate.getRating(), Integer::sum);
                similarityMapCounter.merge(rate.getUser(),1,Integer::sum);
            }
        }

        for (Map.Entry<User,Integer> me : similarityMapValue.entrySet()) {
            similarityMap.put(me.getKey(), me.getValue()/similarityMapCounter.get(me.getKey()));
        }

        //Retrieve all ratings from the users that did those ratings and excludeFromRatingIfSameMovie:
        List<Rating> userRatings=excludeFromRatingIfSameMovie(allRatings.stream().filter((rating)->{
            for(Rating rate:sameMovieRatings){
                if(rating.getUser()==rate.getUser()) return true;
            }
            return false;
        }).collect(Collectors.toList()),ownRatings);


        HashMap<Movie,Integer> moviesHashMap=new HashMap<>();

        //Applying similarityFactor to values to be sorted.
        for(Rating userRating: userRatings){
            moviesHashMap.merge(userRating.getMovie(),userRating.getRating()*similarityMap.get(userRating.getUser()),Integer::sum);
        }
        // Sorts and returns List<Movie> accordingly.
        return new ArrayList<>(sortHashMap(moviesHashMap).keySet());
    }

    private boolean badAllRatingsParameter(List<Rating> allRatings){
        return (allRatings == null || allRatings.size() == 0);
    }

    private List<Rating> excludeFromRatingIfSameMovie(List<Rating> allRatings, List<Rating> excludeRatings){
        if(excludeRatings==null||excludeRatings.size()==0) return allRatings;
        //Sorting -- remove videos already rated...
        return allRatings.stream().filter((rating)->{
            for(Rating rate:excludeRatings){
                if(rating.getMovie()==rate.getMovie()) return false;
            }
            return true;
        }).collect(Collectors.toList());
    }
    
}
