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

import com.nur1popcorn.irrlicht.launcher.rmi.IPerformanceCharts;
import com.nur1popcorn.irrlicht.launcher.rmi.impl.RmiManager;
import com.nur1popcorn.irrlicht.utils.StringUtils;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.Axis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 * The {@link PerformanceCharts} is used to information about the clients performance.
 *
 * @see IPerformanceCharts
 *
 * @author nur1popcorn
 * @since 1.1.0-alpha
 */
public class PerformanceCharts extends HBox implements IPerformanceCharts
{
    private final PerformanceChart performanceCharts[] = new PerformanceChart[Type.values().length];

    public PerformanceCharts()
    {
        RmiManager.getInstance()
                  .register("PerformanceCharts", this);
        getStyleClass().add("performance-charts");
        setAlignment(Pos.CENTER_RIGHT);
        for(int i = 0; i < performanceCharts.length; i++)
            getChildren().addAll(performanceCharts[i] = new PerformanceChart(Type.values()[i]));
    }

    @Override
    public void update(Type type, String text, int value)
    {
        Platform.runLater(() -> {
            final PerformanceChart performanceChart = performanceCharts[type.ordinal()];
            performanceChart.info.setText(text);
            final XYChart.Series<Number, Number> series = performanceChart.stackedAreaChart.getData().get(0);
            if(series.getData().size() == 40)
                series.getData().remove(0);
            series.getData().add(new XYChart.Data<>(40, value));
            for(XYChart.Data data : series.getData())
                data.setXValue((int)data.getXValue() - 1);
        });
    }

    /**
     * A Single performance-chart.
     */
    private static class PerformanceChart extends StackPane
    {
        private final Label info = new Label();
        private final StackedAreaChart<Number, Number> stackedAreaChart = new StackedAreaChart<>(new NumberAxis(0, 39, 1), new NumberAxis());

        public PerformanceChart(Type type)
        {
            super();
            stackedAreaChart.setPadding(new Insets(0, -6, 0, -9));
            stackedAreaChart.setMinHeight(80);
            stackedAreaChart.setPrefHeight(80);
            stackedAreaChart.setCreateSymbols(false);
            stackedAreaChart.setAnimated(false);

            for (Axis axis : new Axis[] {
                        stackedAreaChart.getXAxis(),
                        stackedAreaChart.getYAxis()
                    }
                )
            {
                axis.setTickLabelsVisible(false);
                axis.setTickMarkVisible(false);
                axis.lookup(".axis-minor-tick-mark").setVisible(false);
            }
            stackedAreaChart.getData().add(new XYChart.Series<>());

            Label label = new Label(StringUtils.capitalize(type.toString().replaceAll("_", "-"), '-'));
            label.setPadding(new Insets(13, 0, 0, 8));
            label.getStyleClass().add("chart-type-label");
            info.setPadding(new Insets(13, 8, 0, 0));

            getChildren().addAll(stackedAreaChart, label, info);
            StackPane.setAlignment(label, Pos.TOP_LEFT);
            StackPane.setAlignment(info, Pos.TOP_RIGHT);
        }
    }
}
