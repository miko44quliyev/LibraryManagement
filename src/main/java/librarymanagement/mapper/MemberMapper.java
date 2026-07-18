package librarymanagement.mapper;

import librarymanagement.dto.request.MemberRequest;
import librarymanagement.dto.response.MemberResponse;
import librarymanagement.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    @Mapping(target = "id", ignore = true)
    Member toEntity(MemberRequest request);

    MemberResponse toResponse(Member member);

}