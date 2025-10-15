# Guía de Instalación - LibroNova

## 🚀 Instalación Rápida

### Prerrequisitos
- Java 11 o superior
- MySQL 8.0 o superior
- Maven 3.6 o superior

### Pasos de Instalación

1. **Clonar el repositorio**
```bash
git clone <repository-url>
cd Libro-Nova
```

2. **Configurar la base de datos**
```bash
# Crear la base de datos
mysql -u root -p -e "CREATE DATABASE libronova CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# Importar el esquema
mysql -u root -p libronova < database/schema.sql
```

3. **Configurar la aplicación**
Editar `src/main/resources/config.properties`:
```properties
db.url=jdbc:mysql://localhost:3306/libronova?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
db.username=tu_usuario
db.password=tu_contraseña
```

4. **Ejecutar la aplicación**
```bash
# Opción 1: Usar el script de ejecución
./run.sh

# Opción 2: Usar Maven directamente
mvn clean compile
mvn exec:java
```

## 🔧 Configuración Detallada

### Base de Datos MySQL

1. **Instalar MySQL**
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install mysql-server

# CentOS/RHEL
sudo yum install mysql-server

# macOS con Homebrew
brew install mysql
```

2. **Iniciar MySQL**
```bash
sudo systemctl start mysql
sudo systemctl enable mysql
```

3. **Configurar usuario**
```bash
sudo mysql_secure_installation
```

### Java Development Kit (JDK)

1. **Instalar OpenJDK 11**
```bash
# Ubuntu/Debian
sudo apt install openjdk-11-jdk

# CentOS/RHEL
sudo yum install java-11-openjdk-devel

# macOS con Homebrew
brew install openjdk@11
```

2. **Configurar JAVA_HOME**
```bash
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk
export PATH=$JAVA_HOME/bin:$PATH
```

### Apache Maven

1. **Instalar Maven**
```bash
# Ubuntu/Debian
sudo apt install maven

# CentOS/RHEL
sudo yum install maven

# macOS con Homebrew
brew install maven
```

2. **Verificar instalación**
```bash
mvn -version
```

## 🧪 Verificación de la Instalación

### 1. Verificar Prerrequisitos
```bash
java -version
mysql --version
mvn -version
```

### 2. Ejecutar Pruebas
```bash
mvn test
```

### 3. Compilar el Proyecto
```bash
mvn clean compile
```

### 4. Ejecutar la Aplicación
```bash
mvn exec:java
```

## 🐛 Solución de Problemas

### Error: "Java no encontrado"
```bash
# Verificar instalación
which java
java -version

# Configurar PATH si es necesario
export PATH=/usr/lib/jvm/java-11-openjdk/bin:$PATH
```

### Error: "MySQL no encontrado"
```bash
# Verificar estado del servicio
sudo systemctl status mysql

# Iniciar MySQL si es necesario
sudo systemctl start mysql
```

### Error: "Maven no encontrado"
```bash
# Verificar instalación
which mvn
mvn -version

# Instalar si es necesario
sudo apt install maven  # Ubuntu/Debian
```

### Error de Conexión a Base de Datos
1. Verificar que MySQL esté ejecutándose
2. Comprobar credenciales en `config.properties`
3. Verificar que la base de datos `libronova` existe
4. Verificar permisos del usuario de MySQL

### Error de Compilación
```bash
# Limpiar y recompilar
mvn clean compile

# Verificar dependencias
mvn dependency:resolve
```

## 📁 Estructura de Archivos

```
Libro-Nova/
├── src/
│   ├── main/
│   │   ├── java/com/libronova/
│   │   └── resources/config.properties
│   └── test/java/com/libronova/
├── database/
│   └── schema.sql
├── pom.xml
├── README.md
├── INSTALACION.md
└── run.sh
```

## 🔐 Configuración de Seguridad

### Base de Datos
1. Cambiar contraseñas por defecto
2. Crear usuario específico para la aplicación
3. Configurar firewall si es necesario

### Aplicación
1. Configurar credenciales en `config.properties`
2. Revisar logs en `app.log`
3. Configurar respaldos de base de datos

## 📊 Monitoreo

### Logs de la Aplicación
- Archivo: `app.log`
- Niveles: INFO, WARNING, ERROR, DEBUG

### Base de Datos
- Monitorear conexiones activas
- Revisar logs de MySQL
- Configurar respaldos automáticos

## 🚀 Despliegue en Producción

### Consideraciones
1. Configurar base de datos en servidor dedicado
2. Configurar logging centralizado
3. Implementar respaldos automáticos
4. Configurar monitoreo de la aplicación
5. Configurar SSL/TLS para conexiones

### Variables de Entorno
```bash
export LIBRONOVA_DB_URL="jdbc:mysql://servidor:3306/libronova"
export LIBRONOVA_DB_USER="usuario_produccion"
export LIBRONOVA_DB_PASSWORD="contraseña_segura"
```

## 📞 Soporte

Para problemas de instalación o configuración:
1. Revisar este documento
2. Consultar el README.md principal
3. Revisar logs de la aplicación
4. Contactar al equipo de desarrollo

---

**LibroNova v1.0.0** - Guía de Instalación
