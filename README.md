# LibroNova - Sistema de Gestión de Bibliotecas

##  Descripción del Proyecto

LibroNova es un sistema de gestión de bibliotecas desarrollado en Java SE que permite administrar libros, miembros, usuarios y préstamos con control de roles y validaciones de negocio. El sistema está diseñado con una arquitectura en capas que incluye persistencia con JDBC, manejo de excepciones, logging y pruebas unitarias.

##  Objetivos del Proyecto

- Desarrollar una aplicación Java SE funcional aplicando Programación Orientada a Objetos (POO)
- Implementar un sistema de gestión de bibliotecas con control sobre libros, usuarios, miembros y préstamos
- Aplicar buenas prácticas de desarrollo con arquitectura en capas
- Documentar correctamente la aplicación con un README.md profesional
- Implementar persistencia con JDBC y PreparedStatement
- Manejar excepciones con mensajes al usuario y logs en archivo
- Crear pruebas unitarias con JUnit 5
- Exportar reportes en formato CSV

##  Arquitectura del Sistema

El proyecto sigue una arquitectura en capas (Layered Architecture) con las siguientes capas:

```
┌─────────────────────────────────────┐
│           Presentation Layer        │
│         (LibroNovaUI)              │
├─────────────────────────────────────┤
│            Service Layer            │
│    (BookService, MemberService,    │
│           LoanService)             │
├─────────────────────────────────────┤
│            Data Access Layer       │
│    (BookDAO, MemberDAO, LoanDAO)   │
├─────────────────────────────────────┤
│            Model Layer              │
│    (Book, User, Member, Loan)      │
├─────────────────────────────────────┤
│         Database Layer              │
│         (MySQL Database)           │
└─────────────────────────────────────┘
```

##  Características Principales

### Gestión de Libros
-  CRUD completo de libros
-  Validación de ISBN único
-  Control de stock disponible
-  Categorización de libros
- Búsqueda por título, autor o ISBN

### Gestión de Miembros
-  CRUD completo de miembros
-  Tipos de membresía (REGULAR, PREMIUM, VIP)
-  Control de límites de préstamos por tipo
-  Validación de edad mínima (16 años)
-  Validación de email

### Gestión de Préstamos
-  Realización de préstamos con validaciones
-  Control de stock disponible
-  Control de límites de préstamos por miembro
-  Cálculo automático de multas por retraso
-  Devolución de libros
-  Seguimiento de préstamos vencidos

### Reportes y Exportación
-  Exportación de catálogo de libros a CSV
- Exportación de préstamos a CSV
-  Reportes completos del sistema
-  Estadísticas de préstamos

### Seguridad y Validaciones
-  Manejo de excepciones personalizadas
-  Logging completo de operaciones
-  Validaciones de negocio
-  Transacciones para operaciones críticas

##  Tecnologías Utilizadas

- **Java 17** - Lenguaje de programación
- **Maven** - Gestión de dependencias
- **MySQL 8.0** - Base de datos
- **JDBC** - Conexión a base de datos
- **JUnit 5** - Pruebas unitarias
- **SLF4J + Logback** - Logging
- **Swing (JOptionPane)** - Interfaz de usuario

##  Requisitos del Sistema

### Requisitos Mínimos
- Java 17 o superior
- MySQL 8.0 o superior
- Maven 3.6 o superior
- 2 GB RAM
- 1 GB espacio en disco

### Dependencias
- MySQL Connector Java 8.0.33
- JUnit Jupiter 5.9.2
- SLF4J API 2.0.7
- Logback Classic 1.4.8

##  Instalación y Configuración

### 1. Clonar el Repositorio
```bash
git clone <repository-url>
cd Libro-Nova
```

### 2. Configurar la Base de Datos
1. Crear la base de datos MySQL:
```sql
CREATE DATABASE libronova CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. Ejecutar el script de creación:
```bash
mysql -u root -p libronova < database/schema.sql
```

### 3. Configurar la Aplicación
Editar el archivo `src/main/resources/config.properties`:
```properties
# Configuración de la base de datos
db.url=jdbc:mysql://localhost:3306/libronova?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
db.username=tu_usuario
db.password=tu_contraseña
```

### 4. Compilar y Ejecutar
```bash
# Compilar el proyecto
mvn clean compile

# Ejecutar las pruebas
mvn test

