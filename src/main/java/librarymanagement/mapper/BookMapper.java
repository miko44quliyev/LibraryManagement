package librarymanagement.mapper;

import librarymanagement.dto.request.BookRequest;
import librarymanagement.dto.response.BookResponse;
import librarymanagement.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "author", ignore = true)
    Book toEntity(BookRequest request);

    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "authorName",
            expression = "java(book.getAuthor().getFirstName() + \" \" + book.getAuthor().getLastName())")
    BookResponse toResponse(Book book);

    List<BookResponse> toResponseList(List<Book> books);

}