package gr.fetrax.security.auth.controller;

import gr.fetrax.security.auth.AuthenticationRequest;
import gr.fetrax.security.auth.AuthenticationResponse;
import gr.fetrax.security.auth.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "APIs for user registration and authentication (JWT-based)")
public interface AuthenticationController {

    @Operation(
            summary = "Register New User",
            description = """
                    Registers a new user in the system and returns a JWT authentication token.
                    
                    **Registration Process:**
                    1. Creates a new user account with the provided credentials
                    2. Encrypts the password using BCrypt
                    3. Assigns default USER role
                    4. Generates and returns a JWT token
                    
                    **Required Fields:**
                    - `firstName`: User's first name
                    - `lastName`: User's last name
                    - `email`: Valid email address (used as username)
                    - `password`: Secure password
                    
                    **Example Request:**
                    ```json
                    {
                      "firstName": "John",
                      "lastName": "Doe",
                      "email": "john.doe@example.com",
                      "password": "SecurePassword123!"
                    }
                    ```
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User registered successfully, JWT token returned",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid registration data (e.g., email already exists)",
                    content = @Content
            )
    })
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request);

    @Operation(
            summary = "Authenticate User",
            description = """
                    Authenticates an existing user and returns a JWT token for API access.
                    
                    **Authentication Process:**
                    1. Validates user credentials (email and password)
                    2. Generates a new JWT token upon successful authentication
                    3. Returns the token for use in subsequent API calls
                    
                    **How to Use the Token:**
                    1. Copy the returned token value
                    2. Click the "Authorize" button at the top of this page
                    3. Paste the token and click "Authorize"
                    4. All subsequent requests will include the authentication token
                    
                    **Example Request:**
                    ```json
                    {
                      "email": "john.doe@example.com",
                      "password": "SecurePassword123!"
                    }
                    ```
                    
                    **Token Format:**
                    The token should be included in request headers as:
                    ```
                    Authorization: Bearer <your-token-here>
                    ```
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Authentication successful, JWT token returned",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication failed - Invalid credentials",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request format",
                    content = @Content
            )
    })
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request);
}
