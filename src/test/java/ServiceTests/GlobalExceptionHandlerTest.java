package ServiceTests;


import jakarta.servlet.http.HttpServletRequest;
import ServiceTests.Exceptions.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleResourceNotFound_shouldReturn404() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/employees/99");

        ResourceNotFoundException ex =
                new ResourceNotFoundException("Employee not found");

        ResponseEntity<ApiError> response =
                handler.handleResourceNotFound(ex, request);

        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Employee not found", response.getBody().getMessage());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("Not Found", response.getBody().getError());
        assertEquals("/employees/99", response.getBody().getPath());
    }

    @Test
    void handleDuplicate_shouldReturn409() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/managers");

        DuplicateResourceException ex =
                new DuplicateResourceException("Manager already exists");

        ResponseEntity<ApiError> response =
                handler.handleDuplicateNotFound(ex, request);

        assertEquals(409, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(409, response.getBody().getStatus());
        assertEquals("Conflict", response.getBody().getError());
    }

    @Test
    void handleForbiddenOperation_shouldReturn403() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/workgroups/1");

        ForbiddenOperationException ex =
                new ForbiddenOperationException("Not allowed");

        ResponseEntity<ApiError> response =
                handler.handleForbiddenOperationNotFound(ex, request);

        assertEquals(403, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(403, response.getBody().getStatus());
        assertEquals("Forbidden", response.getBody().getError());
    }

    @Test
    void handleBusinessValidation_shouldReturn422() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/TaskAssignment");

        BusinessValidationException ex =
                new BusinessValidationException("Deadline invalid");

        ResponseEntity<ApiError> response =
                handler.handleBusinessValidationNotFound(ex, request);

        assertEquals(422, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(422, response.getBody().getStatus());
        assertEquals("Unprocessable Entity", response.getBody().getError());
    }


}