package bank.money.transfer.controllers;


import bank.money.transfer.domain.dto.Transaction;
import bank.money.transfer.services.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Tag(
        name = "Transaction Processing",
        description = "Complete REST API for executing and managing financial transactions between accounts. " +
                "Supports real-time money transfers with comprehensive validation, concurrency handling, "

)
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(final TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(
            summary = "Execute Money Transfer",
            description = """
                    Executes a money transfer between two accounts with comprehensive validation.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "✅ Transaction completed successfully - Money transferred",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Success - USD Transfer",
                                            summary = "Successfully transferred 100.50 USD",
                                            value = """
                                                    {
                                                      "Message: ": "Transaction was completed successfully",
                                                      "Transaction: ": {
                                                        "id": 1,
                                                        "sourceAccountId": 1,
                                                        "targetAccountId": 2,
                                                        "amount": 100.50,
                                                        "currency": "USD",
                                                        "createdAt": "2024-11-02T10:35:00"
                                                      }
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Success - EUR Transfer",
                                            summary = "Successfully transferred 500.00 EUR",
                                            value = """
                                                    {
                                                      "Message: ": "Transaction was completed successfully",
                                                      "Transaction: ": {
                                                        "id": 2,
                                                        "sourceAccountId": 3,
                                                        "targetAccountId": 4,
                                                        "amount": 500.00,
                                                        "currency": "EUR",
                                                        "createdAt": "2024-11-02T10:36:00"
                                                      }
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Success - Large GBP Transfer",
                                            summary = "Successfully transferred 10,000.00 GBP",
                                            value = """
                                                    {
                                                      "Message: ": "Transaction was completed successfully",
                                                      "Transaction: ": {
                                                        "id": 3,
                                                        "sourceAccountId": 5,
                                                        "targetAccountId": 6,
                                                        "amount": 10000.00,
                                                        "currency": "GBP",
                                                        "createdAt": "2024-11-02T10:37:00"
                                                      }
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Success - Small TRY Transfer",
                                            summary = "Successfully transferred 0.01 TRY (minimum amount)",
                                            value = """
                                                    {
                                                      "Message: ": "Transaction was completed successfully",
                                                      "Transaction: ": {
                                                        "id": 4,
                                                        "sourceAccountId": 7,
                                                        "targetAccountId": 8,
                                                        "amount": 0.01,
                                                        "currency": "TRY",
                                                        "createdAt": "2024-11-02T10:38:00"
                                                      }
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "❌ Bad Request - Transaction validation failed",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Error - Insufficient Balance",
                                            summary = "Source account doesn't have enough funds",
                                            value = """
                                                    {
                                                      "Error: ": "Source has Insufficient balance!"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Error - Same Account Transfer",
                                            summary = "Cannot transfer money to the same account",
                                            value = """
                                                    {
                                                      "Error: ": "Source and Target account can't be the same!"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Error - Source Account Not Found",
                                            summary = "Source account doesn't exist in system",
                                            value = """
                                                    {
                                                      "Error: ": "Source account not found"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Error - Target Account Not Found",
                                            summary = "Target account doesn't exist in system",
                                            value = """
                                                    {
                                                      "Error: ": "Target account not found"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Error - Currency Mismatch (Accounts)",
                                            summary = "Source and target accounts have different currencies",
                                            value = """
                                                    {
                                                      "Error: ": "Currency Source and Currency Target must be the same!"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Error - Currency Mismatch (Transaction)",
                                            summary = "Transaction currency doesn't match account currencies",
                                            value = """
                                                    {
                                                      "Error: ": "Currency Source and Currency Target must match with the Transaction Currency!"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Error - Invalid Amount (Negative)",
                                            summary = "Transfer amount cannot be negative",
                                            value = """
                                                    {
                                                      "timestamp": "2024-11-02T10:30:00",
                                                      "status": 400,
                                                      "error": "Bad Request",
                                                      "message": "Amount must be at least 0.01",
                                                      "path": "/api/v1/transaction"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Error - Invalid Amount (Too Large)",
                                            summary = "Transfer amount exceeds maximum allowed",
                                            value = """
                                                    {
                                                      "timestamp": "2024-11-02T10:30:00",
                                                      "status": 400,
                                                      "error": "Bad Request",
                                                      "message": "Amount cannot exceed 999,999.99",
                                                      "path": "/api/v1/transaction"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Error - Invalid Amount Format",
                                            summary = "Amount has too many decimal places",
                                            value = """
                                                    {
                                                      "timestamp": "2024-11-02T10:30:00",
                                                      "status": 400,
                                                      "error": "Bad Request",
                                                      "message": "Amount must have at most 6 digits before decimal and 2 after",
                                                      "path": "/api/v1/transaction"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Error - Missing Required Field",
                                            summary = "Required field not provided in request",
                                            value = """
                                                    {
                                                      "timestamp": "2024-11-02T10:30:00",
                                                      "status": 400,
                                                      "error": "Bad Request",
                                                      "message": "Amount is required",
                                                      "path": "/api/v1/transaction"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Error - Invalid Currency Code",
                                            summary = "Currency code not supported",
                                            value = """
                                                    {
                                                      "timestamp": "2024-11-02T10:30:00",
                                                      "status": 400,
                                                      "error": "Bad Request",
                                                      "message": "Invalid currency type: XXX\\nChoose one of the following types: USD, EUR, GBP, TRY",
                                                      "path": "/api/v1/transaction"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Error - Concurrent Modification",
                                            summary = "Transaction failed due to concurrent update",
                                            value = """
                                                    {
                                                      "Error: ": "Transaction failed due to a concurrent modification. Please try again later."
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
                                            name = "Error - Missing JWT Token",
                                            summary = "No authentication token provided",
                                            value = """
                                                    {
                                                      "timestamp": "2024-11-02T10:30:00",
                                                      "status": 401,
                                                      "error": "Unauthorized",
                                                      "message": "Full authentication is required to access this resource",
                                                      "path": "/api/v1/transaction"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Error - Invalid JWT Token",
                                            summary = "Token is invalid or expired",
                                            value = """
                                                    {
                                                      "timestamp": "2024-11-02T10:30:00",
                                                      "status": 401,
                                                      "error": "Unauthorized",
                                                      "message": "Invalid JWT token",
                                                      "path": "/api/v1/transaction"
                                                    }
                                                    """
                                    )
                            }
                    )
            )
    })
    @PostMapping(path = "/transaction")
    public ResponseEntity<?> createTransaction(@RequestBody @Valid final Transaction transaction){
        try{
            final Transaction newTransaction=transactionService.createNewTransaction(transaction);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("Message: ","Transaction was completed successfully","Transaction: ",newTransaction));
        }catch (IllegalArgumentException | AccountNotFoundException e){
            return ResponseEntity.badRequest().body(Map.of("Error: ",e.getMessage()));
        }
    }

    @Operation(
            summary = "List All Transactions",
            description = """
                    Retrieves a complete history of all transactions in the system.
                    Useful for audit logs and transaction history tracking.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "✅ Successfully retrieved transaction history",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Transaction.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Success - Multiple Transactions",
                                            summary = "List of 3 transactions in different currencies",
                                            value = """
                                                    [
                                                      {
                                                        "id": 1,
                                                        "sourceAccountId": 1,
                                                        "targetAccountId": 2,
                                                        "amount": 100.50,
                                                        "currency": "USD",
                                                        "createdAt": "2024-11-02T10:35:00"
                                                      },
                                                      {
                                                        "id": 2,
                                                        "sourceAccountId": 3,
                                                        "targetAccountId": 4,
                                                        "amount": 500.00,
                                                        "currency": "EUR",
                                                        "createdAt": "2024-11-02T10:36:00"
                                                      },
                                                      {
                                                        "id": 3,
                                                        "sourceAccountId": 5,
                                                        "targetAccountId": 6,
                                                        "amount": 2500.00,
                                                        "currency": "GBP",
                                                        "createdAt": "2024-11-02T10:37:00"
                                                      }
                                                    ]
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Success - Single Transaction",
                                            summary = "Only one transaction in history",
                                            value = """
                                                    [
                                                      {
                                                        "id": 1,
                                                        "sourceAccountId": 1,
                                                        "targetAccountId": 2,
                                                        "amount": 100.50,
                                                        "currency": "USD",
                                                        "createdAt": "2024-11-02T10:35:00"
                                                      }
                                                    ]
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Success - Empty History",
                                            summary = "No transactions have been executed yet",
                                            value = "[]"
                                    ),
                                    @ExampleObject(
                                            name = "Success - Large History",
                                            summary = "Multiple transactions showing various patterns",
                                            value = """
                                                    [
                                                      {
                                                        "id": 1,
                                                        "sourceAccountId": 1,
                                                        "targetAccountId": 2,
                                                        "amount": 1000.00,
                                                        "currency": "USD",
                                                        "createdAt": "2024-11-02T08:00:00"
                                                      },
                                                      {
                                                        "id": 2,
                                                        "sourceAccountId": 2,
                                                        "targetAccountId": 3,
                                                        "amount": 500.00,
                                                        "currency": "USD",
                                                        "createdAt": "2024-11-02T09:00:00"
                                                      },
                                                      {
                                                        "id": 3,
                                                        "sourceAccountId": 4,
                                                        "targetAccountId": 5,
                                                        "amount": 2500.50,
                                                        "currency": "EUR",
                                                        "createdAt": "2024-11-02T10:00:00"
                                                      },
                                                      {
                                                        "id": 4,
                                                        "sourceAccountId": 6,
                                                        "targetAccountId": 7,
                                                        "amount": 0.01,
                                                        "currency": "TRY",
                                                        "createdAt": "2024-11-02T11:00:00"
                                                      },
                                                      {
                                                        "id": 5,
                                                        "sourceAccountId": 1,
                                                        "targetAccountId": 3,
                                                        "amount": 99999.99,
                                                        "currency": "GBP",
                                                        "createdAt": "2024-11-02T12:00:00"
                                                      }
                                                    ]
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Success - Same Account Transactions",
                                            summary = "Multiple transactions involving same accounts",
                                            value = """
                                                    [
                                                      {
                                                        "id": 1,
                                                        "sourceAccountId": 1,
                                                        "targetAccountId": 2,
                                                        "amount": 100.00,
                                                        "currency": "USD",
                                                        "createdAt": "2024-11-02T10:00:00"
                                                      },
                                                      {
                                                        "id": 2,
                                                        "sourceAccountId": 1,
                                                        "targetAccountId": 2,
                                                        "amount": 200.00,
                                                        "currency": "USD",
                                                        "createdAt": "2024-11-02T11:00:00"
                                                      },
                                                      {
                                                        "id": 3,
                                                        "sourceAccountId": 2,
                                                        "targetAccountId": 1,
                                                        "amount": 50.00,
                                                        "currency": "USD",
                                                        "createdAt": "2024-11-02T12:00:00"
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
                            examples = {
                                    @ExampleObject(
                                            name = "Error - Unauthorized Access",
                                            summary = "Missing or invalid JWT token",
                                            value = """
                                                    {
                                                      "timestamp": "2024-11-02T10:30:00",
                                                      "status": 401,
                                                      "error": "Unauthorized",
                                                      "message": "Full authentication is required to access this resource",
                                                      "path": "/api/v1/LogTransaction"
                                                    }
                                                    """
                                    )
                            }
                    )
            )
    })
    @GetMapping(path = "/LogTransaction")
    public ResponseEntity<List<Transaction>> listAllTransactions(){
        return new ResponseEntity<>(transactionService.listTransactions(),HttpStatus.OK);
    }

}
