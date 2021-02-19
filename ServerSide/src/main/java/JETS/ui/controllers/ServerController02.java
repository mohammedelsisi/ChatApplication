package JETS.ui.controllers;

import JETS.ServerMain;
import JETS.service.ConnectionService;
import JETS.service.ConnectionServiceFactory;
import JETS.ui.helpers.Pair;
import Services.ClientServices;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import javax.sql.RowSet;
import javax.sql.rowset.JdbcRowSet;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ServerController02 implements Initializable {
    int online=0;
    PieChart genderChart = new PieChart();
    PieChart countryChart = new PieChart();
    PieChart onlineChart = new PieChart();
    PieChart.Data onlineUsers;
    PieChart.Data offlineUsers;
    ObservableList<PieChart.Data> usersGender = FXCollections.observableArrayList();
    ObservableList<PieChart.Data> usersCountries = FXCollections.observableArrayList();
    ObservableList<PieChart.Data> usersAvailable = FXCollections.observableArrayList();
    Map<String, PieChart.Data> genderMap = new HashMap<>();
    Map<String, PieChart.Data> countryMap = new HashMap<>();

    @FXML
    private HBox hBoxAnalysis;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {

            JdbcRowSet rowSet = ServerMain.serverDao.getUsersInfo();

            onlineUsers = new PieChart.Data("Online", online);
            int allusersIni = 0;
            rowSet.beforeFirst();
            while (rowSet.next()) {
                allusersIni++;
            }
            offlineUsers = new PieChart.Data("Offline", allusersIni);
            onlineChart.setTitle("Users Available");
            usersAvailable.add(onlineUsers);
            usersAvailable.add(offlineUsers);

            onlineChart.setData(usersAvailable);
            hBoxAnalysis.getChildren().add(onlineChart);

            ConnectionServiceFactory.getConnectionService().getConnectedClients().addListener(new MapChangeListener<String, ClientServices>() {
                @Override
                public void onChanged(Change<? extends String, ? extends ClientServices> change) {
                    try {


                        online = ConnectionServiceFactory.getConnectionService().getConnectedClients().size();
                        int allusers = 0;
                        rowSet.beforeFirst();
                        while (rowSet.next()) {
                            allusers++;
                        }
                        onlineUsers.setPieValue(online);
                        offlineUsers.setPieValue(allusers - online);
                        addTooltipToChartSlice(onlineChart);


                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            });


            Map<String, Double> mapOfUsersGenders = getUserData(rowSet, "gender");
            mapOfUsersGenders.forEach((e, s) -> {
                genderMap.put(e, new PieChart.Data(e, s));
                usersGender.add(genderMap.get(e));
            });

            genderChart.setData(usersGender);
            genderChart.setTitle("Users Gender");
            hBoxAnalysis.getChildren().add(genderChart);


            Map<String, Double> mapOfUsersCountry = getUserData(rowSet, "country");
            mapOfUsersCountry.forEach((e, s) -> {
                countryMap.put(e, new PieChart.Data(e, s));
                usersCountries.add(countryMap.get(e));
            });


            countryChart.setData(usersCountries);
            countryChart.setTitle("Users Countries");
            hBoxAnalysis.getChildren().add(countryChart);

            addTooltipToChartSlice(onlineChart);
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
            int allusersInis = 0;
            rowSet.beforeFirst();
            while (rowSet.next()) {
                allusersInis++;
            }


            offlineUsers.setPieValue(allusersInis-online);




            Map<String, Double> mapOfUsersCountry = getUserData(rowSet, "country");
            mapOfUsersCountry.forEach((e, s) -> {

                if (countryMap.containsKey(e)) {
                    countryMap.get(e).setPieValue(s);
                    System.out.println(s);
                } else {
                    countryMap.put(e, new PieChart.Data(e, s));
                    usersCountries.add(countryMap.get(e));
                }
            });

            Map<String, Double> mapOfUsersGenders = getUserData(rowSet, "gender");
            mapOfUsersGenders.forEach((e, s) -> {

                if (genderMap.containsKey(e)) {
                    genderMap.get(e).setPieValue(s);
                } else {
                    genderMap.put(e, new PieChart.Data(e, s));
                    usersGender.add(genderMap.get(e));
                }


            });
            addTooltipToChartSlice(onlineChart);
            addTooltipToChartSlice(genderChart);
            addTooltipToChartSlice(countryChart);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }


}



