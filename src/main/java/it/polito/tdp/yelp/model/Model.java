package it.polito.tdp.yelp.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	
	private Map<String, Review> idMapRec;
	private YelpDao dao;
	private Graph<Review, DefaultWeightedEdge> grafo;
	
	public Model() {
		this.dao = new YelpDao();
		this.idMapRec = new HashMap<>();
		
		this.dao.getAllReviews(idMapRec);
		
	}
	
	public String creaGrafo(Business b) {
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(this.grafo, dao.getVertici(b, this.idMapRec));
		
		for(Adiacenza a : dao.getArchi(b, idMapRec)) {
			Graphs.addEdgeWithVertices(this.grafo, a.getR1(), a.getR2(), a.getPeso());
		}
			
		return "Grafo creato con "+this.grafo.vertexSet().size()+" vertici e "+this.grafo.edgeSet().size()+" archi\n";
		
	}
	
	public String getRMax() {
		String out = "Recensione con maggior numero di archi uscenti: \n";
		int max = 0;
		for(Review r : this.grafo.vertexSet()) {
			if(Graphs.successorListOf(this.grafo, r).size()>max) {
				max = Graphs.successorListOf(this.grafo, r).size();
			}
		}
		
		for(Review r : this.grafo.vertexSet()) {
			if(Graphs.successorListOf(this.grafo, r).size()==max) {
				out += r.getReviewId()+" - #ARCHI USCENTI : "+max+"\n";
			}
		}
		
		return out;
		
	}

	public List<Business> getLocali(String citta) {
		return dao.getLocaliCitta(citta);
	}

	public List<String> getCitta() {
		return dao.getAllCities();
	}
	
	
}
