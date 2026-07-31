package librarymanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import librarymanagement.dto.request.BorrowRequest;
import librarymanagement.dto.response.LoanResponse;
import librarymanagement.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping("/borrow")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Operation(summary = "Borrow a book")
    public ResponseEntity<LoanResponse> borrowBook(
            @Valid @RequestBody BorrowRequest request) {

        return ResponseEntity.ok(loanService.borrowBook(request));
    }
}