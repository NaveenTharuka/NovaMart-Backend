# PowerShell script to test Supabase connection
# Load environment variables from .env file
Get-Content .env | ForEach-Object {
    if ($_ -match '^([^=]+)=(.*)$') {
        $name = $matches[1].Trim()
        $value = $matches[2].Trim()
        # Expand variables like ${SUPABASE_DB_URL}
        $value = $ExecutionContext.InvokeCommand.ExpandString($value)
        Set-Item -Path "env:$name" -Value $value
    }
}

Write-Host "Testing Supabase Connection..." -ForegroundColor Cyan
Write-Host "URL: $env:DB_URL" -ForegroundColor Yellow
Write-Host "User: $env:DB_USERNAME" -ForegroundColor Yellow
Write-Host ""

# Extract connection details from JDBC URL
if ($env:DB_URL -match 'jdbc:postgresql://([^:]+):(\d+)/(\w+)') {
    $host = $matches[1]
    $port = $matches[2]
    $database = $matches[3]
    
    Write-Host "Parsed Connection Details:" -ForegroundColor Green
    Write-Host "  Host: $host"
    Write-Host "  Port: $port"
    Write-Host "  Database: $database"
    Write-Host "  Username: $env:DB_USERNAME"
    Write-Host ""
    
    # Test if psql is available
    $psqlExists = Get-Command psql -ErrorAction SilentlyContinue
    
    if ($psqlExists) {
        Write-Host "Testing with psql..." -ForegroundColor Cyan
        $env:PGPASSWORD = $env:DB_PASSWORD
        psql -h $host -p $port -U $env:DB_USERNAME -d $database -c "SELECT version();"
    } else {
        Write-Host "psql not found. Install PostgreSQL client or use the Java test below." -ForegroundColor Yellow
        Write-Host ""
        Write-Host "Alternative: Run your Spring Boot app to test the connection:" -ForegroundColor Cyan
        Write-Host "  ./mvnw spring-boot:run" -ForegroundColor White
    }
} else {
    Write-Host "Could not parse DB_URL. Please check your .env file." -ForegroundColor Red
}
