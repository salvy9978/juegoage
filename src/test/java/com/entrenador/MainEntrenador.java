package com.entrenador;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainEntrenador {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		List<VariableEstrategia> variablesConfig = new ArrayList<VariableEstrategia>();
		/*variablesConfig.add(new VariableEstrategia("actualizacionTiempoBolas", 0, 4, 2, 3));
		variablesConfig.add(new VariableEstrategia("aceleracionBuscarBolasMax", 0, 200, 120, 150));
		variablesConfig.add(new VariableEstrategia("aceleracionBuscarBolasMax", 0, 200, 120, 150));
		variablesConfig.add(new VariableEstrategia("aceleracionBuscarBolasMin", 0, 200, 120, 150));
		variablesConfig.add(new VariableEstrategia("umbralAcercamientoABola", 0, 4000, 2000, 3000));
		variablesConfig.add(new VariableEstrategia("aceleracionAcercarAlCentro", 0, 200, 120, 150));
		variablesConfig.add(new VariableEstrategia("aceleracionChoqueMax", 0, 200, 120, 150));*/
		
		variablesConfig.add(new VariableEstrategia("distanciaUmbralCogerBola", 0, 4000, 3000, 3500));
		variablesConfig.add(new VariableEstrategia("distanciaUmbralCirculoDefensa", 0, 4000, 3000, 3500));
		variablesConfig.add(new VariableEstrategia("distanciaUmbralCirculoAtaque", 0, 4000, 3000, 3500));
		variablesConfig.add(new VariableEstrategia("distanciaEnemigoEstaEnCentro", 0, 4000, 3000, 3500));
		variablesConfig.add(new VariableEstrategia("distanciaUmbralFrenarParaDefender", 0, 4000, 3000, 3500));
		variablesConfig.add(new VariableEstrategia("umbralRentaIrPorBola", 0, 4000, 3000, 3500));
		variablesConfig.add(new VariableEstrategia("anguloParaIrAlCentro", 0, 180, 120, 160));
		variablesConfig.add(new VariableEstrategia("umbralAnguloChoqueConMiCoche", 0, 180, 120, 160));
		variablesConfig.add(new VariableEstrategia("umbralAnguloChoqueConCocheEnemigo", 0, 180, 120, 160));
		variablesConfig.add(new VariableEstrategia("penalizacionMetrosPorAngulo", 0, 4000, 3000, 3500));
		
		
		String rutaRelativa = new File("").getAbsolutePath();
        String rutaProyecto = "\\Agent2Train.py";
		String jugadorAEntrenar = "python3 "+ "\""+ rutaRelativa + rutaProyecto +"\"";
		System.out.println(jugadorAEntrenar);
        String rutaProyecto2 = "\\src\\test\\java\\com\\entrenador\\AgentSalvi5.py";
		String jugadorARival = "python3 "+ "\""+ rutaRelativa + rutaProyecto2 +"\"";
		
		EntrenadorEscaladaEE1 entrenador = new EntrenadorEscaladaEE1(10000, variablesConfig, jugadorAEntrenar, jugadorARival);
		
		entrenador.entrenar();
		
		
		
		
		
	}

}
