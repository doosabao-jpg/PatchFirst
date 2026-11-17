# Generador de incidentes SIMULADOS para PatchFirst.
# Crea el archivo incidentes_simulados.sql con mas de 1.000 INSERT.
# La semilla fija (random.seed) hace que siempre salgan los mismos datos.

import random
import calendar

random.seed(2026)

# ---------- Catalogo (igual que la tabla tipo_ataque) ----------
# id: (nombre, umbral_normal, probabilidad_base, impacto_base)
ataques = {
    1: ("ARP spoofing", 2, 3, 4),
    2: ("MAC flooding", 5, 2, 3),
    3: ("Punto de acceso falso", 1, 3, 4),
    4: ("DHCP starvation", 3, 2, 3),
    5: ("Desautenticacion WiFi", 10, 3, 2),
    6: ("DDoS volumetrico", 100, 4, 5),
    7: ("IP spoofing", 1, 3, 3),
    8: ("Ataque Smurf", 5, 1, 4),
    9: ("Barrido de red", 10, 4, 2),
    10: ("SYN flood", 50, 3, 5),
    11: ("UDP flood", 500, 3, 4),
    12: ("Escaneo de puertos", 20, 5, 2),
    13: ("Secuestro de sesion TCP", 1, 2, 4),
    14: ("Fuerza bruta", 5, 5, 4),
    15: ("Credential stuffing", 3, 4, 4),
    16: ("Password spraying", 5, 4, 4),
    17: ("DDoS de aplicacion", 120, 4, 5),
    18: ("Inyeccion SQL", 1, 3, 5),
    19: ("Cross-Site Scripting", 1, 3, 3),
    20: ("Phishing", 5, 5, 4),
    21: ("Ransomware", 1, 2, 5),
    22: ("Malware por USB", 1, 4, 3),
    23: ("Cryptojacking", 20, 3, 2),
    24: ("DNS spoofing", 100, 2, 4),
    25: ("Exfiltracion de datos", 500, 2, 5),
    26: ("Escalada de privilegios", 1, 2, 5),
    27: ("Software sin parche", 1, 3, 5),
}

# ---------- A que dispositivos ataca cada tipo (id_dispositivo) ----------
# 1 PORTAL, 2 PLATAFORMA, 3 CORREO, 4 ADMIN, 5 BIBLIOTECA,
# 6-8 routers, 9-12 AP, 13-16 PC de laboratorio
objetivos = {
    1: [6, 7, 8], 2: [6, 7, 8], 3: [9, 10, 11, 12], 4: [9, 10, 11, 12],
    5: [9, 10, 11, 12], 6: [2, 1], 7: [6, 7, 8], 8: [6, 7, 8],
    9: [6, 7, 8], 10: [1, 2], 11: [6, 7, 8], 12: [6, 7, 8, 1, 4, 5],
    13: [1, 2], 14: [1], 15: [3], 16: [3, 1], 17: [2],
    18: [5], 19: [2], 20: [3], 21: [4], 22: [13, 14, 15, 16],
    23: [13, 14, 15, 16], 24: [6, 7, 8], 25: [4, 1], 26: [4, 1], 27: [5, 4],
}

# Ataques que se hacen DESDE ADENTRO de la red (IP origen interna)
internos = [1, 2, 3, 4, 5, 22, 23, 26]

# LA SITUACION PROBLEMATICA: 4 ataques dominan
dominantes = [14, 17, 6, 20]          # fuerza bruta, DDoS app, DDoS vol, phishing
peso_dominantes = [40, 25, 10, 25]

# Pocas IP reincidentes hacen la mayoria de esos ataques
ip_reincidentes = ["203.0.113.45", "198.51.100.12", "192.0.2.77",
                   "203.0.113.88", "198.51.100.200"]
peso_reincidentes = [35, 25, 20, 12, 8]

# Cuantos incidentes por mes. Parciales = marzo, abril, septiembre, octubre.
meses = {
    (2025, 1): 40, (2025, 2): 45, (2025, 3): 70, (2025, 4): 75,
    (2025, 5): 50, (2025, 6): 35, (2025, 7): 30, (2025, 8): 45,
    (2025, 9): 90, (2025, 10): 95, (2025, 11): 55, (2025, 12): 30,
    (2026, 1): 45, (2026, 2): 50, (2026, 3): 80, (2026, 4): 85,
    (2026, 5): 55, (2026, 6): 40, (2026, 7): 35, (2026, 8): 55,
    (2026, 9): 180, (2026, 10): 200,
}
meses_parciales = [3, 4, 9, 10]


def ip_externa():
    redes = ["203.0.113.", "198.51.100.", "192.0.2."]
    return random.choice(redes) + str(random.randint(1, 254))


def ip_interna():
    return "10." + random.choice(["20", "30"]) + "." + str(random.randint(1, 15)) + "." + str(random.randint(20, 250))


