import sqlite3
cursor = sqlite3.connect("ejemplo.db").cursor()

cursor.execute("INSERT INTO personas (nombre, edad) VALUES (?, ?)", ("Ana", 25))

def listar_productos_baratos(precio_max):
    with sqlite3.connect("db.db") as conn:
        cur = conn.cursor()
        cur.execute("SELECT * FROM productos WHERE precio < ?", (precio_max,))
        return cur.fetchall()
