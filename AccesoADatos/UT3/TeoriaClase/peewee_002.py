from peewee import *
from datetime import datetime
import os
import sys

if len(sys.argv) != 2:
    sys.exit()

name_db = sys.argv(2)

db = SqliteDatabase(name_db)
db.connect()

class BaseModel(Model):
    class Meta:
        database = db

class Coche (BaseModel):
    matricula = CharField(primary_key=True,max_length=7, constraints = [Check("matricula LIKE '_______'")])
    num_bastidor = TextField(unique=True)
    marca = TextField(constraints = [Check("marca LIKE '__%'")])
    modelo = TextField()
    fecha_fab = DateField(default=datetime.now().date(), constraints=[Check("julianday()")])
    num_bastidor = IntegerField(default=0)