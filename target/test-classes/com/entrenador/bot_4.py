import sys
import math
from operator import itemgetter

# Made by Illedan, pb4 and Agade

map_radius = int(input())
center_radius = int(input())
min_swap_impulse = int(input())  # Impulse needed to steal a prisoner from another car
car_count = int(input())  # the number of cars you control

wall_control = 0 #indica si hay rebote o no, se reinicia despues de cada print
wall_counter = [0, 0]


###########VARIABLES############

lowSpeedRotation = 5
lowSpeedAngle = 10

mediumSpeedRotation = 30
mediumSpeedAngle = 45

highSpeedRotation = 100
highSpeedAngle = 90

fullSpeedRotation = 200

wallMinAngle = 145 #angulo al target a partir del cual puede priorizar rebotar en la pared
wallMaxDist = 1500 #menos a esta distancia puede que rebote en pared dependiendo del angulo
wallSpeed = 200 #velocidad base para rebotes en pared
wallFactor = 1000 #factor para hallar la nueva posicion para rebotar


###########FUNCIONES############

def reevaluateTarget(n, target, car): #evalua si es necesario ajustar el objetivo, por ejemplo para rebotar contra la pared
    global wall_control
    global wall_counter
    new_target = target[:]

    wall_dist = map_radius - math.sqrt((car[2]**2)+(car[3]**2))
    #print("map_radious  "+str(map_radius)+" - position "+str(math.sqrt((car[2]**2)+(car[3]**2))), file=sys.stderr, flush=True)
    #print("WALL_DIST ID "+str(car[0])+": "+str(wall_dist), file=sys.stderr, flush=True)

    if(wall_counter[n] > 0 or wall_dist<700):
        wall_counter[n] = wall_counter[n]+1
        if(wall_counter[n] == 10):
            wall_counter[n] = 0

    print("RAngulo "+str(car[0])+"/"+str(target[0])+": "+str(getDiferenciaAngulos(car[6], car[2], car[3], target[2], target[3]))+ " Counter: "+str(wall_counter[n]), file=sys.stderr, flush=True)
    if(wall_counter[n] == 0):
        dist = getDistance(target[2], target[3], car[2], car[3])
       # wall_dist = map_radius - math.sqrt((car[2]**2)+(car[3]**2))
        
        if(getDiferenciaAngulos(car[6], car[2], car[3], target[2], target[3]) >= wallMinAngle and wall_dist < wallMaxDist):
            y = math.sin(car[6])*car[3]*wallFactor
            x = math.cos(car[6])*car[3]*wallFactor
            new_target[2] = int(x)
            new_target[3] = int(y)
            print(str(car[0])+" Busca rebote"+str(allies), file=sys.stderr, flush=True)
            wall_control = 1

    return new_target

def getAngulo(x, y):
    angulo = 0
    if(x>0 and y>0):
        anguloAux = math.atan(y/x)
        angulo = math.degrees(anguloAux)
    if(x<0 and y>0):
        anguloAux = math.atan(y/-x)
        angulo = 180 - math.degrees(anguloAux)
    if(x<0 and y<0):
        anguloAux = math.atan(-y/-x)
        angulo = 180 + math.degrees(anguloAux)
    if(x>0 and y<0):
        anguloAux = math.atan(-y/x)
        angulo = 360 - math.degrees(anguloAux)
    return angulo

def getDiferenciaAngulos(angulo, x1, y1, x2, y2):
    anguloAux = getAngulo(x2-x1, y2-y1)
    angulo1 = abs(angulo-anguloAux)
    angulo2 = abs(angulo+abs(360-anguloAux))
    angulo3 = abs(anguloAux+abs(360-angulo))
    angulos = []
    angulos.append(angulo1)
    angulos.append(angulo2)
    angulos.append(angulo3)
    return min(angulos)

def calculateThrust(car, target):
    final_thurst = 10
    global wall_control

    angle = getDiferenciaAngulos(car[6], car[2], car[3], target[2], target[3])
    print("Angulo "+str(car[0])+"/"+str(target[0])+": "+str(angle), file=sys.stderr, flush=True)

    if(angle <= lowSpeedAngle):
        final_thurst = fullSpeedRotation
    elif(angle <= mediumSpeedAngle):
        final_thurst = highSpeedRotation
    elif(angle <= highSpeedAngle):
        final_thurst = mediumSpeedRotation
    else:
        final_thurst = lowSpeedRotation
    
    if(wall_control == 1):
        final_thurst = wallSpeed

    return final_thurst

def getDistance(x1, y1, x2, y2):
    return math.sqrt(math.pow(x1-x2,2)+math.pow(y1-y2,2))

