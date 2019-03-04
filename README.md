# Autor Jose Ortiz

fecha 4/03/2019

## Requerimentos

```bash
apache maven
java version 1.8
smbd mysql 
```

##Configuracion BD

```
Se debe modificar el archivo application.properties
 ubicado en la ruta /src/main/resources/config/application.properties
 en las propiedades
#spring.datasource.username = root
#spring.datasource.password =geocom
para configurar la base de datos
 ```
 
##Despliegue

```posicionarse en la raiz del proyecto y abrir una consola, 
ejecutar los comando 
--mvn clean
--mvn install
```
Estos comandos generaran los recursos necesarios para el despliegue de la aplicación.

```
Ejecutar los comandos
mvn spring-boot:run
```
con este comando se inician los servicios de la aplicación.
