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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private LoanService loanService;

    @Test
    void borrowBookShouldCreateLoan() {

        BorrowRequest request = new BorrowRequest();
        request.setBookId(1L);
        request.setMemberId(1L);

        Book book = Book.builder()
                .id(1L)
                .title("Clean Code")
                .totalCopies(5)
                .build();

        Member member = Member.builder()
                .id(1L)
                .build();

        Loan loan = Loan.builder()
                .id(1L)
                .book(book)
                .member(member)
                .borrowDate(LocalDate.now())
                .returned(false)
                .build();

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));

        when(memberRepository.findById(1L))
                .thenReturn(Optional.of(member));

        when(loanRepository.save(any(Loan.class)))
                .thenReturn(loan);

        LoanResponse response = loanService.borrowBook(request);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getBookId()).isEqualTo(1L);
        assertThat(response.getMemberId()).isEqualTo(1L);
        assertThat(book.getTotalCopies()).isEqualTo(4);

        verify(loanRepository).save(any(Loan.class));
    }

    @Test
    void borrowBookShouldThrowWhenBookNotFound() {

        BorrowRequest request = new BorrowRequest();
        request.setBookId(1L);
        request.setMemberId(1L);

        when(bookRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> loanService.borrowBook(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Book not found");
    }

    @Test
    void borrowBookShouldThrowWhenMemberNotFound() {

        BorrowRequest request = new BorrowRequest();
        request.setBookId(1L);
        request.setMemberId(1L);

        Book book = Book.builder()
                .id(1L)
                .totalCopies(5)
                .build();

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));

        when(memberRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> loanService.borrowBook(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Member not found");
    }

    @Test
    void borrowBookShouldThrowWhenNoCopiesAvailable() {

        BorrowRequest request = new BorrowRequest();
        request.setBookId(1L);
        request.setMemberId(1L);

        Book book = Book.builder()
                .id(1L)
                .totalCopies(0)
                .build();

        Member member = Member.builder()
                .id(1L)
                .build();

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));

        when(memberRepository.findById(1L))
                .thenReturn(Optional.of(member));

        assertThatThrownBy(() -> loanService.borrowBook(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("No copies available.");

        verify(loanRepository, never()).save(any());
    }
}