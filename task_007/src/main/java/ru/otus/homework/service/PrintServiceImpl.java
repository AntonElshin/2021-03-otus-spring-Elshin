package ru.otus.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintStream;

@Service
@RequiredArgsConstructor
public class PrintServiceImpl implements PrintService {

    private PrintStream out;

    public void print(String message) {

        configurateEnvironment();

        try {
            byte[] genreStrBytes = message.getBytes();
            out.write(genreStrBytes);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void configurateEnvironment() {
        out = System.out;
    }

}
