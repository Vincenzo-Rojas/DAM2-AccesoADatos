from peewee import * # type: ignore
import envyte # type: ignore

db = PostgresqlDatabase(
    envyte.get("DATABASE"),
    host=envyte.get("HOST"),
    port=int(envyte.get("PORT")),
    user=envyte.get("USER"),
    password=envyte.get("PASSWORD")
)
try:
    db.connect()
    print("Conexión exitosa")
except Exception as e:
    print("ERROR", e)

def inicializar_base(tablas, reiniciar=True):
    if reiniciar:
        db.drop_tables(tablas, safe=True)
        db.create_tables(tablas, safe=True)
    else:
        db.create_tables(tablas, safe=True)