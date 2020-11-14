package movierecsys.bll.util;

import movierecsys.be.User;
import movierecsys.dal.UserDAO;

public class LoginManager {
    private static User loggedIn=null;

    public static void login(Integer id){
        loggedIn=UserDAO.getUser(id);
        if(loggedIn!=null){
            System.out.println("Logged in as: "+loggedIn.getName());
        }
    }
    public static void login(String username){
        //See if user exists...
        User user=UserDAO.getUser(username);
        System.out.println(user);
        if(user==null){ user=UserDAO.createUser(username); }
        loggedIn=user;
        System.out.println("Logged in as: "+loggedIn.getName());
    }
    public static void logout(){
        loggedIn=null;
    }

    public static boolean isLoggedIn(){
        return (loggedIn!=null);
    }
    public static boolean isLoggedIn(User user){
        return (loggedIn==user);
    }
    public static User getLoggedInUser(){
        return loggedIn;
    }
}
