package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {
	private List<Esame> esami;
	private double bestMedia;
	private Set<Esame> bestSet;
	
	public Model() {
		EsameDAO dao= new EsameDAO();
		this.esami=dao.getTuttiEsami();
	}
	
	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		bestMedia=0.0;
		bestSet=null;
		Set<Esame> parziale=new HashSet<>();
		cerca(parziale, 0, numeroCrediti);
		
		return bestSet;
	}
	
	public void cerca(Set<Esame> parziale, int L, int m) {
		
		int crediti= sommaCrediti(parziale);
		
		//Caso terminale 1: troppi crediti
		if(crediti>m) {
			return;
		}
		
		//Crediti esatti. Calcola media.
		if(crediti==m) {
			double media=calcolaMedia(parziale);
			if(media>bestMedia) {
				this.bestSet=new HashSet<>(parziale);
				this.bestMedia=media;
			}
		}
		
		if(L==esami.size()) { //SE IL LIVELLO HA RAGGIUNTO IL NUMERO DI TUTTI GLI ESAMI MA NON HO ANCORA I CREDITI
			return;
		}
		
		parziale.add(esami.get(L));
		cerca(parziale, L+1,m);
		parziale.remove(esami.get(L));
		
		cerca(parziale,L+1,m);
	}

	private int sommaCrediti(Set<Esame> parziale) {
		int crediti=0;
		for(Esame e: parziale) {
			crediti+=e.getCrediti();
		}
		return crediti;
	}

	/**
	 * Calcola la media pesata (voti*crediti)
	 * @param parziale
	 * @return
	 */
	public double calcolaMedia(Set<Esame> parziale) {
		int somma=0;
		int crediti=0;
		for(Esame e: parziale) {
			crediti+=e.getCrediti();
			somma+=e.getCrediti()*e.getVoto();
		}
		return (double)(somma/crediti);
	}
}
