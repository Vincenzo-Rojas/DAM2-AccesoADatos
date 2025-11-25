from peewee import *
from .Base_Model import BaseModel

class Ubicacion(BaseModel):
    id_ubicacion = AutoField()
    nombre = TextField(null=False)
    descripcion = TextField(null=True)
    tipo = TextField(null=True)
    