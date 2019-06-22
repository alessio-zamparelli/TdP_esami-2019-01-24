package it.polito.tdp.extflightdelays.model;

public class TestModel {

	public static void main(String[] args) {
		
		Model model = new Model();
		model.creaGrafo();
		String statoPartenza = model.getAllStates().get(0);
		model.simula(statoPartenza, 100000, 50);
	}

}
