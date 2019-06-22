package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.util.NeighborCache;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Simulatore {

	private int T;
	private int G;

	private Graph<String, DefaultWeightedEdge> grafo;
	private NeighborCache<String, DefaultWeightedEdge> neighborCache;
	private PriorityQueue<Evento> queue;
	private Map<String, Integer> totVoliDa;
	private Map<String, Integer> risultatoSimulazione;
	private Map<String,Map<String, Double>> nextStateIdMap;
	private Random r = new Random();

	public void init(String statoIniziale, int T, int G, Graph<String, DefaultWeightedEdge> grafo) {
		queue = new PriorityQueue<>();
		nextStateIdMap = new HashMap<>();
		this.grafo = grafo;
		this.risultatoSimulazione = new HashMap<>();
		this.neighborCache = new NeighborCache<>(this.grafo);
		this.totVoliDa = new HashMap<String, Integer>();
		this.T = T;
		this.G = G;

		for (int i = 0; i < T; i++)
			queue.add(new Evento(statoIniziale, 0));

	}

	public void run() {

		Evento e;
		while ((e = queue.poll()) != null) {
			String statoAttuale = e.getStato();
			String prossimoStato = nextState(statoAttuale);
			if (e.getData() < this.G) {
				Evento newEvento = new Evento(prossimoStato, e.getData() + 1);
				queue.add(newEvento);
			} else {
				if (!this.risultatoSimulazione.containsKey(e.getStato()))
					this.risultatoSimulazione.put(e.getStato(), 1);
				else
					this.risultatoSimulazione.put(e.getStato(), this.risultatoSimulazione.get(e.getStato()) + 1);
			}
		}
//		System.out.println("\n\n------------------------\nRisultato simulazione");
//		risultatoSimulazione.forEach((k, v) -> System.out.println(k + " - " + v));
//		double totPersone = risultatoSimulazione.values().parallelStream().mapToDouble(a->a).sum();
//		System.out.println("totale persone: " + totPersone);
	}

	private String nextState(String statoAttuale) {
		Map<String, Double> pesi;
		if(nextStateIdMap.containsKey(statoAttuale)) {
			pesi = nextStateIdMap.get(statoAttuale);
		} else {
		double pesoTot = 0;
		
		Set<DefaultWeightedEdge> archiUscenti = this.grafo.outgoingEdgesOf(statoAttuale);
		pesi = new HashMap<>();
//		if (this.totVoliDa.containsKey(statoAttuale)) {
//			pesoTot = totVoliDa.get(statoAttuale);
//		} else {

			pesoTot = 0;
			for (DefaultWeightedEdge e : archiUscenti) {

//				System.out.println("archo: " + e.toString());
//				double peso = this.grafo.getEdgeWeight(e);
//				System.out.println("peso: " + peso);
				pesoTot += this.grafo.getEdgeWeight(e);

				pesi.put(this.grafo.getEdgeTarget(e), this.grafo.getEdgeWeight(e));

			}
			
			for(String s: pesi.keySet()) {
				pesi.put(s, pesi.get(s)/pesoTot);
			}
			nextStateIdMap.put(statoAttuale, pesi);
//		}
		
		}
		double min = 0.0;
		
		double choose = this.r.nextDouble();
		
//		System.out.println("\n\nValore selezionato: " + choose*100);
//		System.out.println("Possibili valori:");
		for (Map.Entry<String, Double> entry : pesi.entrySet()) {
			double max = min + (entry.getValue());
//			double attuale = entry.getValue();
//			System.out.println("Stato : " + entry.getKey() + " al " + attuale*100 + "%");
//			System.out.println("Valuto:");
//			System.out.println("Minimo = " + min);
//			System.out.println("Massimo = " + max);
//			System.out.println("valore considerato = " + attuale);
//			System.out.println("Scelto = " + choose);
			if (choose > min && choose <= max) {
//				System.out.println("Stato scelto: " + entry.getKey());
				return entry.getKey();
			}
				
			min = max;

		}

		return "";

	}
	
	public Map<String, Integer> getRisultatiSimulazione(){
		return this.risultatoSimulazione;
	}
}
