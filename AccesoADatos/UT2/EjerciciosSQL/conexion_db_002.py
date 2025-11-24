import sqlite3

with sqlite3.connect("formula1.db") as conn:
    cursor = conn.cursor()

    cursor.execute('''
                   CREATE TABLE IF NOT EXISTS escuderias (
                    nombre TEXT PRIMARY KEY,
                    ayo_fundacion INTEGER,
                    titulos_constructor INTEGER NOT NULL,
                   team_principal
                   )'''
                   )
    
    cursor.execute('''
                     CREATE TABLE IF NOT EXISTS piloto (
                      nombre TEXT PRIMARY KEY,
                      nacionalidad TEXT NOT NULL,
                      fecha_nacimiento DATE NOT NULL,
                      victorias INTEGER NOT NULL,
                      podios INTEGER NOT NULL,
                      polas INTEGER NOT NULL,
                      escuderias TEXT,
                      FOREIGN KEY (escuderias) REFERENCES escuderias(nombre)
                     )'''
                   )
    
    cursor.execute(''' 
                   INSERT INTO escuderias (nombre, ayo_fundacion, titulos_constructor, team_principal)
                   VALUES
                   ('Mercedes', 1954, 8, 'Toto Wolff'),
                   ('Red Bull', 2005, 5, 'Christian Horner'),
                   ('Ferrari', 1929, 16, 'Mattia Binotto'),
                    ('McLaren', 1963, 8, 'Andreas Seidl'),
                    ('Williams', 1977, 9, 'Jost Capito'),
                    ('Renault', 1977, 2, 'Cyril Abiteboul'),
                    ('AlphaTauri', 2006, 0, 'Franz Tost');

                   '''
                   )
    
    cursor.execute(''' SELECT * FROM escuderias''')
    resultado = cursor.fetchall()
    for fila in resultado:
        print(fila)