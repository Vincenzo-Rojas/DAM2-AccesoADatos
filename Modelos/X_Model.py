from conexion import db
from peewee import * # type: ignore
from Modelos.BaseModel import BaseModel

class X(BaseModel):
    #id no hace falta, lo hace automatico
    
    
    class Meta:
        indexes = (
            # create a unique on user/post
            #(('user', 'post'), True),

        )
