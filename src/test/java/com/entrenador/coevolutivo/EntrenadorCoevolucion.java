package com.entrenador.coevolutivo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.codingame.gameengine.runner.MultiplayerGameRunner;
import com.codingame.gameengine.runner.dto.GameResult;
import com.entrenador.AgentAGE2;
import com.entrenador.VariableEstrategia;

public class EntrenadorCoevolucion {

	private PoblacionRival poblacionRival;
	private Poblacion poblacion;
	private int ciclos;
	private Object jugadorAEntrenar;
	private int n;
	private static int memoria;
	private List<VariableEstrategia> configListaVariables;
	
	public EntrenadorCoevolucion(int ciclos, double minVarianzaRentaSeguir, int sizePoblacion, int memoria, int n, List<VariableEstrategia> configListaVariables, Object jugadorAEntrenar){
		this.ciclos = ciclos;
		this.poblacion = new Poblacion(sizePoblacion, memoria, configListaVariables, minVarianzaRentaSeguir);
		this.jugadorAEntrenar = jugadorAEntrenar;
		this.n = n;
		this.memoria = memoria;
		this.configListaVariables = configListaVariables;
		this.poblacionRival = new PoblacionRival();
	}
	
	
	
	public void entrenar() {
		File archivoSoluciones = null;
		FileWriter fwArchivoSoluciones = null;
		try {
			archivoSoluciones = new File("soluciones.txt");
			archivoSoluciones.createNewFile();
			fwArchivoSoluciones = new FileWriter(archivoSoluciones.getAbsoluteFile(), true);
		  } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		  }
		
		
		
