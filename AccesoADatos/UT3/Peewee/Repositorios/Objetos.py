from peewee import *
from Base_Model import BaseModel

from Ubicaciones import Ubicacion
from Personajes import Personaje
from Enemigos import Enemigo

class Objeto(BaseModel):
    id_objeto = AutoField()
    nombre = TextField(null=False)
    descripcion = TextField(null=True)
    rareza = TextField(null=True, default="comun",constraints=[Check("rareza IN (comun, raro, epico, legendario)")])
    id_ubicacion = ForeignKeyField(Ubicacion, backref='ubicacion_objeto')
    id_personaje_dropea = ForeignKeyField(Personaje, backref='personaje_dropea')
    id_enemigo_dropea = ForeignKeyField(Enemigo, backref='enemigo_dropea')

