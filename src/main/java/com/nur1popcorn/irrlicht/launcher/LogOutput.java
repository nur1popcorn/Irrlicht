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

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.css.PseudoClass;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * The {@link LogOutput} is used to display the clients logs.
 *
 * @see Main
 * @see LogRecord
 * @see com.nur1popcorn.irrlicht.utils.LoggerFactory
 *
 * @author nur1popcorn
 * @since 1.0.0-alpha
 */
public class LogOutput extends ListView<LogRecord>
{
    public static final int SERVER_PORT = 25560,
                            MAX_ENTRIES = 10_000;

    private BooleanProperty showTimestamp = new SimpleBooleanProperty(true),
                            showCaller = new SimpleBooleanProperty(true),
                            showThread = new SimpleBooleanProperty(true);

    private StringProperty textFilter = new SimpleStringProperty();

    private ObjectProperty<Level> filter = new SimpleObjectProperty<>();
    private ObservableList<LogRecord> logRecords = FXCollections.observableArrayList();
    private Set<PseudoClass> levelClasses = new HashSet<>();

    public LogOutput()
    {
        getStyleClass().add("log-output");
        final Thread thread = new Thread(() -> {
            try
            {
                final ServerSocket serverSocket = new ServerSocket(SERVER_PORT, 0, InetAddress.getByName("localhost"));
                for(;;)
                {
                    if(logRecords.size() == MAX_ENTRIES)
                        break;

                    Socket socket = null;
                    ObjectInputStream objectInputStream = null;
                    try
                    {
                        socket = serverSocket.accept();
                        final InputStream is = socket.getInputStream();
                        final BufferedReader br = new BufferedReader(new InputStreamReader(is, "US-ASCII"));
                        String line = null;
                        while ((line = br.readLine()) != null)
                        {
                            objectInputStream = new ObjectInputStream(new ByteArrayInputStream(Base64.getDecoder().decode(line)));
                            final Object object = objectInputStream.readObject();
                            if(object instanceof LogRecord)
                                Platform.runLater(() -> logRecords.add((LogRecord) object));
                        }
                    }
                    catch (IOException | ClassNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                    finally
                    {
                        try
                        {
                            if(socket != null)
                                socket.close();
                            if(objectInputStream != null)
                                objectInputStream.close();
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
            catch (IOException e)
            {
                logRecords.add(new LogRecord(Level.SEVERE, "Could not bind port " + SERVER_PORT + " logging will be disabled."));
                e.printStackTrace();
            }
        }, "Logging-Server-Thread");
        thread.setDaemon(true);
        thread.start();

        setItems(new FilteredList<>(logRecords, logRecord -> filter.get() == null || logRecord.getLevel().equals(filter.get())));
        filter.addListener(observable -> setItems(new FilteredList<>(logRecords, logRecord -> filter.get() == null || logRecord.getLevel().equals(filter.get()))));

        textFilter.addListener(observable -> setItems(new FilteredList<>(logRecords, logRecord -> textFilter.get().equals("") || logRecord.getMessage().toLowerCase().contains(textFilter.get().toLowerCase()))));

        setCellFactory(param -> new ListCell<LogRecord>() {
            {
                showTimestamp.addListener(observable -> updateItem(getItem(), isEmpty()));
                showCaller.addListener(observable -> updateItem(getItem(), isEmpty()));
                showThread.addListener(observable -> updateItem(getItem(), isEmpty()));
            }
            @Override
            protected void updateItem(LogRecord record, boolean empty)
            {
                super.updateItem(record, empty);

                if(record != null &&
                        !empty)
                {
                    final StringWriter stringWriter = new StringWriter();
                    if (record.getThrown() != null)
                    {
                        PrintWriter printWriter = new PrintWriter(stringWriter);
                        printWriter.println();
                        record.getThrown().printStackTrace(printWriter);
                        printWriter.close();
                    }

                    setText(new MessageFormat((showTimestamp.get() ? "[{0,date}, {0,time}] " : "") + (showCaller.get() ? "[{1}#{2}] " : "") +  "{3}: " + (showThread.get() ? "[T:{4}] " : "") + "{5}\n").format(new Object[] {
                            new Date(record.getMillis()),
                            record.getSourceClassName(),
                            record.getSourceMethodName(),
                            record.getLevel().getName(),
                            record.getThreadID(),
                            record.getMessage() + stringWriter.toString()
                    }));
                    levelClasses.forEach(pseudoClass -> pseudoClassStateChanged(pseudoClass, false));
                    final PseudoClass pseudoClass = PseudoClass.getPseudoClass(record.getLevel().getName().toLowerCase());
                    pseudoClassStateChanged(pseudoClass, true);
                    levelClasses.add(pseudoClass);
                }
                else
                    setText(null);
            }
        });
    }

    /**
     * @return a changeable property used to determine whether or not the timestamp should
     *         be shown in the {@link LogOutput}.
     */
    public BooleanProperty getShowTimestampProperty()
    {
        return showTimestamp;
    }

    /**
     * @return a changeable property used to determine whether or not the caller class should
     *         be shown in the {@link LogOutput}.
     */
    public BooleanProperty getShowCallerProperty()
    {
        return showCaller;
    }

    /**
     * @return a changeable property used to determine whether or not the thread should
     *         be shown in the {@link LogOutput}.
     */
    public BooleanProperty getShowThreadProperty()
    {
        return showThread;
    }

    /**
     * @return all the {@link LogRecord}s that can be shown.
     */
    public ObservableList<LogRecord> getLogRecords()
    {
        return logRecords;
    }

    /**
     * @return a changeable property that determines what type of log levels can be shown.
     */
    public ObjectProperty<Level> getFilter()
    {
        return filter;
    }

    /**
     * @return a changeable property that determines what kind of logs can be shown.
     */
    public StringProperty getTextFilter()
    {
        return textFilter;
    }
}