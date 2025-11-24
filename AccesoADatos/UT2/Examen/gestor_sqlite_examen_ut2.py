"""
gestor_sqlite_examen_ut2.py

Archivo unificado y organizado para practicar y demostrar todo lo necesario
sobre SQLite + Python (DB-API 2.0) para un examen práctico basado en UT2 - Manejo
de conectores.

Estructura por clases:
- ConexionBD: utilidades de conexión y ejecución genérica.
- OperacionesDDL: creación / eliminación de tablas (DDL).
- OperacionesDML: inserciones / actualizaciones / borrados (DML).
- Consultas: SELECTs simples, parametrizadas, JOINs y metadatos.
- Transacciones: ejemplos de control de transacción (commit/rollback/manual).
- ProcedimientosSimulados: funciones que simulan procedimientos almacenados.

Notas:
- Se evita cualquier referencia a Turso o librerías remotas.
- Todas las consultas parametrizadas usan '?' para prevenir inyección SQL.
- Se usa 'with sqlite3.connect(...) as conn' salvo cuando demostramos control manual.
"""

import sqlite3
from typing import Any, Iterable, List, Optional, Tuple


# ---------------------------
# Clase: ConexionBD
# ---------------------------
class ConexionBD:
    """
    Gestiona la conexión con la base de datos SQLite y proporciona métodos
    auxiliares para ejecutar sentencias SQL de forma segura.
    """

    def __init__(self, archivo_db: str = "examen_ut2.db"):
        """
        Inicializa con el nombre/archivo de la base de datos.
        """
        self.archivo_db = archivo_db

    def ejecutar(self, sql: str, params: Optional[Iterable[Any]] = None) -> None:
        """
        Ejecuta una sentencia SQL que no devuelve filas (CREATE, INSERT, UPDATE, DELETE).
        Usa 'with' para garantizar commit/rollback automático.
        """
        with sqlite3.connect(self.archivo_db) as conn:
            cur = conn.cursor()
            if params:
                cur.execute(sql, tuple(params))
            else:
                cur.execute(sql)
            # con 'with' el commit se hace automáticamente si no hay excepción

    def ejecutar_many(self, sql: str, seq_of_params: Iterable[Iterable[Any]]) -> None:
        """
        Ejecuta la misma sentencia SQL varias veces con diferentes parámetros.
        Ideal para insertar múltiples filas con executemany.
        """
        with sqlite3.connect(self.archivo_db) as conn:
            cur = conn.cursor()
            cur.executemany(sql, seq_of_params)

    def consultar(self, sql: str, params: Optional[Iterable[Any]] = None) -> List[Tuple]:
        """
        Ejecuta una consulta SELECT y devuelve todas las filas como lista de tuplas.
        """
        with sqlite3.connect(self.archivo_db) as conn:
            cur = conn.cursor()
            if params:
                cur.execute(sql, tuple(params))
            else:
                cur.execute(sql)
            resultados = cur.fetchall()
        return resultados

    def ejecutar_tx_manual(self) -> sqlite3.Connection:
        """
        Abre y devuelve una conexión sin 'with', para controlar manualmente commit/rollback.
        El llamador debe usar conn.commit()/conn.rollback() y conn.close().
        """
        conn = sqlite3.connect(self.archivo_db)
        return conn

    def tablas_existentes(self) -> List[str]:
        """
        Devuelve nombres de tablas existentes en la BBDD.
        """
        sql = "SELECT name FROM sqlite_master WHERE type='table' ORDER BY name"
        filas = self.consultar(sql)
        return [f[0] for f in filas]


