from peewee import fn
from data_base import db
from ingesta import ingesta

from Modelos.Ubicaciones_Model import Ubicacion
from Modelos.Personajes_Model import Personaje
from Modelos.Enemigos_Model import EnemigoModel
from Modelos.Objetos_Model import ObjetoModel


from Repositorios.Ubicaciones import UbicacionRepo
from Repositorios.Personajes import PersonajeRepo
# (añadirás aquí más repositorios después)

def main():
    # Conexión a la base de datos
    db.connect()

    #INGESTA DE DATOS CON DB
    ingesta(db)


    # MAIN DE UBICACIONES
    """
    existeB=UbicacionRepo.existe("Bosque Oscuro")
    print(existeB)
    #query en ubicaciones
    query = (
        Ubicacion
        .select(Ubicacion.nombre)
        .where(Ubicacion.nombre.contains("ciu"))
    )
    resultado = query.execute()

    for r in resultado:
        print("hola",r.nombre)
    
    # crear registro en ubicacion
    datos = UbicacionRepo.asignarData("castillo", "Antigua fortaleza legendaria", "urbano")
    creado = UbicacionRepo.create(datos)
    print(creado)

    crearN = UbicacionRepo.createP("castillo32", "Antigua fortaleza legendaria23", "urbano23")
    print(crearN)

    #listar ubicaciones
    UbicacionRepo.listar()

    # Consultar por id
    ubicacion = UbicacionRepo.get_by_id(1)
    if ubicacion:
        print(f"Ubicación encontrada: {ubicacion.nombre}")
    else:
        print("No se encontró la ubicación con ID 1.")

    # Actualizar
    ubicacion = UbicacionRepo.get_by_id(3)
    print(f"Ubicación encontrada pre acta: {ubicacion.descripcion}")

    UbicacionRepo.update(3, {"descripcion": "Capital del reino"})
    ubicacion = UbicacionRepo.get_by_id(3)
    print(f"Ubicación actualizada: {ubicacion.descripcion}")

    # Eliminar
    UbicacionRepo.delete(3)
    print("Ubicación con ID 3 eliminada.\n")
        

    #CONSULTA BACKREF DE UBICACION A PERSONAJES: Desde Ubicacion -> Personajes usando backref
    ciudadela = Ubicacion.get(Ubicacion.nombre == "Ciudadela")
    print(f"\nPersonajes en {ciudadela.nombre} (usando backref):")
    for p in ciudadela.ubicacion_personaje:  # backref definido en el modelo
        print(f"- {p.nombre} ({p.rol})")
    """
    
    

    # MAIN PERSONAJES
    
    """ METODOS PERSONAJE
    
    # --- CREAR registros ---
    print("Creando personajes individualmente...")
    p1_data = PersonajeRepo.asignarData("Aria", "Mercader amigable", 1, True, "mercader")
    p1 = PersonajeRepo.create(p1_data)
    
    p2 = PersonajeRepo.createP("Borin", "Guía NPC", 2, False, "NPC")
    
    print(f"Personajes creados: {p1.nombre}, {p2.nombre}\n")
    
    # --- LISTAR ---
    print("Listando personajes actuales:")
    PersonajeRepo.listar()
    
    # --- GET BY ID ---
    print("\nConsulta por ID:")
    personaje = PersonajeRepo.get_by_id(p1.id_personaje)
    if personaje:
        print(f"Personaje con ID {p1.id_personaje}: {personaje.nombre}")
    
    # --- GET BY campo genérico ---
    print("\nConsulta por nombre usando get_by:")
    personaje = PersonajeRepo.get_by("nombre", "Borin")
    if personaje:
        print(f"Personaje encontrado: {personaje.nombre}, rol: {personaje.rol}, id: {personaje.id_personaje }")
    
    # --- UPDATE ---
    print("\nActualizando descripción de Borin...")
    PersonajeRepo.update(p2.id_personaje, {"descripcion": "NPC guía del bosque"})
    
    personaje = PersonajeRepo.get_by_id(p2.id_personaje)
    print(f"Actualización: {personaje.nombre} -> {personaje.descripcion}")
    
    # --- EXISTE ---
    existe = PersonajeRepo.existe("Aria")
    print(f"\nExiste Aria? {'Sí' if existe else 'No'}")
    
    # --- INGESTA MULTIPLE ---
    print("\nInsertando múltiples personajes con ingesta_multiple...")
    datos = [
        ("Ciri", "Comerciante de rarezas", 1, True, "mercader"),
        ("Dandelion", "Bardo errante", 2, False, "NPC")
    ]
    PersonajeRepo.ingesta_multiple(db, datos)
    
    print("Listando personajes después de ingesta_multiple:")
    PersonajeRepo.listar()
    
    # --- DELETE ---
    print("\nEliminando Borin...")
    PersonajeRepo.delete(p2.id_personaje)
    print("Listando personajes finales:")
    PersonajeRepo.listar()
    """
    
    """ Personaje Consultas

    print("\n--- Consultas SQL usando Peewee ---\n")

    # SELECT + WHERE simple (lazy)
    print("1. Personajes con rol 'mercader':")
    query1 = Personaje.select().where(Personaje.rol == "mercader")
    for p in query1:
        print(p.nombre)

    # GET (único registro)
    print("\n2. Obtener Personaje con id 2:")
    p2 = Personaje.get(Personaje.id_personaje == 2)
    print(p2.nombre, "-", p2.descripcion)

    # WHERE con BETWEEN
    print("\n3. Personajes con id entre 2 y 4:")
    query3 = Personaje.select().where(Personaje.id_personaje.between(2, 4))
    for p in query3:
        print(p.nombre)

    # WHERE con IN y NOT IN
    print("\n4. Personajes en id IN [1,3,5]:")
    query4 = Personaje.select().where(Personaje.id_personaje.in_([1,3,5]))
    for p in query4:
        print(p.nombre)

    print("\nPersonajes en id NOT IN [1,3,5]:")
    query4b = Personaje.select().where(Personaje.id_personaje.not_in([1,3,5]))
    for p in query4b:
        print(p.nombre)

    # WHERE con LIKE
    print("\n5. Personajes cuyo nombre contiene 'i':")
    query5 = Personaje.select().where(Personaje.nombre ** "%i%")
    for p in query5:
        print(p.nombre)
    
    #Forma 2
    query5b = (Personaje.select().where(Personaje.nombre.contains("i")))
    for p in query5b:
        print(p.nombre)

    # WHERE con AND / OR
    print("\n6. Personajes con id > 1 AND rol='NPC':")
    query6 = Personaje.select().where((Personaje.id_personaje > 1) & (Personaje.rol == "NPC"))
    for p in query6:
        print(p.nombre, p.id_personaje, p.rol)

    print("\nPersonajes con id < 3 OR rol='villano':")
    query6b = Personaje.select().where((Personaje.id_personaje < 3) | (Personaje.rol == "villano"))
    for p in query6b:
        print(p.nombre, p.id_personaje, p.rol)

    # ORDER BY + LIMIT
    print("\n7. Personajes ordenados por nombre descendente, limit 3:")
    query7 = Personaje.select().order_by(Personaje.nombre.desc()).limit(3)
    for p in query7:
        print(p.nombre)

    # DISTINCT
    print("\n8. Distintos roles de personajes:")
    query8 = Personaje.select(Personaje.rol).distinct()
    for p in query8:
        print(p.rol)

    # AGREGACIONES con fn
    print("\n9. Conteo de personajes por rol:")
    query9 = (
        Personaje
        .select(Personaje.rol, fn.COUNT(Personaje.id_personaje).alias("total"))
        .group_by(Personaje.rol)
    )
    for row in query9:
        print(row.rol, "-", row.total)

    # HAVING
    print("\nRoles con más de 1 personaje (HAVING):")
    query9b = (
        Personaje
        .select(Personaje.rol, fn.COUNT(Personaje.id_personaje).alias("total"))
        .group_by(Personaje.rol)
        .having(fn.COUNT(Personaje.id_personaje) > 1)
    )
    for row in query9b:
        print(row.rol, "-", row.total)

    
    """



    # Cerrar conexión
    db.close()
    print("Conexión cerrada correctamente.")

if __name__ == "__main__":
    main()
