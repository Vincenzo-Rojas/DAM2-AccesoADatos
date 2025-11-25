from Modelos.Enemigos_Objetos_Model import EnemigoObjetoModel
from Modelos.Base_Model import BaseModel

from Enemigos import Enemigo
from Objetos import Objeto

class EnemigoObjeto(BaseModel):
    id_enemigo = ForeignKeyField(Enemigo, backref='objeto_de_enemigo', on_delete='CASCADE')
    id_objeto = ForeignKeyField(Objeto, backref='ubicacion_objeto', on_delete='CASCADE')
    probabilidad = FloatField(default=1.0, null=False)

"""Maneja todas las operaciones con perros guía"""
    @staticmethod
    def create_perro_guia(fecha_nac, sexo, raza, nivel_entrenamiento, institucion):
        return PerroGuia.create(
            fecha_nac = fecha_nac,
            sexo = sexo,
            raza = raza,
            nivel_entrenamiento = nivel_entrenamiento,
            institucion = institucion
        )
    
    @staticmethod
    def ingesta_multiple(lista_tuplas):
        with db.atomic():
            PerroGuia.insert_many(lista_tuplas, fields=[
                PerroGuia.fecha_nac,
                PerroGuia.sexo,
                PerroGuia.raza,
                PerroGuia.nivel_entrenamiento,
                PerroGuia.institucion
            ]).execute()

    @staticmethod
    def consulta_todos():
        return PerroGuia.select()