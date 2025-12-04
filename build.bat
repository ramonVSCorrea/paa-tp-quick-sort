@echo off
REM Monorepo Build Helper Script
REM Usage: build.bat [command] [module]

setlocal enabledelayedexpansion

if "%1"=="" (
    echo PAA - Monorepo Build Helper
    echo.
    echo Available commands:
    echo   all          - Compile all modules
    echo   tp2          - Compile paa_tp_ii only
    echo   tp1          - Compile paa_tp_quick_sort only
    echo   clean        - Clean all modules
    echo   clean-tp2    - Clean paa_tp_ii only
    echo   clean-tp1    - Clean paa_tp_quick_sort only
    echo   package      - Package all modules
    echo   package-tp2  - Package paa_tp_ii only
    echo   package-tp1  - Package paa_tp_quick_sort only
    echo.
    goto :eof
)

if "%1"=="all" (
    echo Building all modules...
    call mvn clean compile
    goto :eof
)

if "%1"=="tp2" (
    echo Building paa_tp_ii...
    call mvn -pl paa_tp_ii clean compile
    goto :eof
)

if "%1"=="tp1" (
    echo Building paa_tp_quick_sort...
    call mvn -pl paa_tp_quick_sort clean compile
    goto :eof
)

if "%1"=="clean" (
    echo Cleaning all modules...
    call mvn clean
    goto :eof
)

if "%1"=="clean-tp2" (
    echo Cleaning paa_tp_ii...
    call mvn -pl paa_tp_ii clean
    goto :eof
)

if "%1"=="clean-tp1" (
    echo Cleaning paa_tp_quick_sort...
    call mvn -pl paa_tp_quick_sort clean
    goto :eof
)

if "%1"=="package" (
    echo Packaging all modules...
    call mvn clean package
    goto :eof
)

if "%1"=="package-tp2" (
    echo Packaging paa_tp_ii...
    call mvn -pl paa_tp_ii clean package
    goto :eof
)

if "%1"=="package-tp1" (
    echo Packaging paa_tp_quick_sort...
    call mvn -pl paa_tp_quick_sort clean package
    goto :eof
)

echo Unknown command: %1
echo Run "build.bat" with no arguments to see available commands.

