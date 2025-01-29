package com.libApp.LibraryMgtSystem.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("")
@PageTitle("LibMgtSys-Home")
public class HomeView extends VerticalLayout {
    public HomeView() {
        System.out.println("Rendering HomeView...");
        H1 welcomeMessage = new H1("Welcome to Library Management System");

        Button bookViewButton = new Button("Manage Books");
        Button memberViewButton = new Button("Manage Members");

        bookViewButton.addClickListener(event ->
                bookViewButton.getUI().ifPresent(ui -> ui.navigate("books")));
        memberViewButton.addClickListener(event ->
                memberViewButton.getUI().ifPresent(ui -> ui.navigate("members")));

        // Add components to the layout
        add(welcomeMessage, bookViewButton, memberViewButton);
    }
}
