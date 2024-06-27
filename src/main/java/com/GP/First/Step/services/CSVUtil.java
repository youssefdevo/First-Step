package com.GP.First.Step.services;

import com.GP.First.Step.entities.Project;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.models.BlobStorageException;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
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
        downloadBlobToFile(tempFilePath.toString());
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
            throw new RuntimeException("Blob storage exception", e);
        }
    }

    public static void appendProjectToCSV(Project project) {
        Path tempFilePath = Paths.get("temp_projects.csv");
        downloadBlobToFile(tempFilePath.toString());
        try (CSVWriter writer = new CSVWriter(new FileWriter(tempFilePath.toString(), true))) {
            String[] projectData = {
                    project.getCompanyName(),
                    project.getSlogan(),
                    project.getAmountRaised().toString(),
                    String.valueOf(project.getYear()),
                    project.getStage(),
                    project.getBusinessModel(),
                    project.getImageURL(),
                    project.getFullDescription(),
                    project.getPdf_URL(),
                    project.getInvestors(),
                    project.getAbout(),
                    project.getIndustry(),
                    project.getTags(),
                    project.getCustomerModel(),
                    project.getWebsite(),
                    project.getLegalName(),
                    project.getType(),
                    String.valueOf(project.getUserId()),
                    String.valueOf(project.getProjectID())
            };
            writer.writeNext(projectData);
        } catch (IOException e) {
            throw new RuntimeException("Error appending project to CSV file", e);
        }

        uploadFileToBlob(tempFilePath.toString());

        try {
            Files.deleteIfExists(tempFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void uploadFileToBlob(String filePath) {
        BlobClient blobClient = new BlobClientBuilder()
                .connectionString(CONNECTION_STRING)
                .containerName(CONTAINER_NAME)
                .blobName(BLOB_NAME)
                .buildClient();

        try {
            blobClient.uploadFromFile(filePath, true);
        } catch (BlobStorageException e) {
            // Handle the blob storage exception
            throw new RuntimeException("Blob storage exception", e);
        }
    }


}
