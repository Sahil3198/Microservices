# Define the root directory (current directory)
$rootDir = Get-Location

# Define the folder to image name mapping
$folderImageMapping = @(
    @{ "folderName" = "ServiceRegistry"; "imageName" = "service-registry" },
    @{ "folderName" = "ConfigServer"; "imageName" = "config-server" },
    @{ "folderName" = "ApiGateway"; "imageName" = "api-gateway" },
    @{ "folderName" = "UserService"; "imageName" = "user-service" },
    @{ "folderName" = "AuthenticationService"; "imageName" = "authentication-service" }
)

# Loop through the folder-image mapping array
foreach ($mapping in $folderImageMapping) {
    $originalName = $mapping.folderName
    $imageName = $mapping.imageName

    Write-Host "Processing $originalName as $imageName"

    # Check if pom.xml exists in the directory
    $dirPath = Join-Path $rootDir $originalName
    if (Test-Path "$dirPath\pom.xml") {
        Write-Host "pom.xml found for $originalName"

        # Check if Dockerfile exists in the directory
        if (Test-Path "$dirPath\Dockerfile") {
            Write-Host "Dockerfile found for $originalName"

            Set-Location $dirPath

            # First, run Maven clean install with the prod profile
            Write-Host "Running mvn clean install for $originalName with application-prod.yml..."
            mvn clean install "-DskipTests" "-Dspring.profiles.active=prod"

            # Check if Maven build was successful before proceeding to Docker build
            if ($?) {
                Write-Host "Maven build successful. Proceeding with Docker build..."

                # Build Docker image
                Write-Host "Building Docker image for $originalName..."
                $dockerImageName = "sahilparekh/$($imageName):latest"
                docker build -t $dockerImageName .

                # Push Docker image to the registry
                Write-Host "Pushing Docker image for $originalName..."
                docker push $dockerImageName
            } else {
                Write-Host "Maven build failed for $originalName. Skipping Docker build and push."
            }

            Set-Location $rootDir
        }
        else {
            Write-Host "Skipping $originalName (Dockerfile not found)"
        }
    }
    else {
        Write-Host "Skipping $originalName (pom.xml not found)"
    }
}

Write-Host "All services processed."
