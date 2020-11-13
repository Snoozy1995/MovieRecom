package movierecsys.bll.util;

import movierecsys.be.User;

public class LoginManager {
    private static User loggedIn=null;

    public static void login(User user){
        loggedIn=user;
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
}
