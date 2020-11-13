/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.bll.util;

import java.util.*;
import java.util.stream.Collectors;

import movierecsys.be.Movie;
import movierecsys.be.Rating;

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
        allRatings=excludeFromRating(allRatings,excludeRatings);

        //Using a hashmap and java stream -> count and sort ratings...
        Map<Movie,Integer> mapOfObjects = new HashMap<>();
        for(Rating rate:allRatings){
            mapOfObjects.merge(rate.getMovie(), rate.getRating(), Integer::sum);
        }
        Map<Movie,Integer> sortedMap=mapOfObjects.entrySet().stream()
            .sorted(Map.Entry.<Movie,Integer>comparingByValue().reversed())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                    (e1, e2) -> e1, LinkedHashMap::new));

        return new ArrayList<>(sortedMap.keySet());
    }


    
    /**
     * Returns a list of movie recommendations based on weighted recommendations. Excluding already rated movies from the list of results. 
     * @param allRatings List of all users ratings.
     * @param excludeRatings List of Ratings (aka. movies) to exclude. 
     * @return Sorted list of movies recommended to the caller. Sorted in descending order.
     */
    public List<Movie> weightedRecommendations(List<Rating> allRatings, List<Rating> excludeRatings)
    {
        if(badAllRatingsParameter(allRatings)) return null;
        allRatings=excludeFromRating(allRatings,excludeRatings);
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

        //Retrieve all ratings from movies that also you rated yourself...
        List<Rating> sameMovieRatings=allRatings.stream().filter((rating)->{
            for(Rating rate:ownRatings){
                if(rating.getMovie()==rate.getMovie()) return true;
            }
            return false;
        }).collect(Collectors.toList());


        //Retrieve all ratings from the users that did those ratings...
        List<Rating> similarUsersRatings=allRatings.stream().filter((rating)->{
            for(Rating rate:sameMovieRatings){
                if(rating.getUser()==rate.getUser()) return true;
            }
            return false;
        }).collect(Collectors.toList());

        //Do either a highAverageRecommendation or weighted recommendation. For now highAverageRecommendation:...
        return highAverageRecommendations(similarUsersRatings,ownRatings);
    }

    private boolean badAllRatingsParameter(List<Rating> allRatings){
        if(allRatings==null||allRatings.size()==0) return true;
        return false;
    }

    private List<Rating> excludeFromRating(List<Rating> allRatings, List<Rating> excludeRatings){
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
