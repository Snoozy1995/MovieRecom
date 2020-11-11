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
    public List<Movie> search(List<Movie> searchBase, String query) { // fix ioexception in search to remove ioexception here @todo

        return searchBase.stream().filter((movie) ->(movie.getTitle().toLowerCase().trim().replaceAll("\\s+", "").indexOf(query.toLowerCase().trim().replaceAll("\\s+", "")) >= 0)).collect(Collectors.toList());
    }
}
