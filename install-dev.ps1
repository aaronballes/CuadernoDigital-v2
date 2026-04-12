# Script de instalacion para Java 17 y Maven
# Ejecutar como Administrador

$ErrorActionPreference = "Stop"

Write-Host "=== Instalando Java 17 (Eclipse Temurin) ===" -ForegroundColor Cyan

$jdkUrl = "https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.18%2B8/OpenJDK17U-jdk_x64_windows_hotspot_msi-17.0.18_8.msi"
$jdkInstaller = "$env:TEMP\OpenJDK17.msi"
$jdkInstallDir = "C:\Program Files\Eclipse Adoptium\jdk-17.0.18.8-hotspot"

# Descargar JDK 17
if (!(Test-Path $jdkInstallDir)) {
    Write-Host "Descargando JDK 17..."
    & curl.exe -L --retry 3 --retry-delay 5 -o $jdkInstaller $jdkUrl
    
    Write-Host "Instalando JDK 17 (puede pedir permisos de administrador)..."
    Start-Process msiexec.exe -ArgumentList "/i `"$jdkInstaller`" /quiet INSTALLDIR=`"$jdkInstallDir`"" -Wait -NoNewWindow
    
    Remove-Item $jdkInstaller -Force -ErrorAction SilentlyContinue
    Write-Host "JDK 17 instalado en: $jdkInstallDir" -ForegroundColor Green
} else {
    Write-Host "JDK 17 ya esta instalado" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "=== Instalando Apache Maven 3.9.14 ===" -ForegroundColor Cyan

$mavenUrl = "https://dlcdn.apache.org/maven/maven-3/3.9.14/binaries/apache-maven-3.9.14-bin.zip"
$mavenZip = "$env:TEMP\apache-maven-3.9.14-bin.zip"
$mavenInstallDir = "C:\Maven\apache-maven-3.9.14"

# Descargar Maven
if (!(Test-Path $mavenInstallDir)) {
    Write-Host "Descargando Maven..."
    & curl.exe -L --retry 3 --retry-delay 5 -o $mavenZip $mavenUrl
    
    Write-Host "Extrayendo Maven..."
    Expand-Archive -Path $mavenZip -DestinationPath "C:\Maven" -Force
    
    Remove-Item $mavenZip -Force -ErrorAction SilentlyContinue
    Write-Host "Maven instalado en: $mavenInstallDir" -ForegroundColor Green
} else {
    Write-Host "Maven ya esta instalado" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "=== Configurando Variables de Entorno ===" -ForegroundColor Cyan

# Configurar JAVA_HOME
[System.Environment]::SetEnvironmentVariable("JAVA_HOME", $jdkInstallDir, "User")
$env:JAVA_HOME = $jdkInstallDir
Write-Host "JAVA_HOME = $jdkInstallDir" -ForegroundColor Green

# Configurar MAVEN_HOME
[System.Environment]::SetEnvironmentVariable("MAVEN_HOME", $mavenInstallDir, "User")
$env:MAVEN_HOME = $mavenInstallDir
Write-Host "MAVEN_HOME = $mavenInstallDir" -ForegroundColor Green

# Agregar al PATH
$currentPath = [System.Environment]::GetEnvironmentVariable("Path", "User")
$newPath = "$mavenInstallDir\bin;$jdkInstallDir\bin"

if ($currentPath -notlike "*$mavenInstallDir*") {
    [System.Environment]::SetEnvironmentVariable("Path", "$newPath;$currentPath", "User")
    Write-Host "PATH actualizado" -ForegroundColor Green
}

Write-Host ""
Write-Host "=== Verificando instalacion ===" -ForegroundColor Cyan
Write-Host "Abre una nueva terminal y ejecuta:" -ForegroundColor Yellow
Write-Host "  java -version" -ForegroundColor White
Write-Host "  mvn -version" -ForegroundColor White
