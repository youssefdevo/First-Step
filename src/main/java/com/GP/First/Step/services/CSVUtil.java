package com.GP.First.Step.services;

import com.GP.First.Step.entities.Project;
import com.azure.core.util.Context;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.models.BlobStorageException;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CSVUtil {
    private static final String CONNECTION_STRING = "DefaultEndpointsProtocol=https;AccountName=firststepdata;AccountKey=/1MX4Fc4Y9d7Bo94AMt2+CxMzMS/FgXMEOchPKHQnLvg9CB+Yh2C1/WMDU8BOrHUk5TI9Xf6gLbc+AStnxXlGw==;EndpointSuffix=core.windows.net";
    private static final String CONTAINER_NAME = "projectsdata";
    private static final String BLOB_NAME = "updated_pitch_decks_dataset.csv";

    public static List<Project> readProjectsFromCSV() {
        Path tempFilePath = Paths.get("temp_projects.csv");
        downloadBlobToFile(String.valueOf(tempFilePath.toString()));
        try {
            return new CsvToBeanBuilder<Project>(Files.newBufferedReader(tempFilePath))
                    .withType(Project.class)
                    .build()
                    .parse();
        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file", e);
        } finally {
            try {
                Files.deleteIfExists(tempFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void downloadBlobToFile(String filePath) {
        BlobClient blobClient = new BlobClientBuilder()
                .connectionString(CONNECTION_STRING)
                .containerName(CONTAINER_NAME)
                .blobName(BLOB_NAME)
                .buildClient();

        try {
            File file = new File(filePath);
            if (file.exists()) {
                Files.delete(Paths.get(filePath));
            }
            blobClient.downloadToFile(filePath);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (BlobStorageException e) {
            // Handle the blob storage exception
        }
    }

    public static void appendProjectToCSV(String csvFilePath, Project project) {
        // Implementation for appending project to CSV remains the same
    }
}
