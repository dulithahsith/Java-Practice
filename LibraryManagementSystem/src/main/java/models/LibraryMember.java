package models;
import exceptions.InvalidEmailException;
import exceptions.InvalidYearException;
import validators.EmailValidator;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class LibraryMember implements Serializable {
    private String memID;
    private String name;
    private String emailAddress;
    private String address;
    private ArrayList<String> borrowed;
    private LocalDate lastRenewDate;

    public LibraryMember(String name, String address, String email) {
        this.name=name;
        this.address=address;
        this.emailAddress=email;
        this.borrowed = new ArrayList<>();
        this.lastRenewDate = LocalDate.now();
    }

    public LibraryMember() {

    }

    public LibraryMember(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) throws InvalidEmailException {
        if (EmailValidator.isValid(emailAddress)){
            this.emailAddress = emailAddress;
        }
        else{
            throw new InvalidEmailException("Input year invalid: "+ emailAddress);
        }
    }

    public LocalDate getLastRenewDate() {
        return lastRenewDate;
    }

    public void setLastRenewDate(LocalDate lastRenewDate) {
        this.lastRenewDate = lastRenewDate;
    }

    public String getMemID() {
        return memID;
    }

    public void setMemID(String memID) {
        this.memID = memID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getBorrowed() {
        return borrowed;
    }

    public void setBorrowed(ArrayList<String> borrowed) {
        this.borrowed = borrowed;
    }

}
