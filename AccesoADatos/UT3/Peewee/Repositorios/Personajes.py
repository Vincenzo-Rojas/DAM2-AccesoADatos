from peewee import *
from Modelos.Personajes_Model import Personaje

class PersonajeRepo:
    #Asignar campos de la tabla, formato para introducir en create y update, no necesario
    def asignarData(nom: str, des: str, id_ubi: int, da_Obj: bool, typ: str):
        return {"nombre":nom, "descripcion": des,"id_ubicacion": id_ubi, "da_objeto": da_Obj, "rol":typ}

    #CRUD
    @staticmethod
    def create(data):
        return Personaje.create(**data)
    
    @staticmethod
    def createP(nom: str, des: str, id_ubi: int, da_Obj: bool, typ: str):
        return Personaje.create(nombre= nom, descripcion= des,id_ubicacion= id_ubi, da_objeto= da_Obj, rol= typ)

    @staticmethod
    def get_by_id(id):
        #.get_or_none(): forma segura de obtener un único registro de la db sin provocar un error si no existe.
        return Personaje.get_or_none(Personaje.id_personaje == id)
     
    #buscar por cualquier campo
    @staticmethod
    def get_by(field_name: str, value):
        field = getattr(Personaje, field_name)
        return Personaje.get_or_none(field == value)

    @staticmethod
    def update(id, data):
        query = Personaje.update(**data).where(Personaje.id_personaje == id)
        return query.execute()

    @staticmethod
    def delete(id):
        query = Personaje.delete().where(Personaje.id_personaje == id)
        return query.execute()

    #Metodos adicionales
    @staticmethod 
    # Insercion Multiple, encapsular el metodo en with db.atomic():
    def insertar_personajes(lista_dicc):
        # Opción 1: insertar uno a uno
        for data in lista_dicc:
            Personaje.create(**data)
        # Opción 2 (más eficiente):
            # Personaje.insert_many(lista_dicc).execute()

    @staticmethod
    def ingesta_multiple(db, lista_tuplas):
        with db.atomic():
            Personaje.insert_many(lista_tuplas, fields=[
                Personaje.nombre,
                Personaje.descripcion,
                Personaje.id_ubicacion,
                Personaje.da_objeto,
                Personaje.rol
            ]).execute()

    """ EJEMPLO DE DATOS - lista_tuplas
    datos = [
        ("Mario", "Fontanero", 1, False, "Héroe"),
        ("Bowser", "Rey de los Koopas", 2, True, "Villano")
    ]
    PersonajeRepo.ingesta_multiple(datos)

    
    """
    
    @staticmethod 
    # Obtener todos los registros de la tabla
    def listar():
        objetos = Personaje.select()
        for o in objetos:
            print(f" {o.nombre}")
        return list(Personaje.select())
    
    @staticmethod 
    # Comprueba si existe una Personaje por su nombre
    def existe(nom: str):
        return Personaje.select().where(Personaje.nombre == nom).exists()
    
 