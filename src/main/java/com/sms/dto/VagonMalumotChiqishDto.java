package com.sms.dto;

import org.hibernate.annotations.ColumnDefault;

import javax.validation.constraints.NotNull;

public class VagonMalumotChiqishDto {

    @NotNull
    private Integer ramaOng1;
    @NotNull
    private Integer ramaOng1Nomeri;

    @NotNull
    private Integer ramaOng2;
    @NotNull
    private Integer ramaOng2Nomeri;

    @NotNull
    private Integer ramaChap1;
    @NotNull
    private Integer ramaChap1Nomeri;

    @NotNull
    private Integer ramaChap2;
    @NotNull
    private Integer ramaChap2Nomeri;

    @NotNull
    private Integer balka1;
    @NotNull
    private Integer balka1Nomeri;

    @NotNull
    private Integer balka2;
    @NotNull
    private Integer balka2Nomeri;

    @NotNull
    private Integer gildirak1;
    @NotNull
    private Integer gildirak1Nomeri;

    @NotNull
    private Integer gildirak2;
    @NotNull
    private Integer gildirak2Nomeri;

    @NotNull
    private Integer gildirak3;
    @NotNull
    private Integer gildirak3Nomeri;

    @NotNull
    private Integer gildirak4;
    @NotNull
    private Integer gildirak4Nomeri;

    @ColumnDefault("0")
    private String saqlanganVaqtiChiqish;

    @ColumnDefault("0")
    private String izohChiqish;

    public Integer getRamaOng1() {
        return ramaOng1;
    }

    public void setRamaOng1(Integer ramaOng1) {
        this.ramaOng1 = ramaOng1;
    }

    public Integer getRamaOng1Nomeri() {
        return ramaOng1Nomeri;
    }

    public void setRamaOng1Nomeri(Integer ramaOng1Nomeri) {
        this.ramaOng1Nomeri = ramaOng1Nomeri;
    }

    public Integer getRamaOng2() {
        return ramaOng2;
    }

    public void setRamaOng2(Integer ramaOng2) {
        this.ramaOng2 = ramaOng2;
    }

    public Integer getRamaOng2Nomeri() {
        return ramaOng2Nomeri;
    }

    public void setRamaOng2Nomeri(Integer ramaOng2Nomeri) {
        this.ramaOng2Nomeri = ramaOng2Nomeri;
    }

    public Integer getRamaChap1() {
        return ramaChap1;
    }

    public void setRamaChap1(Integer ramaChap1) {
        this.ramaChap1 = ramaChap1;
    }

    public Integer getRamaChap1Nomeri() {
        return ramaChap1Nomeri;
    }

    public void setRamaChap1Nomeri(Integer ramaChap1Nomeri) {
        this.ramaChap1Nomeri = ramaChap1Nomeri;
    }

    public Integer getRamaChap2() {
        return ramaChap2;
    }

    public void setRamaChap2(Integer ramaChap2) {
        this.ramaChap2 = ramaChap2;
    }

    public Integer getRamaChap2Nomeri() {
        return ramaChap2Nomeri;
    }

    public void setRamaChap2Nomeri(Integer ramaChap2Nomeri) {
        this.ramaChap2Nomeri = ramaChap2Nomeri;
    }

    public Integer getBalka1() {
        return balka1;
    }

    public void setBalka1(Integer balka1) {
        this.balka1 = balka1;
    }

    public Integer getBalka1Nomeri() {
        return balka1Nomeri;
    }

    public void setBalka1Nomeri(Integer balka1Nomeri) {
        this.balka1Nomeri = balka1Nomeri;
    }

    public Integer getBalka2() {
        return balka2;
    }

    public void setBalka2(Integer balka2) {
        this.balka2 = balka2;
    }

    public Integer getBalka2Nomeri() {
        return balka2Nomeri;
    }

    public void setBalka2Nomeri(Integer balka2Nomeri) {
        this.balka2Nomeri = balka2Nomeri;
    }

    public Integer getGildirak1() {
        return gildirak1;
    }

    public void setGildirak1(Integer gildirak1) {
        this.gildirak1 = gildirak1;
    }

    public Integer getGildirak1Nomeri() {
        return gildirak1Nomeri;
    }

    public void setGildirak1Nomeri(Integer gildirak1Nomeri) {
        this.gildirak1Nomeri = gildirak1Nomeri;
    }

    public Integer getGildirak2() {
        return gildirak2;
    }

    public void setGildirak2(Integer gildirak2) {
        this.gildirak2 = gildirak2;
    }

    public Integer getGildirak2Nomeri() {
        return gildirak2Nomeri;
    }

    public void setGildirak2Nomeri(Integer gildirak2Nomeri) {
        this.gildirak2Nomeri = gildirak2Nomeri;
    }

    public Integer getGildirak3() {
        return gildirak3;
    }

    public void setGildirak3(Integer gildirak3) {
        this.gildirak3 = gildirak3;
    }

    public Integer getGildirak3Nomeri() {
        return gildirak3Nomeri;
    }

    public void setGildirak3Nomeri(Integer gildirak3Nomeri) {
        this.gildirak3Nomeri = gildirak3Nomeri;
    }

    public Integer getGildirak4() {
        return gildirak4;
    }

    public void setGildirak4(Integer gildirak4) {
        this.gildirak4 = gildirak4;
    }

    public Integer getGildirak4Nomeri() {
        return gildirak4Nomeri;
    }

    public void setGildirak4Nomeri(Integer gildirak4Nomeri) {
        this.gildirak4Nomeri = gildirak4Nomeri;
    }

    public String getSaqlanganVaqtiChiqish() {
        return saqlanganVaqtiChiqish;
    }

    public void setSaqlanganVaqtiChiqish(String saqlanganVaqtiChiqish) {
        this.saqlanganVaqtiChiqish = saqlanganVaqtiChiqish;
    }

    public String getIzohChiqish() {
        return izohChiqish;
    }

    public void setIzohChiqish(String izohChiqish) {
        this.izohChiqish = izohChiqish;
    }
}
