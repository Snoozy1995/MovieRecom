/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.bll.util;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import movierecsys.be.Movie;
import movierecsys.dal.MovieDAO;

/**
 *
 * @author pgn
 */
public class MovieSearcher
{
    public List<Movie> search(List<Movie> searchBase, String query) throws IOException { // fix ioexception in search to remove ioexception here @todo
        System.out.println("query1: "+query);
        return searchBase.stream().filter((movie)-> {
            System.out.println("Movie: "+movie.getTitle());
            //int searchSimilarityRate;
            int similar=0;
            for(int x=0;x<query.length();x++){
                System.out.println("charAt: "+query.charAt(x));
                System.out.println("indexOf: "+movie.getTitle().toLowerCase().indexOf(query.toLowerCase().charAt(x)));
                if(movie.getTitle().toLowerCase().indexOf(query.toLowerCase().charAt(x))>=0){
                    similar++;
                }
            }
            //similarRate=similar/query.length()
            if(similar==query.length()) return true;
            return false;
        }).collect(Collectors.toList());
        //TODO Movie search
    }
    
}
