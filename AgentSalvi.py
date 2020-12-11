import sys
import math
import json
#import os

#wd = os.getcwd()
#print(wd, file=sys.stderr, flush=True)
map_radius = int(input())
center_radius = int(input())
min_swap_impulse = int(input())  # Impulse needed to steal a prisoner from another car
car_count = int(input())  # the number of cars you control

####################################### VARIABLES #################################
actualizacionTiempoBolas = 0
aceleracionBuscarBolasMax = 0
aceleracionBuscarBolasMin = 0
umbralAcercamientoABola = 0
aceleracionAcercarAlCentro = 0
aceleracionChoqueMax = 0

with open('archivoVariables.json') as file:
    data = json.load(file)
    actualizacionTiempoBolas = data['actualizacionTiempoBolas']
    aceleracionBuscarBolasMax = data['aceleracionBuscarBolasMax']
    aceleracionBuscarBolasMin = data['aceleracionBuscarBolasMin']
    umbralAcercamientoABola = data['umbralAcercamientoABola']
    aceleracionAcercarAlCentro = data['aceleracionAcercarAlCentro']
    aceleracionChoqueMax = data['aceleracionChoqueMax']



######################################### FUNCIONES ##############################
def getDistancia(x1, y1, x2, y2):
    return math.sqrt(math.pow(x1-x2,2)+math.pow(y1-y2,2))
def quienBucarBolas(coches, bolas): #devuelve el id del coche que este mas cercano a una bola
    ids = [-1,-1]
    distancia = 2*map_radius+1
    for i in range(len(bolas)):
        for j in range(len(coches)):
            distanciaAux = getDistancia(coches[i][2],coches[i][3],bolas[i][2],bolas[i][3])
            if(distanciaAux<distancia and coches[j][7]==-1):
                distancia = distanciaAux
                ids[0]=j
                ids[1]=i
    return [ids,distancia]; #ids[0] --> posicion en la lista de coches, id[1] --> posicion en la lista de bolas, distancia --> distancia entre ambas



# game loop
while True:
    myscore = int(input())  # your score
    enemyscore = int(input())  # the other player's score
    current_winner = int(input())  # winner as score is now, in case of a tie. -1: you lose, 0: draw, 1: you win
    entities = int(input())  # number of entities this round
    misCoches = []
    cochesEnemigo = []
    bolas = []
    for i in range(entities):
        # _id: the ID of this unit
        # _type: type of entity. 0 is your car, 1 is enemy car, 2 is prisoners
        # x: position x relative to center 0
        # y: position y relative to center 0
        # vx: horizontal speed. Positive is right
        # vy: vertical speed. Positive is downwards
        # angle: facing angle of this car
        # prisoner_id: id of carried prisoner, -1 if none
        _id, _type, x, y, vx, vy, angle, prisoner_id = [int(j) for j in input().split()]

        entity = [_id, _type, x, y, vx, vy, angle, prisoner_id]

        if _type==0:
            misCoches.append(entity)
        elif _type==1:
            cochesEnemigo.append(entity)
        elif _type==2:
            bolas.append(entity)
    print(misCoches, file=sys.stderr, flush=True)
    print(cochesEnemigo, file=sys.stderr, flush=True)
    print(bolas, file=sys.stderr, flush=True)

    buscaBolas = quienBucarBolas(misCoches, bolas)
    enemigosCochesConBolas = []
    for i in range(len(cochesEnemigo)):
        if(cochesEnemigo[i][7]!=-1):
            enemigosCochesConBolas.append(cochesEnemigo[i])
    for i in range(car_count):
        coche = ""
        # Write an action using print
        # To debug: print("Debug messages...", file=sys.stderr, flush=True)
        dirX = 0
        dirY = 0
        aceleracion = 0

        if(buscaBolas[0][0]==i): #si existen bolas, el no tenga y esta mas cerca  de una la busca si o si
            coche="buscaBolas"
            dirX = bolas[buscaBolas[0][1]][2] + actualizacionTiempoBolas*bolas[buscaBolas[0][1]][4]
            dirY = bolas[buscaBolas[0][1]][3] + actualizacionTiempoBolas*bolas[buscaBolas[0][1]][5]
            if buscaBolas[1]<umbralAcercamientoABola:
                aceleracion = aceleracionBuscarBolasMin
            else:
                aceleracion = aceleracionBuscarBolasMax
        else: #el que esta mas lejos (bucara bolas o intentara chocar adversarios) o los dos si no existen bolas
            if misCoches[i][7]==-1:
                if(len(bolas)<2):
                    coche="choque"
                    if(len(enemigosCochesConBolas)>0):
                        cocheEnemigo = enemigosCochesConBolas.pop()
                        dirX = cocheEnemigo[2]
                        dirY = cocheEnemigo[3]
                        aceleracion = aceleracionChoqueMax
                    else:
                        dirX = cochesEnemigo[0][2]
                        dirY = cochesEnemigo[0][3]
                        aceleracion = aceleracionChoqueMax
                else:
                    coche = "bucaBolas"
                    dirX = bolas[(buscaBolas[0][1]+1)%2][2] + actualizacionTiempoBolas*bolas[(buscaBolas[0][1]+1)%2][4]
                    dirY = bolas[(buscaBolas[0][1]+1)%2][3] + actualizacionTiempoBolas*bolas[(buscaBolas[0][1]+1)%2][5]
                    if getDistancia(misCoches[i][2],misCoches[i][3],bolas[(buscaBolas[0][1]+1)%2][2],bolas[(buscaBolas[0][1]+1)%2][3])<umbralAcercamientoABola:
                        aceleracion = aceleracionBuscarBolasMin
                    else:
                        aceleracion = aceleracionBuscarBolasMax
            else:
                coche="centro"
                dirX = 0
                dirY = 0
                aceleracion = aceleracionAcercarAlCentro

        # X Y THRUST MESSAGE

        print(str(dirX)+" "+str(dirY)+" "+str(aceleracion)+" "+coche)
        #print(str(dirX)+" "+str(dirY)+" "+"52.4"+" "+coche)
