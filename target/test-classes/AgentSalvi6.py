import sys
import math


map_radius = int(input())
center_radius = int(input())
min_swap_impulse = int(input())  # Impulse needed to steal a prisoner from another car
car_count = int(input())  # the number of cars you control

####################################### VARIABLES #################################
epsilon = 1
#aceleraciones
aceleracionMinIrCentro = 10
aceleracionMaxIrCentro = 200
minAceleracionCogerBola = 0
maxAceleracionCogerBola = 200
aceleracionIrADefender = 150
aceleracionAtaque = 200
#distancias umbrales
distanciaUmbralCogerBola = 1400
distanciaUmbralCirculoDefensa = 800
distanciaUmbralCirculoAtaque = 2500
distanciaEnemigoEstaEnCentro = 800
distanciaUmbralFrenarParaDefender = 1200
#cosas de angulos
anguloParaIrAlCentro = 18
umbralAnguloChoqueConMiCoche = 18
umbralAnguloChoqueConCocheEnemigo = 90
penalizacionMetrosPorAngulo = 400
umbralRentaIrPorBola = 3000

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
    anguloAux = getAngulo(x2-x1, y2-y1)
    angulo1 = abs(angulo-anguloAux)
    angulo2 = abs(angulo+abs(360-anguloAux))
    angulo3 = abs(anguloAux+abs(360-angulo))
    angulos = []
    angulos.append(angulo1)
    angulos.append(angulo2)
    angulos.append(angulo3)
    return min(angulos)


def getDistanciaABolas(misCohes, bolas): #coche1 - bola1, coche2 - bola1, coche1 - bola2, coche2 - bola2
    listaDistancias = []
    for i in bolas:
        for j in misCohes:
            listaDistancias.append(getDistancia(j[2],j[3],i[2],i[3]))
    return listaDistancias

def getTurnosABola(coche, bola): # TODO: add velocidad
    turnos = 0
    turnos = turnos + getDistancia(coche[2], coche[3], bola[2], bola[3])
    turnos = turnos + int(getDiferenciaAngulos(coche[6], coche[2], coche[3], bola[2], bola[3])/18) * penalizacionMetrosPorAngulo
    return turnos

def getTurnosEnemigosABolas(cochesEnemigos, bolas):
    turnosEnemigosABolas = []
    for i in bolas:
        for j in cochesEnemigos:
            if j[7]==-1:
                turnosEnemigosABolas.append(getTurnosABola(j, i))
    return turnosEnemigosABolas

def queBolaBusco(coche, misTurnosABolas, turnosEnemigosABolas, bolas):
    id = -1
    minTurnosBolas = -1
    for i in range(len(bolas)): # TODO: hay que cambiar para que mire los turnos a la bola en concreto y no pille el minimo global
        turnosAux = getTurnosABola(coche, bolas[i])
        print(turnosAux, file=sys.stderr, flush=True)
        if(turnosAux<min(turnosEnemigosABolas) and turnosAux<=min(misTurnosABolas) and (turnosAux<minTurnosBolas or minTurnosBolas==-1) and turnosAux<umbralRentaIrPorBola):
            id = i
            minTurnosBolas = turnosAux
    return id;

def aQuienAtaco(coche, misCoches, cochesEnemigo, listaEnemigosAtacados):
    id = [-1, -1] #primero 0 seria mis coches 1 cochesRival, y la segunda la posicion en la lista
    for i in range(len(misCoches)):
        if misCoches[i][0]!=coche[0]:
            if misCoches[i][7]!=-1:
                if getDiferenciaAngulos(coche[6], coche[2], coche[3], misCoches[i][2], misCoches[i][3])<umbralAnguloChoqueConMiCoche and getDistancia(coche[2], coche[3],0,0)<getDistancia(misCoches[i][2], misCoches[i][3],0,0) and getDiferenciaAngulos(misCoches[i][6], misCoches[i][2], misCoches[i][3], coche[2], coche[3])<umbralAnguloChoqueConMiCoche:
                    id = [0,i]
                    return id
    anguloAux = -1
    for i in range(len(cochesEnemigo)): # TODO: posibilidad de que enemigo este apuntando al centro
        if(cochesEnemigo[i][7]!=-1 and getDistancia(cochesEnemigo[i][2], cochesEnemigo[i][3], 0, 0)<distanciaUmbralCirculoAtaque and getDiferenciaAngulos(coche[6], coche[2], coche[3], cochesEnemigo[i][2], cochesEnemigo[i][3])<umbralAnguloChoqueConCocheEnemigo):
            if(getDiferenciaAngulos(coche[6], coche[2], coche[3], cochesEnemigo[i][2], cochesEnemigo[i][3])<anguloAux or anguloAux==-1):
                if i not in listaEnemigosAtacados:
                    id[0]=1
                    id[1]=i

    return id # TODO que no vayan los dos a por el mismo

