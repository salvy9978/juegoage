package com.entrenador.coevolutivo;

import java.util.ArrayList;
import java.util.List;

public class PoblacionRival {
	List<Rival> listaEjemplosRival;
	
	public PoblacionRival() {
		listaEjemplosRival = new ArrayList<Rival>();
	}
	
	public void addRival(Object rivalObject, int sizeMemoria) {
		Rival rival = new Rival(rivalObject, sizeMemoria);
		this.listaEjemplosRival.add(rival);
	}
	
	
	
	public Rival seleccionarEjemplo() {
		Rival rivalSeleccionado = null;
		WeightedRandomBag<Rival> poblacionWheel = new WeightedRandomBag<>();
		double fitnessMinimo = 0;
		for(Rival r:this.listaEjemplosRival) {
			if(r.getFitness()<fitnessMinimo) {
				fitnessMinimo = r.getFitness();
			}
		}
		
		for(Rival r:this.listaEjemplosRival) {
			poblacionWheel.addEntry(r, r.getFitness()-fitnessMinimo+1);
		}
		rivalSeleccionado = poblacionWheel.getRandom();
		return rivalSeleccionado;
	}

	public List<Rival> getListaEjemplosRival() {
		return listaEjemplosRival;
	}

	public void setListaEjemplosRival(List<Rival> listaEjemplosRival) {
		this.listaEjemplosRival = listaEjemplosRival;
	}
	
	
	
	
	
	
}
