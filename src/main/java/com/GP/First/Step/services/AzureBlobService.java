package com.GP.First.Step.services;


import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.models.BlobStorageException;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;


// Implements the BlobService interface for Azure Blob Storage.
public class AzureBlobService implements BlobService {
    // Holds the Azure Storage connection string.
    private final String connectionString;

    // Holds the Azure Storage container name.
    private final String containerName;


    // Initializes connectionString and containerName with provided values.
    public AzureBlobService(String connectionString, String containerName) {
        this.connectionString = connectionString;
        this.containerName = containerName;
    }

    @Override
    public void downloadToFile(String blobName, String filePath) {
        // BlobClient Creation: Creates a BlobClient using the provided connection string, container name, and blob name.
        BlobClient blobClient = new BlobClientBuilder()
                .connectionString(connectionString)
                .containerName(containerName)
                .blobName(blobName)
                .buildClient();

        try {
            // Checks if the target file exists. If it does, the existing file is deleted to ensure the download is clean.
            File file = new File(filePath);
            if (file.exists()) {
                Files.delete(Paths.get(filePath));
            }
            // Downloads the blob content to the specified file path.
            blobClient.downloadToFile(filePath);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (BlobStorageException e) {
            // Handles IO exceptions by wrapping them in an UncheckedIOException. Handles blob storage exceptions by wrapping them in a RuntimeException.
            throw new RuntimeException("Blob storage exception", e);
        }
    }

    @Override
    public void uploadFile(String blobName, String filePath) {
        // Creates a BlobClient using the provided connection string, container name, and blob name.
        BlobClient blobClient = new BlobClientBuilder()
                .connectionString(connectionString)
                .containerName(containerName)
                .blobName(blobName)
                .buildClient();

        try {
            // Uploads the specified file to the blob storage, with the true parameter indicating that existing blobs should be overwritten.
            blobClient.uploadFromFile(filePath, true);
        } catch (BlobStorageException e) {
            // Handles blob storage exceptions by wrapping them in a RuntimeException.
            throw new RuntimeException("Blob storage exception", e);
        }
    }
}


