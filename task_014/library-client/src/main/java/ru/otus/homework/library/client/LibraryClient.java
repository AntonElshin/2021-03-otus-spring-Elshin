package ru.otus.homework.library.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.otus.homework.library.props.LibraryClientProperties;
import ru.otus.homework.library.dto.*;

import java.util.List;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

public class LibraryClient {

    private RestTemplate restTemplate;
    private LibraryClientProperties props;

    public LibraryClient(RestTemplate restTemplate, LibraryClientProperties props) {
        this.restTemplate = restTemplate;
        this.props = props;
    }

    // Жанры

    public ResponseEntity<List<GenreResListDTO>> getAllGenres(MultiValueMap<String, String> headers) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                .path("/genre/genres/all")
                .encode()
                .build()
                .toUri(),
                HttpMethod.GET,
                new HttpEntity(headers),
                new ParameterizedTypeReference<List<GenreResListDTO>>() {}
        );

    }

    public ResponseEntity<List<GenreResListDTO>> findGenresByParams(MultiValueMap<String, String> headers, String genreName) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path("/genre/genres")
                        .queryParam("genreName", genreName)
                        .encode()
                        .build()
                        .toUri(),
                HttpMethod.GET,
                new HttpEntity(headers),
                new ParameterizedTypeReference<List<GenreResListDTO>>() {}
        );

    }

    public ResponseEntity<GenreResDTO> getGenre(MultiValueMap<String, String> headers, Long id) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path("/genre/genres/{id}")
                        .buildAndExpand(id)
                        .toUriString(),
                HttpMethod.GET,
                new HttpEntity(headers),
                new ParameterizedTypeReference<GenreResDTO>() {});

    }

    public ResponseEntity<GenreResDTO> createGenre(MultiValueMap<String, String> headers, GenreReqDTO genreReqDTO) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path("/genre/genres")
                        .toUriString(),
                HttpMethod.POST,
                new HttpEntity(genreReqDTO, headers),
                new ParameterizedTypeReference<GenreResDTO>() {});

    }

    public ResponseEntity<GenreResDTO> modifyGenre(MultiValueMap<String, String> headers, Long id, GenreReqDTO genreReqDTO) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path("/genre/genres/{id}")
                        .buildAndExpand(id)
                        .toUriString(),
                HttpMethod.PUT,
                new HttpEntity(genreReqDTO, headers),
                new ParameterizedTypeReference<GenreResDTO>() {});

    }

    public ResponseEntity<Void> deleteGenre(MultiValueMap<String, String> headers, Long id) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path("/genre/genres/{id}")
                        .buildAndExpand(id)
                        .toUriString(),
                HttpMethod.DELETE,
                new HttpEntity(headers),
                new ParameterizedTypeReference<Void>() {});

    }

    // Авторы

    public ResponseEntity<List<AuthorResListDTO>> getAllAuthors(MultiValueMap<String, String> headers) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path("/author/authors/all")
                        .encode()
                        .build()
                        .toUri(),
                HttpMethod.GET,
                new HttpEntity(headers),
                new ParameterizedTypeReference<List<AuthorResListDTO>>() {}
        );

    }

    public ResponseEntity<List<AuthorResListDTO>> findAuthorsByParams(MultiValueMap<String, String> headers, String authorLastName, String authorFirstName, String authorMiddleName) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path("/author/authors")
                        .queryParam("authorLastName", authorLastName)
                        .queryParam("authorFirstName", authorFirstName)
                        .queryParam("authorMiddleName", authorMiddleName)
                        .encode()
                        .build()
                        .toUri(),
                HttpMethod.GET,
                new HttpEntity(headers),
                new ParameterizedTypeReference<List<AuthorResListDTO>>() {}
        );

    }

    public ResponseEntity<AuthorResDTO> getAuthor(MultiValueMap<String, String> headers, Long authorId) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path("/author/authors/{id}")
                        .buildAndExpand(authorId)
                        .toUriString(),
                HttpMethod.GET,
                new HttpEntity(headers),
                new ParameterizedTypeReference<AuthorResDTO>() {});

    }

    public ResponseEntity<AuthorResDTO> createAuthor(MultiValueMap<String, String> headers, AuthorReqDTO authorReqDTO) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path("/author/authors")
                        .toUriString(),
                HttpMethod.POST,
                new HttpEntity(authorReqDTO, headers),
                new ParameterizedTypeReference<AuthorResDTO>() {});

    }

    public ResponseEntity<AuthorResDTO> modifyAuthor(MultiValueMap<String, String> headers, Long id, AuthorReqDTO authorReqDTO) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path("/author/authors/{id}")
                        .buildAndExpand(id)
                        .toUriString(),
                HttpMethod.PUT,
                new HttpEntity(authorReqDTO, headers),
                new ParameterizedTypeReference<AuthorResDTO>() {});

    }

    public ResponseEntity<Void> deleteAuthor(MultiValueMap<String, String> headers, Long id) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path("/author/authors/{id}")
                        .buildAndExpand(id)
                        .toUriString(),
                HttpMethod.DELETE,
                new HttpEntity(headers),
                new ParameterizedTypeReference<Void>() {});

    }

    // Книги

    public ResponseEntity<List<BookResListDTO>> getAllBooks(MultiValueMap<String, String> headers) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path("/book/books/all")
                        .encode()
                        .build()
                        .toUri(),
                HttpMethod.GET,
                new HttpEntity(headers),
                new ParameterizedTypeReference<List<BookResListDTO>>() {}
        );

    }

    public ResponseEntity<List<BookResListDTO>> findBooksByParams(MultiValueMap<String, String> headers, String bookTitle, String bookISBN, Long bookAuthorId, Long bookGenreId) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path("/book/books")
                        .queryParam("bookTitle", bookTitle)
                        .queryParam("bookISBN", bookISBN)
                        .queryParam("bookAuthorId", bookAuthorId)
                        .queryParam("bookGenreId", bookGenreId)
                        .encode()
                        .build()
                        .toUri(),
                HttpMethod.GET,
                new HttpEntity(headers),
                new ParameterizedTypeReference<List<BookResListDTO>>() {}
        );

    }

    public ResponseEntity<BookResWithCommentsDTO> getBook(MultiValueMap<String, String> headers, Long bookId) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path("/book/books/{id}")
                        .buildAndExpand(bookId)
                        .toUriString(),
                HttpMethod.GET,
                new HttpEntity(headers),
                new ParameterizedTypeReference<BookResWithCommentsDTO>() {});

    }

    public ResponseEntity<BookResDTO> createBook(MultiValueMap<String, String> headers, BookReqDTO bookReqDTO) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path("/book/books")
                        .toUriString(),
                HttpMethod.POST,
                new HttpEntity(bookReqDTO, headers),
                new ParameterizedTypeReference<BookResDTO>() {});

    }

    public ResponseEntity<BookResDTO> modifyBook(MultiValueMap<String, String> headers, Long bookId, BookReqDTO bookReqDTO) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path("/book/books/{id}")
                        .buildAndExpand(bookId)
                        .toUriString(),
                HttpMethod.PUT,
                new HttpEntity(bookReqDTO, headers),
                new ParameterizedTypeReference<BookResDTO>() {});

    }

    public ResponseEntity<Void> deleteBook(MultiValueMap<String, String> headers, Long id) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path("/book/books/{id}")
                        .buildAndExpand(id)
                        .toUriString(),
                HttpMethod.DELETE,
                new HttpEntity(headers),
                new ParameterizedTypeReference<Void>() {});

    }

    // Комментарии к книге

    public ResponseEntity<List<BookCommentResListDTO>> findBookCommentsByParams(MultiValueMap<String, String> headers, Long commentBookId) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path("/book-comment/bookcomments")
                        .queryParam("commentBookId", commentBookId)
                        .encode()
                        .build()
                        .toUri(),
                HttpMethod.GET,
                new HttpEntity(headers),
                new ParameterizedTypeReference<List<BookCommentResListDTO>>() {}
        );

    }

    public ResponseEntity<BookCommentResDTO> getBookComment(MultiValueMap<String, String> headers, Long bookCommentId) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path("/book-comment/bookcomments/{id}")
                        .buildAndExpand(bookCommentId)
                        .toUriString(),
                HttpMethod.GET,
                new HttpEntity(headers),
                new ParameterizedTypeReference<BookCommentResDTO>() {});

    }

    public ResponseEntity<BookCommentResDTO> createBookComment(MultiValueMap<String, String> headers, BookCommentReqDTO bookCommentReqDTO) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path("/book-comment/bookcomments")
                        .toUriString(),
                HttpMethod.POST,
                new HttpEntity(bookCommentReqDTO, headers),
                new ParameterizedTypeReference<BookCommentResDTO>() {});

    }

    public ResponseEntity<BookCommentResDTO> modifyBookComment(MultiValueMap<String, String> headers, Long bookCommentId, BookCommentReqDTO bookCommentReqDTO) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path("/book-comment/bookcomments/{id}")
                        .buildAndExpand(bookCommentId)
                        .toUriString(),
                HttpMethod.PUT,
                new HttpEntity(bookCommentReqDTO, headers),
                new ParameterizedTypeReference<BookCommentResDTO>() {});

    }

    public ResponseEntity<Void> deleteBookComment(MultiValueMap<String, String> headers, Long bookCommentId) {

        return restTemplate.exchange(fromHttpUrl(props.baseUrl)
                        .path("/book-comment/bookcomments/{id}")
                        .buildAndExpand(bookCommentId)
                        .toUriString(),
                HttpMethod.DELETE,
                new HttpEntity(headers),
                new ParameterizedTypeReference<Void>() {});

    }

}
