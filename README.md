# 🧾 Proyecto Cuotas-Partes - Backend Pensionados

Este proyecto corresponde al backend del sistema para la gestión de cuotas partes de pensionados de la Universidad del Cauca. Está desarrollado en **Java 17** utilizando **Spring Boot 3.4.4**, **MySQL**, **JPA/Hibernate**, y sigue una arquitectura en capas.

---

## 🔧 1. Herramientas necesarias

Asegúrate de instalar y configurar las siguientes herramientas para ejecutar correctamente el proyecto:

| Herramienta       | Versión Recomendada | Enlace de descarga |
|-------------------|---------------------|--------------------|
| **Java JDK**      | 17                  | [Oracle JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) / [OpenJDK 17](https://www.openlogic.com/openjdk-downloads) |
| **Apache Maven**  | 3.8.1 o superior    | [Descargar Maven](https://maven.apache.org/download.cgi) |
| **MySQL Server**  | 8.0 o superior      | [Descargar MySQL](https://dev.mysql.com/downloads/mysql/) |
| **MySQL Workbench** | Última versión    | [Descargar Workbench](https://dev.mysql.com/downloads/workbench/) |
| **IntelliJ IDEA** | Community o Ultimate| [Descargar IntelliJ](https://www.jetbrains.com/idea/download/) |
| **VisualStudioCode**| Última versión    | [Descargar VisualStudioCode]( https://code.visualstudio.com/) |
| **Git**           | Última versión      | [Descargar Git](https://git-scm.com/downloads) |
| **Postman**       | Última versión      | [Descargar Postman](https://www.postman.com/downloads/) |

---

## ⚙️ 2. Configuración del entorno

### 2.1 Verificar instalación de Java

```bash
java -version
```
Debe devoler algo como:
```bash
java version "17.x.x"
```

### 2.2 Verificar instalación de Maven
```bash
mvn -version
```
### 2.3 Verificar instalación de MySQL
```bash
mysql -u root -p
```
## 🗃️ 3. Creación de la base de datos
1. Abre MySQL Workbench o la consola de MySQL.
2. Ejecuta el siguiente comando:
   ```sql
   CREATE DATABASE cuotapartes_pensionados_db;
   ```
3. Asegúrate de que el usuario root tenga acceso a esta base de datos.
4. En el archivo src/main/resources/application.properties, la configuración de conexión es la siguiente:
   ```properties
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.datasource.url=jdbc:mysql://localhost:3306/cuotapartes_pensionados_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    spring.datasource.username=usuario
    spring.datasource.password=contraseña
    
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.generate-ddl=true
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.format_sql=true
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
    spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
    spring.jpa.defer-datasource-initialization=true
    
    spring.sql.init.mode=always
    spring.sql.init.encoding=UTF-8
    spring.sql.init.schema-locations=classpath:schema.sql
    spring.sql.init.data-locations=classpath:data.sql
    
    spring.flyway.enabled=false
    logging.level.org.hibernate.SQL=debug
    logging.level.org.springframework.security=DEBUG
    logging.level.org.springframework.web=INFO
    logging.level.org.hibernate=ERROR
    logging.level.com.gestionpensiones=DEBUG

## 🔁 4. Clonar el repositorio
```bash
git clone https://github.com/Cuotas-Partes/back-pensionados.git
```
## ▶️ 5. Pasos para ejecutar

  1. Abre IntelliJ IDEA o Visual Studio Code y selecciona File > Open, elige la carpeta del proyecto.

  2. Espera a que Maven descargue todas las dependencias automáticamente.
  
  3. Asegúrate de que el proyecto esté usando JDK 17 (File > Project Structure > Project > SDK).
  
  4. Asegúrate de tener MySQL corriendo con la base de datos creada.
  
  5. Ejecuta la clase principal que contiene la anotación @SpringBootApplication.
  
  6. La aplicación se ejecutará en el puerto 8080 (por defecto).
  
  7. Puedes probar los endpoints utilizando Postman o algún cliente REST.











