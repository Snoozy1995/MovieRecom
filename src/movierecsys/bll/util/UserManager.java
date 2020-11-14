package movierecsys.bll.util;

import movierecsys.be.User;
import movierecsys.dal.UserDAO;

import java.util.ArrayList;
import java.util.List;

public class UserManager {

    public static List<String> getUsersListView(){
        List<User> users= UserDAO.getAllUsers();
        List<String> usersListView=new ArrayList<>();
        for(User user:users){
            usersListView.add("["+user.getId()+"] "+user.getName());
        }
        return usersListView;
    }
}
