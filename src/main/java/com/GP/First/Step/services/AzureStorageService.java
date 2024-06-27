package com.GP.First.Step.services;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class AzureStorageService {

    @Value("${azure.storage.account-name}")
    private String accountName;

    @Value("${azure.storage.account-key}")
    private String accountKey;

    @Value("${azure.storage.blob-container}")
    private String containerName;

    public InputStream downloadFile(String fileName) {
        BlobClient blobClient = new BlobClientBuilder()
                .endpoint(String.format("https://%s.blob.core.windows.net", accountName))
                .sasToken(accountKey)
                .containerName(containerName)
                .blobName(fileName)
                .buildClient();

        return blobClient.openInputStream();
    }

    public void uploadFile(String fileName, Path filePath) {
        BlobClient blobClient = new BlobClientBuilder()
                .endpoint(String.format("https://%s.blob.core.windows.net", accountName))
                .sasToken(accountKey)
                .containerName(containerName)
                .blobName(fileName)
                .buildClient();

        try (InputStream inputStream = Files.newInputStream(filePath)) {
            blobClient.upload(inputStream, Files.size(filePath), true);
            BlobHttpHeaders headers = new BlobHttpHeaders().setContentType("text/csv");
            blobClient.setHttpHeaders(headers);
        } catch (Exception e) {
            throw new RuntimeException("Error uploading file to Azure Blob Storage", e);
        }
    }
}
