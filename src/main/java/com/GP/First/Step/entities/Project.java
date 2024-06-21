package com.GP.First.Step.entities;

import com.opencsv.bean.CsvBindByName;
import jakarta.persistence.*;

@Entity
@Table(name = "project_table")
public class Project {
    @CsvBindByName(column = "ProjectID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private long projectID;

//    @CsvBindByName(column = "User ID")
//    @Column(name = "user_id")
//    private long user_id;

    @CsvBindByName(column = "Company Name")
    @Column(name = "company_name")
    private String companyName;

    @CsvBindByName(column = "Slogan")
    @Column(name = "slogan")
    private String slogan;

    @CsvBindByName(column = "Amount Raised")
    @Column(name = "amount_raised")
    private String amountRaised;

    @CsvBindByName(column = "Year")
    @Column(name = "year")
    private String year;

    @CsvBindByName(column = "Business Model")
    @Column(name = "business_model")
    private String businessModel;

    @CsvBindByName(column = "Full Description")
    @Column(name = "full_description")
    private String fullDescription;

    @CsvBindByName(column = "Image URL")
    @Column(name = "image_url")
    private String imageURL;

    @CsvBindByName(column = "PDF URL")
    @Column(name = "pdf_url")
    private String pdf_URL;

    @CsvBindByName(column = "Investors")
    @Column(name = "investors")
    private String investors;

    @CsvBindByName(column = "About")
    @Column(name = "about")
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

    // Getters and Setters
    public void setProjectID(long projectID) {
        this.projectID = projectID;
    }

//    public void setUser_id(long user_id) {
//        this.user_id = user_id;
//    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public void setAmountRaised(String amountRaised) {
        this.amountRaised = amountRaised;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setBusinessModel(String businessModel) {
        this.businessModel = businessModel;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setPDF_URL(String pdf_URL) {
        this.pdf_URL = pdf_URL;
    }

    public void setInvestors(String investors) {
        this.investors = investors;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setCustomerModel(String customerModel) {
        this.customerModel = customerModel;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getProjectID() {
        return projectID;
    }

//    public long getUser_id() {
//        return user_id;
//    }

    public String getCompanyName() {
        return companyName;
    }

    public String getSlogan() {
        return slogan;
    }

    public String getAmountRaised() {
        return amountRaised;
    }

    public String getYear() {
        return year;
    }

    public String getBusinessModel() {
        return businessModel;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getPDF_URL() {
        return pdf_URL;
    }

    public String getInvestors() {
        return investors;
    }

    public String getAbout() {
        return about;
    }

    public String getIndustry() {
        return industry;
    }

    public String getTags() {
        return tags;
    }

    public String getCustomerModel() {
        return customerModel;
    }

    public String getWebsite() {
        return website;
    }

    public String getLegalName() {
        return legalName;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Project{" +
                "projectID=" + projectID +
//                ", user_id=" + user_id +
                ", companyName='" + companyName + '\'' +
                ", slogan='" + slogan + '\'' +
                ", amountRaised='" + amountRaised + '\'' +
                ", year='" + year + '\'' +
                ", businessModel='" + businessModel + '\'' +
                ", fullDescription='" + fullDescription + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", PDF_URL='" + pdf_URL + '\'' +
                ", investors='" + investors + '\'' +
                ", about='" + about + '\'' +
                ", industry='" + industry + '\'' +
                ", tags='" + tags + '\'' +
                ", customerModel='" + customerModel + '\'' +
                ", website='" + website + '\'' +
                ", legalName='" + legalName + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
