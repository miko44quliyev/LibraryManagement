package librarymanagement.service;

import librarymanagement.dto.request.MemberRequest;
import librarymanagement.dto.response.MemberResponse;
import librarymanagement.entity.Member;
import librarymanagement.exception.ResourceNotFoundException;
import librarymanagement.mapper.MemberMapper;
import librarymanagement.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberMapper memberMapper;

    @InjectMocks
    private MemberService memberService;

    @Test
    void createShouldSaveMember() {
        MemberRequest request = MemberRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phoneNumber("555-0100")
                .membershipDate(LocalDate.of(2026, 7, 18))
                .build();
        Member member = Member.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phoneNumber("555-0100")
                .membershipDate(request.getMembershipDate())
                .build();
        Member saved = Member.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phoneNumber("555-0100")
                .membershipDate(request.getMembershipDate())
                .build();
        MemberResponse response = MemberResponse.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phoneNumber("555-0100")
                .membershipDate(LocalDate.of(2026, 7, 18))
                .build();

        when(memberMapper.toEntity(request)).thenReturn(member);
        when(memberRepository.save(member)).thenReturn(saved);
        when(memberMapper.toResponse(saved)).thenReturn(response);

        MemberResponse result = memberService.create(request);

        assertThat(result).isEqualTo(response);
        verify(memberRepository).save(member);
    }

    @Test
    void getAllShouldReturnPagedMembers() {
        Member member = Member.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phoneNumber("555-0100")
                .membershipDate(LocalDate.of(2026, 7, 18))
                .build();
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Member> page = new PageImpl<>(List.of(member), pageable, 1);
        MemberResponse response = MemberResponse.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phoneNumber("555-0100")
                .membershipDate(LocalDate.of(2026, 7, 18))
                .build();

        when(memberRepository.findAll(pageable)).thenReturn(page);
        when(memberMapper.toResponse(member)).thenReturn(response);

        Page<MemberResponse> result = memberService.getAll(pageable);

        assertThat(result).hasSize(1);
        assertThat(result.getContent()).containsExactly(response);
    }

    @Test
    void getByIdShouldReturnMember() {
        Member member = Member.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phoneNumber("555-0100")
                .membershipDate(LocalDate.of(2026, 7, 18))
                .build();
        MemberResponse response = MemberResponse.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phoneNumber("555-0100")
                .membershipDate(LocalDate.of(2026, 7, 18))
                .build();

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberMapper.toResponse(member)).thenReturn(response);

        MemberResponse result = memberService.getById(1L);

        assertThat(result).isEqualTo(response);
    }

    @Test
    void getByIdShouldThrowWhenMissing() {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.getById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Member not found");
    }

    @Test
    void updateShouldPersistChanges() {
        MemberRequest request = MemberRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phoneNumber("555-0100")
                .membershipDate(LocalDate.of(2026, 7, 18))
                .build();
        Member existing = Member.builder()
                .id(1L)
                .firstName("Old")
                .lastName("Name")
                .email("old@example.com")
                .phoneNumber("000-0000")
                .membershipDate(LocalDate.of(2020, 1, 1))
                .build();
        Member updated = Member.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phoneNumber("555-0100")
                .membershipDate(request.getMembershipDate())
                .build();
        MemberResponse response = MemberResponse.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phoneNumber("555-0100")
                .membershipDate(LocalDate.of(2026, 7, 18))
                .build();

        when(memberRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(memberRepository.save(existing)).thenReturn(updated);
        when(memberMapper.toResponse(updated)).thenReturn(response);

        MemberResponse result = memberService.update(1L, request);

        assertThat(result).isEqualTo(response);
        assertThat(existing.getFirstName()).isEqualTo("John");
        assertThat(existing.getLastName()).isEqualTo("Doe");
        assertThat(existing.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(existing.getPhoneNumber()).isEqualTo("555-0100");
        assertThat(existing.getMembershipDate()).isEqualTo(LocalDate.of(2026, 7, 18));
    }

    @Test
    void deleteShouldRemoveMember() {
        Member existing = Member.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phoneNumber("555-0100")
                .membershipDate(LocalDate.of(2026, 7, 18))
                .build();

        when(memberRepository.findById(1L)).thenReturn(Optional.of(existing));

        memberService.delete(1L);

        verify(memberRepository).delete(existing);
    }

    @Test
    void deleteShouldThrowWhenMissing() {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.delete(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Member not found");

        verify(memberRepository, never()).delete(any());
    }
}
