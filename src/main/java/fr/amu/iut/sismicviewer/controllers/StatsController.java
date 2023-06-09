package fr.amu.iut.sismicviewer.controllers;

import fr.amu.iut.sismicviewer.CSV.CSVManager;
import fr.amu.iut.sismicviewer.CSV.SeismeDataManager;
import fr.amu.iut.sismicviewer.Seisme;
import fr.amu.iut.sismicviewer.controllers.TopBarController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Classe qui contrôle la fenêtre Statistique en l'initialisant et on chargeant toute les données dans tout les objets graphs etc...
 *
 * @author BAMAS Mathias
 * @author BEDDIAF Miloud
 * @author BENDJEDDOU Rayan
 * @author LOUARN Mathis
 * @version 1.0
 */
public class StatsController implements Initializable {

    @FXML
    private Button carte;
    @FXML
    private Button dashboard;
    @FXML
    private Button stats;
    @FXML
    private BarChart barChartStats;
    @FXML
    private LineChart lineChartStats;
    @FXML
    private Label seismeMagnitudeInconnuValueLabel;
    @FXML
    private Label seismeQuiOnCauseDesDegats;
    @FXML
    private Label seismeEnFrance;
    @FXML
    private Label seismeHorsFrance;
    @FXML
    private Label seismeAvecMagnitude;
    @FXML
    private Label seismeSansConsequence;
    @FXML
    private TableView tableviewStats;

    /**
     * Initialise la page StatsController
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TopBarController topBarController = new TopBarController();
        topBarController.initTopBar(carte, dashboard, stats);
        barChartStats.setAnimated(false);
        initBarChart(barChartStats);
        initLineChart(lineChartStats);
        initStats();
        initTableView(tableviewStats);
    }

    /**
     * Charge les données dans le barchart dans la fenêtre statisitique
     *
     * @param graphique
     */
    public void initBarChart(BarChart graphique) {
        ArrayList<Seisme> data = new ArrayList<>(CSVManager.getListeSeisme());
        SeismeDataManager seismeDataManager = new SeismeDataManager();
        XYChart.Series series = new XYChart.Series<String, Integer>();
        for (double i = 2; i < 9.5; i = i + 0.5) {
            series.getData().add(new XYChart.Data<>(String.valueOf(i), seismeDataManager.getSeismeParMagnitude(data, i, i).size()));
        }
        graphique.getData().add(series);
    }

    /**
     * charge les données dans le linechart dans la fenêtre statisitique
     *
     * @param graphique
     */

    public void initLineChart(LineChart graphique) {
        ArrayList<Seisme> data = new ArrayList<>(CSVManager.getListeSeisme());
        SeismeDataManager seismeDataManager = new SeismeDataManager();
        XYChart.Series series = new XYChart.Series<String, Double>();
        int derniereAnnee = seismeDataManager.getSeismeLePlusVieux(data).getAnnee();
        if(derniereAnnee < 1900){
            derniereAnnee = 1900;
        }
        for (int i = derniereAnnee; i < seismeDataManager.getSeismeLePlusRecent(data).getAnnee() + 1; ++i) {
            double moyenne = seismeDataManager.getMagnitudeMoyenne(seismeDataManager.getAnneeFromTo(data, i, i));
            series.getData().add(new XYChart.Data<>(String.valueOf(i), moyenne));
        }
        graphique.getData().add(series);
    }

    /**
     * initialise les statisitique dans les petit carré au centre de la fenêtre statisitique
     */
    public void initStats() {
        SeismeDataManager seismeDataManager = new SeismeDataManager();
        ArrayList<Seisme> data = CSVManager.getListeSeisme();
        seismeMagnitudeInconnuValueLabel.setText(String.valueOf(seismeDataManager.getNombreSeisme(data) - seismeDataManager.getNombreSeismeAvecMagnitudeConnue(data)));
        seismeQuiOnCauseDesDegats.setText(String.valueOf(seismeDataManager.getSeismeParMagnitude(data, 6, 10).size()));
        seismeEnFrance.setText(String.valueOf(seismeDataManager.getSeismeEnFrance(data).size()));
        seismeHorsFrance.setText(String.valueOf(seismeDataManager.getNombreSeisme(data) - seismeDataManager.getSeismeEnFrance(data).size()));
        seismeAvecMagnitude.setText(String.valueOf(seismeDataManager.getNombreSeismeAvecMagnitudeConnue(data)));
        seismeSansConsequence.setText(String.valueOf(seismeDataManager.getSeismeParMagnitude(data, 2, 5.5).size()));
    }

    /**
     * charge les données dans la tableview dans la fenêtre statistique
     *
     * @param tableau
     */
    public void initTableView(TableView tableau) {
        SeismeDataManager seismeDataManager = new SeismeDataManager();
        ArrayList<Seisme> data = CSVManager.getListeSeisme();
        ObservableList<Seisme> listeSeisme = FXCollections.observableArrayList(seismeDataManager.getSeismeParMagnitude(data, 2, 10));
        tableau.setItems(listeSeisme);
    }
}
