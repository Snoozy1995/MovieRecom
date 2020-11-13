package movierecsys.dal;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileDAO {

    public static void appendLineToFile(String source, String line){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(source, true));
            writer.write(line+"\n");
            writer.close();
        }catch(Exception e){
            System.out.println("["+source+"]");
            System.out.println("Problem saving to persistent storage, only saved in memory.");
        }
    }

    public static List<String> readFileToList(String source){
        List<String> array = new ArrayList<>();
        File file = new File(source);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) { array.add(line); }
        }catch(Exception e){
            System.out.println("["+source+"]");
            System.out.println("[FileDAO] Problem reading from persistent storage.");
        }
        return array;
    }

    public static void saveListToFile(String source, List<String> lists){
        try{
            File file = new File(source);
            Files.write(file.toPath(), lists, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        }catch(Exception e){
            System.out.println("["+source+"]");
            System.out.println("[FileDAO] Problem saving to persistent storage.");
        }
    }
}
