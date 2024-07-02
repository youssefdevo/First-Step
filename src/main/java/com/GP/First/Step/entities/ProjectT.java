package com.GP.First.Step.entities;

import com.opencsv.bean.CsvBindByName;


import java.util.ArrayList;
import java.util.List;

// temp project for retrieving projects from CSV file.
public class ProjectT {

    @CsvBindByName(column = "PROJECTID")
    private long id;

    @CsvBindByName(column = "USERID")
    private long userId;

    @CsvBindByName(column = "COMPANY NAME")
    private String companyName;

    @CsvBindByName(column = "SLOGAN")
    private String slogan;

    @CsvBindByName(column = "AMOUNT RAISED")
    private String amountRaised;

    @CsvBindByName(column = "YEAR")
    private String year;

    @CsvBindByName(column = "STAGE")
    private String stage;

    @CsvBindByName(column = "BUSINESS MODEL")
    private String businessModel;

    @CsvBindByName(column = "FULL DESCRIPTION")
    private String fullDescription;

    @CsvBindByName(column = "IMAGE URL")
    private String imageURL;

    @CsvBindByName(column = "PDF URL")
    private String pdfURL;

    @CsvBindByName(column = "INVESTORS")
    private String investors;

    @CsvBindByName(column = "ABOUT")
    private String about;

    @CsvBindByName(column = "INDUSTRY")
    private String industry;

    @CsvBindByName(column = "TAGS")
    private String tags;

    @CsvBindByName(column = "CUSTOMER MODEL")
    private String customerModel;

    @CsvBindByName(column = "WEBSITE")
    private String website;

    @CsvBindByName(column = "LEGAL NAME")
    private String legalName;

    @CsvBindByName(column = "TYPE")
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
        return pdfURL;
    }

    public void setPdf_URL(String pdf_URL) {
        this.pdfURL = pdf_URL;
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