def twoBalls(balls, allies): #si hay dos bolas libres 

    final_targets = []

    dist_matrix = [[balls[0][0]],[balls[1][0]]]

    for i in range(2):
        dist_matrix[i].append([allies[0][0], getDistance(balls[i][2], balls[i][3], allies[0][2], allies[0][3]), balls[i]])
        dist_matrix[i].append([allies[1][0], getDistance(balls[i][2], balls[i][3], allies[1][2], allies[1][3]), balls[i]])

    if(dist_matrix[0][1][1]-dist_matrix[0][2][1] > dist_matrix[1][1][1]-dist_matrix[1][2][1]): #se prioriza el minimo de distancia para los que tengan menor diferencia de distancia

        if(dist_matrix[0][1][1] < dist_matrix[0][2][1]):

            final_targets.append([dist_matrix[0][1][0], dist_matrix[0][1][2]])

            if(dist_matrix[1][1][0] == dist_matrix[0][1][0]):
                final_targets.append([dist_matrix[1][2][0], dist_matrix[1][2][2]])
            else:
                final_targets.append([dist_matrix[1][1][0], dist_matrix[1][1][2]])
        else:
            final_targets.append([dist_matrix[0][2][0], dist_matrix[0][2][2]])
         
            if(dist_matrix[1][1][0] == dist_matrix[0][2][0]):
                final_targets.append([dist_matrix[1][2][0], dist_matrix[1][2][2]])
            else:
                final_targets.append([dist_matrix[1][1][0], dist_matrix[1][1][2]])

    else:

        if(dist_matrix[1][1][1] < dist_matrix[1][2][1]):
            final_targets.append([dist_matrix[1][1][0], dist_matrix[1][1][2]])

            if(dist_matrix[0][1][0] == dist_matrix[1][1][0]):
                final_targets.append([dist_matrix[0][2][0], dist_matrix[0][2][2]])
            else:
                final_targets.append([dist_matrix[0][1][0], dist_matrix[0][1][2]])

        else:
            final_targets.append([dist_matrix[1][2][0], dist_matrix[1][2][2]])

            if(dist_matrix[0][1][0] == dist_matrix[1][2][0]):
                final_targets.append([dist_matrix[0][2][0], dist_matrix[0][2][2]])
            else:
                final_targets.append([dist_matrix[0][1][0], dist_matrix[0][1][2]])
 
    return final_targets


def twoEnemies(enemies, allies): #si hay dos enemigos con bolas

    final_targets = twoBalls(enemies, allies) #funciona igual

    return final_targets


def oneEach(balls, enemies, allies): #si hay una bola libre y un enemigo

    final_targets = []

    dist1 = getDistance(balls[0][2], balls[0][3], allies[0][2], allies[0][3])
    dist2 = getDistance(balls[0][2], balls[0][3], allies[1][2], allies[1][3])

    if(dist1 < dist2):
        final_targets.append([allies[0][0], balls[0]])
        final_targets.append([allies[1][0], enemies[0]])
    else:
        final_targets.append([allies[1][0], balls[0]])
        final_targets.append([allies[0][0], enemies[0]])

    return final_targets

def twoPrisoners(prisoners):

    final_targets = []
    center = [-2, -2, 0, 0]

    for i in range(len(prisoners)):
        final_targets.append([prisoners[i][0], center])

    return final_targets

def onePrisoner(allies, target_list):

    final_targets = []
    center = [-2, -2, 0, 0]

    if(allies[0][7] != -1):
        final_targets.append([allies[0][0], center])
        final_targets.append([allies[1][0], target_list[0]])
    else:
        final_targets.append([allies[0][0], target_list[0]])
        final_targets.append([allies[1][0], center])

    return final_targets

# game loop
while True:
    myscore = int(input())  # your score
    enemyscore = int(input())  # the other player's score
    current_winner = int(input())  # winner as score is now, in case of a tie. -1: you lose, 0: draw, 1: you win
    entities = int(input())  # number of entities this round

    enemies = []
    balls = []
    allies = []
    prisoners = []

    targets = []


    for i in range(entities):
        # 0 _id: the ID of this unit
        # 1 _type: type of entity. 0 is your car, 1 is enemy car, 2 is prisoners
        # 2 x: position x relative to center 0
        # 3 y: position y relative to center 0
        # 4 vx: horizontal speed. Positive is right
        # 5 vy: vertical speed. Positive is downwards
        # 6 angle: facing angle of this car
        # 7 prisoner_id: id of carried prisoner, -1 if none
        _id, _type, x, y, vx, vy, angle, prisoner_id = [int(j) for j in input().split()]
        
        item = [ _id, _type, x, y, vx, vy, angle, prisoner_id]

        if(_type == 0):
            allies.append(item)
            if(item[7] != -1):
                prisoners.append(item)
        elif(_type == 1 and prisoner_id != -1):
            enemies.append(item)
        elif(_type == 2):
            balls.append(item)
        
    print("Allies: "+str(allies), file=sys.stderr, flush=True)
    print("Enemies: "+str(enemies), file=sys.stderr, flush=True)
    print("Balls: "+str(balls), file=sys.stderr, flush=True)

        
    if(len(balls) == 2):
        targets = twoBalls(balls, allies)
    elif(len(enemies) == 2):
        targets = twoEnemies(enemies, allies)
    elif((len(enemies) == 1) and (len(balls) == 1)):
        targets = oneEach(balls, enemies, allies)
    elif(len(prisoners) == 2):
        targets = twoPrisoners(prisoners)
    elif((len(prisoners) == 1) and (len(balls) == 1)):
        targets = onePrisoner(allies, balls)
    elif((len(prisoners) == 1) and (len(enemies) == 1)):
        targets = onePrisoner(allies, enemies)
    else:
        print("ERROR EN OBJETIVOS", file=sys.stderr, flush=True)

        #print(select_target(balls, enemies, allies), file=sys.stderr, flush=True)

    for i in range(car_count):
        # Write an action using print
        # To debug: print("Debug messages...", file=sys.stderr, flush=True)
        for k in range(len(targets)):
            if(allies[i][0] == targets[k][0]):
                targets[k][1] = reevaluateTarget(i, targets[k][1], allies[i]) #calcula si es mejor hacer rebote
                print(str(targets[k][1][2])+" "+str(targets[k][1][3])+" "+str(calculateThrust(allies[i], targets[k][1]))+" "+str(allies[i][0]))
        
        # X Y THRUST MESSAGE
        #print("0 0 50 message")

