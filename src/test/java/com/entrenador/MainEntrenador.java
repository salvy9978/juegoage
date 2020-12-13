package com.entrenador;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainEntrenador {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
//		private String nombreVariable;
//		private double minVariable;
//		private double maxVariable;
//		private double minVarianza;
//		private double maxVarianza;
		
		List<VariableEstrategia> variablesConfig = new ArrayList<VariableEstrategia>();
		variablesConfig.add(new VariableEstrategia("actualizacionTiempoBolas", 0, 4, 2, 3));
		variablesConfig.add(new VariableEstrategia("aceleracionBuscarBolasMax", 0, 200, 120, 150));
		variablesConfig.add(new VariableEstrategia("aceleracionBuscarBolasMax", 0, 200, 120, 150));
		variablesConfig.add(new VariableEstrategia("aceleracionBuscarBolasMin", 0, 200, 120, 150));
		variablesConfig.add(new VariableEstrategia("umbralAcercamientoABola", 0, 4000, 2000, 3000));
		variablesConfig.add(new VariableEstrategia("aceleracionAcercarAlCentro", 0, 200, 120, 150));
		variablesConfig.add(new VariableEstrategia("aceleracionChoqueMax", 0, 200, 120, 150));

// 		VARIABLES MODELO AGENTEADRI2
		
//		variablesConfig.add(new VariableEstrategia("factorPenal", 1, 3, 0.01, 0.5));
//		variablesConfig.add(new VariableEstrategia("factorPrediccion", 1, 3, 0.01, 0.5));
//		
//		variablesConfig.add(new VariableEstrategia("distanciaUmbralCogerBola", 500, 5000, 1000, 4000));
//		variablesConfig.add(new VariableEstrategia("distanciaUmbralCirculoDefensa", 500, 3000, 700, 2500));
//		variablesConfig.add(new VariableEstrategia("distanciaUmbralCirculoAtaque", 500, 5000, 1000, 4000));
//		variablesConfig.add(new VariableEstrategia("distanciaEnemigoEstaEnCentro", 200, 2000, 300, 1800));
//		variablesConfig.add(new VariableEstrategia("distanciaUmbralFrenarParaDefender", 500, 3000, 700, 2500));
//
//		variablesConfig.add(new VariableEstrategia("anguloParaIrAlCentro", 5, 30, 5, 25));
//		variablesConfig.add(new VariableEstrategia("umbralAnguloChoqueConMiCoche", 5, 30, 5, 25));
//		variablesConfig.add(new VariableEstrategia("umbralAnguloChoqueConCocheEnemigo", 30, 200, 40, 160));
//		variablesConfig.add(new VariableEstrategia("penalizacionMetrosPorAngulo", 1, 50, 5, 35));
//		variablesConfig.add(new VariableEstrategia("umbralRentaIrPorBola", 500, 6000, 800, 4500));
//		variablesConfig.add(new VariableEstrategia("anguloMaxParaHuir", 10, 80, 15, 65));
//		variablesConfig.add(new VariableEstrategia("velocidadMinimaParaFrenar", 100, 800, 150, 650));

		
		String rutaRelativa = new File("").getAbsolutePath();
        String rutaProyecto = "\\src\\test\\java\\com\\entrenador\\AgentSalvi.py";
		String jugadorAEntrenar = "python3 "+ "\""+ rutaRelativa + rutaProyecto +"\"";
		
		String rutaProyecto3 = "\\agentAdri2.py";
		String jugadorAEntrenar3 = "python3 "+ "\""+ rutaRelativa + rutaProyecto3 +"\"";
		
		String rutaRelativa2 = new File("").getAbsolutePath(); //enemigo
        String rutaProyecto2 = "\\src\\test\\java\\com\\entrenador\\AgentSalvi5.py";
		String jugadorAEntrenar2 = "python3 "+ "\""+ rutaRelativa2 + rutaProyecto2 +"\"";
		
		EntrenadorEscaladaEE1 entrenador = new EntrenadorEscaladaEE1(10000, variablesConfig, jugadorAEntrenar3, jugadorAEntrenar2);
		
		entrenador.entrenar();
		
		
		
		
		
	}

}
