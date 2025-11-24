from conexion import db
from peewee import * # type: ignore
from Modelos.BaseModel import BaseModel
from Modelos.Usuario_Model import Usuario
from Modelos.Post_Model import Post

class Like(BaseModel):
    #id no hace falta, lo hace automatico
    user = ForeignKeyField(Usuario, backref='autor_like', null=False, on_delete='CASCADE') #FK usuario
    post = ForeignKeyField(Post, backref='like_post', null=False, on_delete='CASCADE') #FK usuario
    
    class Meta:
        indexes = (
            # create a unique on user/post
            (('user', 'post'), True),
        )