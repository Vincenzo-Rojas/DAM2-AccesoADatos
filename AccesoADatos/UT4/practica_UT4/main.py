import json
from peewee import * # type: ignore
from conexion import inicializar_base, db
from pprint import pprint
from Modelos.Like_Model import Like
from Modelos.Post_Model import Post
from Modelos.Usuario_Model import Usuario

def main():
    tablas = [Like, Post, Usuario]

    #inicializar_base
    inicializar_base(tablas, True)


if __name__ == "__main__":
    main()