/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.dal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import movierecsys.be.Movie;
import movierecsys.be.User;

/**
 *
 * @author Snoozy1995
 */


//todo test functions below thoroughly


public class UserDAO
{
    private static final String MOVIE_SOURCE = "data/movie_titles.txt";
    public static List<User> usersInMemory=null;
    /**
     * Gets a list of all known users.
     * @return List of users.
     */
    public static List<User> getAllUsers(){
        if(usersInMemory!=null) return usersInMemory;
        List<User> allUsers = new ArrayList<User>();
        File file = new File(MOVIE_SOURCE);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    User user = stringArrayToUser(line);
                    allUsers.add(user);
                } catch (Exception ex) {
                    System.out.println("Could not resolve string line to movie, moving on to next line...\n["+line+"]");
                    //Do nothing we simply do not accept malformed lines of data.
                    //In a perfect world you should at least log the incident.
                }
            }
        }catch(Exception e){
            //todo handle
        }
        usersInMemory=allUsers;
        return allUsers;
    }

    /**
     * Reads a movie from a , s
     *
     * @param t represents the line string.
     * @return Movie class object
     * @throws NumberFormatException
     */
    private static User stringArrayToUser(String t) {
        String[] arrUser = t.split(",");

        int id = Integer.parseInt(arrUser[0]);
        String name = arrUser[1];
        if (arrUser.length > 2) {
            for (int i = 2; i < arrUser.length; i++) {
                name += "," + arrUser[i];
            }
        }
        User user = new User(id,name);
        return user;
    }
    
    /**
     * Gets a single User by its ID.
     * @param id The ID of the user.
     * @return The User with the ID.
     */
    public User getUser(int id)
    {
        //TODO Get User
        return null;
    }
    
    /**
     * Updates a user so the persistence storage reflects the given User object.
     * @param user The updated user.
     */
    public void updateUser(User user)
    {
        //TODO Update user.
    }
    
}
