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
    private List<String> titles;
    private List<String> aliases;

    public CharacterDTO(CharacterOfHouse character) {
        List<String> titleList = new ArrayList<>();
        List<String> aliasList = new ArrayList<>();
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

        for (Title title : character.getTitles()) {
            titleList.add(title.getTitle());
        }
        this.titles = titleList;
        for (Alias alias : character.getAliases()) {
            aliasList.add(alias.getAlias());
        }
        this.aliases = aliasList;
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
        if (in.readByte() == 0x01) {
            titles = new ArrayList<String>();
            in.readList(titles, String.class.getClassLoader());
        } else {
            titles = null;
        }
        if (in.readByte() == 0x01) {
            aliases = new ArrayList<String>();
            in.readList(aliases, String.class.getClassLoader());
        } else {
            aliases = null;
        }
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
        if (titles == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(titles);
        }
        if (aliases == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(aliases);
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

    public List<String> getAliases() {
        return aliases;
    }

    public List<String> getTitles() {
        return titles;
    }
}
