package com.sms.dto;

import org.hibernate.annotations.ColumnDefault;

import javax.validation.constraints.NotNull;
import java.sql.Date;

public class VagonMalumotDto {

    @NotNull
    private Integer nomer;

    @NotNull
    private String depoNomi;

    @NotNull
    private Date oxirgiTamirKuni;

    @NotNull
    private String remontTuri;

    @NotNull
    private Integer ishlabChiqarilganYili;

    //Kirishdagi(k)
    @NotNull
    private Integer kramaOng1;
    @NotNull
    private Integer kramaOng1Nomeri;

    @NotNull
    private Integer kramaOng2;
    @NotNull
    private Integer kramaOng2Nomeri;

    @NotNull
    private Integer kramaChap1;
    @NotNull
    private Integer kramaChap1Nomeri;

    @NotNull
    private Integer kramaChap2;
    @NotNull
    private Integer kramaChap2Nomeri;

    @NotNull
    private Integer kbalka1;
    @NotNull
    private Integer kbalka1Nomeri;

    @NotNull
    private Integer kbalka2;
    @NotNull
    private Integer kbalka2Nomeri;

    @NotNull
    private Integer kgildirak1;
    @NotNull
    private Integer kgildirak1Nomeri;

    @NotNull
    private Integer kgildirak2;
    @NotNull
    private Integer kgildirak2Nomeri;

    @NotNull
    private Integer kgildirak3;
    @NotNull
    private Integer kgildirak3Nomeri;

    @NotNull
    private Integer kgildirak4;
    @NotNull
    private Integer kgildirak4Nomeri;

    private String saqlanganVaqti;
    @ColumnDefault("0")
    @NotNull
    private String country;

    @ColumnDefault("0")
    @NotNull
    private String korxona;

    private String izoh;

    public Integer getNomer() {
        return nomer;
    }

    public void setNomer(Integer nomer) {
        this.nomer = nomer;
    }

    public String getDepoNomi() {
        return depoNomi;
    }

    public void setDepoNomi(String depoNomi) {
        this.depoNomi = depoNomi;
    }

    public Date getOxirgiTamirKuni() {
        return oxirgiTamirKuni;
    }

    public void setOxirgiTamirKuni(Date oxirgiTamirKuni) {
        this.oxirgiTamirKuni = oxirgiTamirKuni;
    }

    public String getRemontTuri() {
        return remontTuri;
    }

    public void setRemontTuri(String remontTuri) {
        this.remontTuri = remontTuri;
    }

    public Integer getIshlabChiqarilganYili() {
        return ishlabChiqarilganYili;
    }

    public void setIshlabChiqarilganYili(Integer ishlabChiqarilganYili) {
        this.ishlabChiqarilganYili = ishlabChiqarilganYili;
    }

    public Integer getKramaOng1() {
        return kramaOng1;
    }

    public void setKramaOng1(Integer kramaOng1) {
        this.kramaOng1 = kramaOng1;
    }

    public Integer getKramaOng1Nomeri() {
        return kramaOng1Nomeri;
    }

    public void setKramaOng1Nomeri(Integer kramaOng1Nomeri) {
        this.kramaOng1Nomeri = kramaOng1Nomeri;
    }

    public Integer getKramaOng2() {
        return kramaOng2;
    }

    public void setKramaOng2(Integer kramaOng2) {
        this.kramaOng2 = kramaOng2;
    }

    public Integer getKramaOng2Nomeri() {
        return kramaOng2Nomeri;
    }

    public void setKramaOng2Nomeri(Integer kramaOng2Nomeri) {
        this.kramaOng2Nomeri = kramaOng2Nomeri;
    }

    public Integer getKramaChap1() {
        return kramaChap1;
    }

    public void setKramaChap1(Integer kramaChap1) {
        this.kramaChap1 = kramaChap1;
    }

    public Integer getKramaChap1Nomeri() {
        return kramaChap1Nomeri;
    }

    public void setKramaChap1Nomeri(Integer kramaChap1Nomeri) {
        this.kramaChap1Nomeri = kramaChap1Nomeri;
    }

    public Integer getKramaChap2() {
        return kramaChap2;
    }

    public void setKramaChap2(Integer kramaChap2) {
        this.kramaChap2 = kramaChap2;
    }

    public Integer getKramaChap2Nomeri() {
        return kramaChap2Nomeri;
    }

    public void setKramaChap2Nomeri(Integer kramaChap2Nomeri) {
        this.kramaChap2Nomeri = kramaChap2Nomeri;
    }

    public Integer getKbalka1() {
        return kbalka1;
    }

    public void setKbalka1(Integer kbalka1) {
        this.kbalka1 = kbalka1;
    }

    public Integer getKbalka1Nomeri() {
        return kbalka1Nomeri;
    }

    public void setKbalka1Nomeri(Integer kbalka1Nomeri) {
        this.kbalka1Nomeri = kbalka1Nomeri;
    }

    public Integer getKbalka2() {
        return kbalka2;
    }

    public void setKbalka2(Integer kbalka2) {
        this.kbalka2 = kbalka2;
    }

    public Integer getKbalka2Nomeri() {
        return kbalka2Nomeri;
    }

    public void setKbalka2Nomeri(Integer kbalka2Nomeri) {
        this.kbalka2Nomeri = kbalka2Nomeri;
    }

    public Integer getKgildirak1() {
        return kgildirak1;
    }

    public void setKgildirak1(Integer kgildirak1) {
        this.kgildirak1 = kgildirak1;
    }

    public Integer getKgildirak1Nomeri() {
        return kgildirak1Nomeri;
    }

    public void setKgildirak1Nomeri(Integer kgildirak1Nomeri) {
        this.kgildirak1Nomeri = kgildirak1Nomeri;
    }

    public Integer getKgildirak2() {
        return kgildirak2;
    }

    public void setKgildirak2(Integer kgildirak2) {
        this.kgildirak2 = kgildirak2;
    }

    public Integer getKgildirak2Nomeri() {
        return kgildirak2Nomeri;
    }

    public void setKgildirak2Nomeri(Integer kgildirak2Nomeri) {
        this.kgildirak2Nomeri = kgildirak2Nomeri;
    }

    public Integer getKgildirak3() {
        return kgildirak3;
    }

    public void setKgildirak3(Integer kgildirak3) {
        this.kgildirak3 = kgildirak3;
    }

    public Integer getKgildirak3Nomeri() {
        return kgildirak3Nomeri;
    }

    public void setKgildirak3Nomeri(Integer kgildirak3Nomeri) {
        this.kgildirak3Nomeri = kgildirak3Nomeri;
    }

    public Integer getKgildirak4() {
        return kgildirak4;
    }

    public void setKgildirak4(Integer kgildirak4) {
        this.kgildirak4 = kgildirak4;
    }

    public Integer getKgildirak4Nomeri() {
        return kgildirak4Nomeri;
    }

    public void setKgildirak4Nomeri(Integer kgildirak4Nomeri) {
        this.kgildirak4Nomeri = kgildirak4Nomeri;
    }

    public String getSaqlanganVaqti() {
        return saqlanganVaqti;
    }

    public void setSaqlanganVaqti(String saqlanganVaqti) {
        this.saqlanganVaqti = saqlanganVaqti;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getKorxona() {
        return korxona;
    }

    public void setKorxona(String korxona) {
        this.korxona = korxona;
    }

    public String getIzoh() {
        return izoh;
    }

    public void setIzoh(String izoh) {
        this.izoh = izoh;
    }
}
