package com.GP.First.Step.Config;
import com.GP.First.Step.DAO.CommentRepository;
import com.GP.First.Step.DAO.ProjectRepository;
import com.GP.First.Step.DAO.UserRepository;
import com.GP.First.Step.services.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

// class provides the necessary configuration for creating and wiring these beans.
@Configuration
public class AppConfig {

    // Injects the value of the Azure storage connection string from properties.
    @Value("${azure.storage.connection-string}")
    private String connectionString;

    // Injects the value of the Azure storage container name from properties.
    @Value("${azure.storage.container-name}")
    private String containerName;

    // Defines a bean for RestTemplate.
    //  RestTemplate is used for making HTTP requests, such as GET
    //  in the SearchService class, RestTemplate is used to send GET requests to a search API.
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // Defines a bean for BlobService.
    // provides methods for interacting with Azure Blob Storage, such as uploading, downloading, and deleting files.
    // in the ProjectService class, BlobService is used to handle CSV files for project data.
    @Bean
    public BlobService blobService() {
        return new AzureBlobService(connectionString, containerName);
    }

    // Defines a bean for CsvService.
    // provides methods for reading from and writing to CSV files
    // in the ProjectService class, CsvService is used to import and export project data to/from CSV files.
    @Bean
    public CsvService csvService(UserRepository userRepository) {
        return new OpenCsvService(userRepository);
    }

    // Defines a bean for ProjectService
    // Provides business logic related to projects, such as creating, updating, deleting projects, and handling associated comments.
    // ProjectRepository: For interacting with the project data in the database.
    //CommentRepository: For interacting with the comment data in the database.
    //BlobService: For handling file operations related to projects.
    //CsvService: For CSV import/export operations.
    //Usage: This bean encapsulates the business logic and can be injected into controllers or other services to manage projects and comments.
    @Bean
    public ProjectService projectService(ProjectRepository projectRepository,
                                         CommentRepository commentRepository,
                                         BlobService blobService,
                                         CsvService csvService) {
        return new ProjectService(projectRepository, commentRepository, blobService, csvService);
    }
}