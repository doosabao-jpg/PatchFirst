CREATE DATABASE patchfirst
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_spanish_ci;

USE patchfirst;

CREATE TABLE sede (
    id_sede INT AUTO_INCREMENT,
    nombre VARCHAR(60) NOT NULL UNIQUE,
    ciudad VARCHAR(40) NOT NULL,
    PRIMARY KEY (id_sede)
);

INSERT INTO sede (nombre, ciudad) VALUES
("Claustro de San Agustin", "Cartagena"),
("Campus San Pablo", "Cartagena"),
("Campus Zaragocilla", "Cartagena"),
("Campus Piedra de Bolivar", "Cartagena"),
("Claustro de La Merced", "Cartagena"),
("Maternidad", "Cartagena"),
("Casa del Niño", "Cartagena"),
("Consultorio Juridico", "Cartagena"),
("Centro Tutorial San Juan Nepomuceno", "San Juan Nepomuceno"),
("Centro Tutorial Cerete", "Cerete"),
("Centro Tutorial Mompox", "Mompox"),
("Centro Tutorial Montecarlos", "Magangue"),
("Centro Tutorial Camilo Torres", "Magangue"),
("Centro Tutorial El Carmen de Bolívar", "El Carmen de Bolívar"),
("Centro Tutorial Lorica", "Lorica");

CREATE TABLE dispositivo (
    id_dispositivo INT AUTO_INCREMENT,
    nombre VARCHAR(40) NOT NULL,
    ip VARCHAR(15) NOT NULL UNIQUE,
    mac VARCHAR(17) NOT NULL UNIQUE,
    tipo VARCHAR(20) NOT NULL,
    servicio VARCHAR(30) NOT NULL,
    id_sede INT NOT NULL,
    PRIMARY KEY (id_dispositivo),
    CHECK (tipo IN ("servidor", "PC de laboratorio", "Router", "AP")),
    CHECK (servicio IN ("Portal academico", "Plataforma virtual", "Correo institucional", "Biblioteca", "Administrativo", "Wifi", "Laboratorios", "Red del campus")),
    FOREIGN KEY (id_sede) REFERENCES sede(id_sede)
);

-- aqui ingreso fake data generada por claude 

INSERT INTO dispositivo (nombre, ip, mac, tipo, servicio, id_sede) VALUES
('SRV-PORTAL',        '10.10.1.10',  '02:00:00:00:00:01', 'Servidor',          'Portal academico',     1),
('SRV-PLATAFORMA',    '10.10.1.11',  '02:00:00:00:00:02', 'Servidor',          'Plataforma virtual',   1),
('SRV-CORREO',        '10.10.1.12',  '02:00:00:00:00:03', 'Servidor',          'Correo institucional', 1),
('SRV-ADMIN',         '10.10.1.13',  '02:00:00:00:00:04', 'Servidor',          'Administrativo',       1),
('SRV-BIBLIOTECA',    '10.10.4.10',  '02:00:00:00:00:05', 'Servidor',          'Biblioteca',           4),
('RTR-SANAGUSTIN',    '10.10.1.1',   '02:00:00:00:00:06', 'Router',            'Red del campus',       1),
('RTR-ZARAGOCILLA',   '10.10.3.1',   '02:00:00:00:00:07', 'Router',            'Red del campus',       3),
('RTR-PIEDRA',        '10.10.4.1',   '02:00:00:00:00:08', 'Router',            'Red del campus',       4),
('AP-SANPABLO',       '10.20.2.2',   '02:00:00:00:00:09', 'AP',           'Wifi',          2),
('AP-ZARAGOCILLA',    '10.20.3.2',   '02:00:00:00:00:0A', 'AP',           'Wifi',          3),
('AP-PIEDRA',         '10.20.4.2',   '02:00:00:00:00:0B', 'AP',           'Wifi',          4),
('AP-MONTECARLOS',    '10.20.12.2',  '02:00:00:00:00:0C', 'AP',           'Wifi',         12),
('LAB-SANPABLO-01',   '10.30.2.21',  '02:00:00:00:00:0D', 'PC de laboratorio', 'Laboratorios',         2),
('LAB-ZARAGOCILLA-01','10.30.3.21',  '02:00:00:00:00:0E', 'PC de laboratorio', 'Laboratorios',         3),
('LAB-PIEDRA-01',     '10.30.4.21',  '02:00:00:00:00:0F', 'PC de laboratorio', 'Laboratorios',         4),
('LAB-LORICA-01',     '10.30.15.21', '02:00:00:00:00:10', 'PC de laboratorio', 'Laboratorios',        15);

