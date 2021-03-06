package movierecsys.dal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import movierecsys.be.User;

/**
 * @author Snoozy1995
 */
//todo remove inmemory? and implement sql also.
public class UserDAO {
    private static final String FILE_SOURCE = "data/users.txt";
    private static final String SQL_SOURCE="users";
    public static List<User> usersInMemory=null;
    public static HashMap<Integer,User> usersHashMap=new HashMap<>();
    /**
     * Gets a list of all known users.
     * @return List of users.
     */
    public static List<User> getAllUsers(){
        if(usersInMemory!=null) return usersInMemory;
        List<String> array;
        if(!DAOConfiguration.useSQL) {
            array = FileDAO.readFileToList(FILE_SOURCE);
        }else{
            array = SQLDAO.selectToStringList(SQL_SOURCE,"id,name");
        }
        List<User> allUsers = new ArrayList<>();
        assert array != null;
        for(String line: array){
            try {
                User user=stringArrayToUser(line);
                usersHashMap.put(user.getId(),user);
                allUsers.add(user);
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
     * @return User class object
     * @throws NumberFormatException Can be tossed due to invalid ID int.
     */
    private static User stringArrayToUser(String t) {
        String[] arrUser = t.split(",");

        StringBuilder name = new StringBuilder(arrUser[1]);
        if (arrUser.length > 2) {
            for (int i = 2; i < arrUser.length; i++) {
                name.append(",").append(arrUser[i]);
            }
        }
        return new User(Integer.parseInt(arrUser[0]), name.toString());
    }
    
    /**
     * Gets a single User by its ID.
     * @param id The ID of the user.
     * @return The User with the ID.
     */
    public static User getUser(int id){
        List<User> users=getAllUsers().stream().filter(a -> a.getId()==id).collect(Collectors.toList());
        if(users.size()==0) return null;
        return users.get(0);
    }

    public static User getUser(String name){
        List<User> users=getAllUsers().stream().filter(a -> a.getName().equals(name)).collect(Collectors.toList());
        if(users.size()==0) return null;
        return users.get(0);
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
        if(!DAOConfiguration.useSQL) saveStorage();
        else SQLDAO.updateToTable(SQL_SOURCE,"name="+user.getName(),"id="+user.getId());
    }

    public static User createUser(String name){
        if(usersInMemory==null){
            getAllUsers();
        }
        int id=getNewID();
        if(!DAOConfiguration.useSQL){
            FileDAO.appendLineToFile(FILE_SOURCE,id+","+name);
        }else{
            SQLDAO.insertToTable(SQL_SOURCE,"id,name",id+","+name);
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
        if(!DAOConfiguration.useSQL) saveStorage();
        else SQLDAO.deleteFromTable(SQL_SOURCE,"id="+user.getId());
    }

    private static Integer getNewID(){
        List<User> usersList=getAllUsers();
        int maxValue=-1;
        for(User user:usersList){
            if(maxValue<user.getId()){
                maxValue=user.getId();
            }
        }
        return maxValue+1;
    }

    private static void saveStorage(){
        if(DAOConfiguration.useSQL) return;
        List<String> out= new ArrayList<>();
        for(User user:usersInMemory){
            out.add(user.getId()+","+user.getName());
        }
        FileDAO.saveListToFile(FILE_SOURCE,out);
    }
}
