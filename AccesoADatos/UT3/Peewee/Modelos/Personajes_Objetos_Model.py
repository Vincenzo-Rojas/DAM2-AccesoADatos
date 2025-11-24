from peewee import *
from .Base_Model import BaseModel

from .Personajes_Model import Personaje
from .Objetos_Model import ObjetoModel

class PersonajeObjetoModel(BaseModel):
    id_personaje = ForeignKeyField(Personaje, backref='objeto_de_enemigo', on_delete='CASCADE')
    id_objeto = ForeignKeyField(ObjetoModel, backref='ubicacion_objeto', on_delete='CASCADE')
    cantidad = FloatField(default=1, null=True)
