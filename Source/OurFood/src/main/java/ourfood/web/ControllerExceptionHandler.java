package ourfood.web;

import javax.servlet.ServletException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import ourfood.exception.InvalidInputException;
import ourfood.exception.ResourceNotFoundException;
import ourfood.exception.UnsupportedFileExtensionException;

/**
 * Default exception handler
 * 
 \* @author raghu.mulukoju
 */
@ControllerAdvice(annotations = Controller.class)
public class ControllerExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String handleException(Exception ex) {
        ex.printStackTrace();
        return "error";
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String handleException(RuntimeException ex) {
        ex.printStackTrace();
        return "400";
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String handleException(ServletException ex) {
        ex.printStackTrace();
        return "400";
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String handleException(ResourceNotFoundException ex) {
        return "404";
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ModelAndView handleException(AccessDeniedException ex, Authentication authentication) {

        if (authentication != null && authentication.isAuthenticated()) {
            // User is unauthorized to access a specific resource
            return new ModelAndView("un-authorized", "unauthorized", true);
        }

        return new ModelAndView("user/login", "unauthorized", true);
    }

    @ExceptionHandler(UnsupportedFileExtensionException.class)
    public ResponseEntity<?> UnsupportedFileExtensionException(UnsupportedFileExtensionException ex) {
        System.out.println(ex.toString());
        return new ResponseEntity<>(ex.getMessage(), org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler
    public ResponseEntity<?> ArrayIndexOutOfBoundsException(ArrayIndexOutOfBoundsException ex) {
        System.out.println(ex.toString());
        return new ResponseEntity<>(ex.getMessage(), org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<?> InvalidInputException(InvalidInputException ex) {
        System.out.println(ex.toString());
        return new ResponseEntity<>(ex.getMessage(), org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
    }
}