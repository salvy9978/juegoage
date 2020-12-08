package com.entrenador;

import java.util.List;

public class Individuo {
	private List<Double> vectorCodificacion;
	private List<Double> vectorVarianzas;
	
	public Individuo(List<Double> vectorCodificacion, List<Double> vectorVarianzas) {
		this.vectorCodificacion = vectorCodificacion;
		this.vectorVarianzas = vectorVarianzas;
	}
	public List<Double> getVectorCodificacion() {
		return vectorCodificacion;
	}
	public void setVectorCodificacion(List<Double> vectorCodificacion) {
		this.vectorCodificacion = vectorCodificacion;
	}
	public List<Double> getVectorVarianzas() {
		return vectorVarianzas;
	}
	public void setVectorVarianzas(List<Double> vectorVarianzas) {
		this.vectorVarianzas = vectorVarianzas;
	}
	
	
	
	
}
