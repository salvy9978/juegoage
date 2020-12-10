import sys
import math

# Made by Illedan, pb4 and Agade

map_radius = int(input())
center_radius = int(input())
min_swap_impulse = int(input())  # Impulse needed to steal a prisoner from another car
car_count = int(input())  # the number of cars you control

####################################### VARIABLES #################################
epsilon = 0.01
aceleracionBuscarBolas = 200
######################################### FUNCIONES ##############################
def getDistancia(x1, y1, x2, y2):
    return math.sqrt(math.pow(x1-x2,2)+math.pow(y1-y2,2))

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
    return abs(angulo-getAngulo(x2-x1, y2-y1))

def getDistanciaABolas(misCohes, bolas): #coche1 - bola1, coche2 - bola1, coche1 - bola2, coche2 - bola2
    listaDistancias = []
    for i in bolas:
        for j in misCohes:
            listaDistancias.append(getDistancia(j[2],j[3],i[2],i[3]))
    return listaDistancias

'''def getAnguloABolas(misCohes, bolas):
    listaAngulos = []
    for i in bolas:
        for j in misCohes:
            listaAngulos.append(getDiferenciaAngulos(j[6], j[2], j[3], i[2], i[3]))
    return listaAngulos'''
def getTurnosABola(coche, bola):
    turnos = 0
    velocidad = math.sqrt(math.pow(coche[4],2)+math.pow(coche[5],2))
    velocidad = (velocidad==0)?1:velocidad
    turnos = turnos + getDistancia(coche[2], coche[3], bola[2], bola[3])/velocidad
    turnos = turnos + getDiferenciaAngulos(coche[6], coche[2], coche[3], bola[2], bola[3])/18
    return turnos

def getTurnosEnemigosABolas(cochesEnemigos, bolas):
    turnosEnemigosABolas = []
    for i in bolas:
        for j in cochesEnemigos:
            turnosEnemigosABolas.append(getTurnosABola(j, i))
    return turnosEnemigosABolas

def queBolaBusco(coche, turnosEnemigosABolas, bolas):
    id = -1
    for i in range(len(bolas)):
        turnosAux = getTurnosABola(coche, bolas[i])
        if(turnosAux>max(turnosEnemigosABolas)):
            id = i
    return id;


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
    '''print(misCoches, file=sys.stderr, flush=True)
    print(cochesEnemigo, file=sys.stderr, flush=True)
    print(bolas, file=sys.stderr, flush=True)
    print(getDistanciaABolas(misCoches, bolas), file=sys.stderr, flush=True)
    print(getAnguloABolas(misCoches, bolas), file=sys.stderr, flush=True)'''


    turnosEnemigosABolas = getTurnosEnemigosABolas(cochesEnemigo, bolas)
    for i in range(car_count):
        coche = ""
        # Write an action using print
        # To debug: print("Debug messages...", file=sys.stderr, flush=True)
        dirX = 0
        dirY = 0
        aceleracion = 0
        ############ El codigo empieza aqui
        bola = queBolaBusco(misCoches[i], turnosEnemigosABolas, bolas)
        if(id!=-1):
            dirX = bolas[id][2] + (getDistancia(misCoches[i][2], misCoches[i][3], bolas[id][2], bolas[id][3])/(math.sqrt(math.pow(misCoches[i][4],2)+math.pow(misCoches[i][5],2))))*bolas[id][4]#add diferencia de tiempo para localizar la bola
            dirY = bolas[id][3] + (getDistancia(misCoches[i][2], misCoches[i][3], bolas[id][2], bolas[id][3])/(math.sqrt(math.pow(misCoches[i][4],2)+math.pow(misCoches[i][5],2))))*bolas[id][5]#add diferencia de tiempo para localizar la bola
            aceleracion = aceleracionBuscarBolas
        '''else:
            if(coches[i][7]!=-1):
                dirX = 0
                dirY = 0
                if(getDiferenciaAngulos()<18):
                    aceleracion = 200
                else:
                    aceleracion = 20
            else:'''




        ########### El codigo acaba aqui
        # X Y THRUST MESSAGE
        dirX = int(dirX)
        dirY = int(dirY)
        aceleracion = int(aceleracion)


        print(str(dirX)+" "+str(dirY)+" "+str(aceleracion)+" "+coche)
