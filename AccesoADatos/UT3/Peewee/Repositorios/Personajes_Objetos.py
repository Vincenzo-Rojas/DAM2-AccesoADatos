from peewee import *
from Base_Model import BaseModel

from Personajes import Personaje
from Objetos import Objeto

class Enemigo_Objeto(BaseModel):
    id_personaje = ForeignKeyField(Personaje, backref='objeto_de_enemigo', on_delete='CASCADE')
    id_objeto = ForeignKeyField(Objeto, backref='ubicacion_objeto', on_delete='CASCADE')
    cantidad = FloatField(default=1, null=True)
