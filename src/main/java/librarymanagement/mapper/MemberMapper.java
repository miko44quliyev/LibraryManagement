package librarymanagement.mapper;

import librarymanagement.dto.request.MemberRequest;
import librarymanagement.dto.response.MemberResponse;
import librarymanagement.entity.Member;
import org.mapstruct.Mapper;


import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    Member toEntity(MemberRequest request);

    MemberResponse toResponse(Member member);

    List<MemberResponse> toResponseList(List<Member> members);

}