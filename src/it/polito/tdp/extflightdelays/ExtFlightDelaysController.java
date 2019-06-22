/**
 * Sample Skeleton for 'ExtFlightDelays.fxml' Controller Class
 */

package it.polito.tdp.extflightdelays;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.google.common.collect.Comparators;

import java.util.Map.Entry;

import it.polito.tdp.extflightdelays.model.Model;
import it.polito.tdp.extflightdelays.model.Route;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ExtFlightDelaysController {
	private Model model;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader

	@FXML // fx:id="btnCreaGrafo"
	private Button btnCreaGrafo; // Value injected by FXMLLoader

	@FXML // fx:id="cmbBoxStati"
	private ComboBox<String> cmbBoxStati; // Value injected by FXMLLoader

	@FXML // fx:id="btnVisualizzaVelivoli"
	private Button btnVisualizzaVelivoli; // Value injected by FXMLLoader

	@FXML // fx:id="txtT"
	private TextField txtT; // Value injected by FXMLLoader

	@FXML // fx:id="txtG"
	private TextField txtG; // Value injected by FXMLLoader

	@FXML // fx:id="btnSimula"
	private Button btnSimula; // Value injected by FXMLLoader

	@FXML
	void doCreaGrafo(ActionEvent event) {
		model.creaGrafo();
		txtResult.appendText("Grafo creato\n");
	}

	@FXML
	void doSimula(ActionEvent event) {
		String partenza = cmbBoxStati.getSelectionModel().getSelectedItem();
		if (partenza == null) {
			txtResult.appendText("Selezionare una stato di partenza\n");
			return;
		}
		int T;
		int G;
		try {
			T = Integer.parseInt(txtT.getText());
			G = Integer.parseInt(txtG.getText());
		} catch (NumberFormatException e) {
			txtResult.appendText("Formato dati non valido, T e G devono essere solo numeri\n");
			return;
		}
		model.simula(partenza, T, G);
		Map<String, Integer> resSim = model.getRisultatiSimulazione();
		resSim.entrySet().stream().sorted(Comparator.comparing(Entry::getValue)).forEach(a->txtResult.appendText("Stato: " + a.getKey() + " Turisti: " + a.getValue() + "\n"));
		
	}

	@FXML
	void doVisualizzaVelivoli(ActionEvent event) {
		String statoDiPartenza = cmbBoxStati.getSelectionModel().getSelectedItem();
		if(statoDiPartenza==null || statoDiPartenza.equals("")) {
			txtResult.appendText("Selezionare uno stato\n");
			return;
		}
		String voli = this.model.getOutgoingFlights(statoDiPartenza);
		txtResult.setText(voli);
	}

	public void setModel(Model model) {
		this.model = model;
		this.cmbBoxStati.getItems().setAll(model.getAllStates());
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
		assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
		assert cmbBoxStati != null : "fx:id=\"cmbBoxStati\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
		assert btnVisualizzaVelivoli != null : "fx:id=\"btnVisualizzaVelivoli\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
		assert txtT != null : "fx:id=\"txtT\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
		assert txtG != null : "fx:id=\"txtG\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
		assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";

	}
}