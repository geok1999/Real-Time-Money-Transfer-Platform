package bank.money.transfer.controllers;

import bank.money.transfer.domain.dto.Account;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequestMapping("/api/v1")
@Tag(
        name = "Account Management",
        description = "Complete REST API for managing bank accounts with support for create, read, update operations. "
)
@SecurityRequirement(name = "bearerAuth")
public interface AccountController {

    @Operation(
            summary = "Create or Update Account",
            description = """
                    Creates a new bank account or updates an existing one.
                    
                    - If the account ID doesn't exist, a new account is created (HTTP 201)
                    - If the account ID exists, the account is updated (HTTP 200)
                    
                    **Required Fields:**
                    - `id`: Positive account identifier
                    - `balance`: Non-negative balance amount
                    - `currency`: Valid currency code (USD, EUR, GBP, TRY)
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "✅ Account created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Account.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Success - New EUR Account",
                                            summary = "Successfully created EUR account",
                                            value = """
                                                    {
                                                      "id": 1,
                                                      "balance": 5000.50,
                                                      "currency": "EUR"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "200",
                    description = "✅ Account updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Account.class),
                            examples = @ExampleObject(
                                    name = "Success - Updated Balance",
                                    summary = "Balance successfully updated",
                                    value = """
                                            {
                                              "id": 1,
                                              "balance": 1500.00,
                                              "currency": "EUR"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "❌ Invalid input data - Validation failed",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Error - Negative Balance",
                                            summary = "Balance cannot be negative",
                                            value = """
                                                    {
                                                      "timestamp": "2024-11-02T10:30:00",
                                                      "status": 400,
                                                      "error": "Bad Request",
                                                      "message": "Balance cannot be negative",
                                                      "path": "/api/accounts/account/1"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Error - Invalid Currency",
                                            summary = "Currency code not supported",
                                            value = """
                                                    {
                                                      "timestamp": "2024-11-02T10:30:00",
                                                      "status": 400,
                                                      "error": "Bad Request",
                                                      "message": "Invalid currency type: EURO. Choose one of the following types: USD, EUR, GBP, TRY",
                                                      "path": "/api/accounts/account/1"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Error - Missing Required Field",
                                            summary = "Balance field is required",
                                            value = """
                                                    {
                                                      "timestamp": "2024-11-02T10:30:00",
                                                      "status": 400,
                                                      "error": "Bad Request",
                                                      "message": "Balance is required",
                                                      "path": "/api/accounts/account/1"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Error - Invalid Account ID",
                                            summary = "Account ID must be positive",
                                            value = """
                                                    {
                                                      "timestamp": "2024-11-02T10:30:00",
                                                      "status": 400,
                                                      "error": "Bad Request",
                                                      "message": "Account ID must be positive",
                                                      "path": "/api/accounts/account/-1"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "❌ Unauthorized - Authentication required",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Error - Missing Token",
                                            summary = "JWT token not provided",
                                            value = """
                                                    {
                                                      "timestamp": "2024-11-02T10:30:00",
                                                      "status": 401,
                                                      "error": "Unauthorized",
                                                      "message": "Full authentication is required to access this resource",
                                                      "path": "/api/accounts/account/1"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Error - Invalid Token",
                                            summary = "JWT token is invalid or expired",
                                            value = """
                                                    {
                                                      "timestamp": "2024-11-02T10:30:00",
                                                      "status": 401,
                                                      "error": "Unauthorized",
                                                      "message": "Invalid JWT token",
                                                      "path": "/api/accounts/account/1"
                                                    }
                                                    """
                                    )
                            }
                    )
            )
    })
    @PostMapping(path = "/account/{id}")
    public ResponseEntity<Account> createOrUpdateAccount(@PathVariable final Long id, @Valid @RequestBody final Account account);

