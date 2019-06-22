package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {

	private ExtFlightDelaysDAO dao;

	private Graph<String, DefaultWeightedEdge> grafo;
	private List<String> stati;
	private List<Flight> voli;
	private Map<Integer, Airport> aeroporti;
	private List<Route> rotte;
	private Simulatore sim;

	public Model() {
		this.dao = new ExtFlightDelaysDAO();

	}

	public void creaGrafo() {
		if (this.stati == null)
			this.stati = dao.loadAllStates();
		this.voli = dao.loadAllFlights();
		this.aeroporti = dao.loadAllAirports().stream().collect(Collectors.toMap(Airport::getId, a -> a));
		this.grafo = new DefaultDirectedWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);

		this.rotte = dao.loadAllRoutesCount(this.aeroporti);

		Graphs.addAllVertices(this.grafo, stati);
		for (Flight f : voli) {
			String partenza = aeroporti.get(f.getOriginAirportId()).getState();
			String arrivo = aeroporti.get(f.getDestinationAirportId()).getState();
			grafo.addEdge(partenza, arrivo);
		}

		for (Route r : rotte) {
			Double oldWeight = this.grafo
					.getEdgeWeight(this.grafo.getEdge(r.getDeparture().getState(), r.getDestination().getState()));
			this.grafo.setEdgeWeight(r.getDeparture().getState(), r.getDestination().getState(),
					oldWeight + r.getNumRoute());
		}
//		System.out.println("Stati:\n");
//		stati.forEach(a -> System.out.println(a));
//		System.out.println("Voli:\n");
//		voli.forEach(a -> System.out.println(a));
//		System.out.println("Aeroporti:\n");
//		aeroporti.values().forEach(a -> System.out.println(a));
//		System.out.println("Rotte:\n");
//		rotte.forEach(a -> System.out.println(a));

	}

	public List<String> getAllStates() {
		if (stati == null)
			this.stati = this.dao.loadAllStates().stream().sorted().collect(Collectors.toList());
		return this.stati.stream().sorted().collect(Collectors.toList());
	}

	public String getOutgoingFlights(String state) {
		if (this.grafo == null)
			return "Devi prima inizializzare il grafo\n";
		List<DefaultWeightedEdge> edges = new ArrayList<>();
		edges.addAll(this.grafo.outgoingEdgesOf(state));
		Collections.sort(edges, new Comparator<DefaultWeightedEdge>() {

			@Override
			public int compare(DefaultWeightedEdge o1, DefaultWeightedEdge o2) {
				return (int) grafo.getEdgeWeight(o2) - (int) grafo.getEdgeWeight(o1);
			}
		});
		StringBuilder sb = new StringBuilder();
		for (DefaultWeightedEdge e : edges) {
			if (this.grafo.getEdgeTarget(e) != state && this.grafo.getEdgeWeight(e) > 0) {
				sb.append("Per ");
				sb.append(this.grafo.getEdgeTarget(e));
				sb.append(" ci sono ");
				sb.append((int)this.grafo.getEdgeWeight(e));
				sb.append(" voli\n");
			}
		}
		return sb.toString();
	}
	
	public void simula(String partenza, int T, int G) {
		sim = new Simulatore();
		sim.init(partenza, T, G, grafo);
		sim.run();
	}

	public Map<String, Integer> getRisultatiSimulazione() {
		return sim.getRisultatiSimulazione();
	}

}
