import java.io.File;
import java.net.URL;

import com.codingame.gameengine.runner.MultiplayerGameRunner;

public class Main {
    public static void main(String[] args) {
    	String os = "Windows";
        /* Multiplayer Game */
        MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();
        gameRunner.setLeagueLevel(4);
        
       // gameRunner.addAgent(AgentAGE1.class);
        

        // Para incluir agentes en python
        switch(os) {
        case "NonePython":
        	gameRunner.addAgent(AgentAGE2.class);
        	break;
        case "Windows":
        	String rutaRelativa = new File("").getAbsolutePath();
            String rutaProyecto = "\\src\\test\\java\\Agent.py";
            gameRunner.addAgent("python3 "+ "\""+ rutaRelativa + rutaProyecto +"\"");
            gameRunner.addAgent("python3 "+ "\""+ rutaRelativa + rutaProyecto +"\"");
        	break;
        case "Linux":
        	gameRunner.addAgent("python3 /home/user/player.py");
        	break;
        }
        
        //gameRunner.simulate(); //Sirve para simular partidas
        gameRunner.start(); //Sirve para arrancar el servidor
    }
}
