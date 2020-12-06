import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Made by Illedan, pb4 and Agade
 **/
class AgentAdri {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int mapRadius = in.nextInt();
        int centerRadius = in.nextInt();
        int minSwapImpulse = in.nextInt(); // Impulse needed to steal a prisoner from another car
        int carCount = in.nextInt(); // the number of cars you control

        // game loop
        while (true) {
            int myscore = in.nextInt(); // your score
            int enemyscore = in.nextInt(); // the other player's score
            int currentWinner = in.nextInt(); // winner as score is now, in case of a tie. -1: you lose, 0: draw, 1: you win
            int entities = in.nextInt(); // number of entities this round
            
            ArrayList<Car> carPoses = new ArrayList<>();
            ArrayList<Car> balls = new ArrayList<>();
            ArrayList<Car> enemies = new ArrayList<>(); //se almacenan los enemigos si tienen una bola
            
            for(int i = 0; i < entities; i++){
                int id = in.nextInt(); // the ID of this unit (-2 center)
                int type = in.nextInt(); // type of entity. 0 is your car, 1 is enemy car, 2 is prisoners
                int x = in.nextInt(); // position x relative to center 0
                int y = in.nextInt(); // position y relative to center 0
                int vx = in.nextInt(); // horizontal speed. Positive is right
                int vy = in.nextInt(); // vertical speed. Positive is downwards
                int angle = in.nextInt(); // facing angle of this car
                int prisonerId = in.nextInt(); // id of carried prisoner, -1 if none

                if(type == 0)
                    carPoses.add(new Car(x, y, prisonerId, id, vx, vy, angle));
                if(type == 1 && prisonerId != -1) //se almacena si el enemigo tiene una bola
                    enemies.add(new Car(x, y, prisonerId, id, vx, vy, angle));
                if(type == 2)
                    balls.add(new Car(x, y, prisonerId, id, vx, vy, angle));
                
               // System.err.println("ID: "+ id+" (type: "+ type+"): "+ angle);
            }
            Car target = new Car(0,0,0,-2,0,0,0);

			//if(balls.size()>0) target = balls.get(0);

            for (int i = 0; i < carCount; i++) {

                Car car = carPoses.get(i);
                int thrust = 150;
                System.err.println("CAR "+car.id+" prisonerID: "+ car.ballId);
                if(car.ballId != -1){
                   System.err.println("ENTRA 0");
                   target = new Car(0,0,0,-2,0,0,0); //si tiene una pelota, va al centro, deberia cambiarse para que vaya a un lado del centro
                }
                else if(balls.size()>0 && Car.anyTargetNotTracked(balls)){ //si hay bolas y no estan trackeadas
                    System.err.println("ENTRA 1");
                    System.err.println("BALLS SIZE :"+ balls.size());

                    Car goal = car.getclosestTarget(balls);
                    goal.tracked = true;
                    target = goal;
                    
                }
                else if(enemies.size()>0 && Car.anyTargetNotTracked(enemies)){//a estampar a los enemigos
                    System.err.println("ENTRA 2");
                    System.err.println("ENEMY SIZE :"+ balls.size());

                    Car goal = car.getclosestTarget(enemies);
                    goal.tracked = true;
                    target = goal;
                    
                }
                System.err.println("TARGET "+car.id+": "+ target.id);
                String s = i == 0? "Ic3_a" : "Ic3_b";

                thrust = car.calculateThrust(target);

                System.out.println(target.X + " " + target.Y + " " + thrust + " " + s);
            }
        }
    
    }
      public static class Car{
        public int X, Y, ballId, id, vx, vy, angle;
        public boolean tracked = false;
        public int targetID = -1;
        public Car(int x, int y, int ballId, int id, int vx, int vy, int angle){
            X = x;
            Y = y;
            this.ballId = ballId;
            this.id = id;
            this.vx = vx;
            this.vy = vy;
            this.angle = angle;
        }

        public double distance(Car c2){
            double distance = Math.sqrt(Math.pow(c2.X-X, 2) + Math.pow(c2.Y-Y, 2));
            System.err.println("DISTANCE "+this.id+ " & "+c2.id+": "+distance);
            return distance;    
        }

        public static boolean anyTargetNotTracked(ArrayList<Car> list){ //devuelve si hay algun item no trackeado
            boolean sol = false;
             for(int j=0 ; j<list.size(); j++){
                Car ball = list.get(j);
                if(ball.tracked == false) sol = true;
            }
            return sol;
        }

        public Car getclosestTarget(ArrayList<Car> list){
            double min_dist = 100000;
            Car target = new Car(0,0,0,-3,0,0,0);
            for(int j=0 ; j<list.size(); j++){
                Car item = list.get(j);
                double item_dist = this.distance(item);
                if(item_dist < min_dist && item.tracked == false){
                    min_dist = item_dist;
                    target = item;
                }
            }
            return target;
        }

        public int calculateThrust(Car target){
            int thrust = 150;
            if(this.distance(target) < 550) thrust = 10;
            //necesito un sistema que si el objetivo necesita girarse mucho, no acelere casi nada
            return thrust;
        }
    
    }
}