package com.entrenador.coevolutivo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.entrenador.VariableEstrategia;

public class Poblacion {
	private int topMejores = 5;
	private List<Individuo> mejoresIndividuos;
	private List<Individuo> poblacion;
	private int sizePoblacion;
	private List<VariableEstrategia> configListaVariables;
	private static int memoriaIndividuos;
	private double minVarianzaRenta;
	
	public Poblacion(int sizePoblacion, int memoriaIndividuos, List<VariableEstrategia> configListaVariables, double minVarianzaRenta) {
		this.sizePoblacion = sizePoblacion;
		Poblacion.memoriaIndividuos = memoriaIndividuos;
		this.configListaVariables = configListaVariables;
		this.minVarianzaRenta = minVarianzaRenta;
	}


	public List<Individuo> getMejoresIndividuos() {
		return mejoresIndividuos;
	}


	public List<Individuo> getPoblacion() {
		return poblacion;
	}


	public void setPoblacion(List<Individuo> poblacion) {
		this.poblacion = poblacion;
	}
	
	public List<Individuo> inicializarPoblacion(){
		List<Individuo> listaIndividuosPoblacion = new ArrayList<Individuo>();
		for(int i= 0; i<this.sizePoblacion;i++) {
			listaIndividuosPoblacion.add(inicializarIndividuo(configListaVariables));
			
		}
		this.poblacion = listaIndividuosPoblacion;
		
		return listaIndividuosPoblacion;
	}
	
	
	public Individuo seleccionarIndividuo() {
		Individuo individuoSeleccionado = null;
		WeightedRandomBag<Individuo> poblacionWheel = new WeightedRandomBag<>();
		double fitnessMinimo = 0;
		for(Individuo i:this.poblacion) {
			if(i.getFitness()<fitnessMinimo) {
				fitnessMinimo = i.getFitness();
			}
		}
		
		for(Individuo i:this.poblacion) {
			poblacionWheel.addEntry(i, i.getFitness()-fitnessMinimo+1);
		}
		
		individuoSeleccionado = poblacionWheel.getRandom();
		return individuoSeleccionado;
	}
	
	public void insertarIndividuoSiRenta(Individuo individuoAInsertar) {
		int indexMinimo = 0;
		double fitnessMinimo = 0;
		boolean noCambiado = true;
		double fitnessAux = 0;
		for(int i = 0; i<this.poblacion.size();i++) {
			fitnessAux = this.poblacion.get(i).getFitness();
			if(fitnessAux<fitnessMinimo || noCambiado) {
				indexMinimo = i;
				fitnessMinimo = fitnessAux;
				noCambiado = false;
			}
		}
		
		if(individuoAInsertar.getFitness()>this.poblacion.get(indexMinimo).getFitness()) {
			this.poblacion.remove(indexMinimo);
			this.poblacion.add(individuoAInsertar);
		}
	}
	
	
	boolean rentaSeguir() {
		for(Individuo i:this.poblacion) {
			for(Double d:i.getVectorVarianzas()) {
				if(d>this.minVarianzaRenta) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	public Individuo getMejorIndividuo() {
		Individuo mejorIndividuo = null;
		double fitness = 0;
		boolean cambiado = false;
		for(Individuo i:this.poblacion){
			if(i.getFitness()>fitness || !cambiado) {
				mejorIndividuo = i;
				cambiado = true;
				fitness = i.getFitness();
			}
		}
		return mejorIndividuo;
	}
	
	
	public static Individuo inicializarIndividuo(List<VariableEstrategia> configListaVariables) {
		Individuo individuo = new Individuo(memoriaIndividuos, configListaVariables);
		List<Double> vectorCodificacion = new ArrayList<Double>();
		List<Double> vectorVarianzas = new ArrayList<Double>();
		for(VariableEstrategia variableEstrategia:configListaVariables) {
			vectorCodificacion.add(getRandom(variableEstrategia.getMinVariable(), variableEstrategia.getMaxVariable()));
			vectorVarianzas.add(getRandom(variableEstrategia.getMinVarianza(), variableEstrategia.getMaxVarianza()));
		}
		
		individuo.setVectorCodificacion(vectorCodificacion);
		individuo.setVectorVarianzas(vectorVarianzas);
		
		return individuo;
	}
	
	public static double getRandom(double valorMinimo, double valorMaximo) {
	    Random rand = new Random();
	    return  valorMinimo + ( valorMaximo - valorMinimo ) * rand.nextDouble();
	}
	
	
}
