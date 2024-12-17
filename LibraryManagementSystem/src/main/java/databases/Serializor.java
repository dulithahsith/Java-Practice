package databases;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

// Used java generics feature to make serialize handle both ArrayList<Books> and ArrayList<User>
public class Serializor{
    public static <T> void serialize(ConcurrentHashMap<String,T> objects, String filePath){

        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))){
            oos.writeObject(objects);
            System.out.println("Objects have been serialized and saved to " + filePath);
        }
        catch (IOException e){
            System.out.println("Error while serializing the objects: " + e.getMessage());
        }
    }

    public static <T> ConcurrentHashMap<String,T> deserialize(String filePath){
        ConcurrentHashMap<String,T> objects = null;
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))){
            objects = (ConcurrentHashMap<String, T>) ois.readObject();
            System.out.println("Objects have been deserialized from "+filePath);
        }
        catch (IOException | ClassNotFoundException e){
            System.out.println("Error while deserializing objects: "+ e.getMessage());
        }
        return objects;
    }
}