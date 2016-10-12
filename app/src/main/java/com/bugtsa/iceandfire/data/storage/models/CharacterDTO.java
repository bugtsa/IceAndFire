package com.bugtsa.iceandfire.data.storage.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Передаёт данные о пользователях
 */
public class CharacterDTO implements Parcelable {

    private String mRemoteId;
    private String mPhoto;
    private String mFullName;
    private String mRait;
    private String mRating;
    private String mCodeLines;
    private String mProjects;
    private String mBio;
    private List<String> mRepositories;
    private List<String> mLikes;

    public CharacterDTO(House houseData) {
//        List<String> repoLink = new ArrayList<>();
//        List<String> likeIds = new ArrayList<>();
//
//        mRemoteId = houseData.getRemoteId();
//        mPhoto = houseData.getPhoto();
//        mFullName = houseData.getFullName();
//        mRait = String.valueOf(houseData.getRait());
//        mRating = String.valueOf(houseData.getRating());
//        mCodeLines = String.valueOf(houseData.getCodeLines());
//        mProjects = String.valueOf(houseData.getProjects());
//        mBio = houseData.getBio();
//
//        for (Repository gitLink : houseData.getRepositories()) {
//            repoLink.add(gitLink.getRepositoryName());
//        }
//        mRepositories = repoLink;
//
//        for (Like like : houseData.getLikes()) {
//            likeIds.add(like.getLikeUserId());
//        }
//        mLikes = likeIds;
    }

    protected CharacterDTO(Parcel in) {
        mRemoteId = in.readString();
        mPhoto = in.readString();
        mFullName = in.readString();
        mRait = in.readString();
        mRating = in.readString();
        mCodeLines = in.readString();
        mProjects = in.readString();
        mBio = in.readString();
        if (in.readByte() == 0x01) {
            mRepositories = new ArrayList<String>();
            in.readList(mRepositories, String.class.getClassLoader());
        } else {
            mRepositories = null;
        }
        if (in.readByte() == 0x01) {
            mLikes = new ArrayList<String>();
            in.readList(mLikes, String.class.getClassLoader());
        } else {
            mLikes = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mRemoteId);
        dest.writeString(mPhoto);
        dest.writeString(mFullName);
        dest.writeString(mRait);
        dest.writeString(mRating);
        dest.writeString(mCodeLines);
        dest.writeString(mProjects);
        dest.writeString(mBio);
        if (mRepositories == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mRepositories);
        }
        if (mLikes == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mLikes);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CharacterDTO> CREATOR = new Parcelable.Creator<CharacterDTO>() {
        @Override
        public CharacterDTO createFromParcel(Parcel in) {
            return new CharacterDTO(in);
        }

        @Override
        public CharacterDTO[] newArray(int size) {
            return new CharacterDTO[size];
        }
    };

    public String getRemoteId() { return mRemoteId; }

    public String getPhoto() {
        return mPhoto;
    }

    public String getFullName() {
        return mFullName;
    }

    public String getRait() {return mRait;}

    public String getRating() {
        return mRating;
    }

    public String getCodeLines() {
        return mCodeLines;
    }

    public String getProjects() {
        return mProjects;
    }

    public String getBio() {
        return mBio;
    }

    public List<String> getRepositories() {
        return mRepositories;
    }

    public List<String> getLikes() {
        return mLikes;
    }
}
