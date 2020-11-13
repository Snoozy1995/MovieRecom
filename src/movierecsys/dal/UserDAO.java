package movierecsys.dal;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import movierecsys.be.User;

/**
 * @author Snoozy1995
 */
//todo remove inmemory? and implement sql also.
public class UserDAO {
    private static final String FILE_SOURCE = "data/users.txt";
    private static String SQL_SOURCE;
    public static List<User> usersInMemory=null;
    /**
     * Gets a list of all known users.
     * @return List of users.
     */
    public static List<User> getAllUsers(){
        if(usersInMemory!=null) return usersInMemory;
        List<String> array=new ArrayList<>();
        if(!DAOConfiguration.useSQL) {
            array = FileDAO.readFileToList(FILE_SOURCE);
        }
        List<User> allUsers = new ArrayList<>();
        for(String line: array){
            try {
                allUsers.add(stringArrayToUser(line));
            } catch (Exception ex) {
                System.out.println("["+line+"]\nCould not resolve string line to user object, moving on to next line...");
            }
        }
        usersInMemory=allUsers;
        return allUsers;
    }

    /**
     * Reads a movie from a , s
     *
     * @param t represents the line string.
     * @return Movie class object
     * @throws NumberFormatException Can be tossed due to invalid ID int.
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
        return new User(id,name);
    }
    
    /**
     * Gets a single User by its ID.
     * @param id The ID of the user.
     * @return The User with the ID.
     */
    public static User getUser(int id){
        return getAllUsers().stream().filter(a -> a.getId()==id).collect(Collectors.toList()).get(0);
    }
    
    /**
     * Updates a user so the persistence storage reflects the given User object.
     * @param user The updated user.
     */
    public static void updateUser(User user){
        if(usersInMemory==null){
            getAllUsers();
        }
        if(getUser(user.getId())==null){
            usersInMemory.add(user);
        }
        saveStorage();
    }

    public static User createUser(String name){
        if(usersInMemory==null){
            getAllUsers();
        }
        int id=getNewID();
        if(!DAOConfiguration.useSQL){
            FileDAO.appendLineToFile(FILE_SOURCE,id+","+name);
        }
        User user=new User(id,name);
        usersInMemory.add(user);
        return user;
    }

    public static void deleteUser(User user){
        if(usersInMemory==null){
            getAllUsers();
        }
        usersInMemory.remove(user);
        saveStorage();
    }

    private static Integer getNewID(){
        if(usersInMemory==null){
            getAllUsers();
        }
        int maxValue=-1;
        for(User user:usersInMemory){
            if(maxValue<user.getId()){
                maxValue=user.getId();
            }
        }
        return maxValue+1;
    }

    private static void saveStorage(){
        List<String> out= new ArrayList<>();
        for(User user:usersInMemory){
            out.add(user.getId()+","+user.getName());
        }
        FileDAO.saveListToFile(FILE_SOURCE,out);
    }
    
}
