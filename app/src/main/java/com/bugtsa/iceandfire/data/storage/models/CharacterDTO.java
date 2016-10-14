package com.bugtsa.iceandfire.data.storage.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Передаёт данные о пользователях
 */
public class CharacterDTO implements Parcelable {

    private String mRemoteId;

    private String houseRemoteId;

    private String url;
    private String name;
    private String gender;
    private String culture;
    private String born;
    private String died;
    private String father;
    private String mother;
    private String spouse;
    private String alias;

    public CharacterDTO(CharacterOfHouse character) {
        mRemoteId = character.getRemoteId();
        houseRemoteId = character.getHouseRemoteId();
        url = character.getUrl();
        name = character.getName();
        gender = character.getGender();
        culture = character.getCulture();
        born = character.getBorn();
        died = character.getDied();
        father = character.getFather();
        mother = character.getMother();
        spouse = character.getSpouse();
        alias = character.getAlias();
    }

    protected CharacterDTO(Parcel in) {
        mRemoteId = in.readString();
        houseRemoteId = in.readString();
        url = in.readString();
        name = in.readString();
        gender = in.readString();
        culture = in.readString();
        born = in.readString();
        died = in.readString();
        father = in.readString();
        mother = in.readString();
        spouse = in.readString();
        alias = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mRemoteId);
        dest.writeString(houseRemoteId);
        dest.writeString(url);
        dest.writeString(name);
        dest.writeString(gender);
        dest.writeString(culture);
        dest.writeString(born);
        dest.writeString(died);
        dest.writeString(father);
        dest.writeString(mother);
        dest.writeString(spouse);
        dest.writeString(alias);
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

    public String getAlias() {
        return alias;
    }

    public String getBorn() {
        return born;
    }

    public String getCulture() {
        return culture;
    }

    public String getDied() {
        return died;
    }

    public String getFather() {
        return father;
    }

    public String getGender() {
        return gender;
    }

    public String getHouseRemoteId() {
        return houseRemoteId;
    }

    public String getMother() {
        return mother;
    }

    public String getName() {
        return name;
    }

    public String getSpouse() {
        return spouse;
    }

    public String getUrl() {
        return url;
    }
}
