package models;

import databases.Serializor;

import java.io.File;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MemberHandling{
    private final ConcurrentHashMap<String,LibraryMember> members;
    private final String memFilePath;
    public MemberHandling(){
        this.memFilePath = "C:\\Users\\DulithaHasith\\IdeaProjects\\LibraryManagementSystem\\src\\main\\java\\databases\\MemberDatabase.ser";
        File file = new File(memFilePath);
//                   Making sure the concurrentHashMap exists
        if(!file.exists()){
            this.members = new ConcurrentHashMap<>();
        }
        else{
            this.members = Serializor.deserialize(memFilePath);
            System.out.println("Deserialized books from the file");
        }
    }

    public String getMemFilePath() {
        return memFilePath;
    }

    public ConcurrentHashMap<String, LibraryMember> getMembers() {
        return members;
    }

    public synchronized void addMember(LibraryMember mem){
        if (members.isEmpty()){
            mem.setMemID("M000000");
        }
        else{
            String newID = members.keySet().stream()
                    .max(Comparator.comparingInt(id -> Integer.parseInt(id.substring(1))))
                    .map(maxID -> {
                        int nextNo = Integer.parseInt(maxID.substring(1))+1;
                        return String.format("M%06d",nextNo);
                    })
                    .orElse("M000000");
            mem.setMemID(newID);
        }
        members.put(mem.getMemID(),mem);
        Serializor.serialize(members, memFilePath);

        System.out.println("Member added successfully!\n");
    }
    public synchronized String printAll(){
        System.out.println("All members:");
        System.out.println("__________________________________________________________________________________________________________");
        System.out.println("| Mem ID         | Name             | Address          | emailAddress     | RenewDate  | Borrowed        |");
        System.out.println("|----------------|------------------|------------------|------------------|------------|------------------");

        StringBuilder sb = new StringBuilder();
        for (LibraryMember mem : members.values()) {
            System.out.printf(
                    "|%-16s| %-16s | %-16s | %-16s | %-10s | %-16s|\n",
                    mem.getMemID(),
                    mem.getName(),
                    mem.getAddress(),
                    mem.getEmailAddress(),
                    mem.getLastRenewDate(),
                    mem.getBorrowed()
            );
        }
        for (LibraryMember mem : members.values()) {
            sb.append(String.format(
                    "%s, %s, %s, %s, %s, %s\r\n",
                    mem.getMemID(),
                    mem.getName(),
                    mem.getAddress(),
                    mem.getEmailAddress(),
                    mem.getLastRenewDate(),
                    mem.getBorrowed()
            ));
        }

        System.out.println("--------------------------------------------------------------------------------------------------------");
        System.out.println();
        return sb.toString();
    }

    public synchronized void updateMembers(){
        Serializor.serialize(members,memFilePath);
    }
    public synchronized void removeMember(LibraryMember mem){
        members.remove(mem.getMemID());
    }
    public synchronized boolean checkMember(String memID){
        return members.containsKey(memID);
    }

}