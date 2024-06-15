package com.GP.First.Step.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "project_table")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long projectID;

    @Column(name = "user_id")
    private long user_id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "about")
    private String about;
    @Column(name = "slogan")
    private String slogan;

    @Column(name = "industry")
    private String industry;

    @Column(name = "business_model")
    private String business_model;

    @Column(name = "customer_model")
    private String customer_model;
    @Column(name = "type")
    private String type;

    @Column(name = "website")
    private String website;

    @Column(name = "legal_name")
    private String legal_name;

    @Column(name = "raised_funds")
    private String raised_funds;

    @Column(name = "founding_year")
    private String founding_year;

    @Column(name = "stage")
    private String stage;

    public void setProjectID(long projectID) {
        this.projectID = projectID;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public void setBusiness_model(String business_model) {
        this.business_model = business_model;
    }

    public void setCustomer_model(String customer_model) {
        this.customer_model = customer_model;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setLegal_name(String legal_name) {
        this.legal_name = legal_name;
    }

    public void setRaised_funds(String raised_funds) {
        this.raised_funds = raised_funds;
    }

    public void setFounding_year(String founding_year) {
        this.founding_year = founding_year;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public long getProjectID() {
        return projectID;
    }

    public long getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAbout() {
        return about;
    }

    public String getSlogan() {
        return slogan;
    }

    public String getIndustry() {
        return industry;
    }

    public String getBusiness_model() {
        return business_model;
    }

    public String getCustomer_model() {
        return customer_model;
    }

    public String getType() {
        return type;
    }

    public String getWebsite() {
        return website;
    }

    public String getLegal_name() {
        return legal_name;
    }

    public String getRaised_funds() {
        return raised_funds;
    }

    public String getFounding_year() {
        return founding_year;
    }

    public String getStage() {
        return stage;
    }

    @Override
    public String toString() {
        return "Project{" +
                "projectID=" + projectID +
                ", user_id=" + user_id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", about='" + about + '\'' +
                ", slogan='" + slogan + '\'' +
                ", industry='" + industry + '\'' +
                ", business_model='" + business_model + '\'' +
                ", customer_model='" + customer_model + '\'' +
                ", type='" + type + '\'' +
                ", website='" + website + '\'' +
                ", legal_name='" + legal_name + '\'' +
                ", raised_funds='" + raised_funds + '\'' +
                ", founding_year='" + founding_year + '\'' +
                ", stage='" + stage + '\'' +
                '}';
    }
}
