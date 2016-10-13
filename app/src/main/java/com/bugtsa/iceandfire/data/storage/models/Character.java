package com.bugtsa.iceandfire.data.storage.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Character {

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("region")
    @Expose
    private String region;
    @SerializedName("coatOfArms")
    @Expose
    private String coatOfArms;
    @SerializedName("words")
    @Expose
    private String words;
    @SerializedName("titles")
    @Expose
    private List<String> titles = new ArrayList<String>();
    @SerializedName("seats")
    @Expose
    private List<String> seats = new ArrayList<String>();
    @SerializedName("currentLord")
    @Expose
    private String currentLord;
    @SerializedName("heir")
    @Expose
    private String heir;
    @SerializedName("overlord")
    @Expose
    private String overlord;
    @SerializedName("founded")
    @Expose
    private String founded;
    @SerializedName("founder")
    @Expose
    private String founder;
    @SerializedName("diedOut")
    @Expose
    private String diedOut;
    @SerializedName("ancestralWeapons")
    @Expose
    private List<String> ancestralWeapons = new ArrayList<String>();
    @SerializedName("cadetBranches")
    @Expose
    private List<String> cadetBranches = new ArrayList<String>();
    @SerializedName("swornMembers")
    @Expose
    private List<String> swornMembers = new ArrayList<String>();

    /**
     * @return The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The region
     */
    public String getRegion() {
        return region;
    }

    /**
     * @param region The region
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * @return The coatOfArms
     */
    public String getCoatOfArms() {
        return coatOfArms;
    }

    /**
     * @param coatOfArms The coatOfArms
     */
    public void setCoatOfArms(String coatOfArms) {
        this.coatOfArms = coatOfArms;
    }

    /**
     * @return The words
     */
    public String getWords() {
        return words;
    }

    /**
     * @param words The words
     */
    public void setWords(String words) {
        this.words = words;
    }

    /**
     * @return The titles
     */
    public List<String> getTitles() {
        return titles;
    }

    /**
     * @param titles The titles
     */
    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    /**
     * @return The seats
     */
    public List<String> getSeats() {
        return seats;
    }

    /**
     * @param seats The seats
     */
    public void setSeats(List<String> seats) {
        this.seats = seats;
    }

    /**
     * @return The currentLord
     */
    public String getCurrentLord() {
        return currentLord;
    }

    /**
     * @param currentLord The currentLord
     */
    public void setCurrentLord(String currentLord) {
        this.currentLord = currentLord;
    }

    /**
     * @return The heir
     */
    public String getHeir() {
        return heir;
    }

    /**
     * @param heir The heir
     */
    public void setHeir(String heir) {
        this.heir = heir;
    }

    /**
     * @return The overlord
     */
    public String getOverlord() {
        return overlord;
    }

    /**
     * @param overlord The overlord
     */
    public void setOverlord(String overlord) {
        this.overlord = overlord;
    }

    /**
     * @return The founded
     */
    public String getFounded() {
        return founded;
    }

    /**
     * @param founded The founded
     */
    public void setFounded(String founded) {
        this.founded = founded;
    }

    /**
     * @return The founder
     */
    public String getFounder() {
        return founder;
    }

    /**
     * @param founder The founder
     */
    public void setFounder(String founder) {
        this.founder = founder;
    }

    /**
     * @return The diedOut
     */
    public String getDiedOut() {
        return diedOut;
    }

    /**
     * @param diedOut The diedOut
     */
    public void setDiedOut(String diedOut) {
        this.diedOut = diedOut;
    }

    /**
     * @return The ancestralWeapons
     */
    public List<String> getAncestralWeapons() {
        return ancestralWeapons;
    }

    /**
     * @param ancestralWeapons The ancestralWeapons
     */
    public void setAncestralWeapons(List<String> ancestralWeapons) {
        this.ancestralWeapons = ancestralWeapons;
    }

    /**
     * @return The cadetBranches
     */
    public List<String> getCadetBranches() {
        return cadetBranches;
    }

    /**
     * @param cadetBranches The cadetBranches
     */
    public void setCadetBranches(List<String> cadetBranches) {
        this.cadetBranches = cadetBranches;
    }

    /**
     * @return The swornMembers
     */
    public List<String> getSwornMembers() {
        return swornMembers;
    }

    /**
     * @param swornMembers The swornMembers
     */
    public void setSwornMembers(List<String> swornMembers) {
        this.swornMembers = swornMembers;
    }

}
