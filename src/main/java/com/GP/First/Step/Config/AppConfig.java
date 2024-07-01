package com.GP.First.Step.Config;
import com.GP.First.Step.DAO.CommentRepository;
import com.GP.First.Step.DAO.ProjectRepository;
import com.GP.First.Step.DAO.UserRepository;
import com.GP.First.Step.services.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Value("${azure.storage.connection-string}")
    private String connectionString;

    @Value("${azure.storage.container-name}")
    private String containerName;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public BlobService blobService() {
        return new AzureBlobService(connectionString, containerName);
    }

    @Bean
    public CsvService csvService(UserRepository userRepository) {
        return new OpenCsvService(userRepository);
    }

    @Bean
    public ProjectService projectService(ProjectRepository projectRepository,
                                         CommentRepository commentRepository,
                                         BlobService blobService,
                                         CsvService csvService) {
        return new ProjectService(projectRepository, commentRepository, blobService, csvService);
    }
}