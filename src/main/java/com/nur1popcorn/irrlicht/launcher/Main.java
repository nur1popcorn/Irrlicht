/*
 * Copyright (C) Keanu Poeschko - All Rights Reserved
 * Unauthorized copying of this file is strictly prohibited
 *
 * Created by Keanu Poeschko <nur1popcorn@gmail.com>, April 2017
 * This file is part of {Irrlicht}.
 *
 * Do not copy or distribute files of {Irrlicht} without permission of {Keanu Poeschko}
 *
 * Permission to use, copy, modify, and distribute my software for
 * educational, and research purposes, without a signed licensing agreement
 * and for free, is hereby granted, provided that the above copyright notice
 * and this paragraph appear in all copies, modifications, and distributions.
 *
 *
 *
 *
 */

package com.nur1popcorn.irrlicht.launcher;

import com.nur1popcorn.irrlicht.Irrlicht;
import com.sun.tools.attach.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * The {@link Main} is used by the clients launcher.
 *
 * @see LogOutput
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class Main extends Application
{
    @Override
    public void start(Stage stage) throws IOException
    {
        final LogOutput logOutput = new LogOutput();

        final HBox logOutputSettings = new HBox();
        logOutputSettings.setAlignment(Pos.CENTER_RIGHT);
        {
            final ToggleButton showTimestamp = new ToggleButton("Timestamp");
            showTimestamp.setSelected(true);
            logOutput.getShowTimestampProperty().bind(showTimestamp.selectedProperty());

            final ToggleButton showCaller = new ToggleButton("Caller");
            showCaller.setSelected(true);
            logOutput.getShowCallerProperty().bind(showCaller.selectedProperty());

            final ToggleButton showThread = new ToggleButton("Thread");
            showThread.setSelected(true);
            logOutput.getShowThreadProperty().bind(showThread.selectedProperty());

            final ComboBox levelSelector = new ComboBox();
            levelSelector.setMinWidth(100);
            levelSelector.setOnShowing(new EventHandler<Event>() {
                final Set<Level> levels = new HashSet<>();
                @Override
                public void handle(Event event) {
                    logOutput.getLogRecords()
                            .stream()
                            .map(LogRecord::getLevel)
                            .forEach(levels::add);
                    levelSelector.getItems().clear();
                    levelSelector.getItems().addAll(levels);
                }
            });

            levelSelector.setOnAction(event -> logOutput.getFilter().setValue((Level) levelSelector.getSelectionModel().getSelectedItem()));

            final TextField searchBar = new TextField();
            logOutput.getTextFilter().bind(searchBar.textProperty());

            final HBox left = new HBox(new Label("Filter: "), searchBar);
            final HBox right = new HBox(showTimestamp, showCaller, showThread, levelSelector);
            HBox.setHgrow(left, Priority.ALWAYS);
            HBox.setHgrow(right, Priority.ALWAYS);
            left.setAlignment(Pos.CENTER_LEFT);
            right.setAlignment(Pos.CENTER_RIGHT);

            logOutputSettings.getChildren().addAll(left, right);
        }

        final HBox options = new HBox();
        {
            final ComboBox pidSelector = new ComboBox();
            pidSelector.setMinWidth(100);

            final Button inject = new Button("Inject");

            pidSelector.setOnShowing(event -> {
                pidSelector.getItems().clear();
                VirtualMachine.list().forEach(vmd -> pidSelector.getItems().add(vmd.id()));
                inject.setDisable(true);
            });

            pidSelector.setOnAction(event -> {
                if(pidSelector.getSelectionModel().getSelectedItem() != null)
                    inject.setDisable(false);
            });

            inject.setDisable(true);
            inject.setOnAction(event -> {
                try
                {
                    File currentFile = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath());
                    File tempFile = File.createTempFile(UUID.randomUUID().toString(), ".tmp");
                    Files.copy(currentFile.toPath(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    for(VirtualMachineDescriptor vmd : VirtualMachine.list())
                        if(vmd.id().equals(pidSelector.getSelectionModel().getSelectedItem()))
                        {
                            try
                            {
                                inject.setDisable(true);
                                pidSelector.setDisable(true);
                                inject.setText("Attaching..");
                                VirtualMachine vm = VirtualMachine.attach(vmd);
                                inject.setText("Loading agent..");
                                vm.loadAgent(tempFile.getAbsolutePath(), currentFile.getCanonicalPath());
                                inject.setText("Detaching..");
                                vm.detach();
                                inject.setText("Injected & Detached");
                            }
                            catch (AttachNotSupportedException | IOException | AgentInitializationException | AgentLoadException e)
                            {
                                e.printStackTrace();
                            }
                            return;
                        }
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            });
            inject.setAlignment(Pos.CENTER);

            final HBox center = new HBox(inject);
            final HBox right = new HBox(new Label("Pid: "), pidSelector);
            HBox.setHgrow(center, Priority.ALWAYS);
            HBox.setHgrow(right, Priority.ALWAYS);
            center.setAlignment(Pos.CENTER_RIGHT);
            right.setAlignment(Pos.CENTER_RIGHT);
            options.getChildren().addAll(center, right);
        }

        options.setAlignment(Pos.CENTER);
        options.setMinHeight(40);

        final VBox layout = new VBox(logOutputSettings, logOutput, options);
        VBox.setVgrow(logOutput, Priority.ALWAYS);
        layout.setPadding(new Insets(2, 2, 2, 2));

        final HBox windowOptions = new HBox();
        final VBox rootElement = new VBox(windowOptions, layout);
        rootElement.getStyleClass().add("root-element");

        final Scene scene = new Scene(rootElement, 720, 480);
        {
            final Label titel = new Label(Irrlicht.NAME + " - " + Irrlicht.VERSION + " developed by " + Arrays.toString(Irrlicht.AUTHORS));
            titel.setPadding(new Insets(2, 2, 2, 6));
            final Button close = new Button("X");
            close.setOnAction(event -> Platform.exit());

            final HBox left = new HBox(titel);
            final HBox right = new HBox(close);
            HBox.setHgrow(left, Priority.ALWAYS);
            HBox.setHgrow(right, Priority.ALWAYS);
            left.setAlignment(Pos.CENTER_LEFT);
            right.setAlignment(Pos.CENTER_RIGHT);

            windowOptions.getStyleClass().add("window-border");

            final EventHandler handler = new EventHandler<MouseEvent>() {
                private double dragX,
                               dragY;

                private boolean dragging;

                @Override
                public void handle(MouseEvent event) {
                    if(event.getEventType() == MouseEvent.MOUSE_DRAGGED)
                    {
                        if(dragging)
                        {
                            scene.getWindow().setX(scene.getWindow().getX() + (event.getScreenX() - dragX));
                            scene.getWindow().setY(scene.getWindow().getY() + (event.getScreenY() - dragY));
                            dragX = event.getScreenX();
                            dragY = event.getScreenY();
                        }
                    }
                    else if(event.getEventType() == MouseEvent.MOUSE_PRESSED)
                    {
                        dragX = event.getScreenX();
                        dragY = event.getScreenY();
                        dragging = true;
                    }
                    else if(event.getEventType() == MouseEvent.MOUSE_RELEASED)
                        dragging = false;
                }
            };

            scene.setOnMouseDragged(handler);
            windowOptions.setOnMousePressed(handler);
            scene.setOnMouseReleased(handler);

            windowOptions.getChildren().addAll(left, right);
        }
        scene.getStylesheets().add("/launcher.css");
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    public static void main(String[] args)
    {
        System.loadLibrary("attach");
        launch(args);
    }
}