# ---------------------------
# Clase: OperacionesDDL
# ---------------------------
class OperacionesDDL:
    """
    Agrupa las operaciones DDL: creación y eliminación de tablas.
    """

    def __init__(self, conexion: ConexionBD):
        self.conexion = conexion

    def crear_tablas_basicas(self):
        """
        Crea dos tablas de ejemplo: personas y productos.
        - personas: id, nombre, edad, ciudad
        - productos: id, nombre, precio
        Se usa IF NOT EXISTS para evitar errores si ya existen.
        """
        sql_personas = """
        CREATE TABLE IF NOT EXISTS personas (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre TEXT NOT NULL,
            edad INTEGER,
            ciudad TEXT
        );
        """
        sql_productos = """
        CREATE TABLE IF NOT EXISTS productos (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre TEXT NOT NULL,
            precio REAL
        );
        """
        # Unir ambas ejecuciones en una transacción implícita con 'with'
        self.conexion.ejecutar(sql_personas)
        self.conexion.ejecutar(sql_productos)

    def crear_tabla_relacion_pedidos(self):
        """
        Crea una tabla 'pedidos' con clave foránea a personas y productos.
        Ejemplo de DDL que refleja relaciones (aunque SQLite no impone integridad referencial
        por defecto sin PRAGMA foreign_keys=ON).
        """
        sql_pedidos = """
        CREATE TABLE IF NOT EXISTS pedidos (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            persona_id INTEGER,
            producto_id INTEGER,
            cantidad INTEGER DEFAULT 1,
            fecha TEXT DEFAULT (datetime('now')),
            FOREIGN KEY (persona_id) REFERENCES personas(id),
            FOREIGN KEY (producto_id) REFERENCES productos(id)
        );
        """
        self.conexion.ejecutar(sql_pedidos)

    def eliminar_tablas(self):
        """
        Elimina las tablas creadas (DROP TABLE IF EXISTS).
        Ordena eliminación para evitar problemas con claves foráneas en otros SGBD.
        """
        self.conexion.ejecutar("DROP TABLE IF EXISTS pedidos")
        self.conexion.ejecutar("DROP TABLE IF EXISTS productos")
        self.conexion.ejecutar("DROP TABLE IF EXISTS personas")


# ---------------------------
# Clase: OperacionesDML
# ---------------------------
class OperacionesDML:
    """
    Inserciones, actualizaciones y eliminaciones. Métodos genéricos y ejemplos.
    """

    def __init__(self, conexion: ConexionBD):
        self.conexion = conexion

    # ---- Inserciones ----
    def insertar_persona(self, nombre: str, edad: Optional[int], ciudad: Optional[str]) -> None:
        """
        Inserta una fila en 'personas'. Parametrizado con '?'
        """
        sql = "INSERT INTO personas (nombre, edad, ciudad) VALUES (?, ?, ?)"
        self.conexion.ejecutar(sql, (nombre, edad, ciudad))

    def insertar_varias_personas(self, lista_personas: Iterable[Tuple[str, int, str]]) -> None:
        """
        Inserta varias personas usando executemany para eficiencia.
        lista_personas: iterable de tuplas (nombre, edad, ciudad)
        """
        sql = "INSERT INTO personas (nombre, edad, ciudad) VALUES (?, ?, ?)"
        self.conexion.ejecutar_many(sql, lista_personas)

    def insertar_producto(self, nombre: str, precio: float) -> None:
        """
        Inserta un producto simple.
        """
        sql = "INSERT INTO productos (nombre, precio) VALUES (?, ?)"
        self.conexion.ejecutar(sql, (nombre, precio))

    def insertar_varios_productos(self, lista_productos: Iterable[Tuple[str, float]]) -> None:
        """
        Inserta muchos productos con executemany.
        """
        sql = "INSERT INTO productos (nombre, precio) VALUES (?, ?)"
        self.conexion.ejecutar_many(sql, lista_productos)

    # ---- Actualizaciones ----
    def actualizar_edad_persona(self, id_persona: int, nueva_edad: int) -> None:
        """
        Actualiza la edad de una persona por su id.
        """
        sql = "UPDATE personas SET edad = ? WHERE id = ?"
        self.conexion.ejecutar(sql, (nueva_edad, id_persona))

    def actualizar_precio_producto(self, id_producto: int, nuevo_precio: float) -> None:
        """
        Actualiza el precio de un producto por su id.
        """
        sql = "UPDATE productos SET precio = ? WHERE id = ?"
        self.conexion.ejecutar(sql, (nuevo_precio, id_producto))

    # ---- Eliminaciones ----
    def eliminar_persona_por_id(self, id_persona: int) -> None:
        """
        Elimina una persona por id.
        """
        sql = "DELETE FROM personas WHERE id = ?"
        self.conexion.ejecutar(sql, (id_persona,))

    def eliminar_producto_por_id(self, id_producto: int) -> None:
        """
        Elimina un producto por id.
        """
        sql = "DELETE FROM productos WHERE id = ?"
        self.conexion.ejecutar(sql, (id_producto,))

    def vaciar_tabla(self, nombre_tabla: str) -> None:
        """
        Borra todos los registros de la tabla indicada.
        """
        # IMPORTANTE: no concatenar directamente variables en SQL que vienen del usuario.
        # Aquí se comprueba el nombre contra una lista blanca (por seguridad).
        if nombre_tabla not in ("personas", "productos", "pedidos"):
            raise ValueError("Nombre de tabla no permitido para vaciar.")
        sql = f"DELETE FROM {nombre_tabla}"
        self.conexion.ejecutar(sql)


