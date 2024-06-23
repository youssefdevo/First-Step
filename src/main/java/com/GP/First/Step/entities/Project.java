package com.GP.First.Step.entities;

import com.opencsv.bean.CsvBindByName;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "project_table")
public class Project {
    @CsvBindByName(column = "ProjectID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private long projectID;

    @CsvBindByName(column = "User ID")
    @Column(name = "user_id")
    private long userId;

    @CsvBindByName(column = "Company Name")
    @Column(name = "companyName")
    private String companyName;

    @CsvBindByName(column = "Slogan")
    @Column(name = "slogan", length = 65535)
    private String slogan;

    @CsvBindByName(column = "Amount Raised")
    @Column(name = "amount_raised")
    private String amountRaised;

    @CsvBindByName(column = "Year")
    @Column(name = "year")
    private String year;

    @CsvBindByName(column = "stage")
    @Column(name = "stage")
    private String stage;

    @CsvBindByName(column = "Business Model")
    @Column(name = "business_model")
    private String businessModel;

    @CsvBindByName(column = "Full Description")
    @Column(name = "full_description", length = 65535)
    private String fullDescription;

    @CsvBindByName(column = "Image URL")
    @Column(name = "image_url")
    private String imageURL;

    @CsvBindByName(column = "PDF URL")
    @Column(name = "pdf_url")
    private String pdf_URL;

    @CsvBindByName(column = "Investors")
    @Column(name = "investors", length = 65535)
    private String investors;

    @CsvBindByName(column = "About")
    @Column(name = "about", length = 65535)
    private String about;

    @CsvBindByName(column = "Industry")
    @Column(name = "industry")
    private String industry;

    @CsvBindByName(column = "Tags")
    @Column(name = "tags")
    private String tags;

    @CsvBindByName(column = "Customer Model")
    @Column(name = "customer_model")
    private String customerModel;

    @CsvBindByName(column = "Website")
    @Column(name = "website")
    private String website;

    @CsvBindByName(column = "Legal Name")
    @Column(name = "legal_name")
    private String legalName;

    @CsvBindByName(column = "Type")
    @Column(name = "type")
    private String type;

    @ElementCollection
    @Column(name = "likes")
    List<Long> likes;


    public Project() {
        this.likes = new ArrayList<Long>();
    }

    public long getProjectID() {
        return projectID;
    }

    public void setProjectID(long projectID) {
        this.projectID = projectID;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getAmountRaised() {
        return amountRaised;
    }

    public void setAmountRaised(String amountRaised) {
        this.amountRaised = amountRaised;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getBusinessModel() {
        return businessModel;
    }

    public void setBusinessModel(String businessModel) {
        this.businessModel = businessModel;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getPdf_URL() {
        return pdf_URL;
    }

    public void setPdf_URL(String pdf_URL) {
        this.pdf_URL = pdf_URL;
    }

    public String getInvestors() {
        return investors;
    }

    public void setInvestors(String investors) {
        this.investors = investors;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCustomerModel() {
        return customerModel;
    }

    public void setCustomerModel(String customerModel) {
        this.customerModel = customerModel;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Long> getLikes() {
        return likes;
    }

    public void setLikes(List<Long> likes) {
        this.likes = likes;
    }

    public long getNumberOfLikes() {
        return this.likes.size();
    }


    public void addLike(Long userId) {
        if (this.likes == null) {
            this.likes = new ArrayList<>();
        }
        this.likes.add(userId);

    }

    public void removeLike(Long userId) {
        if (this.likes != null) {
            this.likes.remove(userId);
        }
    }


    @Override
    public String toString() {
        return "Project{" +
                "projectID=" + projectID +
                ", user_id=" + userId +
                ", companyName='" + companyName + '\'' +
                ", slogan='" + slogan + '\'' +
                ", amountRaised='" + amountRaised + '\'' +
                ", year='" + year + '\'' +
                ", businessModel='" + businessModel + '\'' +
                ", fullDescription='" + fullDescription + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", pdf_URL='" + pdf_URL + '\'' +
                ", investors='" + investors + '\'' +
                ", about='" + about + '\'' +
                ", industry='" + industry + '\'' +
                ", tags='" + tags + '\'' +
                ", customerModel='" + customerModel + '\'' +
                ", website='" + website + '\'' +
                ", legalName='" + legalName + '\'' +
                ", type='" + type + '\'' +
                ", numberOfLikes='" + this.likes.size() + '\'' +
                ", likes='" + likes + '\'' +
                '}';
    }
}
