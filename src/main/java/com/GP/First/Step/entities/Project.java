package com.GP.First.Step.entities;

import com.opencsv.bean.CsvBindByName;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "project_table")
public class Project {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private long id;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @Column(name = "companyName")
    private String companyName;


    @Column(name = "slogan", length = 65535)
    private String slogan;


    @Column(name = "amount_raised")
    private String amountRaised;


    @Column(name = "year")
    private String year;


    @Column(name = "stage")
    private String stage;


    @Column(name = "business_model")
    private String businessModel;


    @Column(name = "full_description", length = 65535)
    private String fullDescription;


    @Column(name = "image_url")
    private String imageURL;


    @Column(name = "pdf_url")
    private String pdf_URL;


    @Column(name = "investors", length = 65535)
    private String investors;


    @Column(name = "about", length = 65535)
    private String about;


    @Column(name = "industry")
    private String industry;


    @Column(name = "tags")
    private String tags;


    @Column(name = "customer_model")
    private String customerModel;


    @Column(name = "website")
    private String website;


    @Column(name = "legal_name")
    private String legalName;


    @Column(name = "type")
    private String type;

    @ElementCollection
    @Column(name = "likes")
    List<Long> likes;





    public Project() {
        this.likes = new ArrayList<Long>();
    }

    public long getProjectID() {
        return id;
    }

    public void setProjectID(long projectID) {
        this.id = projectID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
                "projectID=" + id +
                ", user=" + user +
                ", companyName='" + companyName + '\'' +
                ", slogan='" + slogan + '\'' +
                ", amountRaised='" + amountRaised + '\'' +
                ", year='" + year + '\'' +
                ", stage='" + stage + '\'' +
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
                ", likes=" + likes +
                '}';
    }
}
