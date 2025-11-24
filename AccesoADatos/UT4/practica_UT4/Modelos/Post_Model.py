from conexion import db
from peewee import * # type: ignore
from playhouse.postgres_ext import BinaryJSONField # type: ignore
from Modelos.BaseModel import BaseModel
from Modelos.Usuario_Model import Usuario

class Post(BaseModel):
    #id no hace falta, lo hace automatico
    author = ForeignKeyField(Usuario, backref='autor_post', null=False, on_delete='CASCADE') #FK usuario
    text = TextField()
    media = BinaryJSONField()
    stats = BinaryJSONField()
    created_at = DateTimeField()
