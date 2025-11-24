from peewee import SqliteDatabase

db = SqliteDatabase('juegos.sqlite')
#db = SqliteDatabase(':memory:')

""" forma con argumentos de consola
if len(sys.argv) != 2:
    print("Tienes que añadir el nombre de la base de datos")
    sys.exit(-1)

nombre_bd = sys.argv[1]

if os.path.exists(nombre_bd):
    print("Conectando a la base de datos...")
else:
    print("No se ha encontrado la base de datos, creándola...")

db = SqliteDatabase(nombre_bd)

"""

""" con loggers
import logging
from peewee import SqliteDatabase
from pathlib import Path



logger = logging.getLogger('peewee')
logger.addHandler(logging.StreamHandler())
logger.setLevel(logging.INFO)

BASE_DIR = Path(__file__).resolve().parent
DB_PATH = BASE_DIR / "perros.db"

if not DB_PATH.exists():
    logger.error(f"La base de datos {DB_PATH} no existe, se creará al conectar")
else:
    logger.info(f"Conectando con la base de datos {DB_PATH}")

db = SqliteDatabase(DB_PATH)
"""