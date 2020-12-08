package com.entrenador;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONObject;

import com.codingame.gameengine.runner.MultiplayerGameRunner;
import com.codingame.gameengine.runner.dto.GameResult;

public class EntrenadorEscaladaEE1 {
	
	private List<VariableEstrategia> configListaVariables;
	private Individuo individuo = new Individuo(null, null);
	private Class<?> playerEntrenarClass;
	private Class<?> playerRivalClass;
	private String playerCmdEntrenar;
	private String playerCmdRival;
	private int ciclos;
	private double c = 0.82;
	private int s = 10;
	private double fitnessMayor = -1;
	private int aciertos[] = new int[s];
	
	//constructores con las distintas formas de meter a los jugadores
	public EntrenadorEscaladaEE1(int ciclos, List<VariableEstrategia> configListaVariables, Class<?> playerEntrenarClass, Class<?> playerRivalClass) {
		this.configListaVariables = configListaVariables;
		this.playerEntrenarClass = playerEntrenarClass;
		this.playerRivalClass = playerRivalClass;
		this.ciclos = ciclos;
	}
	
	public EntrenadorEscaladaEE1(int ciclos, List<VariableEstrategia> configListaVariables, String playerCmdEntrenar, Class<?> playerRivalClass) {
		this.configListaVariables = configListaVariables;
		this.playerCmdEntrenar = playerCmdEntrenar;
		this.playerRivalClass = playerRivalClass;
		this.ciclos = ciclos;
	}
	
	public EntrenadorEscaladaEE1(int ciclos, List<VariableEstrategia> configListaVariables, Class<?> playerEntrenarClass, String playerCmdRival) {
		this.configListaVariables = configListaVariables;
		this.playerCmdRival = playerCmdRival;
		this.playerEntrenarClass = playerEntrenarClass;
		this.ciclos = ciclos;
	}
	
	public EntrenadorEscaladaEE1(int ciclos, List<VariableEstrategia> configListaVariables, String playerCmdEntrenar, String playerCmdRival) {
		this.configListaVariables = configListaVariables;
		this.playerCmdEntrenar = playerCmdEntrenar;
		this.playerCmdRival = playerCmdRival;
		this.ciclos = ciclos;
	}
	
	

	
	//inicializar el vector de codificacion y el vector de varianzas
	public void inicializarAGE() {
		List<Double> vectorCodificacion = new ArrayList<Double>();
		List<Double> vectorVarianzas = new ArrayList<Double>();
		for(VariableEstrategia variableEstrategia:configListaVariables) {
			vectorCodificacion.add(getRandom(variableEstrategia.getMinVariable(), variableEstrategia.getMaxVariable()));
			vectorVarianzas.add(getRandom(variableEstrategia.getMinVarianza(), variableEstrategia.getMaxVarianza()));
		}
		
		individuo.setVectorCodificacion(vectorCodificacion);
		individuo.setVectorVarianzas(vectorVarianzas);
	}
	
