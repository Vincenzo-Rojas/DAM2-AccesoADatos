# DAM2-AccesoADatos
Repositorio destinado a la práctica y desarrollo de ejercicios de la asignatura Acceso a Datos, del ciclo de Desarrollo de Aplicaciones Multiplataforma (DAM)

# Metodos del Git
git update-UT1 : actualiza la rama profesor-UT1-java
git update-py : actualiza la rama profesor-python

# Ejecutar entero
    git config --global alias.update-UT1 "!git checkout upstream-profesor-aad-main && git fetch profesor-aad && git reset --hard profesor-aad/main && git push --force origin upstream-profesor-aad-main"

    git config --global alias.update-py "!git checkout AAD-profesor-python-main && git fetch profesor-aad && git reset --hard profesor-aad/main && git push --force origin AAD-profesor-python-main"
