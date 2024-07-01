package com.GP.First.Step.entities;

import com.opencsv.bean.CsvBindByName;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

// temp project for retrieving projects from CSV file.
public class ProjectT {

    @CsvBindByName(column = "ProjectID")
    private long id;

    @CsvBindByName(column = "userId")
    private long userId;

    @CsvBindByName(column = "Company Name")
    private String companyName;

    @CsvBindByName(column = "Slogan")
    private String slogan;

    @CsvBindByName(column = "Amount Raised")
    private String amountRaised;

    @CsvBindByName(column = "Year")
    private String year;

    @CsvBindByName(column = "Stage")
    private String stage;

    @CsvBindByName(column = "Business Model")
    private String businessModel;

    @CsvBindByName(column = "Full Description")
    private String fullDescription;

    @CsvBindByName(column = "Image URL")
    private String imageURL;

    @CsvBindByName(column = "PDF URL")
    private String pdf_URL;

    @CsvBindByName(column = "Investors")
    private String investors;

    @CsvBindByName(column = "About")
    private String about;

    @CsvBindByName(column = "Industry")
    private String industry;

    @CsvBindByName(column = "Tags")
    private String tags;

    @CsvBindByName(column = "Customer Model")
    private String customerModel;

    @CsvBindByName(column = "Website")
    private String website;

    @CsvBindByName(column = "Legal Name")
    private String legalName;

    @CsvBindByName(column = "Type")
    private String type;


    List<Long> likes;


    public ProjectT() {
        this.likes = new ArrayList<Long>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}


