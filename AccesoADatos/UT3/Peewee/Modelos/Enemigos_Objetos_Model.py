from peewee import *
from .Base_Model import BaseModel

from .Enemigos_Model import EnemigoModel
from .Objetos_Model import ObjetoModel

class EnemigoObjetoModel(BaseModel):
    id_enemigo = ForeignKeyField(EnemigoModel, backref='objeto_de_enemigo', on_delete='CASCADE')
    id_objeto = ForeignKeyField(ObjetoModel, backref='ubicacion_objeto', on_delete='CASCADE')
    probabilidad = FloatField(default=1.0, null=False)

