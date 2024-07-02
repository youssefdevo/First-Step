package com.GP.First.Step.services;

// Defines methods for blob storage operations.
public interface BlobService {


    // Declares methods for downloading and uploading files to blob storage.
    // Provides an abstraction for different blob storage implementations.

    void downloadToFile(String blobName, String filePath);
    void uploadFile(String blobName, String filePath);
}
