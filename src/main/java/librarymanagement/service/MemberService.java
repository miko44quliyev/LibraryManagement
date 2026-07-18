package librarymanagement.service;

import librarymanagement.dto.request.MemberRequest;
import librarymanagement.dto.response.MemberResponse;
import librarymanagement.entity.Member;
import librarymanagement.mapper.MemberMapper;
import librarymanagement.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<MemberResponse> getAll() {
        return memberMapper.toResponseList(memberRepository.findAll());
    }

    public MemberResponse getById(Long id) {

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        return memberMapper.toResponse(member);
    }

    public MemberResponse update(Long id, MemberRequest request) {

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));

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
                .orElseThrow(() -> new RuntimeException("Member not found"));

        memberRepository.delete(member);
    }
}