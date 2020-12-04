import com.codingame.gameengine.runner.MultiplayerGameRunner;

public class Main {
    public static void main(String[] args) {

        /* Multiplayer Game */
        MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();
        gameRunner.setLeagueLevel(4);
        
        gameRunner.addAgent(AgentAGE1.class);
        gameRunner.addAgent(AgentAGE2.class);
        
        //gameRunner.simulate(); //Sirve para simular partidas

        // Para incluir agentes en python
        // gameRunner.addAgent("python3 /home/user/player.py");
        
        gameRunner.start(); //Sirve para arrancar el servidor
    }
}