def aQuienApunto(coche, cochesEnemigo, bolas):
    id = [-1, -1] #primero 0 seria bolas 1 cochesRival, y la segunda la posicion en la lista
    anguloAux = -1

    for i in range(len(bolas)):
        diferenciaAngulos = getDiferenciaAngulos(coche[6],coche[2],coche[3],bolas[i][2], bolas[i][3])
        if(diferenciaAngulos<anguloAux or anguloAux==-1):
            id[0] = 0
            id[1] = i
            anguloAux = diferenciaAngulos
    for i in range(len(cochesEnemigo)):
        diferenciaAngulos = getDiferenciaAngulos(coche[6],coche[2],coche[3],cochesEnemigo[i][2], cochesEnemigo[i][3])
        if(diferenciaAngulos<anguloAux or anguloAux==-1 or cochesEnemigo[i][7]!=-1):
            id[0] = 1
            id[1] = i
            anguloAux = diferenciaAngulos
            if(cochesEnemigo[i][7]!=-1):
                return id

    return id

def deboIrCentro(coche, cochesEnemigo): # TODO: add si enemigo nos esta apuntando --> IMPORTANTE
    coordenadas = [0,0]
    for i in cochesEnemigo:
        if(getDistancia(i[2], i[3], 0,0)<distanciaEnemigoEstaEnCentro): # TODO: calcula coordenadas de huir, cambiar
            coordenadas[0] = coche[2] + math.cos(math.radians(coche[6])) * map_radius * 100
            coordenadas[1] = coche[3] + math.sin(math.radians(coche[6])) * map_radius * 100
            return coordenadas
    return coordenadas
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

    for i in bolas:
        for j in misCoches:
            print("Angulo"+str(getDiferenciaAngulos(j[6],j[2],j[3], i[2],i[3])), file=sys.stderr, flush=True)

    turnosEnemigosABolas = getTurnosEnemigosABolas(cochesEnemigo, bolas)
    misTurnosABolas = getTurnosEnemigosABolas(misCoches, bolas)
    listaEnemigosAtacados = []
    print(turnosEnemigosABolas, file=sys.stderr, flush=True)
    for i in range(car_count):
        coche = ""
        # Write an action using print
        # To debug: print("Debug messages...", file=sys.stderr, flush=True)

        dirX = 0
        dirY = 0
        aceleracion = 0
        ############ El codigo empieza aqui
        if(misCoches[i][7]!=-1): #dejar bola si la tengo
            #coordenadasIrConPelota = deboIrCentro(misCoches[i], cochesEnemigo)
            coordenadasIrConPelota = deboIrCentro(misCoches[i], cochesEnemigo)
            if(coordenadasIrConPelota[0]==0 and coordenadasIrConPelota[1]==0):
                dirX = 0
                dirY = 0
                coche = "DejarBola"
                '''if(getDiferenciaAngulos(misCoches[i][6],misCoches[i][2],misCoches[i][3],0,0)<anguloParaIrAlCentro):
                    aceleracion = aceleracionMaxIrCentro
                else:
                    aceleracion = aceleracionMinIrCentro'''
                aceleracion = aceleracionMaxIrCentro * (1 - getDiferenciaAngulos(misCoches[i][6],misCoches[i][2],misCoches[i][3],0,0)/180)
            else:
                coche = "Huir"
                dirX = coordenadasIrConPelota[0]
                dirY = coordenadasIrConPelota[1]
                aceleracion = 80
        else: #estrategia
            idBola = queBolaBusco(misCoches[i], misTurnosABolas, turnosEnemigosABolas, bolas)
            print(idBola, file=sys.stderr, flush=True)
            if(idBola!=-1): # cogerBola si se da una buena ocasion
                velocidad = math.sqrt(math.pow(misCoches[i][4],2)+math.pow(misCoches[i][5],2))
                if velocidad==0:
                    velocidad = epsilon
                # TODO: posibilidad de add angulo
                tiempoAprox = getDistancia(misCoches[i][2], misCoches[i][3], bolas[idBola][2], bolas[idBola][3])/velocidad
                dirX = bolas[idBola][2] + tiempoAprox*bolas[idBola][4]
                dirY = bolas[idBola][3] + tiempoAprox*bolas[idBola][5]
                if getDistancia(dirX, dirY, 0, 0)>map_radius: #si la bola rebota
                    distanciaEstimBola = map_radius - getDistancia(dirX, dirY, 0, 0)
                    if(dirX>0 and dirY>0):
                        dirX = dirX - distanciaEstimBola
                        dirY = dirY - distanciaEstimBola
                    if(dirX<0 and dirY>0):
                        dirX = dirX + distanciaEstimBola
                        dirY = dirY - distanciaEstimBola
                    if(dirX<0 and dirY<0):
                        dirX = dirX + distanciaEstimBola
                        dirY = dirY + distanciaEstimBola
                    if(dirX>0 and dirY<0):
                        dirX = dirX - distanciaEstimBola
                        dirY = dirY + distanciaEstimBola
                if(getDistancia(bolas[idBola][2],bolas[idBola][3],misCoches[i][2],misCoches[i][3])<distanciaUmbralCogerBola):
                    aceleracion = minAceleracionCogerBola # si estoy cerca frenar
                    coche = "CogerBolaLento"
                    dirX = 0 # apunta al centro
                    dirY = 0
                else:
                    aceleracion = maxAceleracionCogerBola *  (1 - getDiferenciaAngulos(misCoches[i][6],misCoches[i][2],misCoches[i][3],bolas[idBola][2],bolas[idBola][3])/180) # (getDistancia(bolas[idBola][2],bolas[idBola][3],misCoches[i][2],misCoches[i][3])/2*map_radius)#+1/(anguloABola/360) # si estoy lejos acelero
                    coche = "CogerBolaRapido"
            else: # defender
                idAQuienAtaco = aQuienAtaco(misCoches[i], misCoches, cochesEnemigo, listaEnemigosAtacados) # mirar que coches deberia atacar
                if idAQuienAtaco[0]!=-1:
                    if idAQuienAtaco[0]==0: #atacarme a mi para coger bola antes de robo
                        dirX = misCoches[idAQuienAtaco[1]][2]
                        dirY = misCoches[idAQuienAtaco[1]][3]
                        aceleracion = aceleracionAtaque
                        coche = "AttackMe"
                    else: #atacar rival si reune las condiciones en aQuienAtaco
                        dirX = cochesEnemigo[idAQuienAtaco[1]][2]
                        dirY = cochesEnemigo[idAQuienAtaco[1]][3]
                        aceleracion = aceleracionAtaque
                        coche = "AttackOther"
                        listaEnemigosAtacados.append(idAQuienAtaco)
                else: #ir al circulo de defensa o apuntar si estoy en el
                    print("Hola" + str(getDistancia(misCoches[i][2], misCoches[i][3],0,0)), file=sys.stderr, flush=True)
                    distanciaCocheAlCentro = getDistancia(misCoches[i][2], misCoches[i][3],0,0)
                    if distanciaCocheAlCentro>distanciaUmbralCirculoDefensa:
                        coche = "GoToDefense"
                        dirX = 0
                        dirY = 0
                        if(distanciaCocheAlCentro<distanciaUmbralFrenarParaDefender): #ir rapido a defender si apunta al centro, sino ir lento
                            aceleracion = 0
                            idQuienApuntar = aQuienApunto(misCoches[i], cochesEnemigo, bolas)
                            if idQuienApuntar[0]==0:
                                coche = "DefenseApuntarBolas"
                                dirX = bolas[idQuienApuntar[1]][2]
                                dirY = bolas[idQuienApuntar[1]][3]
                            else:
                                coche = "DefenseApuntarEnemigo"
                                dirX = cochesEnemigo[idQuienApuntar[1]][2]
                                dirY = cochesEnemigo[idQuienApuntar[1]][3]

                        else:
                            aceleracion = aceleracionIrADefender * (distanciaCocheAlCentro/map_radius)
                    else: #apuntar a la bola o enemigo
                        idQuienApuntar = aQuienApunto(misCoches[i], cochesEnemigo, bolas)
                        if idQuienApuntar[0]==0:
                            coche = "ApuntarBolas"
                            dirX = bolas[idQuienApuntar[1]][2]
                            dirY = bolas[idQuienApuntar[1]][3]
                            aceleracion = 0
                        else:
                            coche = "ApuntarEnemigo"
                            dirX = cochesEnemigo[idQuienApuntar[1]][2]
                            dirY = cochesEnemigo[idQuienApuntar[1]][3]
                            aceleracion = 0


        ########### El codigo acaba aqui
        # X Y THRUST MESSAGE
        dirX = int(dirX)
        dirY = int(dirY)
        aceleracion = int(aceleracion)
        if(aceleracion>200):
            aceleracion = 200


        print(str(dirX)+" "+str(dirY)+" "+str(aceleracion)+" "+coche)
