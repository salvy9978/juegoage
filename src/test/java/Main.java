import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.codingame.gameengine.runner.MultiplayerGameRunner;
import com.codingame.gameengine.runner.dto.GameResult;

public class Main {
    public static void main(String[] args) {
    	String os = "Windows";
        /* Multiplayer Game */
    	
    	GameResult gameResult;
		 MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();
	        gameRunner.setLeagueLevel(4);
	        
	        
	        
	
	        // Para incluir agentes en python
	        switch(os) {
	        case "NonePython":
	        	gameRunner.addAgent(AgentAGE1.class);
	        	break;
	        case "Windows":
	        	String rutaRelativa = new File("").getAbsolutePath();
	            String rutaProyecto = "\\src\\test\\java\\AgentSalvi4.py";
	            gameRunner.addAgent("python3 "+ "\""+ rutaRelativa + rutaProyecto +"\"");
	            
	        	break;
	        case "Linux":
	        	gameRunner.addAgent("python3 /home/user/player.py");
	        	break;
	        }
	        gameRunner.addAgent(AgentAGE2.class);
	        /*
	        gameResult = gameRunner.simulate();
	        //resultados.add(gameRunner.simulate()); //Sirve para simular partidas
	        System.out.println();
	        System.out.println();
	        System.out.println();
	        System.out.println();
	        System.out.println(gameResult.scores.toString());*/
    	
       
        
        
        
        gameRunner.start(); //Sirve para arrancar el servidor
    }
}
