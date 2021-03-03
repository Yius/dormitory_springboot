package com.example.xin.dormitory.student;


public class Post {
    private int postID;
    private int imageId;
    private String posterName;
    private String posterID;
    private String postingDate;
    private String postTitle;
    private String postContent;
    private String LatestReplyTime;

    public Post(){
        super();
    }

    public Post(int postID,int imageId, String posterName, String posterID, String postingDate, String postTitle, String postContent, String latestReplyTime) {
        this.imageId = imageId;
        this.posterName = posterName;
        this.posterID = posterID;
        this.postingDate = postingDate;
        this.postTitle = postTitle;
        this.postID = postID;
        this.postContent = postContent;
        LatestReplyTime = latestReplyTime;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getPosterName() {
        return posterName;
    }

    public void setPosterName(String posterName) {
        this.posterName = posterName;
    }

    public String getPosterID() {
        return posterID;
    }

    public void setPosterID(String posterID) {
        this.posterID = posterID;
    }

    public String getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(String postingDate) {
        this.postingDate = postingDate;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getLatestReplyTime() {
        return LatestReplyTime;
    }

    public void setLatestReplyTime(String latestReplyTime) {
        LatestReplyTime = latestReplyTime;
    }
}
