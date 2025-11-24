from peewee import *
from .Base_Model import BaseModel
from .Ubicaciones_Model import Ubicacion

class Personaje(BaseModel):
    id_personaje = AutoField()
    nombre = TextField(null=False)
    descripcion = TextField(null=True)
    id_ubicacion = ForeignKeyField(Ubicacion, backref='ubicacion_personaje', null=True, on_delete='SET NULL')
    da_objeto = BooleanField(default=False)  # True o False
    rol = TextField(null=True)

