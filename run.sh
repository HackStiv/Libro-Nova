#!/bin/bash

# Script de ejecución para LibroNova
# Sistema de Gestión de Bibliotecas

echo "=========================================="
echo "    LibroNova - Sistema de Bibliotecas"
echo "=========================================="
echo ""

# Verificar si Java está instalado
if ! command -v java &> /dev/null; then
    echo "❌ Error: Java no está instalado o no está en el PATH"
    echo "   Por favor, instale Java 11 o superior"
    exit 1
fi

# Verificar versión de Java
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 11 ]; then
    echo "❌ Error: Se requiere Java 11 o superior"
    echo "   Versión actual: $(java -version 2>&1 | head -n 1)"
    exit 1
fi

echo "✅ Java $(java -version 2>&1 | head -n 1 | cut -d'"' -f2) detectado"

# Verificar si Maven está instalado
if ! command -v mvn &> /dev/null; then
    echo "❌ Error: Maven no está instalado o no está en el PATH"
    echo "   Por favor, instale Maven 3.6 o superior"
    exit 1
fi

echo "✅ Maven $(mvn -version | head -n 1 | cut -d' ' -f3) detectado"

# Verificar si MySQL está ejecutándose
if ! pgrep -x "mysqld" > /dev/null; then
    echo "⚠️  Advertencia: MySQL no parece estar ejecutándose"
    echo "   Asegúrese de que MySQL esté iniciado antes de ejecutar la aplicación"
fi

echo ""

# Menú de opciones
echo "Seleccione una opción:"
echo "1. Compilar el proyecto"
echo "2. Ejecutar pruebas"
echo "3. Ejecutar la aplicación"
echo "4. Compilar y ejecutar"
echo "5. Limpiar y recompilar"
echo "6. Salir"
echo ""

read -p "Ingrese su opción (1-6): " option

case $option in
    1)
        echo "🔨 Compilando el proyecto..."
        mvn clean compile
        if [ $? -eq 0 ]; then
            echo "✅ Compilación exitosa"
        else
            echo "❌ Error en la compilación"
            exit 1
        fi
        ;;
    2)
        echo "🧪 Ejecutando pruebas..."
        mvn test
        if [ $? -eq 0 ]; then
            echo "✅ Todas las pruebas pasaron"
        else
            echo "❌ Algunas pruebas fallaron"
            exit 1
        fi
        ;;
    3)
        echo "🚀 Ejecutando la aplicación..."
        mvn exec:java -Dexec.mainClass="com.libronova.LibroNovaApplication"
        ;;
    4)
        echo "🔨 Compilando y ejecutando..."
        mvn clean compile
        if [ $? -eq 0 ]; then
            echo "✅ Compilación exitosa"
            echo "🚀 Ejecutando la aplicación..."
            mvn exec:java -Dexec.mainClass="com.libronova.LibroNovaApplication"
        else
            echo "❌ Error en la compilación"
            exit 1
        fi
        ;;
    5)
        echo "🧹 Limpiando y recompilando..."
        mvn clean compile
        if [ $? -eq 0 ]; then
            echo "✅ Limpieza y compilación exitosas"
        else
            echo "❌ Error en la limpieza o compilación"
            exit 1
        fi
        ;;
    6)
        echo "👋 ¡Hasta luego!"
        exit 0
        ;;
    *)
        echo "❌ Opción inválida"
        exit 1
        ;;
esac

echo ""
echo "=========================================="
echo "    Operación completada"
echo "=========================================="
