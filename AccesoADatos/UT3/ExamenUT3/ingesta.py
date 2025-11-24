from peewee import *

from Modelos.Class_Model import ClassModel

def ingesta(db):
    db.execute_sql('PRAGMA foreign_keys = OFF;')

     # Drop: primero las tablas dependientes
    db.drop_tables([tabla10, tabla9,...], safe=True)

    # Create: primero las tablas de las que dependen las otras
    db.create_tables([ tabla1,tabla2,... ], safe=True)
    db.execute_sql('PRAGMA foreign_keys = ON;')

    print("Base de datos creada")

    # diccionarios para inserciones, lista para no realizar varios for
    dicc = [{}]
    #AQUI

    # Inserción con Peewee
    with db.atomic():
        for data in dicc:
            ClassModel.create(**data)