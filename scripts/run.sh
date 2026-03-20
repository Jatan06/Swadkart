#!/bin/bash
echo "=== Swadkart - Food Ordering System ==="

# Check if db.properties exists
if [ ! -f "db.properties" ]; then
    echo ""
    echo "[ERROR] db.properties not found!"
    echo "Please copy db.properties.example to db.properties and fill in your MySQL credentials."
    echo ""
    exit 1
fi

# Compile all Java files
echo "Compiling..."
mkdir -p bin
javac -cp "lib/*" -d bin \
    src/Main.java \
    src/Menus/*.java \
    src/Services/*.java \
    src/Dao/*.java \
    src/Db/*.java \
    src/Constants/*.java \
    src/Models/*.java \
    src/Admin/*.java \
    src/Ds/*.java \
    src/Session/*.java \
    src/Utils/*.java

if [ $? -ne 0 ]; then
    echo "[ERROR] Compilation failed!"
    exit 1
fi

# Run the application
echo "Running..."
echo ""
java -cp "bin:lib/*" Main
