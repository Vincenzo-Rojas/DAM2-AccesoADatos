# DAM2-AccesoADatos
Repositorio destinado a la práctica y desarrollo de ejercicios de la asignatura Acceso a Datos, del ciclo de Desarrollo de Aplicaciones Multiplataforma (DAM)

Acceso a datos - UT1 - datos binarios, texto, java.nio/io.file y xml crear entorno virtual en local

AAD - UT3 - entorno virtual y pip install peewee

AAD - UT4 - venv + pip install psycopg2 o psycopg2-binary + pip install peewee + pip install envyte + .env(datosimportantes.txt) + pip install playhouse + supabase(https://supabase.com/)

# Metodos del Git
git update-UT1-java : actualiza la rama profesor-UT1-java

git update-py : actualiza la rama profesor-python

# Ejecutar entero
    git config --global alias.update-UT1-java "!git checkout profesor-UT1-java && git fetch origin && git reset --hard origin/profesor-UT1-java && git push --force origin profesor-UT1-java"

    git config --global alias.update-py "!f() { \
  REMOTE_NAME=profesor; \
  REMOTE_URL=https://github.com/Josemedvel/acceso-a-datos-python-25-26.git; \
  LOCAL_BRANCH=profesor-python; \
  REMOTE_BRANCH=main; \
  if ! git remote | grep -q $REMOTE_NAME; then \
    git remote add $REMOTE_NAME $REMOTE_URL; \
  fi; \
  git fetch $REMOTE_NAME --prune; \
  git checkout -B $LOCAL_BRANCH $REMOTE_NAME/$REMOTE_BRANCH; \
  git push origin $LOCAL_BRANCH --force; \
}; f"