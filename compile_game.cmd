@echo off
setlocal

cd /d "%~dp0"

if not exist src\com\progmemorymatch\MemoryMatchApp.java (
    echo [ERROR] Source files not found.
    echo Make sure this script is inside the project root.
    exit /b 1
)

if not exist out mkdir out

javac -d out src\com\progmemorymatch\MemoryMatchApp.java src\com\progmemorymatch\audio\*.java src\com\progmemorymatch\model\*.java src\com\progmemorymatch\ui\*.java
if errorlevel 1 (
    echo.
    echo [FAILED] Compile error.
    exit /b 1
)

echo.
echo [OK] Compile successful.
exit /b 0
