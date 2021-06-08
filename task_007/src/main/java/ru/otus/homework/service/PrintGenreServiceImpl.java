package ru.otus.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Genre;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrintGenreServiceImpl implements PrintGenreService {

    private final PrintService printService;

    @Override
    public String printGenre(Genre genre) {

        StringBuilder genreStr = new StringBuilder("");

        genreStr.append("Genre info:" + "\n");
        genreStr.append(prepareGenre(genre));

        printService.print(genreStr.toString());

        return genreStr.toString();
    }

    @Override
    public String printGenres(List<Genre> genres) {

        StringBuilder genreStr = new StringBuilder("");

        genreStr.append("List of genres:" + "\n");

        int index = 0;

        for(Genre genre : genres) {
            genreStr.append("----------------------------------------------------" + "\n");
            genreStr.append(prepareGenre(genre));
            if(index == genres.size() - 1) {
                genreStr.append("----------------------------------------------------" + "\n\n");
            }
            index++;
        }

        printService.print(genreStr.toString());

        return genreStr.toString();

    }

    @Override
    public String printGenresCount(Long count) {

        String countStr = "\nGenres count: " + count + "\n\n";

        printService.print(countStr);

        return countStr;

    }

    @Override
    public String prepareGenre(Genre genre) {

        StringBuilder genreStr = new StringBuilder("");

        genreStr.append("Id: " + genre.getId() + "\n");
        if(genre.getName() != null && !genre.getName().isBlank()) {
            genreStr.append("Name: " + genre.getName() + "\n");
        }
        if(genre.getDescription() != null && !genre.getDescription().isBlank()) {
            genreStr.append("Description: " + genre.getDescription() + "\n");
        }

        return genreStr.toString();

    }
}
