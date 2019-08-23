package com.coffee.excelsolve.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

@Service
public class StorageServiceImpl implements StorageService {

    public void init() {

    }

    public void store(MultipartFile file) throws IOException {
        String filePath = "D:\\" + file.getOriginalFilename();
        File desFile = new File(filePath);
        file.transferTo(desFile);
    }

    public Stream<Path> loadAll() {
        return null;
    }

    public Path load(String filename) {
        return null;
    }

    public Resource loadAsResource(String filename) {
        return null;
    }

    public void deleteAll() {

    }
}
