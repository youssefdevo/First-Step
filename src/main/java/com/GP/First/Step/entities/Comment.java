package com.GP.First.Step.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "comments_table")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commentID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "userName")
    private String userName;

    @Column(name = "content")
    private String content;

    public Comment() {}

    public long getCommentID() {
        return id;
    }

    public void setCommentID(long commentID) {
        this.id = commentID;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentID=" + id +
                ", project=" + project +
                ", user=" + user +
                ", userName='" + userName + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
