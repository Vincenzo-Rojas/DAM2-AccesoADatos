# DAM2-AccesoADatos
Repositorio destinado a la práctica y desarrollo de ejercicios de la asignatura Acceso a Datos, del ciclo de Desarrollo de Aplicaciones Multiplataforma (DAM)

# Metodos del Git
git actualizar-profesor : actualiza la rama profesor-aad(Tema 1)

# Ejecutar entero
    git config --global alias.actualizar-profesor "!git checkout upstream-profesor-aad-main && git fetch profesor-aad && git reset --hard profesor-aad/main && git push --force origin upstream-profesor-aad-main"