	public double getInidividualFitness(List<Double> vectorCodificacion) {
		double fitness = 0;
		int golesMarcadosMios = 0;
		int golesMarcadosRival = 0;
		int partidasGanadas = 0;
		int partidasPerdidas = 0;
		Map<Integer, Integer> scores;
		File archivoVariables = new File("archivoVariables.json");
		try {
			archivoVariables.createNewFile();
			FileWriter fw = new FileWriter(archivoVariables.getAbsoluteFile(), false);
			JSONObject json = new JSONObject();
			for(int i=0;i<configListaVariables.size();i++) {
				json.put(configListaVariables.get(i).getNombreVariable(),vectorCodificacion.get(i).intValue());
			}
			fw.append(json.toString());
			fw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		GameResult gameResult;
		for(int i=0; i<20;i++) {
			
			MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();
		    gameRunner.setLeagueLevel(4);
		    if(playerEntrenarClass!=null) {
		    	gameRunner.addAgent(playerEntrenarClass);
		    }
		    if(playerCmdEntrenar!=null) {
		    	gameRunner.addAgent(playerCmdEntrenar);
		    }
		    
		    if(playerRivalClass!=null) {
		    	gameRunner.addAgent(playerRivalClass);
		    }
		    
		    if(playerCmdRival!=null) {
		    	gameRunner.addAgent(playerCmdRival);
		    }
		    gameResult = gameRunner.simulate();
		    scores = gameResult.scores;
		    int partidaGolesMarcadosMios = scores.get(0);
		    int partidaGolesMarcadosRival = scores.get(1);
		    golesMarcadosMios += partidaGolesMarcadosMios;
		    golesMarcadosRival += partidaGolesMarcadosRival;
		    if(partidaGolesMarcadosMios>partidaGolesMarcadosRival) {
		    	partidasGanadas += 1;
		    }
		    if(partidaGolesMarcadosRival>partidaGolesMarcadosMios) {
		    	partidasPerdidas += 1;
		    }
		    
		    System.out.println(this.individuo.getVectorVarianzas().toString());
		}
		
		fitness = (partidasGanadas - partidasPerdidas)* Math.abs(golesMarcadosMios - golesMarcadosRival);
		
         
		
		
		//return (partidasGanadas>0)?partidasGanadas:partidasPerdidas;
		return fitness;
	}
	
	
	public List<Double> mutarInidividuo(List<Double> vectorCodificacion, List<Double> vectorVarianzas) {
		List<Double> vectorCodificacionMutado = new ArrayList<Double>();
		for(int i=0;i<vectorCodificacion.size();i++) {
			Random random = new Random();
			double valor = vectorCodificacion.get(i)+random.nextGaussian()*vectorVarianzas.get(i);
			if(valor>this.configListaVariables.get(i).getMaxVariable()){
				valor = this.configListaVariables.get(i).getMaxVariable();
			}
			if(valor<this.configListaVariables.get(i).getMinVariable()){
				valor = this.configListaVariables.get(i).getMinVariable();
			}
			
			vectorCodificacionMutado.add(valor);
		}
		
		return vectorCodificacionMutado;
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
		
		this.inicializarAGE();
		this.fitnessMayor = this.getInidividualFitness(this.individuo.getVectorCodificacion());
		try {
			fwArchivoSoluciones.append("VectorCod: "+ individuo.getVectorCodificacion().toString()+ " VectorVar: "+individuo.getVectorVarianzas().toString()+ " Fitness: "+this.fitnessMayor+"\n");
			fwArchivoSoluciones.flush();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		int contador = 0;
		boolean parar = false;
		while(contador<this.ciclos && !parar) {
			List<Double> vectorNuevoInidividuo = this.mutarInidividuo(this.individuo.getVectorCodificacion(), this.individuo.getVectorVarianzas());
			double nuevoFitness = this.getInidividualFitness(vectorNuevoInidividuo);
			if(nuevoFitness > this.fitnessMayor) {
				this.individuo.setVectorCodificacion(vectorNuevoInidividuo);
				this.fitnessMayor = nuevoFitness;
				this.aciertos[contador%this.s] = 1;
				try {
					fwArchivoSoluciones.append("VectorCod: "+ individuo.getVectorCodificacion().toString()+ " VectorVar: "+individuo.getVectorVarianzas().toString()+ " Fitness: "+this.fitnessMayor+"\n");
					fwArchivoSoluciones.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else {
				this.aciertos[contador%this.s] = 0;
			}
            if(contador>=this.s) {
            	int nAciertos = 0;
            	for(int acierto:this.aciertos) {
            		nAciertos += acierto;
            	}
            	
            	float tasaAciertos = nAciertos/this.aciertos.length;
            	if(tasaAciertos > 0.2) {
            		List<Double> nuevoVectorVarianzas = new ArrayList<Double>();
            		for(int i=0; i<this.individuo.getVectorVarianzas().size();i++) {
            			nuevoVectorVarianzas.add(this.individuo.getVectorVarianzas().get(i)/this.c);
            		}
            		this.individuo.setVectorVarianzas(nuevoVectorVarianzas);
            	}
            	
            	if(tasaAciertos < 0.2) {
            		List<Double> nuevoVectorVarianzas = new ArrayList<Double>();
            		for(int i=0; i<this.individuo.getVectorVarianzas().size();i++) {
            			nuevoVectorVarianzas.add(this.individuo.getVectorVarianzas().get(i)*this.c);
            		}
            		this.individuo.setVectorVarianzas(nuevoVectorVarianzas);
            	}
          
            	
            }
            //ver si se ha perdido la posibilidad de variacion genetica para parar
            int contAux = 0;
            for (int i=0; i<this.individuo.getVectorVarianzas().size();i++) {
            	if(this.individuo.getVectorVarianzas().get(i)<0.1) {
            		++contAux;
            	}
            }
            if(contAux==this.individuo.getVectorVarianzas().size()) {
            	parar = true;
            }
            //incrementar ciclo
			contador += 1;
			
		}
		
	}
	
	
	public void evaluarIndividuoFinal() {
		int partidasGanadas = 0;
		Map<Integer, Integer> scores;
		GameResult gameResult;
		int golesMarcadosMios = 0;
		int golesMarcadosRival = 0;
		int numeroPartidasEvFinal = 100;
		
		File archivoVariables = new File("archivoVariables.json");
		try {
			archivoVariables.createNewFile();
			FileWriter fw = new FileWriter(archivoVariables.getAbsoluteFile(), false);
			JSONObject json = new JSONObject();
			for(int i=0;i<configListaVariables.size();i++) {
				json.put(configListaVariables.get(i).getNombreVariable(),this.individuo.getVectorCodificacion().get(i).intValue());
			}
			fw.append(json.toString());
			fw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		for(int i=0; i<numeroPartidasEvFinal;i++) {
			
			MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();
		    gameRunner.setLeagueLevel(4);
		    if(playerEntrenarClass!=null) {
		    	gameRunner.addAgent(playerEntrenarClass);
		    }
		    if(playerCmdEntrenar!=null) {
		    	gameRunner.addAgent(playerCmdEntrenar);
		    }
		    
		    if(playerRivalClass!=null) {
		    	gameRunner.addAgent(playerRivalClass);
		    }
		    
		    if(playerCmdRival!=null) {
		    	gameRunner.addAgent(playerCmdRival);
		    }
		    gameResult = gameRunner.simulate();
		    scores = gameResult.scores;
		    int partidaGolesMarcadosMios = scores.get(0);
		    int partidaGolesMarcadosRival = scores.get(1);
		    golesMarcadosMios += partidaGolesMarcadosMios;
		    golesMarcadosRival += partidaGolesMarcadosRival;
		    if(partidaGolesMarcadosMios>partidaGolesMarcadosRival) {
		    	partidasGanadas += 1;
		    }
		}
		
		 System.out.println("\n\n\n--------------\nWin Rate: " + partidasGanadas + " partidas ganadas de "+ numeroPartidasEvFinal+"\n------------------\n");
		
	}
	
	
	
	public static double getRandom(double valorMinimo, double valorMaximo) {
	    Random rand = new Random();
	    return  valorMinimo + ( valorMaximo - valorMinimo ) * rand.nextDouble();
	}
	
	
	
	
	
	
	
	
	
}
