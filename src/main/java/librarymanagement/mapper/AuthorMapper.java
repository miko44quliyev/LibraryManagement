package librarymanagement.mapper;

import librarymanagement.dto.request.AuthorRequest;
import librarymanagement.dto.response.AuthorResponse;
import librarymanagement.entity.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = BookMapper.class)
public interface AuthorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "books", ignore = true)
    Author toEntity(AuthorRequest request);

    AuthorResponse toResponse(Author author);

}