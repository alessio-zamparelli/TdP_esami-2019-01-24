package it.polito.tdp.extflightdelays.model;

public class Evento implements Comparable<Evento>{

	private String stato;
	private int data;

	public Evento (String stato, int data) {
		this.stato = stato;
		this.data = data;
	}

	public String getStato() {
		return stato;
	}

	public int getData() {
		return data;
	}

	@Override
	public int compareTo(Evento o) {
		return this.data-o.data;
	}

}
