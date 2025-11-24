import sqlite3

# ============================================
# Clase que gestiona operaciones básicas con SQLite
# ============================================
class GestorBD:
    def __init__(self, nombre_bd):
        """
        Constructor que crea o conecta a una base de datos SQLite.
        Si el archivo no existe, se crea automáticamente.
        """
        self.nombre_bd = nombre_bd

    # --------------------------------------------
    # MÉTODO 1: Crear una tabla
    # --------------------------------------------
    def crear_tabla(self):
        """
        Crea una tabla llamada 'personas' si no existe.
        Contiene los campos: id (clave primaria), nombre y edad.
        """
        with sqlite3.connect(self.nombre_bd) as conn:
            cursor = conn.cursor()
            sql = """
            CREATE TABLE IF NOT EXISTS personas (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                edad INTEGER
            );
            """
            cursor.execute(sql)
            conn.commit()
            print("Tabla 'personas' creada correctamente.")

    # --------------------------------------------
    # MÉTODO 2: Insertar datos en la tabla
    # --------------------------------------------
    def insertar_datos(self):
        """
        Inserta tres registros de ejemplo en la tabla 'personas'.
        """
        with sqlite3.connect(self.nombre_bd) as conn:
            cursor = conn.cursor()
            # Usamos ? para consultas parametrizadas, que evitan inyección SQL
            sql = "INSERT INTO personas (nombre, edad) VALUES (?, ?)"
            datos = [("Ana", 25), ("Luis", 30), ("Marta", 22)]
            cursor.executemany(sql, datos)
            conn.commit()
            print("Datos insertados correctamente en 'personas'.")

    # --------------------------------------------
    # MÉTODO 3: Consultar datos
    # --------------------------------------------
    def consultar_datos(self):
        """
        Consulta y muestra todos los registros de la tabla 'personas'.
        """
        with sqlite3.connect(self.nombre_bd) as conn:
            cursor = conn.cursor()
            sql = "SELECT * FROM personas"
            cursor.execute(sql)
            resultados = cursor.fetchall()
            print("📋 Registros en la tabla 'personas':")
            for fila in resultados:
                print(fila)
            if not resultados:
                print("No hay datos en la tabla.")

    # --------------------------------------------
    # MÉTODO 4: Modificar datos
    # --------------------------------------------
    def modificar_datos(self):
        """
        Modifica la edad de una persona con un ID específico.
        """
        with sqlite3.connect(self.nombre_bd) as conn:
            cursor = conn.cursor()
            # En este ejemplo cambiamos la edad de la persona con id=1
            sql = "UPDATE personas SET edad = ? WHERE id = ?"
            cursor.execute(sql, (28, 1))
            conn.commit()
            print("Edad actualizada correctamente para la persona con ID=1.")

    # --------------------------------------------
    # MÉTODO 5: Eliminar datos
    # --------------------------------------------
    def eliminar_datos(self):
        """
        Elimina una persona específica por su ID.
        """
        with sqlite3.connect(self.nombre_bd) as conn:
            cursor = conn.cursor()
            # En este ejemplo eliminamos la persona con id=2
            sql = "DELETE FROM personas WHERE id = ?"
            cursor.execute(sql, (2,))
            conn.commit()
            print("Persona con ID=2 eliminada correctamente.")

    # --------------------------------------------
    # MÉTODO 6: Eliminar la tabla completa (opcional)
    # --------------------------------------------
    def eliminar_tabla(self):
        """
        Elimina completamente la tabla 'personas' de la base de datos.
        """
        with sqlite3.connect(self.nombre_bd) as conn:
            cursor = conn.cursor()
            sql = "DROP TABLE IF EXISTS personas"
            cursor.execute(sql)
            conn.commit()
            print("Tabla 'personas' eliminada de la base de datos.")


# ============================================
# PROGRAMA PRINCIPAL
# ============================================
if __name__ == "__main__":
    # Se crea un objeto de la clase GestorBD
    gestor = GestorBD("ejemplo.db")

    # Ejecución secuencial de las operaciones típicas de una base de datos
    gestor.crear_tabla()       # Crear tabla
    gestor.insertar_datos()    # Insertar registros
    gestor.consultar_datos()   # Consultar registros
    gestor.modificar_datos()   # Modificar registro
    gestor.consultar_datos()   # Ver cambios
    gestor.eliminar_datos()    # Eliminar registro
    gestor.consultar_datos()   # Ver registros restantes
    # gestor.eliminar_tabla()  # (Descomentar si se quiere eliminar la tabla al final)
