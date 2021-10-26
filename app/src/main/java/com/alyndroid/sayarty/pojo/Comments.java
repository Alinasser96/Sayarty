package com.alyndroid.sayarty.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Comments implements Parcelable {
    private String rightSideComment;
    private String backSideComment;
    private String leftSideComment;
    private String frontSideComment;
    private String generalComment;

    @Inject
    public Comments() {
    }

    private Comments(Parcel in) {
        rightSideComment = in.readString();
        backSideComment = in.readString();
        leftSideComment = in.readString();
        frontSideComment = in.readString();
        generalComment = in.readString();
    }

    public static final Creator<Comments> CREATOR = new Creator<Comments>() {
        @Override
        public Comments createFromParcel(Parcel in) {
            return new Comments(in);
        }

        @Override
        public Comments[] newArray(int size) {
            return new Comments[size];
        }
    };

    public String getRightSideComment() {
        return rightSideComment;
    }

    public void setRightSideComment(String rightSideComment) {
        this.rightSideComment = rightSideComment;
    }

    public String getBackSideComment() {
        return backSideComment;
    }

    public void setBackSideComment(String backSideComment) {
        this.backSideComment = backSideComment;
    }

    public String getLeftSideComment() {
        return leftSideComment;
    }

    public void setLeftSideComment(String leftSideComment) {
        this.leftSideComment = leftSideComment;
    }

    public String getFrontSideComment() {
        return frontSideComment;
    }

    public void setFrontSideComment(String frontSideComment) {
        this.frontSideComment = frontSideComment;
    }

    public String getGeneralComment() {
        return generalComment;
    }

    public void setGeneralComment(String generalComment) {
        this.generalComment = generalComment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(rightSideComment);
        parcel.writeString(backSideComment);
        parcel.writeString(leftSideComment);
        parcel.writeString(frontSideComment);
        parcel.writeString(generalComment);
    }
}
