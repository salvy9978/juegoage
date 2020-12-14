package com.entrenador.coevolutivo;

import java.util.ArrayList;
import java.util.List;

public class Rival {

	private Object jugador;
	private List<Double> memoria;
	private int sizeMemoria;
	private double fitness;
	
	public Rival(Object jugador, int sizeMemoria) {
		this.jugador = jugador;
		this.sizeMemoria = sizeMemoria;
		this.memoria = new ArrayList();
	}
	public Object getJugador() {
		return jugador;
	}

	public void setJugador(Object jugador) {
		this.jugador = jugador;
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public void actualizarFitness(double encuentro) {
		if(this.memoria.size()<this.sizeMemoria) {
			this.memoria.add(encuentro);
		}else {
			this.memoria.remove(0);
			this.memoria.add(encuentro);
		}
		
		this.fitness = 0;
		for(double encuentroEnMemoria:this.memoria){
			this.fitness += encuentroEnMemoria;
		}
		
		
	}
	
	
	
	
	
}
