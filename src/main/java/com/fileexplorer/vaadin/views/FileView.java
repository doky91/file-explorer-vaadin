package com.fileexplorer.vaadin.views;

import com.fileexplorer.entity.FileDetails;
import com.fileexplorer.services.interfaces.FileService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Route("file-content")
@PageTitle("File view")
public class FileView extends VerticalLayout implements HasUrlParameter<String> {

    @Autowired
    private FileService fileService;

    private boolean isNew;

    private String pathParameter;

    private FileDetails fileDetails;

    private TextField fileName;

    private TextArea fileContent;

    private HorizontalLayout actionButtons;

    @Override
    public void setParameter(BeforeEvent event,
                             @OptionalParameter String parameter) {
        paramsToAttr(event);
        init();
    }

    private void init() {
        initView();
        initData();
    }

    private void paramsToAttr(BeforeEvent event) {
        Location location = event.getLocation();
        QueryParameters queryParameters = location.getQueryParameters();

        Map<String, List<String>> parametersMap = queryParameters
                .getParameters();

        List<String> ovo = parametersMap.get("route");
        pathParameter = ovo.get(0);
        isNew = !pathParameter.endsWith(".txt");
    }

    // === UI ====

    private void initView() {
        // === Header section ===

        H1 title = new H1("File viewer");

        Button backButton = new Button("Back");
        backButton.addClickListener(clickEvent -> onBackButtonCLick(clickEvent));

        // === Form section ===

        FormLayout form = new FormLayout();

        TextField fileName = new TextField("File name");
        this.fileName = fileName;
        TextArea fileContent = new TextArea("Content");
        this.fileContent = fileContent;
        fileContent.setMinHeight("350px");

        form.add(
                fileName,
                fileContent
        );

        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1)
        );

        // === Action section ===

        actionButtons = new HorizontalLayout();

        Button saveButton = new Button("Save");
        saveButton.addClickListener(clickEvent -> onSaveButtonCLick(clickEvent));

        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(clickEvent -> onCancelButtonCLick(clickEvent));

        actionButtons.add(
                saveButton,
                cancelButton
        );

        addDeleteButton();

        // === Render all ===

        add(
                title,
                backButton,
                form,
                actionButtons
        );
    }

    private void initData() {
        Path fullPath = Paths.get(pathParameter);

        if (isNew) {
            fileDetails = new FileDetails(fullPath.toString(), "", "");
        } else {
            String path = fullPath.getParent().toString();
            String fileName = fullPath.getFileName().toString();
            fileDetails = new FileDetails(path, fileName, fileService.readFile(fullPath.toString()));
        }

        modelToForm();
    }

    private void addDeleteButton() {
        if (isNew) return;

        Button deleteButton = new Button("Delete");
        deleteButton.addClickListener(clickEvent -> onDeleteButtonCLick(clickEvent));
        actionButtons.add(
                deleteButton
        );
    }

    // === UTILS ===

    private void modelToForm() {
        fileName.setValue(fileDetails.getName().replace(".txt", ""));
        fileContent.setValue(fileDetails.getContent());
    }

    private void formToModel() {
        fileDetails.setName(fileName.getValue() + ".txt");
        fileDetails.setContent(fileContent.getValue());
    }

    // === EVENT LISTENERS ====

    private void onBackButtonCLick(ClickEvent<Button> clickEvent) {
        UI.getCurrent().navigate(MainView.class);
    }

    private void onSaveButtonCLick(ClickEvent<Button> clickEvent) {
        try {
            formToModel();

            if (fileDetails.getName().length() <= 4) {
                Notification.show("Name is not set");
                return;
            }

            fileService.createFile(fileDetails);
            boolean operation = fileService.updateFile(fileDetails);
            if (operation) {
                Notification.show("File changed successfully.");

                if (isNew) {
                    isNew = false;
                    addDeleteButton();
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void onDeleteButtonCLick(ClickEvent<Button> clickEvent) {
        fileService.deleteFile(fileDetails);
        UI.getCurrent().navigate(MainView.class);
    }

    private void onCancelButtonCLick(ClickEvent<Button> clickEvent) {
        UI.getCurrent().navigate(MainView.class);
    }
}
