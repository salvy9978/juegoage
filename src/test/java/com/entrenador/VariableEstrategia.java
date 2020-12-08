package com.entrenador;

public class VariableEstrategia {

	private double minVariable;
	private double maxVariable;
	private double minVarianza;
	private double maxVarianza;
	private String nombreVariable;
	
	
	
	public VariableEstrategia(String nombreVariable, double minVariable, double maxVariable, double minVarianza, double maxVarianza) {
		this.minVariable = minVariable;
		this.maxVariable = maxVariable;
		this.minVarianza = minVarianza;
		this.maxVarianza = maxVarianza;
		this.nombreVariable = nombreVariable;
	}
	public double getMinVariable() {
		return minVariable;
	}
	public void setMinVariable(double minVariable) {
		this.minVariable = minVariable;
	}
	public double getMaxVariable() {
		return maxVariable;
	}
	public void setMaxVariable(double maxVariable) {
		this.maxVariable = maxVariable;
	}
	public double getMinVarianza() {
		return minVarianza;
	}
	public void setMinVarianza(double minVarianza) {
		this.minVarianza = minVarianza;
	}
	public double getMaxVarianza() {
		return maxVarianza;
	}
	public void setMaxVarianza(double maxVarianza) {
		this.maxVarianza = maxVarianza;
	}
	
	public String getNombreVariable() {
		return nombreVariable;
	}
	public void setNombreVariable(String nombreVariable) {
		this.nombreVariable = nombreVariable;
	}
	
}
