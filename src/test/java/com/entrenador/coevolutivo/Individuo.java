package com.entrenador.coevolutivo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.entrenador.VariableEstrategia;

public class Individuo {
	private List<Double> vectorCodificacion;
	private List<Double> vectorVarianzas;
	private List<Double> memoria;
	private double fitness;
	private int sizeMemoria;
	private double tau;
	private List<VariableEstrategia> configListaVariables;
	
	public Individuo(int sizeMemoria, List<VariableEstrategia> configListaVariables) {
		this.sizeMemoria = sizeMemoria;
		this.memoria = new ArrayList<Double>();
		this.configListaVariables = configListaVariables;
	}
	
	
	public Individuo(List<Double> vectorCodificacion, List<Double> vectorVarianzas, int sizeMemoria, List<VariableEstrategia> configListaVariables) {
		this.vectorCodificacion = vectorCodificacion;
		this.vectorVarianzas = vectorVarianzas;
		this.sizeMemoria = sizeMemoria;
		this.memoria = new ArrayList<Double>();
		this.tau = 1/(Math.sqrt(2*Math.sqrt(this.vectorCodificacion.size())));
		this.configListaVariables = configListaVariables;
		
	}
	public List<Double> getVectorCodificacion() {
		return vectorCodificacion;
	}
	public void setVectorCodificacion(List<Double> vectorCodificacion) {
		this.vectorCodificacion = vectorCodificacion;
		this.tau = 1/(Math.sqrt(2*Math.sqrt(this.vectorCodificacion.size())));
	}
	public List<Double> getVectorVarianzas() {
		return vectorVarianzas;
	}
	public void setVectorVarianzas(List<Double> vectorVarianzas) {
		this.vectorVarianzas = vectorVarianzas;
		this.tau = 1/(Math.sqrt(2*Math.sqrt(this.vectorCodificacion.size())));
	}
	public List<Double> getMemoria() {
		return memoria;
	}
	public void setMemoria(List<Double> memoria) {
		this.memoria = memoria;
	}
	
	public double getFitness() {
		return fitness;
	}
	
	public double getSizeMemoria() {
		return sizeMemoria;
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
	
	public void mutarIndividuo() {
		for(int i = 0; i<this.vectorCodificacion.size();i++) {
			Random random = new Random();
			double valor = vectorCodificacion.get(i)+random.nextGaussian()*vectorVarianzas.get(i);
			if(valor>this.configListaVariables.get(i).getMaxVariable()) {
				valor = this.configListaVariables.get(i).getMaxVariable();
			}
			if(valor<this.configListaVariables.get(i).getMinVariable()) {
				valor = this.configListaVariables.get(i).getMinVariable();
			}
			vectorCodificacion.set(i, valor);
		}
		
		for(int i = 0; i<this.vectorVarianzas.size(); i++) {
			Random random = new Random();
			double valor = vectorVarianzas.get(i) * Math.exp(random.nextGaussian()*tau);
			vectorVarianzas.set(i, valor);
		}
		
		
	}
	
	
	
	
}
