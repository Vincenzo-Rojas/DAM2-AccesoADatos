from peewee import *
from .Base_Model import BaseModel

from .Ubicaciones_Model import Ubicacion
from .Personajes_Model import Personaje
from .Enemigos_Model import EnemigoModel

class ObjetoModel(BaseModel):
    id_objeto = AutoField()
    nombre = TextField(null=False)
    descripcion = TextField(null=True)
    rareza = TextField(null=True, default="comun",constraints=[Check("rareza IN ('comun', 'raro', 'epico', 'legendario')")])
    id_ubicacion = ForeignKeyField(Ubicacion, backref='ubicacion_objeto', null=True, on_delete='SET NULL')
    id_personaje_dropea = ForeignKeyField(Personaje, backref='personaje_dropea', null=True, on_delete='SET NULL')
    id_enemigo_dropea = ForeignKeyField(EnemigoModel, backref='enemigo_dropea', null=True, on_delete='SET NULL')

