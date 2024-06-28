package com.GP.First.Step.services;

public interface BlobService {
    void downloadToFile(String blobName, String filePath);
    void uploadFile(String blobName, String filePath);
}
