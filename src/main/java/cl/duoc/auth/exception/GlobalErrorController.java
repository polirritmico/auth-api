/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.auth.exception;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Hidden
public class GlobalErrorController implements ErrorController {
    @RequestMapping("/error")
    public ResponseEntity<Void> handleError(HttpServletRequest req, HttpServletResponse res) {
        log.error(
                "Container error - Status: {}, Path: {} | Message: {}",
                res.getStatus(),
                req.getAttribute(RequestDispatcher.ERROR_REQUEST_URI),
                req.getAttribute(RequestDispatcher.ERROR_MESSAGE));
        return ResponseEntity.status(res.getStatus()).build();
    }
}
