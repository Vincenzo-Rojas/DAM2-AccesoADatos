from peewee import *
from conexion import db

class BaseModel(Model):
    class Meta:
        database = db