# Ejecutar la aplicación
mvn exec:java -Dexec.mainClass="com.libronova.LibroNovaApplication"
```

##  Uso del Sistema

### Menú Principal
Al ejecutar la aplicación, se presenta un menú principal con las siguientes opciones:

1. **Gestión de Libros**
   - Agregar libro
   - Buscar libro
   - Listar libros
   - Actualizar libro
   - Eliminar libro

2. **Gestión de Miembros**
   - Agregar miembro
   - Buscar miembro
   - Listar miembros
   - Actualizar miembro
   - Eliminar miembro

3. **Gestión de Préstamos**
   - Realizar préstamo
   - Devolver libro
   - Buscar préstamo
   - Listar préstamos activos
   - Listar préstamos vencidos
   - Estadísticas

4. **Reportes**
   - Exportar catálogo de libros
   - Exportar préstamos

5. **Configuración**
   - Ver configuración del sistema

### Flujo de Trabajo Típico

1. **Configuración Inicial**
   - Crear usuarios del sistema
   - Agregar libros al catálogo
   - Registrar miembros

2. **Operaciones Diarias**
   - Realizar préstamos
   - Procesar devoluciones
   - Gestionar multas
   - Generar reportes

3. **Mantenimiento**
   - Actualizar información de libros
   - Gestionar miembros
   - Revisar préstamos vencidos

##  Pruebas

El proyecto incluye pruebas unitarias completas para todas las capas del sistema:

```bash
# Ejecutar todas las pruebas
mvn test

# Ejecutar pruebas específicas
mvn test -Dtest=BookServiceTest
mvn test -Dtest=MemberServiceTest
mvn test -Dtest=LoanServiceTest
```

### Cobertura de Pruebas
-  Validaciones de negocio
-  Cálculo de multas
-  Control de stock
-  Límites de préstamos
-  Validaciones de datos

##  Estructura del Proyecto

```
Libro-Nova/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/libronova/
│   │   │       ├── config/
│   │   │       │   └── DatabaseConfig.java
│   │   │       ├── dao/
│   │   │       │   ├── BookDAO.java
│   │   │       │   ├── MemberDAO.java
│   │   │       │   └── LoanDAO.java
│   │   │       ├── exception/
│   │   │       │   ├── LibroNovaException.java
│   │   │       │   ├── BookNotFoundException.java
│   │   │       │   ├── MemberNotFoundException.java
│   │   │       │   ├── InsufficientStockException.java
│   │   │       │   └── MemberLimitExceededException.java
│   │   │       ├── model/
│   │   │       │   ├── Book.java
│   │   │       │   ├── User.java
│   │   │       │   ├── Member.java
│   │   │       │   └── Loan.java
│   │   │       ├── service/
│   │   │       │   ├── BookService.java
│   │   │       │   ├── MemberService.java
│   │   │       │   └── LoanService.java
│   │   │       ├── ui/
│   │   │       │   └── LibroNovaUI.java
│   │   │       ├── util/
│   │   │       │   ├── Logger.java
│   │   │       │   └── CSVExporter.java
│   │   │       └── LibroNovaApplication.java
│   │   └── resources/
│   │       └── config.properties
│   └── test/
│       └── java/
│           └── com/libronova/
│               └── service/
│                   ├── BookServiceTest.java
│                   ├── MemberServiceTest.java
│                   └── LoanServiceTest.java
├── database/
│   └── schema.sql
├── pom.xml
└── README.md
```

##  Configuración Avanzada

### Variables de Entorno
```bash
export LIBRONOVA_DB_URL="jdbc:mysql://localhost:3306/libronova"
export LIBRONOVA_DB_USER="usuario"
export LIBRONOVA_DB_PASSWORD="contraseña"
```

### Configuración de Logging
El sistema genera logs en el archivo `app.log` con los siguientes niveles:
- INFO: Operaciones normales
- WARNING: Advertencias
- ERROR: Errores del sistema
- DEBUG: Información detallada

### Configuración de Reportes
Los reportes se exportan en el directorio `./reports/` con formato CSV.

##  Solución de Problemas

### Problemas Comunes

1. **Error de conexión a la base de datos**
   - Verificar que MySQL esté ejecutándose
   - Comprobar credenciales en `config.properties`
   - Verificar que la base de datos `libronova` existe

2. **Error de compilación**
   - Verificar que Java 11 esté instalado
   - Ejecutar `mvn clean compile`

3. **Error de pruebas**
   - Verificar que la base de datos esté configurada
   - Ejecutar `mvn test` para ver detalles

### Logs de Error
Revisar el archivo `app.log` para obtener información detallada sobre errores.


**LibroNova v1.0.0** - Sistema de Gestión de Bibliotecas