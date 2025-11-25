from peewee import *
from Base_Model import BaseModel
from Ubicaciones import Ubicacion

class Enemigo(BaseModel):
    id_enemigo = AutoField()
    nombre = TextField(null=False)
    descripcion = TextField(null=True)
    nivel = IntegerField(null=True, constraints=[Check("nivel >= 1")])
    id_ubicacion = ForeignKeyField(Ubicacion, backref='ubicacion_enemigo')
    drop_objetos = BooleanField(null=False, default=False)  # True o False

