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
        //@todo parameter error handle?
        //Sorting -- remove videos already rated...
        allRatings=allRatings.stream().filter((rating)->{
            for(Rating rate:excludeRatings){
                if(rating.getMovie()==rate.getMovie()) return false;
            }
            return true;
        }).collect(Collectors.toList());

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
        //TODO Weighted recommender
        return null;
    }
    
}
