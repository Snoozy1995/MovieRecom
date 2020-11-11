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
 * @author pgn
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
        //Sorting -- remove videos already rated...
        allRatings=allRatings.stream().filter((rating)->{
            for(Rating rate:excludeRatings){
                if(rating.getMovie()==rate.getMovie()) return false;
            }
            return true;
        }).collect(Collectors.toList());

        //Count and sort ratings..@todo
        Map<Movie,Integer> mapOfObjects = new HashMap<Movie,Integer>();
        for(Rating rate:allRatings){
            if(mapOfObjects.get(rate.getMovie())!=null){
                mapOfObjects.put(rate.getMovie(),mapOfObjects.get(rate.getMovie())+rate.getRating());
            }else{
                mapOfObjects.put(rate.getMovie(),rate.getRating());
            }
        }
        Map<Movie,Integer> sortedMap=mapOfObjects.entrySet().stream()
            .sorted(Map.Entry.<Movie,Integer>comparingByValue().reversed())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                    (e1, e2) -> e1, LinkedHashMap::new));
        System.out.println(new ArrayList<Movie>(sortedMap.keySet()).get(0).getTitle());
        return new ArrayList<Movie>(sortedMap.keySet());
        //System.out.println("AllRatings sum:"+allRatings.stream().mapToDouble(Rating::getRating).sum());
        //TODO High average recommender
        //return null;
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
