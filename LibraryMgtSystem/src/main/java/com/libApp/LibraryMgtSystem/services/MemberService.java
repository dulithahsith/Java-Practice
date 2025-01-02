package com.libApp.LibraryMgtSystem.services;

import com.libApp.LibraryMgtSystem.models.LibraryMember;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MemberService {
    private ConcurrentHashMap<String, LibraryMember> members;

    public MemberService(){
        members=DataService.loadMembers();
    }

    public ConcurrentHashMap<String,LibraryMember> getAllmembers(){
        return DataService.loadMembers();
    }

    public void addMember(LibraryMember mem){
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
        DataService.saveMembers(members);
    }
    public boolean checkMember(String memID){
        return members.containsKey(memID);
    }

}

