from peewee import *
from datetime import datetime

#print(datetime.now())
db = SqliteDatabase("Jovellanos.db")
db.connect()

class Alumno(Model):
    nia = TextField(primary_key=True)
    nombre = TextField()
    fecha_nac = DateField()
    curso = IntegerField(constraints = [Check("curso BETWEEN 1 AND 2")])
    
    class Meta:
        database = db

db.drop_tables([Alumno], safe=True)
db.create_tables([Alumno], safe=True)

try:
    #alumno = Alumno(nia="1A", nombre="Jairo", fecha_nac="2000-10-26", curso=4)
    #alumno.save()
    alumno = Alumno.create(nia="5454", nombre="jose", fecha_nac="2000-01-18", curso=1)
    print(alumno)
    print(Alumno.select())
    
except:
    print("Error")