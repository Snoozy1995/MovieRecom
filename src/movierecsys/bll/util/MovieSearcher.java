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
 * @author Snoozy
 */
public class MovieSearcher
{
    private MovieDAO movDAO=new MovieDAO(); // @todo move, shouldnt be here...
    public List<Movie> search(List<Movie> searchBase, String query) { // fix ioexception in search to remove ioexception here @todo
        try { if (searchBase == null) searchBase = movDAO.getAllMovies(); }
        catch(Exception e){e.printStackTrace();}
        finally{
            return searchBase.stream().filter((movie) -> {
                if (movie.getTitle().toLowerCase().trim().replaceAll("\\s+", "").indexOf(query.toLowerCase().trim().replaceAll("\\s+", "")) >= 0) return true;
                return false;
            }).collect(Collectors.toList());
        }
    }
}
