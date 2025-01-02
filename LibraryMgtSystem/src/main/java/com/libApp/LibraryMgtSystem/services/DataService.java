package com.libApp.LibraryMgtSystem.services;

import com.libApp.LibraryMgtSystem.models.Book;
import com.libApp.LibraryMgtSystem.models.LibraryMember;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class DataService {

    private static final String BOOKS_FILE = "C:\\Users\\DulithaHasith\\IdeaProjects\\LibraryMgtSystem\\src\\main\\java\\com\\libApp\\LibraryMgtSystem\\databases\\BookDatabase.ser";
    private static final String MEMBERS_FILE = "C:\\Users\\DulithaHasith\\IdeaProjects\\LibraryMgtSystem\\src\\main\\java\\com\\libApp\\LibraryMgtSystem\\databases\\MemberDatabase.ser";

    public static ConcurrentHashMap<String,Book> loadBooks(){
        File file = new File(BOOKS_FILE);
        if (!file.exists()) {
            // If the file doesn't exist, create it with an empty ConcurrentHashMap
            System.out.println("Books file not found. Creating a new file with an empty database.");
            saveBooks(new ConcurrentHashMap<>());
        }
        ConcurrentHashMap<String, Book> objects = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BOOKS_FILE))) {
            objects = (ConcurrentHashMap<String, Book>) ois.readObject();
            System.out.println("Objects have been deserialized from " + BOOKS_FILE);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error while deserializing books: " + e.getMessage());
        }
        return objects;
    }

    public static void saveBooks(ConcurrentHashMap<String,Book> books){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BOOKS_FILE))){
            oos.writeObject(books);
        }
        catch (IOException e){
            System.out.println("Error saving books: "+e.getMessage());
        }
    }

    public static ConcurrentHashMap<String, LibraryMember> loadMembers() {
        File file = new File(MEMBERS_FILE);
        if (!file.exists()) {
            // If the file doesn't exist, create it with an empty ConcurrentHashMap
            System.out.println("Members file not found. Creating a new file with an empty database.");
            saveMembers(new ConcurrentHashMap<>());
        }
        ConcurrentHashMap<String, LibraryMember> objects = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(MEMBERS_FILE))) {
            objects = (ConcurrentHashMap<String, LibraryMember>) ois.readObject();
            System.out.println("Objects have been deserialized from " + MEMBERS_FILE);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error while deserializing members: " + e.getMessage());
        }
        return objects;
    }

    public static void saveMembers(ConcurrentHashMap<String,LibraryMember> members){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(MEMBERS_FILE))){
            oos.writeObject(members);
        }
        catch (IOException e){
            System.out.println("Error saving Members"+ e.getMessage());
        }
    }

}
