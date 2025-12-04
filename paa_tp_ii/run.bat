@echo off
REM Script para compilar e executar o projeto Knapsack
REM Uso: run.bat [opcao]
REM   (sem opcao) - Compilar e executar
REM   runonly - Apenas executar (sem recompilação)
REM   clean - Limpar arquivos compilados

setlocal enabledelayedexpansion

cd /d "%~dp0"

if "%1"=="clean" (
    echo Limpando...
    if exist bin rmdir /s /q bin
    if exist results rmdir /s /q results
    echo Concluido!
    goto :eof
)

if "%1"=="runonly" (
    if not exist bin (
        echo Erro: bin nao existe. Execute sem parametros primeiro.
        exit /b 1
    )
    goto :run
)

REM Compilacao
echo.
 echo ==========================================
 echo  Compilando Projeto Knapsack
 echo ==========================================
 echo.

if not exist bin mkdir bin

echo Compilando todas as classes Java...
echo.

REM Encontrar todos os arquivos .java
set "javac_cmd=javac -d bin -encoding UTF-8"

for /r "src\main\java" %%F in (*.java) do (
    set "javac_cmd=!javac_cmd! "%%F""
)

echo Executando compilação...
%javac_cmd% 2>&1

if errorlevel 1 (
    echo Erro durante compilacao!
    exit /b 1
)

echo.
 echo === Compilacao concluida com sucesso! ===
 echo Classes em: bin\
 echo.

:run
echo.
 echo ==========================================
 echo  Executando Projeto Knapsack
 echo ==========================================
 echo.

java -cp bin paa.knapsack.Main

echo.
 echo ==========================================
 echo  Resultados em: results\
 echo ==========================================