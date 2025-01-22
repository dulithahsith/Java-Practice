package com.libApp.LibraryMgtSystem.views;

import com.libApp.LibraryMgtSystem.exceptions.InvalidEmailException;
import com.libApp.LibraryMgtSystem.models.LibraryMember;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Route("members")
public class MemberView extends VerticalLayout {
    private RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "http://localhost:8080/api/members";
    private Grid<LibraryMember> memberGrid = new Grid<>(LibraryMember.class);
    private TextField nameField = new TextField("Name");
    private TextField emailField = new TextField("Email");
    private TextField addressField = new TextField("Address");

    public MemberView() {
        // Buttons
        Button loadMembersButton = new Button("Load Members", event -> loadMembers());
        Button addMemberButton = new Button("Add Member", event -> {
            try {
                addMember();
            } catch (InvalidEmailException e) {
                throw new RuntimeException(e);
            }
        });

        add(new HorizontalLayout(loadMembersButton), new HorizontalLayout(nameField, emailField, addressField) , addMemberButton, memberGrid);
    }

    private void loadMembers() {
        List<LibraryMember> members = Arrays.asList(restTemplate.getForObject(BASE_URL, LibraryMember[].class));
        memberGrid.setItems(members);
    }

    private void addMember() throws InvalidEmailException {
        LibraryMember member = new LibraryMember(nameField.getValue(),addressField.getValue(),emailField.getValue());
        restTemplate.postForObject(BASE_URL, member, String.class);
        Notification.show("Member added successfully!");
        loadMembers();
    }
}
