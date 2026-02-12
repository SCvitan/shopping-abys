package hr.abysalto.hiring.mid.controller;

import hr.abysalto.hiring.mid.dto.AuthResponse;
import hr.abysalto.hiring.mid.dto.LoginRequest;
import hr.abysalto.hiring.mid.dto.RegisterRequest;
import hr.abysalto.hiring.mid.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService service;

    public AuthController(UserService service) {
        this.service = service;
    }

    @Operation(summary = "Register user", description = "Registers new user and returns JWT in cookie", responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(description = "User with email: {email} already exists.", responseCode = "409",
                            content = @Content)
            }
    )
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request, HttpServletResponse response){
        return ResponseEntity.ok(service.register(request, response));
    }

    @Operation(summary = "Login user", description = "Logs in a user and returns JWT in cookie", responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(description = "User not found!", responseCode = "404",
                            content = @Content),
                    @ApiResponse(description = "Invalid credentials", responseCode = "404",
                            content = @Content)
            }
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request, HttpServletResponse response){
        return ResponseEntity.ok(service.login(request, response));
    }

    @Operation(summary = "Logout user", description = "Logs out the user, clears JWT cookie", responses = {
                    @ApiResponse(responseCode = "204", content = @Content)})
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        service.logout(response);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get current logged-in user info", description = "Returns info on logged in user", responses = {
                    @ApiResponse(responseCode = "200",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(description = "User is not authenticated", responseCode = "401", content = @Content)
            }
    )
    @GetMapping("/me")
    public ResponseEntity<AuthResponse> me(@CookieValue(name = "jwt", required = false) String token) {
        return ResponseEntity.ok(service.me(token));
    }

}
