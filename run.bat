@echo off
echo === Swadkart - Food Ordering System ===

REM Check if db.properties exists
IF NOT EXIST db.properties (
    echo.
    echo [ERROR] db.properties not found!
    echo Please copy db.properties.example to db.properties and fill in your MySQL credentials.
    echo.
    pause
    exit /b 1
)

REM Compile all Java files
echo Compiling...
javac -cp "lib/*" -d bin src\Main.java src\Menus\*.java src\Services\*.java src\Dao\*.java src\Db\*.java src\Constants\*.java src\Models\*.java src\Admin\*.java src\Ds\*.java src\Session\*.java src\Utils\*.java

IF %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Compilation failed!
    pause
    exit /b 1
)

REM Run the application
echo Running...
echo.
java -cp "bin;lib/*" Main

pause
