@echo off
setlocal

cd /d "%~dp0"

if not exist out\com\progmemorymatch\MemoryMatchApp.class (
    echo Build output not found. Running compile first...
    call "%~dp0compile_game.cmd"
    if errorlevel 1 exit /b 1
)

java -cp out com.progmemorymatch.MemoryMatchApp