		this.inicializacion();
		this.evaluarPoblacionInicio();
		int contador = 0;
		while(contador<this.ciclos && poblacion.rentaSeguir()) {
			//ciclo coevolucion
			for(int i=0; i<n; i++) {
				Individuo individuo = poblacion.seleccionarIndividuo();
				Rival rival = poblacionRival.seleccionarEjemplo();
				Map<Integer, Integer> scores = this.getInidividualFitness(individuo, rival);
				individuo.actualizarFitness(scores.get(0)-scores.get(1));
				rival.actualizarFitness(scores.get(1)-scores.get(0));
			}
			//generar hijo
			
			Individuo individuo1 = poblacion.seleccionarIndividuo();
			
			Individuo individuo2 = poblacion.seleccionarIndividuo();
			
			Individuo hijo = cruzarIndividuo(individuo1, individuo2, this.configListaVariables);
			hijo.mutarIndividuo();
			
			for(int i=0;i<hijo.getSizeMemoria();i++) {
				Rival rival = poblacionRival.seleccionarEjemplo();
				Map<Integer, Integer> scores = this.getInidividualFitness(hijo, rival);
				hijo.actualizarFitness(scores.get(0)-scores.get(1));
				rival.actualizarFitness(scores.get(1)-scores.get(0));
			}
			poblacion.insertarIndividuoSiRenta(hijo);
			this.imprimirMejorIndividuo(fwArchivoSoluciones, contador);
			if(contador%25==0) {
				this.imprimirStats(fwArchivoSoluciones);
			}
			
			++contador;
		}
		
		
	}

	
	public void inicializacion() {
		poblacion.inicializarPoblacion();
		String rutaRelativa = new File("").getAbsolutePath();
		String rival1 = "python3 "+ "\""+ rutaRelativa + "\\src\\test\\java\\com\\entrenador\\AgentSalvi.py" +"\"";
		String rival2 = "python3 "+ "\""+ rutaRelativa + "\\src\\test\\java\\com\\entrenador\\bot_4.py" +"\"";
		String rival3 = "python3 "+ "\""+ rutaRelativa + "\\src\\test\\java\\com\\entrenador\\AgentSalvi5.py" +"\"";
		
		
		poblacionRival.addRival(rival1, memoria);
		poblacionRival.addRival(rival2, memoria);
		poblacionRival.addRival(rival3, memoria);
		poblacionRival.addRival(rival3, memoria);
		poblacionRival.addRival(AgentAGE2.class, memoria);
	}
	
	
	
	public void evaluarPoblacionInicio() {
		for(Individuo i:poblacion.getPoblacion()) {
			for(int j=0; j<i.getSizeMemoria();j++) {
				int quienEntrena = j % poblacionRival.getListaEjemplosRival().size();
				Rival jugadorRival = poblacionRival.getListaEjemplosRival().get(quienEntrena);
				Map<Integer, Integer> scores = getInidividualFitness(i, jugadorRival);
				//actualizar fitness solo en poblacion, no en la rival
				i.actualizarFitness(scores.get(0)-scores.get(1));
			}
			
		}
	}
	
	
	
	public Map<Integer, Integer> getInidividualFitness(Individuo individuo, Rival rival) {
		
		Map<Integer, Integer> scores;
		File archivoVariables = new File("archivoVariables.json");
		try {
			archivoVariables.createNewFile();
			FileWriter fw = new FileWriter(archivoVariables.getAbsoluteFile(), false);
			JSONObject json = new JSONObject();
			for(int i=0;i<configListaVariables.size();i++) {
				
				json.put(configListaVariables.get(i).getNombreVariable(),individuo.getVectorCodificacion().get(i).intValue());
			}
			fw.append(json.toString());
			fw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		GameResult gameResult;
		
		MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();
		
		gameRunner.setLeagueLevel(4);
		if(jugadorAEntrenar instanceof String) {
			gameRunner.addAgent((String)jugadorAEntrenar);
		}else {
			gameRunner.addAgent((Class<?>) jugadorAEntrenar);
		}
		
		if(rival.getJugador() instanceof String) {
			gameRunner.addAgent((String)rival.getJugador());
		}else {
			gameRunner.addAgent((Class<?>) rival.getJugador());
		}
		
		gameResult = gameRunner.simulate();
	    scores = gameResult.scores;
		   
		 //int partidaGolesMarcadosMios = scores.get(0);
		 //int partidaGolesMarcadosRival = scores.get(1);

		return scores;
	}
	
	
	public void imprimirMejorIndividuo(FileWriter fwArchivoSoluciones, int ciclo) {
		try {
			fwArchivoSoluciones.append("VectorCod: "+ poblacion.getMejorIndividuo().getVectorCodificacion().toString()+ " VectorVar: "+poblacion.getMejorIndividuo().getVectorVarianzas().toString()+ " Fitness: "+poblacion.getMejorIndividuo().getFitness()+ " Ciclo: "+ciclo+"\n");
			fwArchivoSoluciones.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	
	public void imprimirStats(FileWriter fwArchivoSoluciones) {
		int jugadasPorRival = 20;
		int partidas = poblacionRival.getListaEjemplosRival().size()*jugadasPorRival;
		Map<Integer, Integer> scores;
		int contador = 1;
		Individuo mejorInidividuo = this.poblacion.getMejorIndividuo();
		for(Rival i:poblacionRival.getListaEjemplosRival()) {
			int partidasGanadas = 0;
			for(int j=0;j<jugadasPorRival;j++) {
				scores = getInidividualFitness(mejorInidividuo, i);
				if(scores.get(0)>scores.get(1)) {
					++partidasGanadas;
				}
			}
			try {
				fwArchivoSoluciones.append("Partida Ganadas contra Rival" + contador + ": "+ partidasGanadas +" de "+jugadasPorRival +"\n");
				fwArchivoSoluciones.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			++contador;
		}
	}
	
	
	public static Individuo cruzarIndividuo(Individuo individuo1, Individuo individuo2, List<VariableEstrategia> configListaVariables) {
		Individuo inidividuoCruzado = null;
		List<Double> vectorCod = new ArrayList<Double>();
		List<Double> vectorVar = new ArrayList<Double>();
		
		for(int i = 0; i<individuo1.getVectorVarianzas().size(); i++) {
			vectorCod.add((individuo1.getVectorVarianzas().get(i)+individuo2.getVectorVarianzas().get(i))/2);
			int randomBit =   (int)(Math.random() * (1 - 0 + 1) + 0); //(int)(Math.random() * (max - min + 1) + min);
			if(randomBit==0) {
				vectorVar.add(individuo1.getVectorVarianzas().get(i));
			}else {
				vectorVar.add(individuo2.getVectorVarianzas().get(i));
			}
		}
		
		inidividuoCruzado = new Individuo(vectorCod, vectorVar,  memoria, configListaVariables);
		
		
		return inidividuoCruzado;
	}
	
	
	
	
	
}
