package com.GP.First.Step.services;

import com.GP.First.Step.entities.Project;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.models.BlobStorageException;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class AzureBlobService implements BlobService {
    private static final String CONNECTION_STRING = "DefaultEndpointsProtocol=https;AccountName=firststepdata;AccountKey=/1MX4Fc4Y9d7Bo94AMt2+CxMzMS/FgXMEOchPKHQnLvg9CB+Yh2C1/WMDU8BOrHUk5TI9Xf6gLbc+AStnxXlGw==;EndpointSuffix=core.windows.net";
    private static final String CONTAINER_NAME = "projectsdata";

    @Override
    public void downloadToFile(String blobName, String filePath) {
        BlobClient blobClient = new BlobClientBuilder()
                .connectionString(CONNECTION_STRING)
                .containerName(CONTAINER_NAME)
                .blobName(blobName)
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
            throw new RuntimeException("Blob storage exception", e);
        }
    }

    @Override
    public void uploadFile(String blobName, String filePath) {
        BlobClient blobClient = new BlobClientBuilder()
                .connectionString(CONNECTION_STRING)
                .containerName(CONTAINER_NAME)
                .blobName(blobName)
                .buildClient();

        try {
            blobClient.uploadFromFile(filePath, true);
        } catch (BlobStorageException e) {
            throw new RuntimeException("Blob storage exception", e);
        }
    }
}


