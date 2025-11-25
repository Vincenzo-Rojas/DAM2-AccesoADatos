import json
from peewee import * # type: ignore
from conexion import inicializar_base, db
from pprint import pprint
from Modelos.X_Model import X

def main():
    #inicializar tablas
    tablas = [Tabla1, Tabla2...]

    #inicializar_base
    inicializar_base(tablas, True)


if __name__ == "__main__":

    main()
