package librarymanagement.service;

import librarymanagement.dto.request.MemberRequest;
import librarymanagement.dto.response.MemberResponse;
import librarymanagement.entity.Member;
import librarymanagement.exception.ResourceNotFoundException;
import librarymanagement.mapper.MemberMapper;
import librarymanagement.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    public MemberResponse create(MemberRequest request) {

        Member member = memberMapper.toEntity(request);
        Member savedMember = memberRepository.save(member);

        return memberMapper.toResponse(savedMember);
    }

    public Page<MemberResponse> getAll(Pageable pageable) {
        return memberRepository.findAll(pageable).map(memberMapper::toResponse);
    }

    public MemberResponse getById(Long id) {

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));

        return memberMapper.toResponse(member);
    }

    public MemberResponse update(Long id, MemberRequest request) {

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));

        member.setFirstName(request.getFirstName());
        member.setLastName(request.getLastName());
        member.setEmail(request.getEmail());
        member.setPhoneNumber(request.getPhoneNumber());
        member.setMembershipDate(request.getMembershipDate());

        Member updatedMember = memberRepository.save(member);

        return memberMapper.toResponse(updatedMember);
    }

    public void delete(Long id) {

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));

        memberRepository.delete(member);
    }
}