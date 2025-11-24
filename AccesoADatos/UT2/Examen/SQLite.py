"""
==============================================
GESTIÓN COMPLETA DE SQLITE CON PYTHON (DB-API 2.0)
Basado en la UT2 - Manejo de conectores (Jose Medina Velasco)
==============================================
Este script demuestra todas las operaciones que pueden pedirse
en un examen teórico-práctico sobre conectores de bases de datos
en Python, especialmente con SQLite.
==============================================
"""

import sqlite3

class GestorBD:
    def __init__(self, nombre_bd):
        """
        Constructor: crea o conecta a una base de datos SQLite.
        Si el archivo no existe, se genera automáticamente.
        """
        self.nombre_bd = nombre_bd

    # ============================================================
    # 1. CREACIÓN DE TABLAS (sentencias DDL)
    # ============================================================
    def crear_tablas(self):
        """
        Crea varias tablas para simular un esquema de ejemplo.
        """
        with sqlite3.connect(self.nombre_bd) as conn:
            cursor = conn.cursor()

            # Sentencia DDL multilínea
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

            cursor.execute(sql_personas)
            cursor.execute(sql_productos)
            conn.commit()
            print(" Tablas 'personas' y 'productos' creadas correctamente.")

    # ============================================================
    # 2. INSERCIÓN DE DATOS (DML)
    # ============================================================
    def insertar_datos(self):
        """
        Inserta registros de ejemplo en ambas tablas.
        Usa consultas parametrizadas (?) para evitar inyecciones SQL.
        """
        with sqlite3.connect(self.nombre_bd) as conn:
            cursor = conn.cursor()

            sql_persona = "INSERT INTO personas (nombre, edad, ciudad) VALUES (?, ?, ?)"
            sql_producto = "INSERT INTO productos (nombre, precio) VALUES (?, ?)"

            personas = [
                ("Ana", 25, "Madrid"),
                ("Luis", 30, "Barcelona"),
                ("Marta", 22, "Sevilla")
            ]

            productos = [
                ("Teclado", 29.99),
                ("Ratón", 19.95),
                ("Monitor", 149.99)
            ]

            cursor.executemany(sql_persona, personas)
            cursor.executemany(sql_producto, productos)
            conn.commit()
            print(" Datos insertados correctamente en ambas tablas.")

    # ============================================================
    # 3. CONSULTA DE DATOS
    # ============================================================
    def consultar_personas(self):
        """
        Recupera y muestra todos los registros de la tabla 'personas'.
        """
        with sqlite3.connect(self.nombre_bd) as conn:
            cursor = conn.cursor()
            sql = "SELECT * FROM personas"
            cursor.execute(sql)
            resultados = cursor.fetchall()
            print("\n Personas registradas:")
            for fila in resultados:
                print(fila)

    def consultar_parametrizada(self, ciudad):
        """
        Ejemplo de consulta con parámetro (segura contra inyección SQL).
        """
        with sqlite3.connect(self.nombre_bd) as conn:
            cursor = conn.cursor()
            sql = "SELECT nombre, edad FROM personas WHERE ciudad = ?"
            cursor.execute(sql, (ciudad,))
            resultados = cursor.fetchall()
            print(f"\n Personas de {ciudad}:")
            for fila in resultados:
                print(fila)
            if not resultados:
                print(" Ninguna persona en esa ciudad.")

    # ============================================================
    # 4. ACTUALIZACIÓN DE DATOS (UPDATE)
    # ============================================================
    def actualizar_precio(self, id_producto, nuevo_precio):
        """
        Actualiza el precio de un producto mediante su ID.
        """
        with sqlite3.connect(self.nombre_bd) as conn:
            cursor = conn.cursor()
            sql = "UPDATE productos SET precio = ? WHERE id = ?"
            cursor.execute(sql, (nuevo_precio, id_producto))
            conn.commit()
            print(f" Precio del producto con ID {id_producto} actualizado a {nuevo_precio}€")

    # ============================================================
    # 5. ELIMINACIÓN DE DATOS (DELETE)
    # ============================================================
    def eliminar_persona(self, id_persona):
        """
        Elimina una persona según su ID.
        """
        with sqlite3.connect(self.nombre_bd) as conn:
            cursor = conn.cursor()
            sql = "DELETE FROM personas WHERE id = ?"
            cursor.execute(sql, (id_persona,))
            conn.commit()
            print(f" Persona con ID {id_persona} eliminada correctamente.")

    # ============================================================
    # 6. GESTIÓN DE TRANSACCIONES MANUAL (commit / rollback)
    # ============================================================
    def transaccion_manual(self):
        """
        Ejemplo de transacción controlada manualmente.
        Si hay un error, se revierte todo.
        """
        conn = sqlite3.connect(self.nombre_bd)
        try:
            cursor = conn.cursor()
            cursor.execute("BEGIN IMMEDIATE")  # Bloquea escritura
            cursor.execute("INSERT INTO productos (nombre, precio) VALUES (?, ?)", ("Tablet", 250))
            # Simulamos un error para probar rollback
            raise Exception(" Error simulado: fallo en transacción")
            conn.commit()
        except Exception as e:
            conn.rollback()
            print(" Transacción revertida por error:", e)
        finally:
            conn.close()

    # ============================================================
    # 7. SIMULACIÓN DE PROCEDIMIENTO ALMACENADO
    # ============================================================
    def procedimiento_listar_baratos(self, precio_max):
        """
        Función que actúa como procedimiento almacenado:
        lista los productos por debajo de cierto precio.
        """
        with sqlite3.connect(self.nombre_bd) as conn:
            cursor = conn.cursor()
            sql = "SELECT nombre, precio FROM productos WHERE precio < ?"
            cursor.execute(sql, (precio_max,))
            resultados = cursor.fetchall()
            print(f"\n Productos con precio menor a {precio_max}€:")
            for fila in resultados:
                print(fila)

    # ============================================================
    # 8. ELIMINAR TABLAS
    # ============================================================
    def eliminar_tablas(self):
        """
        Borra las tablas creadas (operación DDL).
        """
        with sqlite3.connect(self.nombre_bd) as conn:
            cursor = conn.cursor()
            cursor.execute("DROP TABLE IF EXISTS personas")
            cursor.execute("DROP TABLE IF EXISTS productos")
            conn.commit()
            print("Tablas eliminadas correctamente.")


# ============================================================
# PROGRAMA PRINCIPAL (DEMO COMPLETA)
# ============================================================
if __name__ == "__main__":
    bd = GestorBD("examen_ut2.db")

    bd.crear_tablas()
    bd.insertar_datos()
    bd.consultar_personas()
    bd.consultar_parametrizada("Madrid")
    bd.actualizar_precio(1, 39.99)
    bd.procedimiento_listar_baratos(100)
    bd.transaccion_manual()
    bd.eliminar_persona(2)
    bd.consultar_personas()
    # bd.eliminar_tablas()  # Descomentar si se desea limpiar la BD al final