# ---------------------------
# Clase: Consultas
# ---------------------------
class Consultas:
    """
    Consultas SELECT: simples, parametrizadas, con JOIN y metadatos.
    """

    def __init__(self, conexion: ConexionBD):
        self.conexion = conexion

    def seleccionar_todas_personas(self) -> List[Tuple]:
        """
        Devuelve todas las filas de personas.
        """
        sql = "SELECT id, nombre, edad, ciudad FROM personas ORDER BY id"
        return self.conexion.consultar(sql)

    def seleccionar_personas_por_ciudad(self, ciudad: str) -> List[Tuple]:
        """
        Consulta parametrizada: todas las personas de una ciudad.
        """
        sql = "SELECT id, nombre, edad FROM personas WHERE ciudad = ?"
        return self.conexion.consultar(sql, (ciudad,))

    def producto_mas_barato(self) -> Optional[Tuple]:
        """
        Devuelve el producto más barato (ejemplo de agregación y ORDER BY LIMIT).
        """
        sql = "SELECT id, nombre, precio FROM productos ORDER BY precio ASC LIMIT 1"
        filas = self.conexion.consultar(sql)
        return filas[0] if filas else None

    def join_pedidos(self) -> List[Tuple]:
        """
        Ejemplo de JOIN entre pedidos, personas y productos mostrando información combinada.
        """
        sql = """
        SELECT p.id, per.nombre AS cliente, prod.nombre AS producto, p.cantidad, p.fecha
        FROM pedidos p
        LEFT JOIN personas per ON p.persona_id = per.id
        LEFT JOIN productos prod ON p.producto_id = prod.id
        ORDER BY p.fecha DESC
        """
        return self.conexion.consultar(sql)

    def informacion_tabla(self, nombre_tabla: str) -> List[Tuple]:
        """
        Muestra metadatos y estructura de una tabla usando PRAGMA table_info.
        Útil para el examen práctico cuando pidan describir la estructura.
        """
        sql = f"PRAGMA table_info({nombre_tabla})"
        return self.conexion.consultar(sql)


