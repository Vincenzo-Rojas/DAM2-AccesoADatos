from peewee import fn
from data_base import db
from ingesta import ingesta

#Modelos
from Modelos.Class_Model import ClassModel

#Repositorios
from Repositorios.Class_Repo import ClassRepo

def main():
    # Conexión a la base de datos
    db.connect()

    #INGESTA DE DATOS CON DB
    ingesta(db)

# EL PROGRAMA


    # Cerrar conexión
    db.close()
    print("Conexión cerrada correctamente.")

if __name__ == "__main__":
    main()