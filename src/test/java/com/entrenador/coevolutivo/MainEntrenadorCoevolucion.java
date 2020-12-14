package com.entrenador.coevolutivo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.entrenador.VariableEstrategia;

public class MainEntrenadorCoevolucion {

	public static void main(String[] args) {
		List<VariableEstrategia> variablesConfig = new ArrayList<VariableEstrategia>();
		variablesConfig.add(new VariableEstrategia("distanciaUmbralCogerBola", 0, 4000, 500, 1500));
		variablesConfig.add(new VariableEstrategia("distanciaUmbralCirculoDefensa", 0, 4000, 500, 1500));
		variablesConfig.add(new VariableEstrategia("distanciaUmbralCirculoAtaque", 0, 4000, 500, 1500));
		variablesConfig.add(new VariableEstrategia("distanciaEnemigoEstaEnCentro", 0, 4000, 500, 1500));
		variablesConfig.add(new VariableEstrategia("distanciaUmbralFrenarParaDefender", 0, 4000, 500, 1500));
		variablesConfig.add(new VariableEstrategia("umbralRentaIrPorBola", 0, 4000, 500, 1500));
		variablesConfig.add(new VariableEstrategia("anguloParaIrAlCentro", 0, 180, 20, 60));
		variablesConfig.add(new VariableEstrategia("umbralAnguloChoqueConMiCoche", 0, 180, 20, 60));
		variablesConfig.add(new VariableEstrategia("umbralAnguloChoqueConCocheEnemigo", 0, 180, 20, 60));
		variablesConfig.add(new VariableEstrategia("penalizacionMetrosPorAngulo", 0, 4000, 500, 1500));
		
		String rutaRelativa = new File("").getAbsolutePath();
        String rutaProyecto = "\\Agent2Train.py";
		String jugadorAEntrenar = "python3 "+ "\""+ rutaRelativa + rutaProyecto +"\"";
		
		EntrenadorCoevolucion entrenadorCoevolucion;
		entrenadorCoevolucion = new EntrenadorCoevolucion(10000, 0.1, 10, 10, 20, variablesConfig, jugadorAEntrenar);

		entrenadorCoevolucion.entrenar();
	}

}