# ---------------------------
# Clase: Transacciones
# ---------------------------
class Transacciones:
    """
    Ejemplos de transacciones automáticas y control manual.
    """

    def __init__(self, conexion: ConexionBD):
        self.conexion = conexion

    def transaccion_automatica_demo(self):
        """
        Muestra que con 'with' cualquier excepción hace rollback implícito.
        Insertamos, provocamos error y comprobamos que la inserción no persiste.
        """
        try:
            with sqlite3.connect(self.conexion.archivo_db) as conn:
                cur = conn.cursor()
                cur.execute("INSERT INTO productos (nombre, precio) VALUES (?, ?)", ("temp_producto", 1.23))
                # provocamos excepción para forzar rollback
                raise RuntimeError("Error intencional para forzar rollback")
                # Si no hubiera excepción, commit se haría al salir del with
        except Exception as e:
            # Al venir aquí, la inserción anterior no se habrá confirmado
            print("Transacción automática revertida:", e)

    def transaccion_manual_demo(self):
        """
        Control manual de transacción mostrando BEGIN, COMMIT y ROLLBACK explícitos.
        Útil para el examen práctico: distinguir manejo automático de manual.
        """
        conn = self.conexion.ejecutar_tx_manual()  # devuelve conexión sin 'with'
        try:
            cur = conn.cursor()
            cur.execute("BEGIN IMMEDIATE")  # bloquea para escritura
            cur.execute("INSERT INTO productos (nombre, precio) VALUES (?, ?)", ("tablet_demo", 199.9))
            # Suponemos que todo va bien:
            conn.commit()
            print("Transacción manual: commit realizado con éxito.")
        except Exception as e:
            conn.rollback()
            print("Transacción manual: rollback realizado por error:", e)
        finally:
            conn.close()

    def demo_modos_begin(self):
        """
        Ejemplos de modos de BEGIN: DEFERRED, IMMEDIATE, EXCLUSIVE.
        Solo demostrativos: en SQLite afectan bloqueo de la base.
        """
        conn = self.conexion.ejecutar_tx_manual()
        try:
            cur = conn.cursor()
            cur.execute("BEGIN DEFERRED")
            # operaciones de solo lectura o que no bloqueen hasta escritura
            conn.commit()
            cur.execute("BEGIN IMMEDIATE")
            conn.commit()
            cur.execute("BEGIN EXCLUSIVE")
            conn.commit()
        finally:
            conn.close()


# ---------------------------
# Clase: ProcedimientosSimulados
# ---------------------------
class ProcedimientosSimulados:
    """
    Simula procedimientos almacenados mediante métodos Python que ejecutan
    operaciones atómicas y parametrizadas.
    """

    def __init__(self, conexion: ConexionBD):
        self.conexion = conexion

    def listar_productos_menores(self, precio_max: float) -> List[Tuple]:
        """
        Lista productos con precio menor que precio_max (parámetro).
        Simula un procedimiento almacenado con parámetros.
        """
        sql = "SELECT id, nombre, precio FROM productos WHERE precio < ? ORDER BY precio"
        return self.conexion.consultar(sql, (precio_max,))

    def incremento_precio_porcentaje(self, porcentaje: float) -> None:
        """
        Incrementa el precio de todos los productos en un porcentaje dado.
        Se hace dentro de una transacción para que sea atómico.
        """
        conn = self.conexion.ejecutar_tx_manual()
        try:
            cur = conn.cursor()
            cur.execute("BEGIN")
            cur.execute("UPDATE productos SET precio = precio * (1 + ? / 100.0)", (porcentaje,))
            conn.commit()
            print(f"Incrementado precio de productos en {porcentaje}%.")
        except Exception as e:
            conn.rollback()
            print("Error durante incremento de precios, se hizo rollback:", e)
        finally:
            conn.close()

    def crear_pedido(self, persona_id: int, producto_id: int, cantidad: int = 1) -> None:
        """
        Inserta un pedido y decrementa stock (si existiera). Aquí solo insertamos
        el registro del pedido. Operación simulada en una transacción.
        """
        conn = self.conexion.ejecutar_tx_manual()
        try:
            cur = conn.cursor()
            cur.execute("BEGIN")
            cur.execute(
                "INSERT INTO pedidos (persona_id, producto_id, cantidad) VALUES (?, ?, ?)",
                (persona_id, producto_id, cantidad),
            )
            conn.commit()
            print("Pedido creado correctamente.")
        except Exception as e:
            conn.rollback()
            print("Error al crear pedido, se hizo rollback:", e)
        finally:
            conn.close()