CREATE TABLE tipo_ataque (
    id_tipo_ataque INT AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    capa_osi INT NOT NULL,
    categoria VARCHAR(30) NOT NULL,
    descripcion VARCHAR(150) NOT NULL,
    indicador VARCHAR(80) NOT NULL,
    umbral_normal INT NOT NULL,
    probabilidad_base INT NOT NULL,
    impacto_base INT NOT NULL,
    PRIMARY KEY (id_tipo_ataque),
    CHECK (capa_osi IN (2, 3, 4, 7)),
    CHECK (categoria IN ("Suplantacion", "Denegacion de servicio", "Reconocimiento", "Acceso no autorizado", "Inyeccion / explotacion", "Ingenieria social", "Malware", "Robo de datos")),
    CHECK (umbral_normal > 0),
    CHECK (probabilidad_base BETWEEN 1 AND 5),
    CHECK (impacto_base BETWEEN 1 AND 5)
);

INSERT INTO tipo_ataque (nombre, capa_osi, categoria, descripcion, indicador, umbral_normal, probabilidad_base, impacto_base) VALUES
-- CAPA 2 (enlace)
("ARP spoofing", 2, "Suplantacion", "Equipo del laboratorio se hace pasar por el router", "Respuestas ARP duplicadas por minuto", 2, 3, 4),
("MAC flooding", 2, "Denegacion de servicio", "Saturar el switch con MAC falsas", "MAC nuevas por minuto en un puerto", 5, 2, 3),
("Punto de acceso falso", 2, "Suplantacion", "WiFi falso UdeC-Estudiantes", "SSID duplicados con otra MAC", 1, 3, 4),
("DHCP starvation", 2, "Denegacion de servicio", "Agotar las IP del WiFi", "Solicitudes DHCP por minuto desde una MAC", 3, 2, 3),
("Desautenticacion WiFi", 2, "Denegacion de servicio", "Desconectar estudiantes del WiFi", "Tramas de desautenticacion por minuto", 10, 3, 2),
-- CAPA 3 (red)
("DDoS volumetrico", 3, "Denegacion de servicio", "Tumbar la plataforma virtual en parciales", "Paquetes ICMP por segundo", 100, 4, 5),
("IP spoofing", 3, "Suplantacion", "IP de origen falsa", "Paquetes con IP interna llegando desde afuera por minuto", 1, 3, 3),
("Ataque Smurf", 3, "Denegacion de servicio", "Amplificar ataque con broadcast", "Respuestas ICMP a broadcast por minuto", 5, 1, 4),
("Barrido de red", 3, "Reconocimiento", "Mapear equipos encendidos", "IP distintas consultadas por minuto", 10, 4, 2),
-- CAPA 4 (transporte)
("SYN flood", 4, "Denegacion de servicio", "Saturar conexiones del portal", "Conexiones TCP a medio abrir por segundo", 50, 3, 5),
("UDP flood", 4, "Denegacion de servicio", "Saturar ancho de banda o DNS", "Paquetes UDP por segundo", 500, 3, 4),
("Escaneo de puertos", 4, "Reconocimiento", "Buscar servicios abiertos", "Puertos distintos consultados por minuto", 20, 5, 2),
("Secuestro de sesion TCP", 4, "Acceso no autorizado", "Tomar la sesion de un docente", "Sesiones con cambio de IP o MAC por hora", 1, 2, 4),
-- CAPA 7 (aplicacion)
("Fuerza bruta", 7, "Acceso no autorizado", "Miles de claves en el portal de notas", "Intentos de login fallidos por minuto", 5, 5, 4),
("Credential stuffing", 7, "Acceso no autorizado", "Claves filtradas probadas en el correo institucional", "Cuentas distintas probadas desde una IP por hora", 3, 4, 4),
("Password spraying", 7, "Acceso no autorizado", "Una clave comun probada en muchas cuentas", "Cuentas con 1 o 2 intentos fallidos desde un origen por hora", 5, 4, 4),
("DDoS de aplicacion", 7, "Denegacion de servicio", "Pagina de inscripciones saturada", "Peticiones HTTP por minuto por IP", 120, 4, 5),
("Inyeccion SQL", 7, "Inyeccion / explotacion", "Buscador de la biblioteca", "Consultas con caracteres sospechosos por hora", 1, 3, 5),
("Cross-Site Scripting", 7, "Inyeccion / explotacion", "Codigo en un foro de la plataforma virtual", "Entradas con script bloqueadas por dia", 1, 3, 3),
("Phishing", 7, "Ingenieria social", "Correo falso de Registro Academico", "Correos reportados por dia", 5, 5, 4),
("Ransomware", 7, "Malware", "Cifran un servidor administrativo", "Archivos cifrados por minuto", 1, 2, 5),
("Malware por USB", 7, "Malware", "PC de laboratorio infectado", "Alertas del antivirus por equipo por dia", 1, 4, 3),
("Cryptojacking", 7, "Malware", "PC de laboratorio minando criptomonedas", "Porcentaje de CPU fuera de horario", 20, 3, 2),
("DNS spoofing", 7, "Suplantacion", "Redireccion a pagina falsa", "Consultas DNS por minuto", 100, 2, 4),
("Exfiltracion de datos", 7, "Robo de datos", "Sacan notas y documentos de estudiantes", "MB enviados hacia afuera por hora", 500, 2, 5),
("Escalada de privilegios", 7, "Acceso no autorizado", "Cuenta de estudiante con permisos de administrador", "Cambios de rol no autorizados por dia", 1, 2, 5),
("Software sin parche", 7, "Inyeccion / explotacion", "Servidor desactualizado", "Peticiones con firmas de exploits por hora", 1, 3, 5);

