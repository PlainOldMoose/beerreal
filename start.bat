@echo off
echo ==============================
echo BeerReal - Local Deployment
echo ==============================
echo.

REM Check if .env exists
if not exist .env (
    echo ERROR: .env file not found!
    echo Please copy .env.example to .env and configure your settings:
    echo     copy .env.example .env
    echo.
    pause
    exit /b 1
)

REM Check for Docker
where docker >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Docker is not installed or not in PATH.
    echo Please install Docker Desktop from https://docker.com
    pause
    exit /b 1
)

echo Checking Docker status...
docker info >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Docker is not running.
    echo Please start Docker Desktop and try again.
    pause
    exit /b 1
)

echo.
echo Starting BeerReal services...
echo This may take several minutes on first run.
echo.

docker-compose up --build -d

if %errorlevel% neq 0 (
    echo.
    echo ERROR: Failed to start services.
    echo Check the logs with: docker-compose logs
    pause
    exit /b 1
)

echo.
echo Waiting for services to start...
timeout /t 30 /nobreak >nul

echo.
echo Checking service status...
docker-compose ps

echo.
echo ==============================
echo BeerReal is starting up!
echo ==============================
echo.
echo Frontend: http://localhost:3000
echo Backend:  http://localhost:8080
echo.
echo Commands:
echo   View logs:    docker-compose logs -f
echo   Stop:         docker-compose down
echo   Restart:      docker-compose restart
echo.
pause
