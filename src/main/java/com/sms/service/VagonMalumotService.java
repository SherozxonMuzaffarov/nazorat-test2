package com.sms.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.sms.dto.VagonMalumotChiqishDto;
import com.sms.dto.VagonMalumotDto;
import com.sms.dto.VagonMalumotUpdateDto;
import com.sms.payload.ApiResponse;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import com.sms.model.VagonMalumot;

public interface VagonMalumotService {

	// bosh admin qoshadi
	ApiResponse saveVagon(VagonMalumotDto vagon);

	ApiResponse saveVagonSam(VagonMalumotDto vagon);

	ApiResponse saveVagonHav(VagonMalumotDto vagon);

	ApiResponse saveVagonAndj(VagonMalumotDto vagon);

	ApiResponse saveVagonChiqish(VagonMalumotChiqishDto vagon, long id);

	ApiResponse saveVagonSamChiqish(VagonMalumotChiqishDto vagon, long id);

	ApiResponse saveVagonHavChiqish(VagonMalumotChiqishDto vagon, long id);

	ApiResponse saveVagonAndjChiqish(VagonMalumotChiqishDto vagon, long id);
	
	List<VagonMalumot> findAll();
	VagonMalumot getVagonById(long id);
	
	ApiResponse updateVagon(VagonMalumotUpdateDto vagon, long id);
	ApiResponse updateVagonSam(VagonMalumotUpdateDto vagon, long id);
	ApiResponse updateVagonHav(VagonMalumotUpdateDto vagon, long id);
	ApiResponse updateVagonAndj(VagonMalumotUpdateDto vagon, long id);
	
	ApiResponse deleteVagonById(long id) throws NotFoundException;
	ApiResponse deleteVagonSam(long id) throws NotFoundException;
	ApiResponse deleteVagonHav(long id) throws NotFoundException;
	ApiResponse deleteVagonAndj(long id) throws NotFoundException;
	

	ApiResponse searchByNomer(Integer nomer);
	
	String getSamDate();
	String getHavDate();
	String getAndjDate();
	
	void createPdf(List<VagonMalumot> vagonsToDownload, HttpServletResponse response) throws IOException;

	//	Filterniki
	List<VagonMalumot> filterByDate(String saqlanganVaqt);

	List<VagonMalumot> filterByCountry(String country);

	List<VagonMalumot> filterByDepoNomi(String depoNomi);

	List<VagonMalumot> filterByCountryAndDate(String country, String saqlanganVaqt);

	List<VagonMalumot> filterByDepoNomiAndCountry(String depoNomi, String country);

	List<VagonMalumot> filterByDepoNomiAndDate(String depoNomi, String saqlanganVaqt);

	List<VagonMalumot> filterByDepoNomiCountryAndDate(String depoNomi, String country, String saqlanganVaqt);

	ApiResponse updateVagonChiqish(VagonMalumotChiqishDto vagon, Long id);

	ApiResponse updateVagonSamChiqish(VagonMalumotChiqishDto vagon, Long id);

	ApiResponse updateVagonHavChiqish(VagonMalumotChiqishDto vagon, Long id);

	ApiResponse updateVagonAndjChiqish(VagonMalumotChiqishDto vagon, Long id);

	VagonMalumot findById(Long id);
}
