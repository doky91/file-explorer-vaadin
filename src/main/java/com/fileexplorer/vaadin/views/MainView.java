package com.fileexplorer.vaadin.views;

import com.fileexplorer.entity.Directory;
import com.fileexplorer.services.DirectoryServiceImpl;
import com.fileexplorer.services.interfaces.DirectoryService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;

import java.util.*;

@Route("")
@PageTitle("Main view")
public class MainView extends VerticalLayout {

    private Grid<Directory> grid;

    public MainView() {
        initView();
        initData();
    }

    // === UI ====

    private void initView() {

        H1 title = new H1("File explorer");
        Button addButton = new Button("Add");
        addButton.addClickListener(clickEvent -> onAddButtonCLick(clickEvent));

        grid = new Grid<>(Directory.class, false);
        grid.addColumn(Directory :: getName).setHeader("Files");
        grid.addSelectionListener(selection -> onSelectionClick(selection));

        add(
                title,
                addButton,
                grid
        );
    }

    private void initData() {
        DirectoryService directoryService = new DirectoryServiceImpl();
        List<Directory> directories = directoryService.listAll();
        grid.setItems(directories);
    }

    // === EVENT LISTENERS ====

    private void onAddButtonCLick(ClickEvent<Button> clickEvent) {
        initQueryParameter(System.getProperty("user.home"));
    }

    private void onSelectionClick(SelectionEvent<Grid<Directory>, Directory> selection) {
        Optional<Directory> optionalDirectory = selection.getFirstSelectedItem();
        if (optionalDirectory.isPresent() && optionalDirectory.get().getName().contains(".txt")) {
            String selectedFilePath = selection.getFirstSelectedItem().get().getPath()
                    + System.getProperty("file.separator") + selection.getFirstSelectedItem().get().getName();
            initQueryParameter(selectedFilePath);
        }
    }

    private void initQueryParameter(String path) {
        Map<String, List<String>> parameterMap = new HashMap<>();
        ArrayList<String> parameterList = new ArrayList<>();
        parameterList.add(path);
        parameterMap.put("route", parameterList);
        QueryParameters queryParameters = new QueryParameters(parameterMap);
        UI.getCurrent().navigate("file-content", queryParameters);
    }
}