# Gestión de Parcelas - API

Sistema de gestión agrícola para control de parcelas, cultivos y ejercicios.

## Requisitos

- Java 8
- Apache Maven 3.6+
- Oracle Database (o modificar para usar H2 en memoria para desarrollo)

## Instalación de dependencias

Ejecutar como **Administrador**:

```powershell
.\install-dev.ps1
```

Este script instalará:
- OpenJDK 8
- Apache Maven 3.8.8

Después de ejecutar, **cerrar y abrir una nueva terminal**.

## Configuración

### Base de datos

Editar `src/main/resources/application.properties` para configurar la conexión a Oracle:

```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:XE
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
```

### Puerto

Por defecto usa el puerto **8081**. Cambiar en `application.properties`:

```properties
server.port=8081
```

## Ejecución

```bash
cd gestion-parcelas-api
mvn spring-boot:run
```

La aplicación estará disponible en: http://localhost:8081

## Endpoints

| Sección | URL |
|---------|-----|
| Página principal | http://localhost:8081/ |
| Parcelas | http://localhost:8081/parcelas |
| Ejercicios | http://localhost:8081/ejercicios |
| Cultivos | http://localhost:8081/cultivos |
| Detalles | http://localhost:8081/detalles |
| Consola H2 | http://localhost:8081/h2-console |

## API REST

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | /api/parcelas | Listar parcelas |
| POST | /api/parcelas | Crear parcela |
| PUT | /api/parcelas/{id} | Actualizar parcela |
| DELETE | /api/parcelas/{id} | Eliminar parcela |
| GET | /api/ejercicios | Listar ejercicios |
| POST | /api/ejercicios | Crear ejercicio |
| DELETE | /api/ejercicios/{id} | Eliminar ejercicio |
| GET | /api/cultivos | Listar cultivos |
| POST | /api/cultivos | Crear cultivo |
| PUT | /api/cultivos/{id} | Actualizar cultivo |
| DELETE | /api/cultivos/{id} | Eliminar cultivo |
| GET | /api/detalles | Listar detalles (matriz parcela × ejercicio) |
| GET | /api/detalles?ejercicio=X | Filtrar detalles por ejercicio |
| POST | /api/detalles | Asignar cultivo a parcela/ejercicio |
| DELETE | /api/detalles | Eliminar detalle |

## Estructura del proyecto

```
gestion-parcelas-api/
├── src/main/java/com/campos/gestionparcelas/
│   ├── controller/     # Controladores REST
│   ├── model/
│   │   ├── entity/    # Entidades JPA
│   │   ├── dto/        # Objetos de transferencia
│   │   └── repository/ # Repositorios JPA
│   ├── service/       # Lógica de negocio
│   └── GestionParcelasApplication.java
├── src/main/resources/
│   ├── application.properties  # Configuración
│   └── templates/     # Plantillas HTML (Thymeleaf)
├── src/test/         # Tests
├── pom.xml           # Dependencias Maven
└── install-dev.ps1   # Script de instalación
```

## Construir para producción

```bash
mvn clean package
```

El JAR se generará en `target/gestion-parcelas-1.0.0.jar`

## Notas sobre Detalles

- La tabla `detalles` representa una relación many-to-many entre parcelas, ejercicios y cultivos
- Clave primaria: (parcela_id, ejercicio_id, cultivo_id)
- La pantalla de detalles muestra una matriz parcelas × ejercicios
- Se detectan automáticamente si una parcela tiene el mismo cultivo en 3+ ejercicios consecutivos (monocultivo)
