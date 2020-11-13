/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.bll.util;

import java.util.List;
import java.util.stream.Collectors;
import movierecsys.be.Movie;

/**
 *
 * @author Snoozy
 */
public class MovieSearcher {
    public List<Movie> search(List<Movie> searchBase, String query) {
        return searchBase.stream().filter((movie) ->(movie.getTitle().toLowerCase().trim().replaceAll("\\s+", "").indexOf(query.toLowerCase().trim().replaceAll("\\s+", "")) >= 0)).collect(Collectors.toList());
    }
}
