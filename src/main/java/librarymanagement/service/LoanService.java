package librarymanagement.service;

import librarymanagement.dto.request.BorrowRequest;
import librarymanagement.dto.response.LoanResponse;
import librarymanagement.entity.Book;
import librarymanagement.entity.Loan;
import librarymanagement.entity.Member;
import librarymanagement.exception.ResourceNotFoundException;
import librarymanagement.repository.BookRepository;
import librarymanagement.repository.LoanRepository;
import librarymanagement.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public LoanResponse borrowBook(BorrowRequest request) {

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));

        if (book.getTotalCopies() <= 0) {
            throw new IllegalStateException("No copies available.");
        }

        book.setTotalCopies(book.getTotalCopies() - 1);

        Loan loan = Loan.builder()
                .book(book)
                .member(member)
                .borrowDate(LocalDate.now())
                .returned(false)
                .build();

        Loan savedLoan = loanRepository.save(loan);

        return LoanResponse.builder()
                .id(savedLoan.getId())
                .bookId(book.getId())
                .memberId(member.getId())
                .borrowDate(savedLoan.getBorrowDate())
                .returnDate(savedLoan.getReturnDate())
                .returned(savedLoan.isReturned())
                .build();
    }
}