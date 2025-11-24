from peewee import *
from Modelos.Ubicaciones_Model import Ubicacion


class UbicacionRepo:
     #Asignar campos de la tabla, formato para introducir en create y update, no necesario
    def asignarData(nom: str, des: str, typ: str):
        return {"nombre":nom, "descripcion": des, "tipo":typ}

    #CRUD
    @staticmethod
    def create(data):
        return Ubicacion.create(**data)
    
    @staticmethod
    def createP(nom: str, des: str, typ: str):
        return Ubicacion.create(nombre=nom, descripcion=des, tipo=typ)

    @staticmethod
    def get_by_id(id):
        #.get_or_none(): forma segura de obtener un único registro de la db sin provocar un error si no existe.
        return Ubicacion.get_or_none(Ubicacion.id_ubicacion == id)
    
    #buscar por cualquier campo
    @staticmethod
    def get_by(field_name: str, value):
        field = getattr(Ubicacion, field_name)
        return Ubicacion.get_or_none(field == value)

    @staticmethod
    def update(id, data):
        query = Ubicacion.update(**data).where(Ubicacion.id_ubicacion == id)
        return query.execute()

    @staticmethod
    def delete(id):
        query = Ubicacion.delete().where(Ubicacion.id_ubicacion == id)
        return query.execute()

    #Metodos adicionales
    
    @staticmethod 
    # Insercion Multiple, encapsular el metodo en with db.atomic():
    def insertar_ubicaciones(lista_dicc):
        # Opción 1: insertar uno a uno
        for data in lista_dicc:
            Ubicacion.create(**data)
        # Opción 2 (más eficiente):
            # Ubicacion.insert_many(lista_dicc).execute()
    
    @staticmethod
    def ingesta_multiple(db, lista_dicc):
        """
        Inserta varias ubicaciones de golpe usando insert_many con fields explícitos
        y encapsulado en db.atomic().
        """
        if lista_dicc:
            with db.atomic():
                Ubicacion.insert_many(lista_dicc, fields=[
                    Ubicacion.nombre,
                    Ubicacion.descripcion,
                    Ubicacion.tipo
                ]).execute()
    
    @staticmethod 
    # Obtener todos los registros de la tabla
    def listar():
        objetos = Ubicacion.select()
        for o in objetos:
            print(f" {o.nombre}")
        return list(Ubicacion.select())
    
    @staticmethod 
    # Comprueba si existe una ubicacion por su nombre
    def existe(nom: str):
        return Ubicacion.select().where(Ubicacion.nombre == nom).exists()
    
 