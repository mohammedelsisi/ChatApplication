package JETS.ui.controllers;

import JETS.ServerMain;
import JETS.ui.helpers.Pair;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import javax.sql.RowSet;
import javax.sql.rowset.JdbcRowSet;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ServerController02 implements Initializable {
    public Pane genderPane;

    PieChart genderChart = new PieChart();
    PieChart countryChart = new PieChart();
    ObservableList<PieChart.Data> usersGender = FXCollections.observableArrayList();
    ObservableList<PieChart.Data> usersCountries = FXCollections.observableArrayList();
    Map<String, PieChart.Data> genderMap= new HashMap<>();
    Map<String,  PieChart.Data> countryMap= new HashMap<>();

    @FXML
    private BorderPane countryPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {

            JdbcRowSet rowSet = ServerMain.serverDao.getUsersInfo();
            Map<String, Double> mapOfUsersGenders = getUserData(rowSet,"gender");
            mapOfUsersGenders.forEach((e, s) -> {
                genderMap.put(e,new PieChart.Data(e,s));
                usersGender.add(genderMap.get(e));
            });

            genderChart.setData(usersGender);
            genderChart.setTitle("Users Gender");
            countryPane.setLeft(genderChart);


            Map<String, Double> mapOfUsersCountry = getUserData(rowSet,"country");
            mapOfUsersCountry.forEach((e, s) -> {
                countryMap.put(e,new PieChart.Data(e,s));
                usersCountries.add(countryMap.get(e));
            });



            countryChart.setData(usersCountries);
            countryChart.setTitle("Users Countries");
            countryPane.setRight(countryChart);
            addTooltipToChartSlice(genderChart);
            addTooltipToChartSlice(countryChart);


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    private void addTooltipToChartSlice(PieChart chart) {
        double total = 0;
        for (PieChart.Data d : chart.getData()) {
            total += d.getPieValue();
        }
        for (PieChart.Data d : chart.getData()) {
            Node slice = d.getNode();
            double sliceValue = d.getPieValue();
            double precent = (sliceValue / total) * 100;

            String tip = d.getName() + "=" + String.format("%.2f", precent) + "%";
            Tooltip tt = new Tooltip(tip);
            Tooltip.install(slice, tt);
        }
    }

    public Map<String, Double> getUserData(RowSet rowSet, String column) throws SQLException {
        rowSet.beforeFirst();
        Map<String, Double> map = new HashMap<>();
        while (rowSet.next()) {
            String tempUCword = rowSet.getString(column);
            if (tempUCword != null) {


                if (map.containsKey(tempUCword)) {
                    map.put(tempUCword,
                            map.get(tempUCword) + 1);
                } else {
                    map.put(tempUCword, 1.0);
                }

            }
        }




        return map;
    }



    public void refreshAnalysis() {

        try {

            JdbcRowSet rowSet = ServerMain.serverDao.getUsersInfo();

            Map<String, Double> mapOfUsersCountry = getUserData(rowSet,"country");
            mapOfUsersCountry.forEach((e, s) -> {

                if(countryMap.containsKey(e)) {
                    countryMap.get(e).setPieValue(s);
                }else {
                    countryMap.put(e,new PieChart.Data(e,s));
                    usersCountries.add(countryMap.get(e));
                }
            });

            Map<String, Double> mapOfUsersGenders = getUserData(rowSet,"gender");
            mapOfUsersGenders.forEach((e, s) -> {

                if(genderMap.containsKey(e)) {
                    genderMap.get(e).setPieValue(s);
                }else {
                    genderMap.put(e,new PieChart.Data(e,s));
                    usersGender.add(genderMap.get(e));
                }


            });
            addTooltipToChartSlice(genderChart);
            addTooltipToChartSlice(countryChart);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

}