# ---------------------------
# Función utilitaria: demo completa (sin menú)
# ---------------------------
def demo_completa():
    """
    Ejecuta una demo secuencial que cubre todas las funcionalidades
    que podrías necesitar para aprobar la parte práctica del examen.
    """
    print("=== DEMO: inicio ===")
    conexion = ConexionBD("examen_ut2.db")

    # Instanciar clases de operaciones
    ddl = OperacionesDDL(conexion)
    dml = OperacionesDML(conexion)
    consultas = Consultas(conexion)
    trans = Transacciones(conexion)
    proc = ProcedimientosSimulados(conexion)

    # 1) Crear tablas (DDL)
    print("\n-> Creando tablas...")
    ddl.crear_tablas_basicas()
    ddl.crear_tabla_relacion_pedidos()

    # 2) Mostrar tablas existentes
    print("Tablas existentes:", conexion.tablas_existentes())

    # 3) Insertar datos (DML)
    print("\n-> Insertando datos de ejemplo...")
    dml.insertar_varias_personas([
        ("Ana", 25, "Madrid"),
        ("Luis", 30, "Barcelona"),
        ("Marta", 22, "Sevilla"),
        ("Pedro", 40, "Madrid"),
    ])
    dml.insertar_varios_productos([
        ("Teclado", 29.99),
        ("Ratón", 19.95),
        ("Monitor", 149.99),
        ("Altavoz", 39.5),
    ])

    # 4) Consultas simples y parametrizadas
    print("\n-> Consultas: todas las personas")
    for fila in consultas.seleccionar_todas_personas():
        print(fila)

    print("\n-> Consultas: personas en Madrid")
    for fila in consultas.seleccionar_personas_por_ciudad("Madrid"):
        print(fila)

    # 5) Actualización (UPDATE)
    print("\n-> Actualizando edad de id=1 a 26...")
    filas_personas = conexion.consultar("SELECT id FROM personas ORDER BY id LIMIT 1")
    if filas_personas:
        id_primero = filas_personas[0][0]
        dml.actualizar_edad_persona(id_primero, 26)

    # 6) Uso de transacciones automáticas y manuales
    print("\n-> Demo transacción automática (intentando insertar y forzar error)...")
    trans.transaccion_automatica_demo()

    print("\n-> Demo transacción manual (insert y commit)...")
    trans.transaccion_manual_demo()

    # 7) Procedimientos simulados (procedimiento: listar baratos)
    print("\n-> Productos más baratos (< 100):")
    baratos = proc.listar_productos_menores(100)
    for p in baratos:
        print(p)

    # 8) Incremento de precios (transacción manual dentro del procedimiento simulado)
    print("\n-> Incremento de precios en 5% (simulado como procedimiento)...")
    proc.incremento_precio_porcentaje(5)

    # 9) Consultar producto más barato tras incremento
    print("\n-> Producto más barato ahora:")
    print(consultas.producto_mas_barato())

    # 10) Crear un pedido (simulación)
    print("\n-> Creando pedido de ejemplo para persona 1 y producto 1...")
    # Asegurarse de que existan ids 1 (si no, se adaptaría; en demo asumimos relleno inicial)
    proc.crear_pedido(1, 1, 2)

    # 11) Mostrar JOIN pedidos
    print("\n-> Pedidos registrados con JOIN:")
    for fila in consultas.join_pedidos():
        print(fila)

    # 12) Información de estructura de tablas (PRAGMA)
    print("\n-> Estructura de la tabla 'personas':")
    for info in consultas.informacion_tabla("personas"):
        print(info)

    # 13) Eliminaciones y limpieza opcional
    print("\n-> Eliminando persona con id=2 (si existe)...")
    filas = conexion.consultar("SELECT id FROM personas WHERE id = 2")
    if filas:
        dml.eliminar_persona_por_id(2)

    print("\n-> Vaciar tabla productos (demo)...")
    # dml.vaciar_tabla("productos")  # descomentar si se desea vaciar

    # 14) Mostrar tablas finales
    print("\nTablas finales:", conexion.tablas_existentes())

    # NOTA: no eliminamos las tablas por defecto; si quieres limpiar:
    # ddl.eliminar_tablas()
    print("\n=== DEMO: fin ===")


# ---------------------------
# Ejecución del demo al lanzar el script
# ---------------------------
if __name__ == "__main__":
    demo_completa()
