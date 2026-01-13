#!/bin/bash

echo "=============================="
echo "BeerReal - Local Deployment"
echo "=============================="
echo

# Check if .env exists
if [ ! -f .env ]; then
    echo "ERROR: .env file not found!"
    echo "Please copy .env.example to .env and configure your settings:"
    echo "    cp .env.example .env"
    echo
    exit 1
fi

# Source environment variables
set -a
source .env
set +a

# Check if JWT_SECRET is default
if grep -q "your-base64-encoded-secret-here" .env 2>/dev/null; then
    echo "WARNING: Default JWT_SECRET detected!"
    echo "Generating a secure secret..."
    NEW_SECRET=$(openssl rand -base64 32)
    if [[ "$OSTYPE" == "darwin"* ]]; then
        # macOS
        sed -i '' "s|JWT_SECRET=.*|JWT_SECRET=$NEW_SECRET|" .env
    else
        # Linux
        sed -i "s|JWT_SECRET=.*|JWT_SECRET=$NEW_SECRET|" .env
    fi
    echo "JWT_SECRET has been updated in .env"
    echo
fi

# Check for Docker
if ! command -v docker &> /dev/null; then
    echo "ERROR: Docker is not installed."
    echo "Please install Docker from https://docker.com"
    exit 1
fi

# Check if Docker is running
if ! docker info &> /dev/null; then
    echo "ERROR: Docker is not running."
    echo "Please start Docker and try again."
    exit 1
fi

echo "Starting BeerReal services..."
echo "This may take several minutes on first run."
echo

docker-compose up --build -d

if [ $? -ne 0 ]; then
    echo
    echo "ERROR: Failed to start services."
    echo "Check the logs with: docker-compose logs"
    exit 1
fi

echo
echo "Waiting for services to start..."
sleep 30

echo
echo "Checking service status..."
docker-compose ps

echo
echo "=============================="
echo "BeerReal is starting up!"
echo "=============================="
echo
echo "Frontend: http://localhost:3000"
echo "Backend:  http://localhost:8080"
echo
echo "Commands:"
echo "  View logs:    docker-compose logs -f"
echo "  Stop:         docker-compose down"
echo "  Restart:      docker-compose restart"
echo
