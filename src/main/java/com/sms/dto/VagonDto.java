package com.sms.dto;

import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class VagonDto {

	@NotNull
	private Integer nomer;

	@NotNull
	private String depoNomi;

	@NotNull
	private String remontTuri;

	@NotNull
	private Integer ishlabChiqarilganYili;

	@NotNull
	private Date kelganVaqti;

	@NotNull
	private String vagonTuri;

	@NotNull
	private String country;


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

	public String getVagonTuri() {
		return vagonTuri;
	}

	public void setVagonTuri(String vagonTuri) {
		this.vagonTuri = vagonTuri;
	}

	public String getIzoh() {
		return izoh;
	}

	public void setIzoh(String izoh) {
		this.izoh = izoh;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Date getKelganVaqti() {
		return kelganVaqti;
	}

	public void setKelganVaqti(Date kelganVaqti) {
		this.kelganVaqti = kelganVaqti;
	}

	
}
