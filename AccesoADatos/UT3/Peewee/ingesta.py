from peewee import *
from Modelos.Ubicaciones_Model import Ubicacion
from Modelos.Personajes_Model import Personaje
from Modelos.Enemigos_Model import EnemigoModel
from Modelos.Objetos_Model import ObjetoModel
from Modelos.Personajes_Objetos_Model import PersonajeObjetoModel
from Modelos.Enemigos_Objetos_Model import EnemigoObjetoModel

from Repositorios.Ubicaciones import UbicacionRepo
from Repositorios.Personajes import PersonajeRepo


def ingesta(db):
    
    db.execute_sql('PRAGMA foreign_keys = OFF;')
    # Drop: primero las tablas dependientes
    db.drop_tables([
        EnemigoObjetoModel,
        PersonajeObjetoModel,
        ObjetoModel,
        EnemigoModel,
        Personaje,
        Ubicacion
    ], safe=True)

    # Create: primero las tablas de las que dependen las otras
    db.create_tables([ Ubicacion, Personaje, EnemigoModel, ObjetoModel, PersonajeObjetoModel, EnemigoObjetoModel ], safe=True)
    db.execute_sql('PRAGMA foreign_keys = ON;')

    print("Base de datos creada")


    # diccionarios para inserciones, lista para no realizar varios for

    # Ubicaciones
    ubicaciones = [
        {"nombre": "Ciudadela", "descripcion": "Ciudad principal del reino", "tipo": "ciudad"},
        {"nombre": "Bosque Oscuro", "descripcion": "Bosque tenebroso con enemigos", "tipo": "mundo"},
        {"nombre": "Mazmorra Abisal", "descripcion": "Mazmorra subterranea con jefes", "tipo": "mazmorra"}
    ]

    # Personajes
    personajes = [
        {"nombre": "Jerjes", "descripcion": "Mercader amigable", "id_ubicacion": 1, "da_objeto": True, "rol": "mercader"},
        {"nombre": "Manolo", "descripcion": "NPC guia", "id_ubicacion": 2, "da_objeto": False, "rol": "NPC"},
        {"nombre": "Yokai", "descripcion": "Comerciante de rarezas", "id_ubicacion": 1, "da_objeto": True, "rol": "mercader"}
        
    ]
     # --- Inserciones ---
    datos_personajes = [
        ("Aria", "Mercader amigable", 1, True, "mercader"),
        ("Borin", "Guía NPC", 2, False, "NPC"),
        ("Ciri", "Comerciante de rarezas", 1, True, "mercader"),
        ("Dandelion", "Bardo errante", 2, False, "NPC"),
        ("Eredin", "Villano poderoso", 3, True, "villano")
    ]

    # Enemigos
    enemigos = [
        {"nombre": "Goblin", "descripcion": "Enemigo debil", "nivel": 1, "id_ubicacion": 2, "drop_objetos": True},
        {"nombre": "Troll", "descripcion": "Enemigo fuerte", "nivel": 5, "id_ubicacion": 3, "drop_objetos": True},
        {"nombre": "Esqueleto", "descripcion": "Enemigo del bosque", "nivel": 2, "id_ubicacion": 2, "drop_objetos": False}
    ]

    # Objetos
    objetos = [
        {"nombre": "Espada de Madera", "descripcion": "Espada basica", "rareza": "comun", "id_ubicacion": 2, "id_personaje_dropea": None, "id_enemigo_dropea": 1},
        {"nombre": "Amuleto Raro", "descripcion": "Amuleto con poderes", "rareza": "raro", "id_ubicacion": None, "id_personaje_dropea": 3, "id_enemigo_dropea": 2},
        {"nombre": "Pocion de Vida", "descripcion": "Recupera salud", "rareza": "comun", "id_ubicacion": 1, "id_personaje_dropea": 1, "id_enemigo_dropea": None}
    ]

    # Personaje_Objeto
    personaje_objeto = [
        {"id_personaje": 1, "id_objeto": 3, "cantidad": 5},
        {"id_personaje": 3, "id_objeto": 2, "cantidad": 1}
    ]

    # Enemigo_Objeto
    enemigo_objeto = [
        {"id_enemigo": 1, "id_objeto": 1, "probabilidad": 0.7},
        {"id_enemigo": 2, "id_objeto": 2, "probabilidad": 0.3}
    ]

    # Inserción con Peewee
    with db.atomic():
        UbicacionRepo.insertar_ubicaciones(ubicaciones)
        PersonajeRepo.insertar_personajes(personajes)

        PersonajeRepo.ingesta_multiple(db, datos_personajes)

        for data in enemigos:
            EnemigoModel.create(**data)
        for data in objetos:
            ObjetoModel.create(**data)
        for data in personaje_objeto:
            PersonajeObjetoModel.create(**data)
        for data in enemigo_objeto:
            EnemigoObjetoModel.create(**data)
        
    print("Datos instroducidos")

