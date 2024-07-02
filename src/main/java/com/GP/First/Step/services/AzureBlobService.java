package com.GP.First.Step.services;


import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.models.BlobStorageException;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;



public class AzureBlobService implements BlobService {
    private final String connectionString;
    private final String containerName;


    public AzureBlobService(String connectionString, String containerName) {
        this.connectionString = connectionString;
        this.containerName = containerName;
    }

    @Override
    public void downloadToFile(String blobName, String filePath) {
        BlobClient blobClient = new BlobClientBuilder()
                .connectionString(connectionString)
                .containerName(containerName)
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
                .connectionString(connectionString)
                .containerName(containerName)
                .blobName(blobName)
                .buildClient();

        try {
            blobClient.uploadFromFile(filePath, true);
        } catch (BlobStorageException e) {
            throw new RuntimeException("Blob storage exception", e);
        }
    }
}


