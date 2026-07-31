package librarymanagement.service;

import librarymanagement.dto.request.BorrowRequest;
import librarymanagement.entity.Author;
import librarymanagement.entity.Book;
import librarymanagement.entity.Member;
import librarymanagement.repository.AuthorRepository;
import librarymanagement.repository.BookRepository;
import librarymanagement.repository.LoanRepository;
import librarymanagement.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
class LoanTransactionTest {

    @Autowired
    private LoanService loanService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private MemberRepository memberRepository;

    @MockBean
    private LoanRepository loanRepository;

    @Test
    void shouldRollbackTransactionWhenLoanSaveFails() {

        Author author = authorRepository.save(
                Author.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .email("john@test.com")
                        .build()
        );

        Book book = bookRepository.save(
                Book.builder()
                        .title("Clean Code")
                        .isbn("123456789")
                        .publishedYear(2008)
                        .totalCopies(5)
                        .author(author)
                        .build()
        );

        Member member = memberRepository.save(
                Member.builder()
                        .firstName("Jane")
                        .lastName("Smith")
                        .email("jane@test.com")
                        .phoneNumber("0501234567")
                        .membershipDate(LocalDate.now())
                        .build()
        );

        BorrowRequest request = new BorrowRequest();
        request.setBookId(book.getId());
        request.setMemberId(member.getId());

        doThrow(new RuntimeException("Database failure"))
                .when(loanRepository)
                .save(any());

        assertThrows(RuntimeException.class,
                () -> loanService.borrowBook(request));

        Book reloadedBook = bookRepository.findById(book.getId())
                .orElseThrow();

        assertThat(reloadedBook.getTotalCopies()).isEqualTo(5);
    }
}