    @Operation(
            summary = "Get Account by ID",
            description = "Retrieves detailed information about a specific account by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "✅ Account found and returned successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Account.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Success - USD Account",
                                            summary = "Found USD account with balance",
                                            value = """
                                                    {
                                                      "id": 1,
                                                      "balance": 1000.00,
                                                      "currency": "USD",
                                                      "createdAt": "2024-11-02T10:30:00",
                                                      "version": 0
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Success - EUR Account",
                                            summary = "Found EUR account with large balance",
                                            value = """
                                                    {
                                                      "id": 2,
                                                      "balance": 50000.75,
                                                      "currency": "EUR",
                                                      "createdAt": "2024-11-01T15:20:00",
                                                      "version": 5
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Success - Zero Balance",
                                            summary = "Account with zero balance",
                                            value = """
                                                    {
                                                      "id": 3,
                                                      "balance": 0.00,
                                                      "currency": "GBP",
                                                      "createdAt": "2024-11-02T09:00:00",
                                                      "version": 0
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "❌ Account not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Error - Account Not Found",
                                    summary = "No account exists with this ID",
                                    value = """
                                            {
                                              "timestamp": "2024-11-02T10:30:00",
                                              "status": 404,
                                              "error": "Not Found",
                                              "message": "Account with ID 999 not found",
                                              "path": "/api/accounts/account/999"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "❌ Unauthorized - JWT token required",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Error - Unauthorized",
                                    summary = "Missing or invalid authentication",
                                    value = """
                                            {
                                              "timestamp": "2024-11-02T10:30:00",
                                              "status": 401,
                                              "error": "Unauthorized",
                                              "message": "Full authentication is required to access this resource",
                                              "path": "/api/accounts/account/1"
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping(path = "/account/{id}")
    public ResponseEntity<Account> findAccount(@PathVariable final Long id);

    @Operation(
            summary = "List All Accounts",
            description = "Retrieves a list of all bank accounts in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "✅ Successfully retrieved account list",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Account.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Success - Multiple Accounts",
                                            summary = "List of 3 accounts in different currencies",
                                            value = """
                                                    [
                                                      {
                                                        "id": 1,
                                                        "balance": 1000.00,
                                                        "currency": "USD",
                                                        "createdAt": "2024-11-02T10:30:00",
                                                        "version": 2
                                                      },
                                                      {
                                                        "id": 2,
                                                        "balance": 5000.50,
                                                        "currency": "EUR",
                                                        "createdAt": "2024-11-02T10:31:00",
                                                        "version": 1
                                                      },
                                                      {
                                                        "id": 3,
                                                        "balance": 2500.00,
                                                        "currency": "GBP",
                                                        "createdAt": "2024-11-02T10:32:00",
                                                        "version": 0
                                                      }
                                                    ]
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Success - Single Account",
                                            summary = "List with only one account",
                                            value = """
                                                    [
                                                      {
                                                        "id": 1,
                                                        "balance": 1000.00,
                                                        "currency": "USD",
                                                        "createdAt": "2024-11-02T10:30:00",
                                                        "version": 0
                                                      }
                                                    ]
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Success - Empty List",
                                            summary = "No accounts in system",
                                            value = "[]"
                                    ),
                                    @ExampleObject(
                                            name = "Success - Large Dataset",
                                            summary = "Multiple accounts with various balances",
                                            value = """
                                                    [
                                                      {
                                                        "id": 1,
                                                        "balance": 10000.00,
                                                        "currency": "USD",
                                                        "createdAt": "2024-11-01T08:00:00",
                                                        "version": 15
                                                      },
                                                      {
                                                        "id": 2,
                                                        "balance": 0.00,
                                                        "currency": "EUR",
                                                        "createdAt": "2024-11-01T09:00:00",
                                                        "version": 0
                                                      },
                                                      {
                                                        "id": 3,
                                                        "balance": 99999.99,
                                                        "currency": "GBP",
                                                        "createdAt": "2024-11-01T10:00:00",
                                                        "version": 3
                                                      },
                                                      {
                                                        "id": 4,
                                                        "balance": 500.25,
                                                        "currency": "TRY",
                                                        "createdAt": "2024-11-02T11:00:00",
                                                        "version": 1
                                                      }
                                                    ]
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "❌ Unauthorized - Authentication required",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Error - Unauthorized",
                                    summary = "Missing or invalid JWT token",
                                    value = """
                                            {
                                              "timestamp": "2024-11-02T10:30:00",
                                              "status": 401,
                                              "error": "Unauthorized",
                                              "message": "Full authentication is required to access this resource",
                                              "path": "/api/accounts/account"
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping(path = "/account")
    public ResponseEntity<List<Account>> ListAllAccounts();

}
