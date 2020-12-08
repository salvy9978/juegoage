package com.entrenador;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainEntrenador {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		List<VariableEstrategia> variablesConfig = new ArrayList<VariableEstrategia>();
		variablesConfig.add(new VariableEstrategia("actualizacionTiempoBolas", 0, 4, 0, 1));
		variablesConfig.add(new VariableEstrategia("aceleracionBuscarBolasMax", 0, 200, 0, 150));
		variablesConfig.add(new VariableEstrategia("aceleracionBuscarBolasMin", 0, 200, 0, 150));
		variablesConfig.add(new VariableEstrategia("umbralAcercamientoABola", 0, 4000, 0, 3000));
		variablesConfig.add(new VariableEstrategia("aceleracionAcercarAlCentro", 0, 200, 0, 150));
		variablesConfig.add(new VariableEstrategia("aceleracionChoqueMax", 0, 200, 0, 150));
		
		String rutaRelativa = new File("").getAbsolutePath();
        String rutaProyecto = "\\src\\test\\java\\AgentSalvi.py";
		String jugadorAEntrenar = "python3 "+ "\""+ rutaRelativa + rutaProyecto +"\"";
		
		EntrenadorEscaladaEE1 entrenador = new EntrenadorEscaladaEE1(10, variablesConfig, jugadorAEntrenar, AgentAGE2.class);
		
		entrenador.entrenar();
		
		
		
		
		
		
	}

}