def limitar(valor):
    # Deja el numero entre 1 y 5
    if valor < 1:
        return 1
    if valor > 5:
        return 5
    return valor


def criticidad_de(nivel):
    if nivel >= 15:
        return "Alta"
    if nivel >= 8:
        return "Media"
    return "Baja"


filas = []
otros = [a for a in ataques if a not in dominantes]
peso_otros = [ataques[a][2] for a in otros]   # mas probables salen mas

for (anio, mes), cantidad in meses.items():
    es_parcial = mes in meses_parciales
    dias_mes = calendar.monthrange(anio, mes)[1]

    for _ in range(cantidad):
        # 1) Que ataque: en parciales dominan los 4 ataques del problema
        prob_dominante = 0.75 if es_parcial else 0.35
        dominante = random.random() < prob_dominante
        if dominante:
            id_ataque = random.choices(dominantes, peso_dominantes)[0]
        else:
            id_ataque = random.choices(otros, peso_otros)[0]
        nombre, umbral, p_base, i_base = ataques[id_ataque]

        # 2) Cuando: en sept/oct los dominantes caen en la semana de parciales
        if dominante and mes in (9, 10) and random.random() < 0.7:
            dia = random.randint(14, 27)
        else:
            dia = random.randint(1, dias_mes)
        fecha = "%d-%02d-%02d" % (anio, mes, dia)
        hora = "%02d:%02d:%02d" % (random.randint(0, 23), random.randint(0, 59), random.randint(0, 59))

        # 3) Desde donde
        if id_ataque in internos:
            ip = ip_interna()
        elif dominante and random.random() < 0.7:
            ip = random.choices(ip_reincidentes, peso_reincidentes)[0]
        else:
            ip = ip_externa()

        id_dispositivo = random.choice(objetivos[id_ataque])
        id_tecnico = random.choices([1, 2, 3, 4, 5], [30, 25, 20, 15, 10])[0]

        # 4) Que tan grave: base del catalogo +/- 1
        probabilidad = limitar(p_base + random.choice([-2, -1, -1, 0, 0, 1]))
        impacto = limitar(i_base + random.choice([-2, -1, 0, 0, 0, 1]))
        nivel = probabilidad * impacto
        criticidad = criticidad_de(nivel)

        # 5) Valor del indicador: cuantas veces sobre lo normal
        if dominante and es_parcial:
            veces = random.uniform(30, 300)
        elif dominante:
            veces = random.uniform(5, 60)
        else:
            veces = random.uniform(1.5, 15)
        valor = int(umbral * veces)

        # 6) Tiempo de respuesta: EL PROBLEMA, los criticos se atienden tarde
        if criticidad == "Alta":
            # Fuera de parciales 45% se atiende a tiempo; en parciales solo 15%
            a_tiempo = 0.45 if not es_parcial else 0.15
            if random.random() < a_tiempo:
                tiempo = random.randint(5, 30)
            else:
                tiempo = random.randint(31, 600)
        elif criticidad == "Media":
            tiempo = random.randint(30, 420)
        else:
            tiempo = random.randint(60, 1440)

        # 7) Estado: lo reciente sigue abierto
        if (anio, mes) == (2026, 10):
            estado = random.choices(["Abierto", "En proceso", "Cerrado"], [35, 35, 30])[0]
        elif (anio, mes) == (2026, 9):
            estado = random.choices(["En proceso", "Cerrado"], [15, 85])[0]
        else:
            estado = "Cerrado"

        filas.append((fecha, hora, ip, id_dispositivo, id_ataque, id_tecnico,
                      valor, probabilidad, impacto, nivel, criticidad, estado, tiempo))

# Ordenar por fecha y hora
filas.sort(key=lambda f: (f[0], f[1]))

# ---------- Escribir el archivo .sql ----------
with open("incidentes_simulados.sql", "w", encoding="utf-8") as archivo:
    archivo.write("-- PatchFirst: %d incidentes SIMULADOS (datos ficticios con fines academicos)\n" % len(filas))
    archivo.write("USE patchfirst;\n\n")
    columnas = ("INSERT INTO incidente (fecha, hora, ip_origen, id_dispositivo, id_tipo_ataque, "
                "id_tecnico, valor_indicador, probabilidad, impacto, nivel_riesgo, criticidad, "
                "estado, tiempo_respuesta_min) VALUES\n")
    # Bloques de 100 filas por INSERT
    for inicio in range(0, len(filas), 100):
        bloque = filas[inicio:inicio + 100]
        archivo.write(columnas)
        lineas = []
        for f in bloque:
            lineas.append("('%s', '%s', '%s', %d, %d, %d, %d, %d, %d, %d, '%s', '%s', %d)" % f)
        archivo.write(",\n".join(lineas) + ";\n\n")

print("Filas generadas:", len(filas))
