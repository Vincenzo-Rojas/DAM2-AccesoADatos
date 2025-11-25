from peewee import *
from .Base_Model import BaseModel
from .Ubicaciones_Model import Ubicacion

class EnemigoModel(BaseModel):
    id_enemigo = AutoField()
    nombre = TextField(null=False)
    descripcion = TextField(null=True)
    nivel = IntegerField(default=1, constraints=[Check("nivel >= 1")])
    id_ubicacion = ForeignKeyField(Ubicacion, backref='ubicacion_enemigo', null=True, on_delete='SET NULL')
    drop_objetos = BooleanField(default=False)  # True o False

