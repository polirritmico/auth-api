/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "System", description = "Service monitoring & status")
public class StatusController {
    @GetMapping("/api/v1/auth/health")
    @Operation(summary = "Check service health", description = "Checks the microservice avaliability")
    @ApiResponse(
            responseCode = "200",
            description = "Successful response",
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                        "status": "health state",
                        "service": "Microservice name"
                    }
                    """)))
    public ResponseEntity<Map<String, String>> checkHealth() {
        return ResponseEntity.ok(Map.of("status", "OK", "service", "AuthMicroservice"));
    }
}
