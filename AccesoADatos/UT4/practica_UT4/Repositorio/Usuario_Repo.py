from Modelos.Usuario_Model import Usuario
from conexion import db

def create_user(username: str, email: str, profile: dict = None) -> Usuario:
    """
    Crea un nuevo usuario en la base de datos.

    :param username: Nombre de usuario único
    :param email: Email único
    :param profile: Diccionario con su perfil, por defecto vacío
    :return: Instancia del usuario creado o None si falla
    """
    # Crear el usuario
    try:
        nuevo_usuario = Usuario.create(
            username=username,
            email=email,
            profile=profile
        )
        print(f"Usuario '{username}' creado correctamente.")
    except Exception as e:
        print(f"Error al crear usuario: {e}")
        nuevo_usuario = None

    return nuevo_usuario

#Obtener todos los usuarios.
def get_all_users():
    """
    Obtiene todos los usuarios de la base de datos.

    :return: Lista de instancias de Usuario
    """
    usuarios = []
    try:
        usuarios = list(Usuario.select())
        return usuarios
    except Exception as e:
        print(f"Error al obtener usuarios: {e}")
    
    