CREATE TABLE tecnico (
    id_tecnico INT AUTO_INCREMENT,
    nombre VARCHAR(60) NOT NULL,
    correo VARCHAR(80) NOT NULL UNIQUE,
    PRIMARY KEY (id_tecnico),
    CHECK (correo LIKE "%@unicartagena.edu.co") 
);

-- aqui ingreso fake data generada por claude

INSERT INTO tecnico (nombre, correo) VALUES 
('Carlos Mendoza Ruiz',    'cmendoza@unicartagena.edu.co'),
('Laura Pertuz Gomez',     'lpertuz@unicartagena.edu.co'),
('Andres Julio Castro',    'ajulio@unicartagena.edu.co'),
('Valentina Arrieta Diaz', 'varrieta@unicartagena.edu.co'),
('Jorge Barrios Navarro',  'jbarrios@unicartagena.edu.co');

CREATE TABLE incidente (
    id_incidente INT AUTO_INCREMENT,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    ip_origen VARCHAR(15) NOT NULL,
    id_dispositivo INT NOT NULL,
    id_tipo_ataque INT NOT NULL,
    id_tecnico INT NOT NULL,
    valor_indicador INT NOT NULL,
    probabilidad INT NOT NULL,
    impacto INT NOT NULL,
    nivel_riesgo INT NOT NULL,
    criticidad VARCHAR(5) NOT NULL,
    estado VARCHAR(10) NOT NULL,
    tiempo_respuesta_min INT NOT NULL,
    fuente_registro VARCHAR(40) NOT NULL DEFAULT "Sistema de monitoreo de red",
    observaciones VARCHAR(200),
    PRIMARY KEY (id_incidente),
    CHECK (valor_indicador >= 0),
    CHECK (probabilidad BETWEEN 1 AND 5),
    CHECK (impacto BETWEEN 1 AND 5),
    CHECK (nivel_riesgo = probabilidad * impacto),
    CHECK ( (criticidad = "Alta"  AND nivel_riesgo >= 15)
         OR (criticidad = "Media" AND nivel_riesgo BETWEEN 8 AND 14)
         OR (criticidad = "Baja"  AND nivel_riesgo <= 7) ),

    CHECK (estado IN ("Abierto", "En proceso", "Cerrado")),
    CHECK (tiempo_respuesta_min > 0),
    FOREIGN KEY (id_dispositivo) REFERENCES dispositivo(id_dispositivo),
    FOREIGN KEY (id_tipo_ataque) REFERENCES tipo_ataque(id_tipo_ataque),
    FOREIGN KEY (id_tecnico)     REFERENCES tecnico(id_tecnico)
);

-- aqui ingreso fake data generada por claude

INSERT INTO incidente (fecha, hora, ip_origen, id_dispositivo, id_tipo_ataque, id_tecnico, valor_indicador, probabilidad, impacto, nivel_riesgo, criticidad, estado, tiempo_respuesta_min) VALUES
--  ==== Fuerza bruta (14) al SRV-PORTAL (1), en parciales ====
('2026-09-15', '08:12:45', '203.0.113.45',  1, 14, 1,  420, 5, 4, 20, 'Alta',  'Cerrado',     95),
-- ==== DDoS de aplicacion (17) a la SRV-PLATAFORMA (2) ====
('2026-09-16', '10:30:00', '198.51.100.12', 2, 17, 2, 3500, 4, 5, 20, 'Alta',  'En proceso', 180),
-- ==== Phishing (20) al SRV-CORREO (3) ====
('2026-10-02', '14:05:10', '192.0.2.77',    3, 20, 3,   40, 5, 4, 20, 'Alta',  'Abierto',     60),
-- ==== Escaneo de puertos (12) al RTR-SANAGUSTIN (6) ====
('2026-05-20', '03:40:00', '203.0.113.200', 6, 12, 5,  150, 5, 2, 10, 'Media', 'Cerrado',    120),
-- ==== Cryptojacking (23) en LAB-PIEDRA-01 (15), desde una IP interna ====
('2026-03-10', '22:15:00', '10.30.4.21',   15, 23, 4,   85, 3, 2,  6, 'Baja',  'Cerrado',    300);

