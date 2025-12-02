from conexion import db
from peewee import * # type: ignore
from playhouse.postgres_ext import BinaryJSONField # type: ignore
from Modelos.BaseModel import BaseModel


class Usuario(BaseModel):
    #id no hace falta, lo hace automatico
    username = CharField(max_length=150,unique=True)
    email = TextField(unique=True)
    profile = BinaryJSONField() # en este caso es binario, indexable

    def __str__(self):
        return f"Username: {self.username}, Email: {self.email}, Profile: {self.profile}"