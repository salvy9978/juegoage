package com.entrenador;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainEntrenador {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		List<VariableEstrategia> variablesConfig = new ArrayList<VariableEstrategia>();
		variablesConfig.add(new VariableEstrategia("actualizacionTiempoBolas", 0, 4, 2, 3));
		variablesConfig.add(new VariableEstrategia("aceleracionBuscarBolasMax", 0, 200, 120, 150));
		variablesConfig.add(new VariableEstrategia("aceleracionBuscarBolasMax", 0, 200, 120, 150));
		variablesConfig.add(new VariableEstrategia("aceleracionBuscarBolasMin", 0, 200, 120, 150));
		variablesConfig.add(new VariableEstrategia("umbralAcercamientoABola", 0, 4000, 2000, 3000));
		variablesConfig.add(new VariableEstrategia("aceleracionAcercarAlCentro", 0, 200, 120, 150));
		variablesConfig.add(new VariableEstrategia("aceleracionChoqueMax", 0, 200, 120, 150));
		
		String rutaRelativa = new File("").getAbsolutePath();
        String rutaProyecto = "\\src\\test\\java\\com\\entrenador\\AgentSalvi.py";
		String jugadorAEntrenar = "python3 "+ "\""+ rutaRelativa + rutaProyecto +"\"";
		
		String rutaProyecto3 = "\\AgentSalvi.py";
		String jugadorAEntrenar3 = "python3 "+ "\""+ rutaRelativa + rutaProyecto3 +"\"";
		
		String rutaRelativa2 = new File("").getAbsolutePath();
        String rutaProyecto2 = "\\src\\test\\java\\com\\entrenador\\AgentSalvi5.py";
		String jugadorAEntrenar2 = "python3 "+ "\""+ rutaRelativa2 + rutaProyecto2 +"\"";
		
		EntrenadorEscaladaEE1 entrenador = new EntrenadorEscaladaEE1(10000, variablesConfig, jugadorAEntrenar3, jugadorAEntrenar2);
		
		entrenador.entrenar();
		
		
		
		
		
	}

}
