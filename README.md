# PatchFirst: Sistema de Priorización de Incidentes de Ciberseguridad

Proyecto de aula de **Base de Datos I**. Fundación Universitaria Tecnológico Comfenalco, Cartagena.

- **Autor:** Sebastián Barrios Velásquez
- **Docente:** Jorge Luis Chavarriaga

PatchFirst registra los incidentes de ciberseguridad de la red de una universidad (portal académico, plataforma virtual, correo institucional, WiFi, laboratorios y servidores) y los **prioriza** según su nivel de riesgo (probabilidad × impacto).

> Todos los datos (incidentes, IP, equipos y técnicos) son **simulados con fines académicos**.

## Contenido

| Carpeta / archivo | Qué es |
|---|---|
| `base_de_datos/PatchFirst MF.sql` | Modelo físico: CREATE DATABASE, 5 tablas con PK, FK, NOT NULL, UNIQUE, CHECK, DEFAULT, INSERT y pruebas |
| `base_de_datos/incidentes_simulados.sql` | 1.485 incidentes simulados |
| `base_de_datos/generar_incidentes.py` | Script en Python que generó los datos simulados |
| `base_de_datos/PatchFirst MER.png` | Modelo entidad-relación |
| `base_de_datos/PatchFirst MR.png` | Modelo relacional |
| `PatchFirst/` | Proyecto Java (NetBeans, Ant) con el CRUD |

## Tablas

`sede` → `dispositivo` → `incidente` ← `tipo_ataque`, y `incidente` ← `tecnico` (todas las relaciones son 1:N).

## Software Java

| Paquete | Clase | Función |
|---|---|---|
| `proyecto.control` | `Conexion` | Conexión a MySQL por JDBC |
| `proyecto.modelo` | `Incidente` | Atributos, getters/setters y cálculo del nivel de riesgo y la criticidad |
| `proyecto.control` | `IncidenteControl` | registrar, buscar, listar, actualizar, eliminar y getContador, con PreparedStatement |
| `proyecto.vista` | `VentanaIncidentes` | Ventana Swing con JTable y botones Agregar, Buscar, Editar, Eliminar y Limpiar |
| `proyecto.prueba` | `Principal` y pruebas | Punto de arranque y programas de prueba |

## Cómo ejecutarlo

1. En MySQL Workbench, ejecutar `base_de_datos/PatchFirst MF.sql` y luego `base_de_datos/incidentes_simulados.sql`.
2. Abrir la carpeta `PatchFirst` en NetBeans (**File → Open Project**).
3. Agregar el conector **mysql-connector-j** en **Libraries → Add JAR/Folder**.
4. En `src/proyecto/control/Conexion.java`, cambiar `TU_CLAVE` por la clave de MySQL.
5. Ejecutar `proyecto.prueba.Principal`.
