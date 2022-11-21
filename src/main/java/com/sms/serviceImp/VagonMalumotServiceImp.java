package com.sms.serviceImp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.property.TextAlignment;
import com.sms.dto.VagonMalumotChiqishDto;
import com.sms.dto.VagonMalumotDto;
import com.sms.dto.VagonMalumotUpdateDto;
import com.sms.model.LastActionTimes;
import com.sms.payload.ApiResponse;
import com.sms.repository.TimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.sms.model.VagonMalumot;
import com.sms.repository.VagonMalumotRepository;
import com.sms.service.VagonMalumotService;

@Service
public class VagonMalumotServiceImp implements VagonMalumotService{

	@Autowired
	VagonMalumotRepository malumotRepository;
	@Autowired
	private TimeRepository utyTimeRepository;

	String currentDate;
	String samDate;
	String havDate ;
	String andjDate ;


	public String getSamDate() {
		Optional<LastActionTimes> optionalQoldiqTime = utyTimeRepository.findById(1);
		if (!optionalQoldiqTime.isPresent())
			return currentDate;
		return optionalQoldiqTime.get().getSamMalumotDate();
	}

	public String getHavDate() {
		Optional<LastActionTimes> optionalQoldiqTime = utyTimeRepository.findById(1);
		if (!optionalQoldiqTime.isPresent())
			return currentDate;
		return optionalQoldiqTime.get().getHavMalumotDate();
	}

	public String getAndjDate() {
		Optional<LastActionTimes> optionalQoldiqTime = utyTimeRepository.findById(1);
		if (!optionalQoldiqTime.isPresent())
			return currentDate;
		return optionalQoldiqTime.get().getAndjMalumotDate();
	}
	
	public void createPdf(List<VagonMalumot> vagons, HttpServletResponse response) throws IOException {

		String home = System.getProperty("user.home");
		  File file = new File(home + "/Downloads" + "/Ta'mirdan chiqgan yuk vagonlar haqida ma'lumot.pdf");
		  if (!file.getParentFile().exists())
		      file.getParentFile().mkdirs();
		  if (!file.exists())
		      file.createNewFile();
		List<VagonMalumot> allVagons = vagons;
		try {
			response.setHeader("Content-Disposition",
                    "attachment;fileName=\"" + "Ta'mirdan chiqgan yuk vagonlar haqida ma'lumot.pdf" +"\"");
			response.setContentType("application/pdf");


			PdfWriter writer = new PdfWriter(file.getAbsolutePath());
			PdfDocument pdfDoc = new PdfDocument(writer);
			pdfDoc.setDefaultPageSize(PageSize.A4.rotate());
			Document doc = new Document(pdfDoc);

			String FONT_FILENAME = "./src/main/resources/arial.ttf";
			PdfFont font = PdfFontFactory.createFont(FONT_FILENAME, PdfEncodings.IDENTITY_H);
			doc.setFont(font);

			Paragraph paragraph = new Paragraph("Ta'mirdan chiqgan yuk vagonlar haqida ma'lumot");
			paragraph.setBackgroundColor(Color.DARK_GRAY);
			paragraph.setFontColor(Color.WHITE);// Setting background color to cell1
			paragraph.setBorder(Border.NO_BORDER);            // Setting border to cell1
			paragraph.setTextAlignment(TextAlignment.CENTER); // Setting text alignment to cell1
			paragraph.setFontSize(16);

			float[] columnWidth = {30f,150f,150f,150f,150f, 150f,150f,150f,150f,150f,150f};
			Table table = new Table(columnWidth);
			table.setTextAlignment(TextAlignment.CENTER);
			table.addCell(new Cell().add("\n № "));
			table.addCell(new Cell().add("\n Nomeri"));
			table.addCell(new Cell().add("\n VCHD"));
			table.addCell(new Cell().add(" Oxirgi ta'mir sanasi"));
			table.addCell(new Cell().add("\n Ta'mir turi"));
			table.addCell(new Cell().add(" Ishlab chiqarilgan yili"));
			table.addCell(new Cell().add("\n Holat"));

			// yon ramaga nested table
			float [] pointColumnWidths2 = {150f};
			Table nestedTableRama = new Table(pointColumnWidths2);
			nestedTableRama.addCell(new Cell().add("Yon ramasi"));

			float [] pointColumnWidths3 = {75f,75f};
			Table nestedTableRamaTomon = new Table(pointColumnWidths3);
			nestedTableRamaTomon.addCell(new Cell().add("O'ng"));
			nestedTableRamaTomon.addCell(new Cell().add("Chap"));
			nestedTableRama.addCell(new Cell().add(nestedTableRamaTomon));

			table.addCell(new Cell().add(nestedTableRama));
			table.addCell(new Cell().add("Ressor usti balkasi"));

			// G'ildirak juftligi tegishliligiga nested table
			float [] pointColumnWidths6 = {150f};
			Table nestedTableGildirak = new Table(pointColumnWidths6);
			nestedTableGildirak.addCell(new Cell().add("G'ildirak juftligi tegishliligi (29)"));

			float [] pointColumnWidths7 = {75f/2,75f/2,75f/2,75f/2};
			Table nestedTableSoni = new Table(pointColumnWidths7);
			nestedTableSoni.addCell(new Cell().add("Birinchi"));
			nestedTableSoni.addCell(new Cell().add("Ikkinchi"));
			nestedTableSoni.addCell(new Cell().add("Uchinchi"));
			nestedTableSoni.addCell(new Cell().add("To'rtinchi"));
			nestedTableGildirak.addCell(new Cell().add(nestedTableSoni));

			table.addCell(new Cell().add(nestedTableGildirak));
			table.addCell(new Cell().add("\nIzoh"));
			int i=0;
			for(VagonMalumot vagon:allVagons) {
				i++;
				table.addCell(new Cell().add(String.valueOf(i)));
				table.addCell(new Cell().add(String.valueOf(vagon.getNomer())));
				table.addCell(new Cell().add(vagon.getDepoNomi()));
				table.addCell(new Cell().add(String.valueOf(vagon.getOxirgiTamirKuni())));
				table.addCell(new Cell().add(vagon.getRemontTuri()));
				table.addCell(new Cell().add(String.valueOf(vagon.getIshlabChiqarilganYili())));

				//holat
				float [] pointColumnWidths9 = {75f, 75f};
				Table nestedTableHolat = new Table(pointColumnWidths9);
				nestedTableHolat.addCell(new Cell().add("\n K:"));

				float [] pointColumnWidths10 = {75f};
				Table nestedTableHolatKirish = new Table(pointColumnWidths10);
				nestedTableHolatKirish.addCell(new Cell().add("yili"));
				nestedTableHolatKirish.addCell(new Cell().add("nomeri"));

				nestedTableHolat.addCell(new Cell().add(nestedTableHolatKirish));
				nestedTableHolat.addCell(new Cell().add("\n Ch:"));

				float [] pointColumnWidths11 = {75f};
				Table nestedTableHolatChiqish = new Table(pointColumnWidths11);
				nestedTableHolatChiqish.addCell(new Cell().add("yili"));
				nestedTableHolatChiqish.addCell(new Cell().add("nomeri"));

				nestedTableHolat.addCell(new Cell().add(nestedTableHolatChiqish));

				table.addCell(new Cell().add(nestedTableHolat));

				// yon ramaga nested table
				float [] pointColumnWidthsrama = {75f,75f};
				Table nestedTableRama1 = new Table(pointColumnWidthsrama);
				//ong tomon 1,2
				float [] pointColumnWidths4 = {75f,75f};
				Table nestedTable = new Table(pointColumnWidths4);
				nestedTable.addCell(new Cell().add(String.valueOf(vagon.getKramaOng1())));
				nestedTable.addCell(new Cell().add(String.valueOf(vagon.getKramaOng2())));
				nestedTable.addCell(new Cell().add(String.valueOf(vagon.getKramaOng1Nomeri())));
				nestedTable.addCell(new Cell().add(String.valueOf(vagon.getKramaOng2Nomeri())));
				nestedTable.addCell(new Cell().add(String.valueOf(vagon.getRamaOng1())));
				nestedTable.addCell(new Cell().add(String.valueOf(vagon.getRamaOng2())));
				nestedTable.addCell(new Cell().add(String.valueOf(vagon.getRamaOng1Nomeri())));
				nestedTable.addCell(new Cell().add(String.valueOf(vagon.getRamaOng2Nomeri())));

				nestedTableRama1.addCell(new Cell().add(nestedTable));

				//chap tomon 1,2
				float [] pointColumnWidthschap = {75f,75f};
				Table nestedTableChap = new Table(pointColumnWidthschap);
				nestedTableChap.addCell(new Cell().add(String.valueOf(vagon.getKramaChap1())));
				nestedTableChap.addCell(new Cell().add(String.valueOf(vagon.getKramaChap2())));
				nestedTableChap.addCell(new Cell().add(String.valueOf(vagon.getKramaChap1Nomeri())));
				nestedTableChap.addCell(new Cell().add(String.valueOf(vagon.getKramaChap2Nomeri())));
				nestedTableChap.addCell(new Cell().add(String.valueOf(vagon.getRamaChap1())));
				nestedTableChap.addCell(new Cell().add(String.valueOf(vagon.getRamaChap2())));
				nestedTableChap.addCell(new Cell().add(String.valueOf(vagon.getRamaChap1Nomeri())));
				nestedTableChap.addCell(new Cell().add(String.valueOf(vagon.getRamaChap2Nomeri())));

				nestedTableRama1.addCell(new Cell().add(nestedTableChap));

				table.addCell(new Cell().add(nestedTableRama1));

				//Ressor usti balkasiga nested table

				float [] pointColumnWidths5 = {75f,75f};
				Table nestedTableBalka = new Table(pointColumnWidths5);
				nestedTableBalka.addCell(new Cell().add(String.valueOf(vagon.getKbalka1())));
				nestedTableBalka.addCell(new Cell().add(String.valueOf(vagon.getKbalka2())));
				nestedTableBalka.addCell(new Cell().add(String.valueOf(vagon.getKbalka1Nomeri())));
				nestedTableBalka.addCell(new Cell().add(String.valueOf(vagon.getKbalka2Nomeri())));
				nestedTableBalka.addCell(new Cell().add(String.valueOf(vagon.getBalka1())));
				nestedTableBalka.addCell(new Cell().add(String.valueOf(vagon.getBalka2())));
				nestedTableBalka.addCell(new Cell().add(String.valueOf(vagon.getBalka1Nomeri())));
				nestedTableBalka.addCell(new Cell().add(String.valueOf(vagon.getBalka2Nomeri())));

				table.addCell(new Cell().add(nestedTableBalka));

				// G'ildirak juftligi tegishliligiga nested table
				float [] pointColumnWidths8 = {75f,75f,75f,75f};
				Table nestedTableTegisliligi = new Table(pointColumnWidths8);
				nestedTableTegisliligi.addCell(new Cell().add(String.valueOf(vagon.getKgildirak1())));
				nestedTableTegisliligi.addCell(new Cell().add(String.valueOf(vagon.getKgildirak2())));
				nestedTableTegisliligi.addCell(new Cell().add(String.valueOf(vagon.getKgildirak3())));
				nestedTableTegisliligi.addCell(new Cell().add(String.valueOf(vagon.getKgildirak4())));

				nestedTableTegisliligi.addCell(new Cell().add(String.valueOf(vagon.getKgildirak1Nomeri())));
				nestedTableTegisliligi.addCell(new Cell().add(String.valueOf(vagon.getKgildirak2Nomeri())));
				nestedTableTegisliligi.addCell(new Cell().add(String.valueOf(vagon.getKgildirak3Nomeri())));
				nestedTableTegisliligi.addCell(new Cell().add(String.valueOf(vagon.getKgildirak4Nomeri())));

				nestedTableTegisliligi.addCell(new Cell().add(String.valueOf(vagon.getGildirak1())));
				nestedTableTegisliligi.addCell(new Cell().add(String.valueOf(vagon.getGildirak2())));
				nestedTableTegisliligi.addCell(new Cell().add(String.valueOf(vagon.getGildirak3())));
				nestedTableTegisliligi.addCell(new Cell().add(String.valueOf(vagon.getGildirak4())));

				nestedTableTegisliligi.addCell(new Cell().add(String.valueOf(vagon.getGildirak1Nomeri())));
				nestedTableTegisliligi.addCell(new Cell().add(String.valueOf(vagon.getGildirak2Nomeri())));
				nestedTableTegisliligi.addCell(new Cell().add(String.valueOf(vagon.getGildirak3Nomeri())));
				nestedTableTegisliligi.addCell(new Cell().add(String.valueOf(vagon.getGildirak4Nomeri())));

				table.addCell(new Cell().add(nestedTableTegisliligi));
				table.addCell(new Cell().add(vagon.getIzoh()));
			}

			doc.add(paragraph);
			doc.add(table);
			doc.close();
			FileInputStream in = new FileInputStream(file.getAbsoluteFile());
			FileCopyUtils.copy(in, response.getOutputStream());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<VagonMalumot> findAll() {
		return malumotRepository.findAll();
	}
	
	// bosh admin qoshadi
	@Override
	public ApiResponse saveVagon(VagonMalumotDto vagon) {

		Optional<VagonMalumot> exist=	malumotRepository.findByNomer(vagon.getNomer());
		if(exist.isPresent())
			return new ApiResponse(vagon.getNomer() + " nomerli vagon avval saqlangan", false );

		VagonMalumot savedVagon = new VagonMalumot();
		savedVagon.setNomer(vagon.getNomer());
		savedVagon.setDepoNomi(vagon.getDepoNomi());
		savedVagon.setRemontTuri(vagon.getRemontTuri());
		savedVagon.setOxirgiTamirKuni(vagon.getOxirgiTamirKuni());
		savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
		savedVagon.setKorxona(vagon.getKorxona());
		savedVagon.setCountry(vagon.getCountry());

		savedVagon.setKramaOng1(vagon.getKramaOng1());
		savedVagon.setKramaOng1Nomeri(vagon.getKramaOng1Nomeri());

		savedVagon.setKramaOng2(vagon.getKramaOng2());
		savedVagon.setKramaOng2Nomeri(vagon.getKramaOng2Nomeri());

		savedVagon.setKramaChap1(vagon.getKramaChap1());
		savedVagon.setKramaChap1Nomeri(vagon.getKramaChap1Nomeri());

		savedVagon.setKramaChap2(vagon.getKramaChap2());
		savedVagon.setKramaChap2Nomeri(vagon.getKramaChap2Nomeri());

		savedVagon.setKbalka1(vagon.getKbalka1());
		savedVagon.setKbalka1Nomeri(vagon.getKbalka1Nomeri());

		savedVagon.setKbalka2(vagon.getKbalka2());
		savedVagon.setKbalka2Nomeri(vagon.getKbalka2Nomeri());

		savedVagon.setKgildirak1(vagon.getKgildirak1());
		savedVagon.setKgildirak1Nomeri(vagon.getKgildirak1Nomeri());

		savedVagon.setKgildirak2(vagon.getKgildirak2());
		savedVagon.setKgildirak2Nomeri(vagon.getKgildirak2Nomeri());

		savedVagon.setKgildirak3(vagon.getKgildirak3());
		savedVagon.setKgildirak3Nomeri(vagon.getKgildirak3Nomeri());

		savedVagon.setKgildirak4(vagon.getKgildirak4());
		savedVagon.setKgildirak4Nomeri(vagon.getKgildirak4Nomeri());

		savedVagon.setIzoh(vagon.getIzoh());

		LocalDateTime today = LocalDateTime.now();
		LocalDateTime minusHours = today.plusHours(5);
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		currentDate = minusHours.format(myFormatObj);

		savedVagon.setSaqlanganVaqti(currentDate);

		malumotRepository.save(savedVagon);

		return new ApiResponse(vagon.getNomer() + " nomerli vagon saqlandi", true);
	}
	@Override
	public ApiResponse saveVagonSam(VagonMalumotDto vagon) {

		Optional<VagonMalumot> exist=malumotRepository.findByNomer(vagon.getNomer());
		if(exist.isPresent())
			return new ApiResponse(vagon.getNomer() + " nomerli vagon avval saqlangan", false);
		if(!vagon.getDepoNomi().equalsIgnoreCase("VCHD-6"))
			return new ApiResponse("Siz faqat VCHD-6 ga ma'lumot kiritishingiz mumkin", false);

		VagonMalumot savedVagon = new VagonMalumot();
		savedVagon.setNomer(vagon.getNomer());
		savedVagon.setDepoNomi(vagon.getDepoNomi());
		savedVagon.setRemontTuri(vagon.getRemontTuri());
		savedVagon.setOxirgiTamirKuni(vagon.getOxirgiTamirKuni());
		savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
		savedVagon.setKorxona(vagon.getKorxona());
		savedVagon.setCountry(vagon.getCountry());

		savedVagon.setKramaOng1(vagon.getKramaOng1());
		savedVagon.setKramaOng1Nomeri(vagon.getKramaOng1Nomeri());

		savedVagon.setKramaOng2(vagon.getKramaOng2());
		savedVagon.setKramaOng2Nomeri(vagon.getKramaOng2Nomeri());

		savedVagon.setKramaChap1(vagon.getKramaChap1());
		savedVagon.setKramaChap1Nomeri(vagon.getKramaChap1Nomeri());

		savedVagon.setKramaChap2(vagon.getKramaChap2());
		savedVagon.setKramaChap2Nomeri(vagon.getKramaChap2Nomeri());

		savedVagon.setKbalka1(vagon.getKbalka1());
		savedVagon.setKbalka1Nomeri(vagon.getKbalka1Nomeri());

		savedVagon.setKbalka2(vagon.getKbalka2());
		savedVagon.setKbalka2Nomeri(vagon.getKbalka2Nomeri());

		savedVagon.setKgildirak1(vagon.getKgildirak1());
		savedVagon.setKgildirak1Nomeri(vagon.getKgildirak1Nomeri());

		savedVagon.setKgildirak2(vagon.getKgildirak2());
		savedVagon.setKgildirak2Nomeri(vagon.getKgildirak2Nomeri());

		savedVagon.setKgildirak3(vagon.getKgildirak3());
		savedVagon.setKgildirak3Nomeri(vagon.getKgildirak3Nomeri());

		savedVagon.setKgildirak4(vagon.getKgildirak4());
		savedVagon.setKgildirak4Nomeri(vagon.getKgildirak4Nomeri());

		savedVagon.setIzoh(vagon.getIzoh());

		LocalDateTime today = LocalDateTime.now();
		LocalDateTime minusHours = today.plusHours(5);
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		samDate = minusHours.format(myFormatObj);

		savedVagon.setSaqlanganVaqti(samDate);

		Optional<LastActionTimes> optionalMalumotTime = utyTimeRepository.findById(1);
		optionalMalumotTime.get().setSamMalumotDate(samDate);
		utyTimeRepository.save(optionalMalumotTime.get());

		malumotRepository.save(savedVagon);

		return new ApiResponse(vagon.getNomer() + " nomerli vagon saqlandi", true);
	}
	@Override
	public ApiResponse saveVagonHav(VagonMalumotDto vagon) {

		Optional<VagonMalumot> exist = malumotRepository.findByNomer(vagon.getNomer());
		if(exist.isPresent())
			return new ApiResponse(vagon.getNomer() + " nomerli vagon avval saqlangan", false);
		if(!vagon.getDepoNomi().equalsIgnoreCase("VCHD-3"))
			return new ApiResponse("Siz faqat VCHD-3 ga ma'lumot kiritishingiz mumkin", false);

		VagonMalumot savedVagon = new VagonMalumot();
		savedVagon.setNomer(vagon.getNomer());
		savedVagon.setDepoNomi(vagon.getDepoNomi());
		savedVagon.setRemontTuri(vagon.getRemontTuri());
		savedVagon.setOxirgiTamirKuni(vagon.getOxirgiTamirKuni());
		savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
		savedVagon.setKorxona(vagon.getKorxona());
		savedVagon.setCountry(vagon.getCountry());

		savedVagon.setKramaOng1(vagon.getKramaOng1());
		savedVagon.setKramaOng1Nomeri(vagon.getKramaOng1Nomeri());

		savedVagon.setKramaOng2(vagon.getKramaOng2());
		savedVagon.setKramaOng2Nomeri(vagon.getKramaOng2Nomeri());

		savedVagon.setKramaChap1(vagon.getKramaChap1());
		savedVagon.setKramaChap1Nomeri(vagon.getKramaChap1Nomeri());

		savedVagon.setKramaChap2(vagon.getKramaChap2());
		savedVagon.setKramaChap2Nomeri(vagon.getKramaChap2Nomeri());

		savedVagon.setKbalka1(vagon.getKbalka1());
		savedVagon.setKbalka1Nomeri(vagon.getKbalka1Nomeri());

		savedVagon.setKbalka2(vagon.getKbalka2());
		savedVagon.setKbalka2Nomeri(vagon.getKbalka2Nomeri());

		savedVagon.setKgildirak1(vagon.getKgildirak1());
		savedVagon.setKgildirak1Nomeri(vagon.getKgildirak1Nomeri());

		savedVagon.setKgildirak2(vagon.getKgildirak2());
		savedVagon.setKgildirak2Nomeri(vagon.getKgildirak2Nomeri());

		savedVagon.setKgildirak3(vagon.getKgildirak3());
		savedVagon.setKgildirak3Nomeri(vagon.getKgildirak3Nomeri());

		savedVagon.setKgildirak4(vagon.getKgildirak4());
		savedVagon.setKgildirak4Nomeri(vagon.getKgildirak4Nomeri());

		savedVagon.setIzoh(vagon.getIzoh());

		LocalDateTime today = LocalDateTime.now();
		LocalDateTime minusHours = today.plusHours(5);
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		havDate = minusHours.format(myFormatObj);

		savedVagon.setSaqlanganVaqti(havDate);

		Optional<LastActionTimes> optionalMalumotTime = utyTimeRepository.findById(1);
		optionalMalumotTime.get().setHavMalumotDate(havDate);
		utyTimeRepository.save(optionalMalumotTime.get());

		malumotRepository.save(savedVagon);

		return new ApiResponse(vagon.getNomer() + " nomerli vagon saqlandi", true);
	}

	@Override
	public ApiResponse saveVagonAndj(VagonMalumotDto vagon) {

		Optional<VagonMalumot> exist = malumotRepository.findByNomer(vagon.getNomer());
		if(exist.isPresent())
			return new ApiResponse(vagon.getNomer() + " nomerli vagon avval saqlangan", false);
		if(!vagon.getDepoNomi().equalsIgnoreCase("VCHD-5"))
			return new ApiResponse("Siz faqat VCHD-5 ga ma'lumot kiritishingiz mumkin", false);

		VagonMalumot savedVagon = new VagonMalumot();
		savedVagon.setNomer(vagon.getNomer());
		savedVagon.setDepoNomi(vagon.getDepoNomi());
		savedVagon.setRemontTuri(vagon.getRemontTuri());
		savedVagon.setOxirgiTamirKuni(vagon.getOxirgiTamirKuni());
		savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
		savedVagon.setKorxona(vagon.getKorxona());
		savedVagon.setCountry(vagon.getCountry());

		savedVagon.setKramaOng1(vagon.getKramaOng1());
		savedVagon.setKramaOng1Nomeri(vagon.getKramaOng1Nomeri());

		savedVagon.setKramaOng2(vagon.getKramaOng2());
		savedVagon.setKramaOng2Nomeri(vagon.getKramaOng2Nomeri());

		savedVagon.setKramaChap1(vagon.getKramaChap1());
		savedVagon.setKramaChap1Nomeri(vagon.getKramaChap1Nomeri());

		savedVagon.setKramaChap2(vagon.getKramaChap2());
		savedVagon.setKramaChap2Nomeri(vagon.getKramaChap2Nomeri());

		savedVagon.setKbalka1(vagon.getKbalka1());
		savedVagon.setKbalka1Nomeri(vagon.getKbalka1Nomeri());

		savedVagon.setKbalka2(vagon.getKbalka2());
		savedVagon.setKbalka2Nomeri(vagon.getKbalka2Nomeri());

		savedVagon.setKgildirak1(vagon.getKgildirak1());
		savedVagon.setKgildirak1Nomeri(vagon.getKgildirak1Nomeri());

		savedVagon.setKgildirak2(vagon.getKgildirak2());
		savedVagon.setKgildirak2Nomeri(vagon.getKgildirak2Nomeri());

		savedVagon.setKgildirak3(vagon.getKgildirak3());
		savedVagon.setKgildirak3Nomeri(vagon.getKgildirak3Nomeri());

		savedVagon.setKgildirak4(vagon.getKgildirak4());
		savedVagon.setKgildirak4Nomeri(vagon.getKgildirak4Nomeri());

		savedVagon.setIzoh(vagon.getIzoh());
		LocalDateTime today = LocalDateTime.now();
		LocalDateTime minusHours = today.plusHours(5);
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		andjDate = minusHours.format(myFormatObj);

		savedVagon.setSaqlanganVaqti(andjDate);

		Optional<LastActionTimes> optionalMalumotTime = utyTimeRepository.findById(1);
		optionalMalumotTime.get().setAndjMalumotDate(andjDate);
		utyTimeRepository.save(optionalMalumotTime.get());

		malumotRepository.save(savedVagon);

		return new ApiResponse(vagon.getNomer() + " nomerli vagon saqlandi", true);
	}

	@Override
	public ApiResponse saveVagonChiqish(VagonMalumotChiqishDto vagon, long id) {

		Optional<VagonMalumot> exist = malumotRepository.findById(id);
		if(!exist.isPresent())
			return new ApiResponse( "Bu nomerli vagon topilmadi", false);

		VagonMalumot savedVagon = exist.get();

		if (!savedVagon.getKorxona().equals("O'TY(ГАЖК)")) {

			List<VagonMalumot> korxonalar = malumotRepository.findAllByEgasi("MDH(СНГ)","Sanoat(ПРОМ)");

			for (VagonMalumot korxona : korxonalar) {

				if (vagon.getRamaOng1Nomeri().equals(korxona.getKramaOng1Nomeri()) || vagon.getRamaOng1Nomeri().equals(korxona.getKramaOng2Nomeri()) || vagon.getRamaOng1Nomeri().equals(korxona.getKramaChap1Nomeri()) || vagon.getRamaOng1Nomeri().equals(korxona.getKramaChap2Nomeri()))
					savedVagon.setRamaOng1Nomeri(vagon.getRamaOng1Nomeri());
				if (vagon.getRamaOng2Nomeri().equals(korxona.getKramaOng1Nomeri()) || vagon.getRamaOng2Nomeri().equals(korxona.getKramaOng2Nomeri()) || vagon.getRamaOng2Nomeri().equals(korxona.getKramaChap1Nomeri()) || vagon.getRamaOng2Nomeri().equals(korxona.getKramaChap2Nomeri()))
					savedVagon.setRamaOng2Nomeri(vagon.getRamaOng2Nomeri());
				if (vagon.getRamaChap1Nomeri().equals(korxona.getKramaOng1Nomeri()) || vagon.getRamaChap1Nomeri().equals(korxona.getKramaOng2Nomeri()) || vagon.getRamaChap1Nomeri().equals(korxona.getKramaChap1Nomeri()) || vagon.getRamaChap1Nomeri().equals(korxona.getKramaChap2Nomeri()))
					savedVagon.setRamaChap1Nomeri(vagon.getRamaChap1Nomeri());
				if (vagon.getRamaChap2Nomeri().equals(korxona.getKramaOng1Nomeri()) || vagon.getRamaChap2Nomeri().equals(korxona.getKramaOng2Nomeri()) || vagon.getRamaChap2Nomeri().equals(korxona.getKramaChap1Nomeri()) || vagon.getRamaChap2Nomeri().equals(korxona.getKramaChap2Nomeri()))
					savedVagon.setRamaChap2Nomeri(vagon.getRamaChap2Nomeri());
				if (vagon.getBalka1Nomeri().equals(korxona.getKbalka1Nomeri()) || vagon.getBalka1Nomeri().equals(korxona.getKbalka2Nomeri()))
					savedVagon.setBalka1Nomeri(vagon.getBalka1Nomeri());
				if (vagon.getBalka2Nomeri().equals(korxona.getKbalka1Nomeri()) || vagon.getBalka2Nomeri().equals(korxona.getKbalka2Nomeri()))
					savedVagon.setBalka2Nomeri(vagon.getBalka2Nomeri());
				if (vagon.getGildirak1Nomeri().equals(korxona.getKgildirak1Nomeri()) || vagon.getGildirak1Nomeri().equals(korxona.getKgildirak2Nomeri()) || vagon.getGildirak1Nomeri().equals(korxona.getKgildirak3Nomeri()) || vagon.getGildirak1Nomeri().equals(korxona.getKgildirak4Nomeri()))
					savedVagon.setGildirak1Nomeri(vagon.getGildirak1Nomeri());
				if (vagon.getGildirak2Nomeri().equals(korxona.getKgildirak1Nomeri()) || vagon.getGildirak2Nomeri().equals(korxona.getKgildirak2Nomeri()) || vagon.getGildirak2Nomeri().equals(korxona.getKgildirak3Nomeri()) || vagon.getGildirak2Nomeri().equals(korxona.getKgildirak4Nomeri()))
					savedVagon.setGildirak2Nomeri(vagon.getGildirak2Nomeri());
				if (vagon.getGildirak3Nomeri().equals(korxona.getKgildirak1Nomeri()) || vagon.getGildirak3Nomeri().equals(korxona.getKgildirak2Nomeri()) || vagon.getGildirak3Nomeri().equals(korxona.getKgildirak3Nomeri()) || vagon.getGildirak3Nomeri().equals(korxona.getKgildirak4Nomeri()))
					savedVagon.setGildirak3Nomeri(vagon.getGildirak3Nomeri());
				if (vagon.getGildirak4Nomeri().equals(korxona.getKgildirak1Nomeri()) || vagon.getGildirak4Nomeri().equals(korxona.getKgildirak2Nomeri()) || vagon.getGildirak4Nomeri().equals(korxona.getKgildirak3Nomeri()) || vagon.getGildirak4Nomeri().equals(korxona.getKgildirak4Nomeri()))
					savedVagon.setGildirak4Nomeri(vagon.getGildirak4Nomeri());

			}
		}else {
			savedVagon.setRamaOng1Nomeri(vagon.getRamaOng1Nomeri());
			savedVagon.setRamaOng2Nomeri(vagon.getRamaOng2Nomeri());
			savedVagon.setRamaChap1Nomeri(vagon.getRamaChap1Nomeri());
			savedVagon.setRamaChap2Nomeri(vagon.getRamaChap2Nomeri());
			savedVagon.setBalka1Nomeri(vagon.getBalka1Nomeri());
			savedVagon.setBalka2Nomeri(vagon.getBalka2Nomeri());
			savedVagon.setGildirak1Nomeri(vagon.getGildirak1Nomeri());
			savedVagon.setGildirak2Nomeri(vagon.getGildirak2Nomeri());
			savedVagon.setGildirak3Nomeri(vagon.getGildirak3Nomeri());
			savedVagon.setGildirak4Nomeri(vagon.getGildirak4Nomeri());
		}

		savedVagon.setRamaOng1(vagon.getRamaOng1());

		savedVagon.setRamaOng2(vagon.getRamaOng2());

		savedVagon.setRamaChap1(vagon.getRamaChap1());

		savedVagon.setRamaChap2(vagon.getRamaChap2());

		savedVagon.setBalka1(vagon.getBalka1());

		savedVagon.setBalka2(vagon.getBalka2());

		savedVagon.setGildirak1(vagon.getGildirak1());

		savedVagon.setGildirak2(vagon.getGildirak2());

		savedVagon.setGildirak3(vagon.getGildirak3());

		savedVagon.setGildirak4(vagon.getGildirak4());

		savedVagon.setIzohChiqish(vagon.getIzohChiqish());

		LocalDateTime today = LocalDateTime.now();
		LocalDateTime minusHours = today.plusHours(5);
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		currentDate = minusHours.format(myFormatObj);

		savedVagon.setSaqlanganVaqtiChiqish(currentDate);

		if (savedVagon.getRamaOng1Nomeri() ==null){

			savedVagon.setRamaOng1(null);

			savedVagon.setRamaOng2(null);
			savedVagon.setRamaOng2Nomeri(null);

			savedVagon.setRamaChap1(null);
			savedVagon.setRamaChap1Nomeri(null);

			savedVagon.setRamaChap2(null);
			savedVagon.setRamaChap2Nomeri(null);

			savedVagon.setBalka1(null);
			savedVagon.setBalka1Nomeri(null);

			savedVagon.setBalka2(null);
			savedVagon.setBalka2Nomeri(null);

			savedVagon.setGildirak1(null);
			savedVagon.setGildirak1Nomeri(null);

			savedVagon.setGildirak2(null);
			savedVagon.setGildirak2Nomeri(null);

			savedVagon.setGildirak3(null);
			savedVagon.setGildirak3Nomeri(null);

			savedVagon.setGildirak4(null);
			savedVagon.setGildirak4Nomeri(null);

			savedVagon.setSaqlanganVaqtiChiqish(null);

//			return new ApiResponse(vagon.getRamaOng1Nomeri() + " (1-o'ng) nomerli yon rama \."" + savedVagon.getKorxona() + "\" vagonlariga tegishli emas yoki bazaga saqlanmagan", false);
			return new ApiResponse(vagon.getRamaOng1Nomeri() + " (1-o'ng) nomerli yon rama  bazaga saqlanmagan", false);
		}
		if (savedVagon.getRamaOng2Nomeri() ==null){

			savedVagon.setRamaOng1(null);
			savedVagon.setRamaOng1Nomeri(null);

			savedVagon.setRamaOng2(null);

			savedVagon.setRamaChap1(null);
			savedVagon.setRamaChap1Nomeri(null);

			savedVagon.setRamaChap2(null);
			savedVagon.setRamaChap2Nomeri(null);

			savedVagon.setBalka1(null);
			savedVagon.setBalka1Nomeri(null);

			savedVagon.setBalka2(null);
			savedVagon.setBalka2Nomeri(null);

			savedVagon.setGildirak1(null);
			savedVagon.setGildirak1Nomeri(null);

			savedVagon.setGildirak2(null);
			savedVagon.setGildirak2Nomeri(null);

			savedVagon.setGildirak3(null);
			savedVagon.setGildirak3Nomeri(null);

			savedVagon.setGildirak4(null);
			savedVagon.setGildirak4Nomeri(null);

			savedVagon.setSaqlanganVaqtiChiqish(null);

			return new ApiResponse(vagon.getRamaOng2Nomeri() + " (2-o'ng) nomerli yon rama  bazaga saqlanmagan", false);
		}
		if (savedVagon.getRamaChap1Nomeri() ==null){

			savedVagon.setRamaOng1(null);
			savedVagon.setRamaOng1Nomeri(null);

			savedVagon.setRamaOng2(null);
			savedVagon.setRamaOng2Nomeri(null);

			savedVagon.setRamaChap1(null);

			savedVagon.setRamaChap2(null);
			savedVagon.setRamaChap2Nomeri(null);

			savedVagon.setBalka1(null);
			savedVagon.setBalka1Nomeri(null);

			savedVagon.setBalka2(null);
			savedVagon.setBalka2Nomeri(null);

			savedVagon.setGildirak1(null);
			savedVagon.setGildirak1Nomeri(null);

			savedVagon.setGildirak2(null);
			savedVagon.setGildirak2Nomeri(null);

			savedVagon.setGildirak3(null);
			savedVagon.setGildirak3Nomeri(null);

			savedVagon.setGildirak4(null);
			savedVagon.setGildirak4Nomeri(null);

			savedVagon.setSaqlanganVaqtiChiqish(null);

			return new ApiResponse(vagon.getRamaChap1Nomeri() + " (1-chap) nomerli yon rama  bazaga saqlanmagan", false);
		}
		if (savedVagon.getRamaChap2Nomeri() ==null){

			savedVagon.setRamaOng1(null);
			savedVagon.setRamaOng1Nomeri(null);

			savedVagon.setRamaOng2(null);
			savedVagon.setRamaOng2Nomeri(null);

			savedVagon.setRamaChap1(null);
			savedVagon.setRamaChap1Nomeri(null);

			savedVagon.setRamaChap2(null);

			savedVagon.setBalka1(null);
			savedVagon.setBalka1Nomeri(null);

			savedVagon.setBalka2(null);
			savedVagon.setBalka2Nomeri(null);

			savedVagon.setGildirak1(null);
			savedVagon.setGildirak1Nomeri(null);

			savedVagon.setGildirak2(null);
			savedVagon.setGildirak2Nomeri(null);

			savedVagon.setGildirak3(null);
			savedVagon.setGildirak3Nomeri(null);

			savedVagon.setGildirak4(null);
			savedVagon.setGildirak4Nomeri(null);

			savedVagon.setSaqlanganVaqtiChiqish(null);

			return new ApiResponse(vagon.getRamaChap2Nomeri() + " (2-chap) nomerli yon rama  bazaga saqlanmagan", false);
		}
		if (savedVagon.getBalka1Nomeri() ==null){

			savedVagon.setRamaOng1(null);
			savedVagon.setRamaOng1Nomeri(null);

			savedVagon.setRamaOng2(null);
			savedVagon.setRamaOng2Nomeri(null);

			savedVagon.setRamaChap1(null);
			savedVagon.setRamaChap1Nomeri(null);

			savedVagon.setRamaChap2(null);
			savedVagon.setRamaChap2Nomeri(null);

			savedVagon.setBalka1(null);

			savedVagon.setBalka2(null);
			savedVagon.setBalka2Nomeri(null);

			savedVagon.setGildirak1(null);
			savedVagon.setGildirak1Nomeri(null);

			savedVagon.setGildirak2(null);
			savedVagon.setGildirak2Nomeri(null);

			savedVagon.setGildirak3(null);
			savedVagon.setGildirak3Nomeri(null);

			savedVagon.setGildirak4(null);
			savedVagon.setGildirak4Nomeri(null);

			savedVagon.setSaqlanganVaqtiChiqish(null);

			return new ApiResponse(vagon.getBalka1Nomeri() + " (1-balka) nomerli ressor usti balka  bazaga saqlanmagan", false);
		}
		if (savedVagon.getBalka2Nomeri() ==null){

			savedVagon.setRamaOng1(null);
			savedVagon.setRamaOng1Nomeri(null);

			savedVagon.setRamaOng2(null);
			savedVagon.setRamaOng2Nomeri(null);

			savedVagon.setRamaChap1(null);
			savedVagon.setRamaChap1Nomeri(null);

			savedVagon.setRamaChap2(null);
			savedVagon.setRamaChap2Nomeri(null);

			savedVagon.setBalka1(null);
			savedVagon.setBalka1Nomeri(null);

			savedVagon.setBalka2(null);

			savedVagon.setGildirak1(null);
			savedVagon.setGildirak1Nomeri(null);

			savedVagon.setGildirak2(null);
			savedVagon.setGildirak2Nomeri(null);

			savedVagon.setGildirak3(null);
			savedVagon.setGildirak3Nomeri(null);

			savedVagon.setGildirak4(null);
			savedVagon.setGildirak4Nomeri(null);

			savedVagon.setSaqlanganVaqtiChiqish(null);

			return new ApiResponse(vagon.getBalka2Nomeri() + " (2-balka) nomerli ressor usti balka  bazaga saqlanmagan", false);
		}

		if (savedVagon.getGildirak1Nomeri() ==null){

			savedVagon.setRamaOng1(null);
			savedVagon.setRamaOng1Nomeri(null);

			savedVagon.setRamaOng2(null);
			savedVagon.setRamaOng2Nomeri(null);

			savedVagon.setRamaChap1(null);
			savedVagon.setRamaChap1Nomeri(null);

			savedVagon.setRamaChap2(null);
			savedVagon.setRamaChap2Nomeri(null);

			savedVagon.setBalka1(null);
			savedVagon.setBalka1Nomeri(null);

			savedVagon.setBalka2(null);
			savedVagon.setBalka2Nomeri(null);

			savedVagon.setGildirak1(null);

			savedVagon.setGildirak2(null);
			savedVagon.setGildirak2Nomeri(null);

			savedVagon.setGildirak3(null);
			savedVagon.setGildirak3Nomeri(null);

			savedVagon.setGildirak4(null);
			savedVagon.setGildirak4Nomeri(null);

			savedVagon.setSaqlanganVaqtiChiqish(null);

			return new ApiResponse(vagon.getGildirak1Nomeri() + " (1-g'ildirak juftligi) nomerli g'ildirak juftligi  bazaga saqlanmagan", false);
		}
		if (savedVagon.getGildirak2Nomeri() ==null){

			savedVagon.setRamaOng1(null);
			savedVagon.setRamaOng1Nomeri(null);

			savedVagon.setRamaOng2(null);
			savedVagon.setRamaOng2Nomeri(null);

			savedVagon.setRamaChap1(null);
			savedVagon.setRamaChap1Nomeri(null);

			savedVagon.setRamaChap2(null);
			savedVagon.setRamaChap2Nomeri(null);

			savedVagon.setBalka1(null);
			savedVagon.setBalka1Nomeri(null);

			savedVagon.setBalka2(null);
			savedVagon.setBalka2Nomeri(null);

			savedVagon.setGildirak1(null);
			savedVagon.setGildirak1Nomeri(null);

			savedVagon.setGildirak2(null);

			savedVagon.setGildirak3(null);
			savedVagon.setGildirak3Nomeri(null);

			savedVagon.setGildirak4(null);
			savedVagon.setGildirak4Nomeri(null);

			savedVagon.setSaqlanganVaqtiChiqish(null);

			return new ApiResponse(vagon.getGildirak2Nomeri() + " (2-g'ildirak juftligi) nomerli g'ildirak juftligi  bazaga saqlanmagan", false);
		}
		if (savedVagon.getGildirak3Nomeri() ==null){

			savedVagon.setRamaOng1(null);
			savedVagon.setRamaOng1Nomeri(null);

			savedVagon.setRamaOng2(null);
			savedVagon.setRamaOng2Nomeri(null);

			savedVagon.setRamaChap1(null);
			savedVagon.setRamaChap1Nomeri(null);

			savedVagon.setRamaChap2(null);
			savedVagon.setRamaChap2Nomeri(null);

			savedVagon.setBalka1(null);
			savedVagon.setBalka1Nomeri(null);

			savedVagon.setBalka2(null);
			savedVagon.setBalka2Nomeri(null);

			savedVagon.setGildirak1(null);
			savedVagon.setGildirak1Nomeri(null);

			savedVagon.setGildirak2(null);
			savedVagon.setGildirak2Nomeri(null);

			savedVagon.setGildirak3(null);

			savedVagon.setGildirak4(null);
			savedVagon.setGildirak4Nomeri(null);

			savedVagon.setSaqlanganVaqtiChiqish(null);

			return new ApiResponse(vagon.getGildirak3Nomeri() + " (3-g'ildirak juftligi) nomerli g'ildirak juftligi  bazaga saqlanmagan", false);
		}

		if (savedVagon.getGildirak4Nomeri() ==null){

			savedVagon.setRamaOng1(null);
			savedVagon.setRamaOng1Nomeri(null);

			savedVagon.setRamaOng2(null);
			savedVagon.setRamaOng2Nomeri(null);

			savedVagon.setRamaChap1(null);
			savedVagon.setRamaChap1Nomeri(null);

			savedVagon.setRamaChap2(null);
			savedVagon.setRamaChap2Nomeri(null);

			savedVagon.setBalka1(null);
			savedVagon.setBalka1Nomeri(null);

			savedVagon.setBalka2(null);
			savedVagon.setBalka2Nomeri(null);

			savedVagon.setGildirak1(null);
			savedVagon.setGildirak1Nomeri(null);

			savedVagon.setGildirak2(null);
			savedVagon.setGildirak2Nomeri(null);

			savedVagon.setGildirak3(null);
			savedVagon.setGildirak3Nomeri(null);

			savedVagon.setGildirak4(null);

			savedVagon.setSaqlanganVaqtiChiqish(null);

			return new ApiResponse(vagon.getGildirak4Nomeri() + " (4-g'ildirak juftligi) nomerli g'ildirak juftligi  bazaga saqlanmagan", false);
		}

		malumotRepository.save(savedVagon);

		return new ApiResponse(savedVagon.getNomer() + " nomerli vagon saqlandi", true);

	}

	@Override
	public ApiResponse saveVagonSamChiqish(VagonMalumotChiqishDto vagon, long id) {

		Optional<VagonMalumot> exist = malumotRepository.findById(id);
		if(!exist.isPresent())
			return new ApiResponse( "Bu nomerli vagon topilmadi", false);

		VagonMalumot savedVagon = exist.get();

		if (!savedVagon.getDepoNomi().equalsIgnoreCase("VCHD-6"))
			return new ApiResponse( "Siz faqat VCHD-6 dagi vagonlarning chiqish ma'lumotlarini kiritishingiz mumkin", false);


		if (!savedVagon.getKorxona().equals("O'TY(ГАЖК)")) {

			List<VagonMalumot> korxonalar = malumotRepository.findAllByEgasi("MDH(СНГ)","Sanoat(ПРОМ)");

			for (VagonMalumot korxona : korxonalar) {

				if (vagon.getRamaOng1Nomeri().equals(korxona.getKramaOng1Nomeri()) || vagon.getRamaOng1Nomeri().equals(korxona.getKramaOng2Nomeri()) || vagon.getRamaOng1Nomeri().equals(korxona.getKramaChap1Nomeri()) || vagon.getRamaOng1Nomeri().equals(korxona.getKramaChap2Nomeri()))
					savedVagon.setRamaOng1Nomeri(vagon.getRamaOng1Nomeri());
				if (vagon.getRamaOng2Nomeri().equals(korxona.getKramaOng1Nomeri()) || vagon.getRamaOng2Nomeri().equals(korxona.getKramaOng2Nomeri()) || vagon.getRamaOng2Nomeri().equals(korxona.getKramaChap1Nomeri()) || vagon.getRamaOng2Nomeri().equals(korxona.getKramaChap2Nomeri()))
					savedVagon.setRamaOng2Nomeri(vagon.getRamaOng2Nomeri());
				if (vagon.getRamaChap1Nomeri().equals(korxona.getKramaOng1Nomeri()) || vagon.getRamaChap1Nomeri().equals(korxona.getKramaOng2Nomeri()) || vagon.getRamaChap1Nomeri().equals(korxona.getKramaChap1Nomeri()) || vagon.getRamaChap1Nomeri().equals(korxona.getKramaChap2Nomeri()))
					savedVagon.setRamaChap1Nomeri(vagon.getRamaChap1Nomeri());
				if (vagon.getRamaChap2Nomeri().equals(korxona.getKramaOng1Nomeri()) || vagon.getRamaChap2Nomeri().equals(korxona.getKramaOng2Nomeri()) || vagon.getRamaChap2Nomeri().equals(korxona.getKramaChap1Nomeri()) || vagon.getRamaChap2Nomeri().equals(korxona.getKramaChap2Nomeri()))
					savedVagon.setRamaChap2Nomeri(vagon.getRamaChap2Nomeri());
				if (vagon.getBalka1Nomeri().equals(korxona.getKbalka1Nomeri()) || vagon.getBalka1Nomeri().equals(korxona.getKbalka2Nomeri()))
					savedVagon.setBalka1Nomeri(vagon.getBalka1Nomeri());
				if (vagon.getBalka2Nomeri().equals(korxona.getKbalka1Nomeri()) || vagon.getBalka2Nomeri().equals(korxona.getKbalka2Nomeri()))
					savedVagon.setBalka2Nomeri(vagon.getBalka2Nomeri());
				if (vagon.getGildirak1Nomeri().equals(korxona.getKgildirak1Nomeri()) || vagon.getGildirak1Nomeri().equals(korxona.getKgildirak2Nomeri()) || vagon.getGildirak1Nomeri().equals(korxona.getKgildirak3Nomeri()) || vagon.getGildirak1Nomeri().equals(korxona.getKgildirak4Nomeri()))
					savedVagon.setGildirak1Nomeri(vagon.getGildirak1Nomeri());
				if (vagon.getGildirak2Nomeri().equals(korxona.getKgildirak1Nomeri()) || vagon.getGildirak2Nomeri().equals(korxona.getKgildirak2Nomeri()) || vagon.getGildirak2Nomeri().equals(korxona.getKgildirak3Nomeri()) || vagon.getGildirak2Nomeri().equals(korxona.getKgildirak4Nomeri()))
					savedVagon.setGildirak2Nomeri(vagon.getGildirak2Nomeri());
				if (vagon.getGildirak3Nomeri().equals(korxona.getKgildirak1Nomeri()) || vagon.getGildirak3Nomeri().equals(korxona.getKgildirak2Nomeri()) || vagon.getGildirak3Nomeri().equals(korxona.getKgildirak3Nomeri()) || vagon.getGildirak3Nomeri().equals(korxona.getKgildirak4Nomeri()))
					savedVagon.setGildirak3Nomeri(vagon.getGildirak3Nomeri());
				if (vagon.getGildirak4Nomeri().equals(korxona.getKgildirak1Nomeri()) || vagon.getGildirak4Nomeri().equals(korxona.getKgildirak2Nomeri()) || vagon.getGildirak4Nomeri().equals(korxona.getKgildirak3Nomeri()) || vagon.getGildirak4Nomeri().equals(korxona.getKgildirak4Nomeri()))
					savedVagon.setGildirak4Nomeri(vagon.getGildirak4Nomeri());

			}
		}else {
			savedVagon.setRamaOng1Nomeri(vagon.getRamaOng1Nomeri());
			savedVagon.setRamaOng2Nomeri(vagon.getRamaOng2Nomeri());
			savedVagon.setRamaChap1Nomeri(vagon.getRamaChap1Nomeri());
			savedVagon.setRamaChap2Nomeri(vagon.getRamaChap2Nomeri());
			savedVagon.setBalka1Nomeri(vagon.getBalka1Nomeri());
			savedVagon.setBalka2Nomeri(vagon.getBalka2Nomeri());
			savedVagon.setGildirak1Nomeri(vagon.getGildirak1Nomeri());
			savedVagon.setGildirak2Nomeri(vagon.getGildirak2Nomeri());
			savedVagon.setGildirak3Nomeri(vagon.getGildirak3Nomeri());
			savedVagon.setGildirak4Nomeri(vagon.getGildirak4Nomeri());
		}

		savedVagon.setRamaOng1(vagon.getRamaOng1());

		savedVagon.setRamaOng2(vagon.getRamaOng2());

		savedVagon.setRamaChap1(vagon.getRamaChap1());

		savedVagon.setRamaChap2(vagon.getRamaChap2());

		savedVagon.setBalka1(vagon.getBalka1());

		savedVagon.setBalka2(vagon.getBalka2());

		savedVagon.setGildirak1(vagon.getGildirak1());

		savedVagon.setGildirak2(vagon.getGildirak2());

		savedVagon.setGildirak3(vagon.getGildirak3());

		savedVagon.setGildirak4(vagon.getGildirak4());

		savedVagon.setIzohChiqish(vagon.getIzohChiqish());

		LocalDateTime today = LocalDateTime.now();
		LocalDateTime minusHours = today.plusHours(5);
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		samDate = minusHours.format(myFormatObj);

		savedVagon.setSaqlanganVaqtiChiqish(samDate);

		if (savedVagon.getRamaOng1Nomeri() ==null){

			savedVagon.setRamaOng1(null);

			savedVagon.setRamaOng2(null);
			savedVagon.setRamaOng2Nomeri(null);

			savedVagon.setRamaChap1(null);
			savedVagon.setRamaChap1Nomeri(null);

			savedVagon.setRamaChap2(null);
			savedVagon.setRamaChap2Nomeri(null);

			savedVagon.setBalka1(null);
			savedVagon.setBalka1Nomeri(null);

			savedVagon.setBalka2(null);
			savedVagon.setBalka2Nomeri(null);

			savedVagon.setGildirak1(null);
			savedVagon.setGildirak1Nomeri(null);

			savedVagon.setGildirak2(null);
			savedVagon.setGildirak2Nomeri(null);

			savedVagon.setGildirak3(null);
			savedVagon.setGildirak3Nomeri(null);

			savedVagon.setGildirak4(null);
			savedVagon.setGildirak4Nomeri(null);

			savedVagon.setSaqlanganVaqtiChiqish(null);

			return new ApiResponse(vagon.getRamaOng1Nomeri() + " (1-o'ng) nomerli yon rama  bazaga saqlanmagan", false);
		}
		if (savedVagon.getRamaOng2Nomeri() ==null){

			savedVagon.setRamaOng1(null);
			savedVagon.setRamaOng1Nomeri(null);

			savedVagon.setRamaOng2(null);

			savedVagon.setRamaChap1(null);
			savedVagon.setRamaChap1Nomeri(null);

			savedVagon.setRamaChap2(null);
			savedVagon.setRamaChap2Nomeri(null);

			savedVagon.setBalka1(null);
			savedVagon.setBalka1Nomeri(null);

			savedVagon.setBalka2(null);
			savedVagon.setBalka2Nomeri(null);

			savedVagon.setGildirak1(null);
			savedVagon.setGildirak1Nomeri(null);

			savedVagon.setGildirak2(null);
			savedVagon.setGildirak2Nomeri(null);

			savedVagon.setGildirak3(null);
			savedVagon.setGildirak3Nomeri(null);

			savedVagon.setGildirak4(null);
			savedVagon.setGildirak4Nomeri(null);

			savedVagon.setSaqlanganVaqtiChiqish(null);

			return new ApiResponse(vagon.getRamaOng2Nomeri() + " (2-o'ng) nomerli yon rama  bazaga saqlanmagan", false);
		}
		if (savedVagon.getRamaChap1Nomeri() ==null){

			savedVagon.setRamaOng1(null);
			savedVagon.setRamaOng1Nomeri(null);

			savedVagon.setRamaOng2(null);
			savedVagon.setRamaOng2Nomeri(null);

			savedVagon.setRamaChap1(null);

			savedVagon.setRamaChap2(null);
			savedVagon.setRamaChap2Nomeri(null);

			savedVagon.setBalka1(null);
			savedVagon.setBalka1Nomeri(null);

			savedVagon.setBalka2(null);
			savedVagon.setBalka2Nomeri(null);

			savedVagon.setGildirak1(null);
			savedVagon.setGildirak1Nomeri(null);

			savedVagon.setGildirak2(null);
			savedVagon.setGildirak2Nomeri(null);

			savedVagon.setGildirak3(null);
			savedVagon.setGildirak3Nomeri(null);

			savedVagon.setGildirak4(null);
			savedVagon.setGildirak4Nomeri(null);

			savedVagon.setSaqlanganVaqtiChiqish(null);

			return new ApiResponse(vagon.getRamaChap1Nomeri() + " (1-chap) nomerli yon rama  bazaga saqlanmagan", false);
		}
		if (savedVagon.getRamaChap2Nomeri() ==null){

			savedVagon.setRamaOng1(null);
			savedVagon.setRamaOng1Nomeri(null);

			savedVagon.setRamaOng2(null);
			savedVagon.setRamaOng2Nomeri(null);

			savedVagon.setRamaChap1(null);
			savedVagon.setRamaChap1Nomeri(null);

			savedVagon.setRamaChap2(null);

			savedVagon.setBalka1(null);
			savedVagon.setBalka1Nomeri(null);

			savedVagon.setBalka2(null);
			savedVagon.setBalka2Nomeri(null);

			savedVagon.setGildirak1(null);
			savedVagon.setGildirak1Nomeri(null);

			savedVagon.setGildirak2(null);
			savedVagon.setGildirak2Nomeri(null);

			savedVagon.setGildirak3(null);
			savedVagon.setGildirak3Nomeri(null);

			savedVagon.setGildirak4(null);
			savedVagon.setGildirak4Nomeri(null);

			savedVagon.setSaqlanganVaqtiChiqish(null);

			return new ApiResponse(vagon.getRamaChap2Nomeri() + " (2-chap) nomerli yon rama  bazaga saqlanmagan", false);
		}
		if (savedVagon.getBalka1Nomeri() ==null){

			savedVagon.setRamaOng1(null);
			savedVagon.setRamaOng1Nomeri(null);

			savedVagon.setRamaOng2(null);
			savedVagon.setRamaOng2Nomeri(null);

			savedVagon.setRamaChap1(null);
			savedVagon.setRamaChap1Nomeri(null);

			savedVagon.setRamaChap2(null);
			savedVagon.setRamaChap2Nomeri(null);

			savedVagon.setBalka1(null);

			savedVagon.setBalka2(null);
			savedVagon.setBalka2Nomeri(null);

			savedVagon.setGildirak1(null);
			savedVagon.setGildirak1Nomeri(null);

			savedVagon.setGildirak2(null);
			savedVagon.setGildirak2Nomeri(null);

			savedVagon.setGildirak3(null);
			savedVagon.setGildirak3Nomeri(null);

			savedVagon.setGildirak4(null);
			savedVagon.setGildirak4Nomeri(null);

			savedVagon.setSaqlanganVaqtiChiqish(null);

			return new ApiResponse(vagon.getBalka1Nomeri() + " (1-balka) nomerli ressor usti balka  bazaga saqlanmagan", false);
		}
		if (savedVagon.getBalka2Nomeri() ==null){

			savedVagon.setRamaOng1(null);
			savedVagon.setRamaOng1Nomeri(null);

			savedVagon.setRamaOng2(null);
			savedVagon.setRamaOng2Nomeri(null);

			savedVagon.setRamaChap1(null);
			savedVagon.setRamaChap1Nomeri(null);

			savedVagon.setRamaChap2(null);
			savedVagon.setRamaChap2Nomeri(null);

			savedVagon.setBalka1(null);
			savedVagon.setBalka1Nomeri(null);

			savedVagon.setBalka2(null);

			savedVagon.setGildirak1(null);
			savedVagon.setGildirak1Nomeri(null);

			savedVagon.setGildirak2(null);
			savedVagon.setGildirak2Nomeri(null);

			savedVagon.setGildirak3(null);
			savedVagon.setGildirak3Nomeri(null);

			savedVagon.setGildirak4(null);
			savedVagon.setGildirak4Nomeri(null);

			savedVagon.setSaqlanganVaqtiChiqish(null);

			return new ApiResponse(vagon.getBalka2Nomeri() + " (2-balka) nomerli ressor usti balka  bazaga saqlanmagan", false);
		}

		if (savedVagon.getGildirak1Nomeri() ==null){

			savedVagon.setRamaOng1(null);
			savedVagon.setRamaOng1Nomeri(null);

			savedVagon.setRamaOng2(null);
			savedVagon.setRamaOng2Nomeri(null);

			savedVagon.setRamaChap1(null);
			savedVagon.setRamaChap1Nomeri(null);

			savedVagon.setRamaChap2(null);
			savedVagon.setRamaChap2Nomeri(null);

			savedVagon.setBalka1(null);
			savedVagon.setBalka1Nomeri(null);

			savedVagon.setBalka2(null);
			savedVagon.setBalka2Nomeri(null);

			savedVagon.setGildirak1(null);

			savedVagon.setGildirak2(null);
			savedVagon.setGildirak2Nomeri(null);

			savedVagon.setGildirak3(null);
			savedVagon.setGildirak3Nomeri(null);

			savedVagon.setGildirak4(null);
			savedVagon.setGildirak4Nomeri(null);

			savedVagon.setSaqlanganVaqtiChiqish(null);

			return new ApiResponse(vagon.getGildirak1Nomeri() + " (1-g'ildirak juftligi) nomerli g'ildirak juftligi  bazaga saqlanmagan", false);
		}
		if (savedVagon.getGildirak2Nomeri() ==null){

			savedVagon.setRamaOng1(null);
			savedVagon.setRamaOng1Nomeri(null);

			savedVagon.setRamaOng2(null);
			savedVagon.setRamaOng2Nomeri(null);

			savedVagon.setRamaChap1(null);
			savedVagon.setRamaChap1Nomeri(null);

			savedVagon.setRamaChap2(null);
			savedVagon.setRamaChap2Nomeri(null);

			savedVagon.setBalka1(null);
			savedVagon.setBalka1Nomeri(null);

			savedVagon.setBalka2(null);
			savedVagon.setBalka2Nomeri(null);

			savedVagon.setGildirak1(null);
			savedVagon.setGildirak1Nomeri(null);

			savedVagon.setGildirak2(null);

			savedVagon.setGildirak3(null);
			savedVagon.setGildirak3Nomeri(null);

			savedVagon.setGildirak4(null);
			savedVagon.setGildirak4Nomeri(null);

			savedVagon.setSaqlanganVaqtiChiqish(null);

			return new ApiResponse(vagon.getGildirak2Nomeri() + " (2-g'ildirak juftligi) nomerli g'ildirak juftligi  bazaga saqlanmagan", false);
		}
		if (savedVagon.getGildirak3Nomeri() ==null){

			savedVagon.setRamaOng1(null);
			savedVagon.setRamaOng1Nomeri(null);

			savedVagon.setRamaOng2(null);
			savedVagon.setRamaOng2Nomeri(null);

			savedVagon.setRamaChap1(null);
			savedVagon.setRamaChap1Nomeri(null);

			savedVagon.setRamaChap2(null);
			savedVagon.setRamaChap2Nomeri(null);

			savedVagon.setBalka1(null);
			savedVagon.setBalka1Nomeri(null);

			savedVagon.setBalka2(null);
			savedVagon.setBalka2Nomeri(null);

			savedVagon.setGildirak1(null);
			savedVagon.setGildirak1Nomeri(null);

			savedVagon.setGildirak2(null);
			savedVagon.setGildirak2Nomeri(null);

			savedVagon.setGildirak3(null);

			savedVagon.setGildirak4(null);
			savedVagon.setGildirak4Nomeri(null);

			savedVagon.setSaqlanganVaqtiChiqish(null);

			return new ApiResponse(vagon.getGildirak3Nomeri() + " (3-g'ildirak juftligi) nomerli g'ildirak juftligi  bazaga saqlanmagan", false);
		}

		if (savedVagon.getGildirak4Nomeri() ==null){

			savedVagon.setRamaOng1(null);
			savedVagon.setRamaOng1Nomeri(null);

			savedVagon.setRamaOng2(null);
			savedVagon.setRamaOng2Nomeri(null);

			savedVagon.setRamaChap1(null);
			savedVagon.setRamaChap1Nomeri(null);

			savedVagon.setRamaChap2(null);
			savedVagon.setRamaChap2Nomeri(null);

			savedVagon.setBalka1(null);
			savedVagon.setBalka1Nomeri(null);

			savedVagon.setBalka2(null);
			savedVagon.setBalka2Nomeri(null);

			savedVagon.setGildirak1(null);
			savedVagon.setGildirak1Nomeri(null);

			savedVagon.setGildirak2(null);
			savedVagon.setGildirak2Nomeri(null);

			savedVagon.setGildirak3(null);
			savedVagon.setGildirak3Nomeri(null);

			savedVagon.setGildirak4(null);

			savedVagon.setSaqlanganVaqtiChiqish(null);

			return new ApiResponse(vagon.getGildirak4Nomeri() + " (4-g'ildirak juftligi) nomerli g'ildirak juftligi  bazaga saqlanmagan", false);
		}

		savedVagon.setSaqlanganVaqtiChiqish(samDate);

		malumotRepository.save(savedVagon);

		return new ApiResponse(savedVagon.getNomer() + " nomerli vagon chiqish ma'lumotlari kiritildi", true);
	}

	@Override
	public ApiResponse saveVagonHavChiqish(VagonMalumotChiqishDto vagon, long id) {

		Optional<VagonMalumot> exist = malumotRepository.findById(id);
		if(!exist.isPresent())
			return new ApiResponse( "Bu nomerli vagon topilmadi", false);

		VagonMalumot savedVagon = exist.get();

		if (!savedVagon.getDepoNomi().equalsIgnoreCase("VCHD-3"))
			return new ApiResponse( "Siz faqat VCHD-3 dagi vagonlarning chiqish ma'lumotlarini kiritishingiz mumkin", false);

		if (!savedVagon.getKorxona().equals("O'TY(ГАЖК)")) {

			List<VagonMalumot> korxonalar = malumotRepository.findAllByEgasi("MDH(СНГ)","Sanoat(ПРОМ)");

			for (VagonMalumot korxona : korxonalar) {

				if (vagon.getRamaOng1Nomeri().equals(korxona.getKramaOng1Nomeri()) || vagon.getRamaOng1Nomeri().equals(korxona.getKramaOng2Nomeri()) || vagon.getRamaOng1Nomeri().equals(korxona.getKramaChap1Nomeri()) || vagon.getRamaOng1Nomeri().equals(korxona.getKramaChap2Nomeri()))
					savedVagon.setRamaOng1Nomeri(vagon.getRamaOng1Nomeri());
				if (vagon.getRamaOng2Nomeri().equals(korxona.getKramaOng1Nomeri()) || vagon.getRamaOng2Nomeri().equals(korxona.getKramaOng2Nomeri()) || vagon.getRamaOng2Nomeri().equals(korxona.getKramaChap1Nomeri()) || vagon.getRamaOng2Nomeri().equals(korxona.getKramaChap2Nomeri()))
					savedVagon.setRamaOng2Nomeri(vagon.getRamaOng2Nomeri());
				if (vagon.getRamaChap1Nomeri().equals(korxona.getKramaOng1Nomeri()) || vagon.getRamaChap1Nomeri().equals(korxona.getKramaOng2Nomeri()) || vagon.getRamaChap1Nomeri().equals(korxona.getKramaChap1Nomeri()) || vagon.getRamaChap1Nomeri().equals(korxona.getKramaChap2Nomeri()))
					savedVagon.setRamaChap1Nomeri(vagon.getRamaChap1Nomeri());
				if (vagon.getRamaChap2Nomeri().equals(korxona.getKramaOng1Nomeri()) || vagon.getRamaChap2Nomeri().equals(korxona.getKramaOng2Nomeri()) || vagon.getRamaChap2Nomeri().equals(korxona.getKramaChap1Nomeri()) || vagon.getRamaChap2Nomeri().equals(korxona.getKramaChap2Nomeri()))
					savedVagon.setRamaChap2Nomeri(vagon.getRamaChap2Nomeri());
				if (vagon.getBalka1Nomeri().equals(korxona.getKbalka1Nomeri()) || vagon.getBalka1Nomeri().equals(korxona.getKbalka2Nomeri()))
					savedVagon.setBalka1Nomeri(vagon.getBalka1Nomeri());
				if (vagon.getBalka2Nomeri().equals(korxona.getKbalka1Nomeri()) || vagon.getBalka2Nomeri().equals(korxona.getKbalka2Nomeri()))
					savedVagon.setBalka2Nomeri(vagon.getBalka2Nomeri());
				if (vagon.getGildirak1Nomeri().equals(korxona.getKgildirak1Nomeri()) || vagon.getGildirak1Nomeri().equals(korxona.getKgildirak2Nomeri()) || vagon.getGildirak1Nomeri().equals(korxona.getKgildirak3Nomeri()) || vagon.getGildirak1Nomeri().equals(korxona.getKgildirak4Nomeri()))
					savedVagon.setGildirak1Nomeri(vagon.getGildirak1Nomeri());
				if (vagon.getGildirak2Nomeri().equals(korxona.getKgildirak1Nomeri()) || vagon.getGildirak2Nomeri().equals(korxona.getKgildirak2Nomeri()) || vagon.getGildirak2Nomeri().equals(korxona.getKgildirak3Nomeri()) || vagon.getGildirak2Nomeri().equals(korxona.getKgildirak4Nomeri()))
					savedVagon.setGildirak2Nomeri(vagon.getGildirak2Nomeri());
				if (vagon.getGildirak3Nomeri().equals(korxona.getKgildirak1Nomeri()) || vagon.getGildirak3Nomeri().equals(korxona.getKgildirak2Nomeri()) || vagon.getGildirak3Nomeri().equals(korxona.getKgildirak3Nomeri()) || vagon.getGildirak3Nomeri().equals(korxona.getKgildirak4Nomeri()))
					savedVagon.setGildirak3Nomeri(vagon.getGildirak3Nomeri());
				if (vagon.getGildirak4Nomeri().equals(korxona.getKgildirak1Nomeri()) || vagon.getGildirak4Nomeri().equals(korxona.getKgildirak2Nomeri()) || vagon.getGildirak4Nomeri().equals(korxona.getKgildirak3Nomeri()) || vagon.getGildirak4Nomeri().equals(korxona.getKgildirak4Nomeri()))
					savedVagon.setGildirak4Nomeri(vagon.getGildirak4Nomeri());

			}
		}else {
			savedVagon.setRamaOng1Nomeri(vagon.getRamaOng1Nomeri());
			savedVagon.setRamaOng2Nomeri(vagon.getRamaOng2Nomeri());
			savedVagon.setRamaChap1Nomeri(vagon.getRamaChap1Nomeri());
			savedVagon.setRamaChap2Nomeri(vagon.getRamaChap2Nomeri());
			savedVagon.setBalka1Nomeri(vagon.getBalka1Nomeri());
			savedVagon.setBalka2Nomeri(vagon.getBalka2Nomeri());
			savedVagon.setGildirak1Nomeri(vagon.getGildirak1Nomeri());
			savedVagon.setGildirak2Nomeri(vagon.getGildirak2Nomeri());
			savedVagon.setGildirak3Nomeri(vagon.getGildirak3Nomeri());
			savedVagon.setGildirak4Nomeri(vagon.getGildirak4Nomeri());
		}
		savedVagon.setRamaOng1(vagon.getRamaOng1());

		savedVagon.setRamaOng2(vagon.getRamaOng2());

		savedVagon.setRamaChap1(vagon.getRamaChap1());

		savedVagon.setRamaChap2(vagon.getRamaChap2());

		savedVagon.setBalka1(vagon.getBalka1());

		savedVagon.setBalka2(vagon.getBalka2());

		savedVagon.setGildirak1(vagon.getGildirak1());

		savedVagon.setGildirak2(vagon.getGildirak2());

		savedVagon.setGildirak3(vagon.getGildirak3());

		savedVagon.setGildirak4(vagon.getGildirak4());

		savedVagon.setIzohChiqish(vagon.getIzohChiqish());

		LocalDateTime today = LocalDateTime.now();
		LocalDateTime minusHours = today.plusHours(5);
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		havDate = minusHours.format(myFormatObj);

		savedVagon.setSaqlanganVaqtiChiqish(havDate);

		if (savedVagon.getRamaOng1Nomeri() ==null){

			savedVagon.setRamaOng1(null);

			savedVagon.setRamaOng2(null);
			savedVagon.setRamaOng2Nomeri(null);

			savedVagon.setRamaChap1(null);
			savedVagon.setRamaChap1Nomeri(null);

			savedVagon.setRamaChap2(null);
			savedVagon.setRamaChap2Nomeri(null);

			savedVagon.setBalka1(null);
			savedVagon.setBalka1Nomeri(null);

			savedVagon.setBalka2(null);
			savedVagon.setBalka2Nomeri(null);

			savedVagon.setGildirak1(null);
			savedVagon.setGildirak1Nomeri(null);

			savedVagon.setGildirak2(null);
			savedVagon.setGildirak2Nomeri(null);

			savedVagon.setGildirak3(null);
			savedVagon.setGildirak3Nomeri(null);

			savedVagon.setGildirak4(null);
			savedVagon.setGildirak4Nomeri(null);

			savedVagon.setSaqlanganVaqtiChiqish(null);

			return new ApiResponse(vagon.getRamaOng1Nomeri() + " (1-o'ng) nomerli yon rama  bazaga saqlanmagan", false);
		}
		if (savedVagon.getRamaOng2Nomeri() ==null){

			savedVagon.setRamaOng1(null);
			savedVagon.setRamaOng1Nomeri(null);

			savedVagon.setRamaOng2(null);

			savedVagon.setRamaChap1(null);
			savedVagon.setRamaChap1Nomeri(null);

			savedVagon.setRamaChap2(null);
			savedVagon.setRamaChap2Nomeri(null);

			savedVagon.setBalka1(null);
			savedVagon.setBalka1Nomeri(null);

			savedVagon.setBalka2(null);
			savedVagon.setBalka2Nomeri(null);

			savedVagon.setGildirak1(null);
			savedVagon.setGildirak1Nomeri(null);

			savedVagon.setGildirak2(null);
			savedVagon.setGildirak2Nomeri(null);

			savedVagon.setGildirak3(null);
			savedVagon.setGildirak3Nomeri(null);

			savedVagon.setGildirak4(null);
			savedVagon.setGildirak4Nomeri(null);

			savedVagon.setSaqlanganVaqtiChiqish(null);

			return new ApiResponse(vagon.getRamaOng2Nomeri() + " (2-o'ng) nomerli yon rama  bazaga saqlanmagan", false);
		}
		if (savedVagon.getRamaChap1Nomeri() ==null){

			savedVagon.setRamaOng1(null);
			savedVagon.setRamaOng1Nomeri(null);

			savedVagon.setRamaOng2(null);
			savedVagon.setRamaOng2Nomeri(null);

			savedVagon.setRamaChap1(null);

			savedVagon.setRamaChap2(null);
			savedVagon.setRamaChap2Nomeri(null);

			savedVagon.setBalka1(null);
			savedVagon.setBalka1Nomeri(null);

			savedVagon.setBalka2(null);
			savedVagon.setBalka2Nomeri(null);

			savedVagon.setGildirak1(null);
			savedVagon.setGildirak1Nomeri(null);

			savedVagon.setGildirak2(null);
			savedVagon.setGildirak2Nomeri(null);

			savedVagon.setGildirak3(null);
			savedVagon.setGildirak3Nomeri(null);

			savedVagon.setGildirak4(null);
			savedVagon.setGildirak4Nomeri(null);

			savedVagon.setSaqlanganVaqtiChiqish(null);

			return new ApiResponse(vagon.getRamaChap1Nomeri() + " (1-chap) nomerli yon rama  bazaga saqlanmagan", false);
		}
		if (savedVagon.getRamaChap2Nomeri() ==null){

			savedVagon.setRamaOng1(null);
			savedVagon.setRamaOng1Nomeri(null);

			savedVagon.setRamaOng2(null);
			savedVagon.setRamaOng2Nomeri(null);

			savedVagon.setRamaChap1(null);
			savedVagon.setRamaChap1Nomeri(null);

			savedVagon.setRamaChap2(null);

			savedVagon.setBalka1(null);
			savedVagon.setBalka1Nomeri(null);

			savedVagon.setBalka2(null);
			savedVagon.setBalka2Nomeri(null);

			savedVagon.setGildirak1(null);
			savedVagon.setGildirak1Nomeri(null);

			savedVagon.setGildirak2(null);
			savedVagon.setGildirak2Nomeri(null);

			savedVagon.setGildirak3(null);
			savedVagon.setGildirak3Nomeri(null);

			savedVagon.setGildirak4(null);
			savedVagon.setGildirak4Nomeri(null);

			savedVagon.setSaqlanganVaqtiChiqish(null);

			return new ApiResponse(vagon.getRamaChap2Nomeri() + " (2-chap) nomerli yon rama  bazaga saqlanmagan", false);
		}
		if (savedVagon.getBalka1Nomeri() ==null){

			savedVagon.setRamaOng1(null);
			savedVagon.setRamaOng1Nomeri(null);

			savedVagon.setRamaOng2(null);
			savedVagon.setRamaOng2Nomeri(null);

			savedVagon.setRamaChap1(null);
			savedVagon.setRamaChap1Nomeri(null);

			savedVagon.setRamaChap2(null);
			savedVagon.setRamaChap2Nomeri(null);

			savedVagon.setBalka1(null);

			savedVagon.setBalka2(null);
			savedVagon.setBalka2Nomeri(null);

			savedVagon.setGildirak1(null);
			savedVagon.setGildirak1Nomeri(null);

			savedVagon.setGildirak2(null);
			savedVagon.setGildirak2Nomeri(null);

			savedVagon.setGildirak3(null);
			savedVagon.setGildirak3Nomeri(null);

			savedVagon.setGildirak4(null);
			savedVagon.setGildirak4Nomeri(null);

			savedVagon.setSaqlanganVaqtiChiqish(null);

			return new ApiResponse(vagon.getBalka1Nomeri() + " (1-balka) nomerli ressor usti balka  bazaga saqlanmagan", false);
		}
		if (savedVagon.getBalka2Nomeri() ==null){

			savedVagon.setRamaOng1(null);
			savedVagon.setRamaOng1Nomeri(null);

			savedVagon.setRamaOng2(null);
			savedVagon.setRamaOng2Nomeri(null);

			savedVagon.setRamaChap1(null);
			savedVagon.setRamaChap1Nomeri(null);

			savedVagon.setRamaChap2(null);
			savedVagon.setRamaChap2Nomeri(null);

			savedVagon.setBalka1(null);
			savedVagon.setBalka1Nomeri(null);

			savedVagon.setBalka2(null);

			savedVagon.setGildirak1(null);
			savedVagon.setGildirak1Nomeri(null);

			savedVagon.setGildirak2(null);
			savedVagon.setGildirak2Nomeri(null);

			savedVagon.setGildirak3(null);
			savedVagon.setGildirak3Nomeri(null);

			savedVagon.setGildirak4(null);
			savedVagon.setGildirak4Nomeri(null);

			savedVagon.setSaqlanganVaqtiChiqish(null);

			return new ApiResponse(vagon.getBalka2Nomeri() + " (2-balka) nomerli ressor usti balka  bazaga saqlanmagan", false);
		}

		if (savedVagon.getGildirak1Nomeri() ==null){

			savedVagon.setRamaOng1(null);
			savedVagon.setRamaOng1Nomeri(null);

			savedVagon.setRamaOng2(null);
			savedVagon.setRamaOng2Nomeri(null);

			savedVagon.setRamaChap1(null);
			savedVagon.setRamaChap1Nomeri(null);

			savedVagon.setRamaChap2(null);
			savedVagon.setRamaChap2Nomeri(null);

			savedVagon.setBalka1(null);
			savedVagon.setBalka1Nomeri(null);

			savedVagon.setBalka2(null);
			savedVagon.setBalka2Nomeri(null);

			savedVagon.setGildirak1(null);

			savedVagon.setGildirak2(null);
			savedVagon.setGildirak2Nomeri(null);

			savedVagon.setGildirak3(null);
			savedVagon.setGildirak3Nomeri(null);

			savedVagon.setGildirak4(null);
			savedVagon.setGildirak4Nomeri(null);

			savedVagon.setSaqlanganVaqtiChiqish(null);

			return new ApiResponse(vagon.getGildirak1Nomeri() + " (1-g'ildirak juftligi) nomerli g'ildirak juftligi  bazaga saqlanmagan", false);
		}
		if (savedVagon.getGildirak2Nomeri() ==null){

			savedVagon.setRamaOng1(null);
			savedVagon.setRamaOng1Nomeri(null);

			savedVagon.setRamaOng2(null);
			savedVagon.setRamaOng2Nomeri(null);

			savedVagon.setRamaChap1(null);
			savedVagon.setRamaChap1Nomeri(null);

			savedVagon.setRamaChap2(null);
			savedVagon.setRamaChap2Nomeri(null);

			savedVagon.setBalka1(null);
			savedVagon.setBalka1Nomeri(null);

			savedVagon.setBalka2(null);
			savedVagon.setBalka2Nomeri(null);

			savedVagon.setGildirak1(null);
			savedVagon.setGildirak1Nomeri(null);

			savedVagon.setGildirak2(null);

			savedVagon.setGildirak3(null);
			savedVagon.setGildirak3Nomeri(null);

			savedVagon.setGildirak4(null);
			savedVagon.setGildirak4Nomeri(null);

			savedVagon.setSaqlanganVaqtiChiqish(null);

			return new ApiResponse(vagon.getGildirak2Nomeri() + " (2-g'ildirak juftligi) nomerli g'ildirak juftligi  bazaga saqlanmagan", false);
		}
		if (savedVagon.getGildirak3Nomeri() ==null){

			savedVagon.setRamaOng1(null);
			savedVagon.setRamaOng1Nomeri(null);

			savedVagon.setRamaOng2(null);
			savedVagon.setRamaOng2Nomeri(null);

			savedVagon.setRamaChap1(null);
			savedVagon.setRamaChap1Nomeri(null);

			savedVagon.setRamaChap2(null);
			savedVagon.setRamaChap2Nomeri(null);

			savedVagon.setBalka1(null);
			savedVagon.setBalka1Nomeri(null);

			savedVagon.setBalka2(null);
			savedVagon.setBalka2Nomeri(null);

			savedVagon.setGildirak1(null);
			savedVagon.setGildirak1Nomeri(null);

			savedVagon.setGildirak2(null);
			savedVagon.setGildirak2Nomeri(null);

			savedVagon.setGildirak3(null);

			savedVagon.setGildirak4(null);
			savedVagon.setGildirak4Nomeri(null);

			savedVagon.setSaqlanganVaqtiChiqish(null);

			return new ApiResponse(vagon.getGildirak3Nomeri() + " (3-g'ildirak juftligi) nomerli g'ildirak juftligi  bazaga saqlanmagan", false);
		}

		if (savedVagon.getGildirak4Nomeri() ==null){

			savedVagon.setRamaOng1(null);
			savedVagon.setRamaOng1Nomeri(null);

			savedVagon.setRamaOng2(null);
			savedVagon.setRamaOng2Nomeri(null);

			savedVagon.setRamaChap1(null);
			savedVagon.setRamaChap1Nomeri(null);

			savedVagon.setRamaChap2(null);
			savedVagon.setRamaChap2Nomeri(null);

			savedVagon.setBalka1(null);
			savedVagon.setBalka1Nomeri(null);

			savedVagon.setBalka2(null);
			savedVagon.setBalka2Nomeri(null);

			savedVagon.setGildirak1(null);
			savedVagon.setGildirak1Nomeri(null);

			savedVagon.setGildirak2(null);
			savedVagon.setGildirak2Nomeri(null);

			savedVagon.setGildirak3(null);
			savedVagon.setGildirak3Nomeri(null);

			savedVagon.setGildirak4(null);

			savedVagon.setSaqlanganVaqtiChiqish(null);

			return new ApiResponse(vagon.getGildirak4Nomeri() + " (4-g'ildirak juftligi) nomerli g'ildirak juftligi  bazaga saqlanmagan", false);
		}

		malumotRepository.save(savedVagon);
		return new ApiResponse(savedVagon.getNomer() + " nomerli vagon chiqish ma'lumotlari kiritildi", true);

	}

	@Override
	public ApiResponse saveVagonAndjChiqish(VagonMalumotChiqishDto vagon, long id) {

		Optional<VagonMalumot> exist = malumotRepository.findById(id);
		if(!exist.isPresent())
			return new ApiResponse( "Bu nomerli vagon topilmadi", false);

		 VagonMalumot savedVagon = exist.get();

		if (!savedVagon.getDepoNomi().equalsIgnoreCase("VCHD-5"))
			return new ApiResponse( "Siz faqat VCHD-5 dagi vagonlarning chiqish ma'lumotlarini kiritishingiz mumkin", false);

		if (!savedVagon.getKorxona().equals("O'TY(ГАЖК)")) {

			List<VagonMalumot> korxonalar = malumotRepository.findAllByEgasi("MDH(СНГ)","Sanoat(ПРОМ)");

			for (VagonMalumot korxona : korxonalar) {

				if (vagon.getRamaOng1Nomeri().equals(korxona.getKramaOng1Nomeri()) || vagon.getRamaOng1Nomeri().equals(korxona.getKramaOng2Nomeri()) || vagon.getRamaOng1Nomeri().equals(korxona.getKramaChap1Nomeri()) || vagon.getRamaOng1Nomeri().equals(korxona.getKramaChap2Nomeri()))
					savedVagon.setRamaOng1Nomeri(vagon.getRamaOng1Nomeri());
				if (vagon.getRamaOng2Nomeri().equals(korxona.getKramaOng1Nomeri()) || vagon.getRamaOng2Nomeri().equals(korxona.getKramaOng2Nomeri()) || vagon.getRamaOng2Nomeri().equals(korxona.getKramaChap1Nomeri()) || vagon.getRamaOng2Nomeri().equals(korxona.getKramaChap2Nomeri()))
					savedVagon.setRamaOng2Nomeri(vagon.getRamaOng2Nomeri());
				if (vagon.getRamaChap1Nomeri().equals(korxona.getKramaOng1Nomeri()) || vagon.getRamaChap1Nomeri().equals(korxona.getKramaOng2Nomeri()) || vagon.getRamaChap1Nomeri().equals(korxona.getKramaChap1Nomeri()) || vagon.getRamaChap1Nomeri().equals(korxona.getKramaChap2Nomeri()))
					savedVagon.setRamaChap1Nomeri(vagon.getRamaChap1Nomeri());
				if (vagon.getRamaChap2Nomeri().equals(korxona.getKramaOng1Nomeri()) || vagon.getRamaChap2Nomeri().equals(korxona.getKramaOng2Nomeri()) || vagon.getRamaChap2Nomeri().equals(korxona.getKramaChap1Nomeri()) || vagon.getRamaChap2Nomeri().equals(korxona.getKramaChap2Nomeri()))
					savedVagon.setRamaChap2Nomeri(vagon.getRamaChap2Nomeri());
				if (vagon.getBalka1Nomeri().equals(korxona.getKbalka1Nomeri()) || vagon.getBalka1Nomeri().equals(korxona.getKbalka2Nomeri()))
					savedVagon.setBalka1Nomeri(vagon.getBalka1Nomeri());
				if (vagon.getBalka2Nomeri().equals(korxona.getKbalka1Nomeri()) || vagon.getBalka2Nomeri().equals(korxona.getKbalka2Nomeri()))
					savedVagon.setBalka2Nomeri(vagon.getBalka2Nomeri());
				if (vagon.getGildirak1Nomeri().equals(korxona.getKgildirak1Nomeri()) || vagon.getGildirak1Nomeri().equals(korxona.getKgildirak2Nomeri()) || vagon.getGildirak1Nomeri().equals(korxona.getKgildirak3Nomeri()) || vagon.getGildirak1Nomeri().equals(korxona.getKgildirak4Nomeri()))
					savedVagon.setGildirak1Nomeri(vagon.getGildirak1Nomeri());
				if (vagon.getGildirak2Nomeri().equals(korxona.getKgildirak1Nomeri()) || vagon.getGildirak2Nomeri().equals(korxona.getKgildirak2Nomeri()) || vagon.getGildirak2Nomeri().equals(korxona.getKgildirak3Nomeri()) || vagon.getGildirak2Nomeri().equals(korxona.getKgildirak4Nomeri()))
					savedVagon.setGildirak2Nomeri(vagon.getGildirak2Nomeri());
				if (vagon.getGildirak3Nomeri().equals(korxona.getKgildirak1Nomeri()) || vagon.getGildirak3Nomeri().equals(korxona.getKgildirak2Nomeri()) || vagon.getGildirak3Nomeri().equals(korxona.getKgildirak3Nomeri()) || vagon.getGildirak3Nomeri().equals(korxona.getKgildirak4Nomeri()))
					savedVagon.setGildirak3Nomeri(vagon.getGildirak3Nomeri());
				if (vagon.getGildirak4Nomeri().equals(korxona.getKgildirak1Nomeri()) || vagon.getGildirak4Nomeri().equals(korxona.getKgildirak2Nomeri()) || vagon.getGildirak4Nomeri().equals(korxona.getKgildirak3Nomeri()) || vagon.getGildirak4Nomeri().equals(korxona.getKgildirak4Nomeri()))
					savedVagon.setGildirak4Nomeri(vagon.getGildirak4Nomeri());

			}
		}else {
			savedVagon.setRamaOng1Nomeri(vagon.getRamaOng1Nomeri());
			savedVagon.setRamaOng2Nomeri(vagon.getRamaOng2Nomeri());
			savedVagon.setRamaChap1Nomeri(vagon.getRamaChap1Nomeri());
			savedVagon.setRamaChap2Nomeri(vagon.getRamaChap2Nomeri());
			savedVagon.setBalka1Nomeri(vagon.getBalka1Nomeri());
			savedVagon.setBalka2Nomeri(vagon.getBalka2Nomeri());
			savedVagon.setGildirak1Nomeri(vagon.getGildirak1Nomeri());
			savedVagon.setGildirak2Nomeri(vagon.getGildirak2Nomeri());
			savedVagon.setGildirak3Nomeri(vagon.getGildirak3Nomeri());
			savedVagon.setGildirak4Nomeri(vagon.getGildirak4Nomeri());
		}
		savedVagon.setRamaOng1(vagon.getRamaOng1());

		savedVagon.setRamaOng2(vagon.getRamaOng2());

		savedVagon.setRamaChap1(vagon.getRamaChap1());

		savedVagon.setRamaChap2(vagon.getRamaChap2());

		savedVagon.setBalka1(vagon.getBalka1());

		savedVagon.setBalka2(vagon.getBalka2());

		savedVagon.setGildirak1(vagon.getGildirak1());

		savedVagon.setGildirak2(vagon.getGildirak2());

		savedVagon.setGildirak3(vagon.getGildirak3());

		savedVagon.setGildirak4(vagon.getGildirak4());

		savedVagon.setIzohChiqish(vagon.getIzohChiqish());


		LocalDateTime today = LocalDateTime.now();
		LocalDateTime minusHours = today.plusHours(5);
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		andjDate = minusHours.format(myFormatObj);

		savedVagon.setSaqlanganVaqtiChiqish(andjDate);

		if (savedVagon.getRamaOng1Nomeri() ==null){

			savedVagon.setRamaOng1(null);

			savedVagon.setRamaOng2(null);
			savedVagon.setRamaOng2Nomeri(null);

			savedVagon.setRamaChap1(null);
			savedVagon.setRamaChap1Nomeri(null);

			savedVagon.setRamaChap2(null);
			savedVagon.setRamaChap2Nomeri(null);

			savedVagon.setBalka1(null);
			savedVagon.setBalka1Nomeri(null);

			savedVagon.setBalka2(null);
			savedVagon.setBalka2Nomeri(null);

			savedVagon.setGildirak1(null);
			savedVagon.setGildirak1Nomeri(null);

			savedVagon.setGildirak2(null);
			savedVagon.setGildirak2Nomeri(null);

			savedVagon.setGildirak3(null);
			savedVagon.setGildirak3Nomeri(null);

			savedVagon.setGildirak4(null);
			savedVagon.setGildirak4Nomeri(null);

			savedVagon.setSaqlanganVaqtiChiqish(andjDate);

			return new ApiResponse(vagon.getRamaOng1Nomeri() + " (1-o'ng) nomerli yon rama  bazaga saqlanmagan", false);
		}
		if (savedVagon.getRamaOng2Nomeri() ==null){

			savedVagon.setRamaOng1(null);
			savedVagon.setRamaOng1Nomeri(null);

			savedVagon.setRamaOng2(null);

			savedVagon.setRamaChap1(null);
			savedVagon.setRamaChap1Nomeri(null);

			savedVagon.setRamaChap2(null);
			savedVagon.setRamaChap2Nomeri(null);

			savedVagon.setBalka1(null);
			savedVagon.setBalka1Nomeri(null);

			savedVagon.setBalka2(null);
			savedVagon.setBalka2Nomeri(null);

			savedVagon.setGildirak1(null);
			savedVagon.setGildirak1Nomeri(null);

			savedVagon.setGildirak2(null);
			savedVagon.setGildirak2Nomeri(null);

			savedVagon.setGildirak3(null);
			savedVagon.setGildirak3Nomeri(null);

			savedVagon.setGildirak4(null);
			savedVagon.setGildirak4Nomeri(null);

			savedVagon.setSaqlanganVaqtiChiqish(andjDate);

			return new ApiResponse(vagon.getRamaOng2Nomeri() + " (2-o'ng) nomerli yon rama  bazaga saqlanmagan", false);
		}
		if (savedVagon.getRamaChap1Nomeri() ==null){

			savedVagon.setRamaOng1(null);
			savedVagon.setRamaOng1Nomeri(null);

			savedVagon.setRamaOng2(null);
			savedVagon.setRamaOng2Nomeri(null);

			savedVagon.setRamaChap1(null);

			savedVagon.setRamaChap2(null);
			savedVagon.setRamaChap2Nomeri(null);

			savedVagon.setBalka1(null);
			savedVagon.setBalka1Nomeri(null);

			savedVagon.setBalka2(null);
			savedVagon.setBalka2Nomeri(null);

			savedVagon.setGildirak1(null);
			savedVagon.setGildirak1Nomeri(null);

			savedVagon.setGildirak2(null);
			savedVagon.setGildirak2Nomeri(null);

			savedVagon.setGildirak3(null);
			savedVagon.setGildirak3Nomeri(null);

			savedVagon.setGildirak4(null);
			savedVagon.setGildirak4Nomeri(null);

			savedVagon.setSaqlanganVaqtiChiqish(andjDate);

			return new ApiResponse(vagon.getRamaChap1Nomeri() + " (1-chap) nomerli yon rama  bazaga saqlanmagan", false);
		}
		if (savedVagon.getRamaChap2Nomeri() ==null){

			savedVagon.setRamaOng1(null);
			savedVagon.setRamaOng1Nomeri(null);

			savedVagon.setRamaOng2(null);
			savedVagon.setRamaOng2Nomeri(null);

			savedVagon.setRamaChap1(null);
			savedVagon.setRamaChap1Nomeri(null);

			savedVagon.setRamaChap2(null);

			savedVagon.setBalka1(null);
			savedVagon.setBalka1Nomeri(null);

			savedVagon.setBalka2(null);
			savedVagon.setBalka2Nomeri(null);

			savedVagon.setGildirak1(null);
			savedVagon.setGildirak1Nomeri(null);

			savedVagon.setGildirak2(null);
			savedVagon.setGildirak2Nomeri(null);

			savedVagon.setGildirak3(null);
			savedVagon.setGildirak3Nomeri(null);

			savedVagon.setGildirak4(null);
			savedVagon.setGildirak4Nomeri(null);

			savedVagon.setSaqlanganVaqtiChiqish(andjDate);

			return new ApiResponse(vagon.getRamaChap2Nomeri() + " (2-chap) nomerli yon rama  bazaga saqlanmagan", false);
		}
		if (savedVagon.getBalka1Nomeri() ==null){

			savedVagon.setRamaOng1(null);
			savedVagon.setRamaOng1Nomeri(null);

			savedVagon.setRamaOng2(null);
			savedVagon.setRamaOng2Nomeri(null);

			savedVagon.setRamaChap1(null);
			savedVagon.setRamaChap1Nomeri(null);

			savedVagon.setRamaChap2(null);
			savedVagon.setRamaChap2Nomeri(null);

			savedVagon.setBalka1(null);

			savedVagon.setBalka2(null);
			savedVagon.setBalka2Nomeri(null);

			savedVagon.setGildirak1(null);
			savedVagon.setGildirak1Nomeri(null);

			savedVagon.setGildirak2(null);
			savedVagon.setGildirak2Nomeri(null);

			savedVagon.setGildirak3(null);
			savedVagon.setGildirak3Nomeri(null);

			savedVagon.setGildirak4(null);
			savedVagon.setGildirak4Nomeri(null);

			savedVagon.setSaqlanganVaqtiChiqish(andjDate);

			return new ApiResponse(vagon.getBalka1Nomeri() + " (1-balka) nomerli ressor usti balka  bazaga saqlanmagan", false);
		}
		if (savedVagon.getBalka2Nomeri() ==null){

			savedVagon.setRamaOng1(null);
			savedVagon.setRamaOng1Nomeri(null);

			savedVagon.setRamaOng2(null);
			savedVagon.setRamaOng2Nomeri(null);

			savedVagon.setRamaChap1(null);
			savedVagon.setRamaChap1Nomeri(null);

			savedVagon.setRamaChap2(null);
			savedVagon.setRamaChap2Nomeri(null);

			savedVagon.setBalka1(null);
			savedVagon.setBalka1Nomeri(null);

			savedVagon.setBalka2(null);

			savedVagon.setGildirak1(null);
			savedVagon.setGildirak1Nomeri(null);

			savedVagon.setGildirak2(null);
			savedVagon.setGildirak2Nomeri(null);

			savedVagon.setGildirak3(null);
			savedVagon.setGildirak3Nomeri(null);

			savedVagon.setGildirak4(null);
			savedVagon.setGildirak4Nomeri(null);

			savedVagon.setSaqlanganVaqtiChiqish(andjDate);

			return new ApiResponse(vagon.getBalka2Nomeri() + " (2-balka) nomerli ressor usti balka  bazaga saqlanmagan", false);
		}

		if (savedVagon.getGildirak1Nomeri() ==null){

			savedVagon.setRamaOng1(null);
			savedVagon.setRamaOng1Nomeri(null);

			savedVagon.setRamaOng2(null);
			savedVagon.setRamaOng2Nomeri(null);

			savedVagon.setRamaChap1(null);
			savedVagon.setRamaChap1Nomeri(null);

			savedVagon.setRamaChap2(null);
			savedVagon.setRamaChap2Nomeri(null);

			savedVagon.setBalka1(null);
			savedVagon.setBalka1Nomeri(null);

			savedVagon.setBalka2(null);
			savedVagon.setBalka2Nomeri(null);

			savedVagon.setGildirak1(null);

			savedVagon.setGildirak2(null);
			savedVagon.setGildirak2Nomeri(null);

			savedVagon.setGildirak3(null);
			savedVagon.setGildirak3Nomeri(null);

			savedVagon.setGildirak4(null);
			savedVagon.setGildirak4Nomeri(null);

			savedVagon.setSaqlanganVaqtiChiqish(andjDate);

			return new ApiResponse(vagon.getGildirak1Nomeri() + " (1-g'ildirak juftligi) nomerli g'ildirak juftligi  bazaga saqlanmagan", false);
		}
		if (savedVagon.getGildirak2Nomeri() ==null){

			savedVagon.setRamaOng1(null);
			savedVagon.setRamaOng1Nomeri(null);

			savedVagon.setRamaOng2(null);
			savedVagon.setRamaOng2Nomeri(null);

			savedVagon.setRamaChap1(null);
			savedVagon.setRamaChap1Nomeri(null);

			savedVagon.setRamaChap2(null);
			savedVagon.setRamaChap2Nomeri(null);

			savedVagon.setBalka1(null);
			savedVagon.setBalka1Nomeri(null);

			savedVagon.setBalka2(null);
			savedVagon.setBalka2Nomeri(null);

			savedVagon.setGildirak1(null);
			savedVagon.setGildirak1Nomeri(null);

			savedVagon.setGildirak2(null);

			savedVagon.setGildirak3(null);
			savedVagon.setGildirak3Nomeri(null);

			savedVagon.setGildirak4(null);
			savedVagon.setGildirak4Nomeri(null);

			savedVagon.setSaqlanganVaqtiChiqish(andjDate);

			return new ApiResponse(vagon.getGildirak2Nomeri() + " (2-g'ildirak juftligi) nomerli g'ildirak juftligi  bazaga saqlanmagan", false);
		}
		if (savedVagon.getGildirak3Nomeri() ==null){

			savedVagon.setRamaOng1(null);
			savedVagon.setRamaOng1Nomeri(null);

			savedVagon.setRamaOng2(null);
			savedVagon.setRamaOng2Nomeri(null);

			savedVagon.setRamaChap1(null);
			savedVagon.setRamaChap1Nomeri(null);

			savedVagon.setRamaChap2(null);
			savedVagon.setRamaChap2Nomeri(null);

			savedVagon.setBalka1(null);
			savedVagon.setBalka1Nomeri(null);

			savedVagon.setBalka2(null);
			savedVagon.setBalka2Nomeri(null);

			savedVagon.setGildirak1(null);
			savedVagon.setGildirak1Nomeri(null);

			savedVagon.setGildirak2(null);
			savedVagon.setGildirak2Nomeri(null);

			savedVagon.setGildirak3(null);

			savedVagon.setGildirak4(null);
			savedVagon.setGildirak4Nomeri(null);

			savedVagon.setSaqlanganVaqtiChiqish(andjDate);

			return new ApiResponse(vagon.getGildirak3Nomeri() + " (3-g'ildirak juftligi) nomerli g'ildirak juftligi  bazaga saqlanmagan", false);
		}

		if (savedVagon.getGildirak4Nomeri() ==null){

			savedVagon.setRamaOng1(null);
			savedVagon.setRamaOng1Nomeri(null);

			savedVagon.setRamaOng2(null);
			savedVagon.setRamaOng2Nomeri(null);

			savedVagon.setRamaChap1(null);
			savedVagon.setRamaChap1Nomeri(null);

			savedVagon.setRamaChap2(null);
			savedVagon.setRamaChap2Nomeri(null);

			savedVagon.setBalka1(null);
			savedVagon.setBalka1Nomeri(null);

			savedVagon.setBalka2(null);
			savedVagon.setBalka2Nomeri(null);

			savedVagon.setGildirak1(null);
			savedVagon.setGildirak1Nomeri(null);

			savedVagon.setGildirak2(null);
			savedVagon.setGildirak2Nomeri(null);

			savedVagon.setGildirak3(null);
			savedVagon.setGildirak3Nomeri(null);

			savedVagon.setGildirak4(null);

			savedVagon.setSaqlanganVaqtiChiqish(andjDate);

			return new ApiResponse(vagon.getGildirak4Nomeri() + " (4-g'ildirak juftligi) nomerli g'ildirak juftligi  bazaga saqlanmagan", false);
		}

		malumotRepository.save(savedVagon);

		return new ApiResponse(savedVagon.getNomer() + " nomerli vagon chiqish ma'lumotlari kiritildi", true);
	}


	@Override
	public ApiResponse updateVagon(VagonMalumotUpdateDto vagon, long id) {

		Optional<VagonMalumot> exist = malumotRepository.findById(id);
		 if(!exist.isPresent())
			 return new ApiResponse("Bu nomerdagi vagon mavjud emas", false);

		VagonMalumot savedVagon = exist.get();
		savedVagon.setId(id);
		savedVagon.setDepoNomi(vagon.getDepoNomi());
		savedVagon.setRemontTuri(vagon.getRemontTuri());
		savedVagon.setOxirgiTamirKuni(vagon.getOxirgiTamirKuni());
		savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
		savedVagon.setKorxona(vagon.getKorxona());
		savedVagon.setCountry(vagon.getCountry());

		savedVagon.setKramaOng1(vagon.getKramaOng1());
		savedVagon.setKramaOng1Nomeri(vagon.getKramaOng1Nomeri());

		savedVagon.setKramaOng2(vagon.getKramaOng2());
		savedVagon.setKramaOng2Nomeri(vagon.getKramaOng2Nomeri());

		savedVagon.setKramaChap1(vagon.getKramaChap1());
		savedVagon.setKramaChap1Nomeri(vagon.getKramaChap1Nomeri());

		savedVagon.setKramaChap2(vagon.getKramaChap2());
		savedVagon.setKramaChap2Nomeri(vagon.getKramaChap2Nomeri());

		savedVagon.setKbalka1(vagon.getKbalka1());
		savedVagon.setKbalka1Nomeri(vagon.getKbalka1Nomeri());

		savedVagon.setKbalka2(vagon.getKbalka2());
		savedVagon.setKbalka2Nomeri(vagon.getKbalka2Nomeri());

		savedVagon.setKgildirak1(vagon.getKgildirak1());
		savedVagon.setKgildirak1Nomeri(vagon.getKgildirak1Nomeri());

		savedVagon.setKgildirak2(vagon.getKgildirak2());
		savedVagon.setKgildirak2Nomeri(vagon.getKgildirak2Nomeri());

		savedVagon.setKgildirak3(vagon.getKgildirak3());
		savedVagon.setKgildirak3Nomeri(vagon.getKgildirak3Nomeri());

		savedVagon.setKgildirak4(vagon.getKgildirak4());
		savedVagon.setKgildirak4Nomeri(vagon.getKgildirak4Nomeri());

		savedVagon.setIzoh(vagon.getIzoh());

		LocalDateTime today = LocalDateTime.now();
		LocalDateTime minusHours = today.plusHours(5);
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		currentDate = minusHours.format(myFormatObj);

		savedVagon.setSaqlanganVaqtiChiqish(currentDate);

		malumotRepository.save(savedVagon);

		return new ApiResponse(savedVagon.getNomer() + " nomerli vagon ma'lumotlari o'zgartirildi", true);
	}

	@Override
	public ApiResponse updateVagonSam(VagonMalumotUpdateDto vagon, long id) {

		 Optional<VagonMalumot> exist = malumotRepository.findById(id);

		VagonMalumot savedVagon = exist.get();

		 if(savedVagon.getDepoNomi().equalsIgnoreCase("VCHD-6") && vagon.getDepoNomi().equalsIgnoreCase("VCHD-6")) {


			 savedVagon.setId(id);
			 savedVagon.setDepoNomi(vagon.getDepoNomi());
			 savedVagon.setRemontTuri(vagon.getRemontTuri());
			 savedVagon.setOxirgiTamirKuni(vagon.getOxirgiTamirKuni());
			 savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
			 savedVagon.setKorxona(vagon.getKorxona());
			 savedVagon.setCountry(vagon.getCountry());

			 savedVagon.setKramaOng1(vagon.getKramaOng1());
			 savedVagon.setKramaOng1Nomeri(vagon.getKramaOng1Nomeri());

			 savedVagon.setKramaOng2(vagon.getKramaOng2());
			 savedVagon.setKramaOng2Nomeri(vagon.getKramaOng2Nomeri());

			 savedVagon.setKramaChap1(vagon.getKramaChap1());
			 savedVagon.setKramaChap1Nomeri(vagon.getKramaChap1Nomeri());

			 savedVagon.setKramaChap2(vagon.getKramaChap2());
			 savedVagon.setKramaChap2Nomeri(vagon.getKramaChap2Nomeri());

			 savedVagon.setKbalka1(vagon.getKbalka1());
			 savedVagon.setKbalka1Nomeri(vagon.getKbalka1Nomeri());

			 savedVagon.setKbalka2(vagon.getKbalka2());
			 savedVagon.setKbalka2Nomeri(vagon.getKbalka2Nomeri());

			 savedVagon.setKgildirak1(vagon.getKgildirak1());
			 savedVagon.setKgildirak1Nomeri(vagon.getKgildirak1Nomeri());

			 savedVagon.setKgildirak2(vagon.getKgildirak2());
			 savedVagon.setKgildirak2Nomeri(vagon.getKgildirak2Nomeri());

			 savedVagon.setKgildirak3(vagon.getKgildirak3());
			 savedVagon.setKgildirak3Nomeri(vagon.getKgildirak3Nomeri());

			 savedVagon.setKgildirak4(vagon.getKgildirak4());
			 savedVagon.setKgildirak4Nomeri(vagon.getKgildirak4Nomeri());

			 savedVagon.setIzoh(vagon.getIzoh());

			LocalDateTime today = LocalDateTime.now();
			LocalDateTime minusHours = today.plusHours(5);
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			samDate = minusHours.format(myFormatObj);

			Optional<LastActionTimes> optionalMalumotTime = utyTimeRepository.findById(1);
			optionalMalumotTime.get().setSamMalumotDate(samDate);
			utyTimeRepository.save(optionalMalumotTime.get());

			malumotRepository.save(savedVagon);

			return new ApiResponse(savedVagon.getNomer() + " nomerli vagon ma'lumotlari o'zgartirildi", true);
		 }else
			return new ApiResponse("Siz faqat VCHD-6 dagi vagon malumotlarini o'zgartirishingiz mumkin", false);

	}

	@Override
	public ApiResponse updateVagonHav(VagonMalumotUpdateDto vagon, long id) {

		 Optional<VagonMalumot> exist = malumotRepository.findById(id);

		VagonMalumot savedVagon = exist.get();

		 if(savedVagon.getDepoNomi().equalsIgnoreCase("VCHD-3") && vagon.getDepoNomi().equalsIgnoreCase("VCHD-3")) {

			 savedVagon.setId(id);
			 savedVagon.setDepoNomi(vagon.getDepoNomi());
			 savedVagon.setRemontTuri(vagon.getRemontTuri());
			 savedVagon.setOxirgiTamirKuni(vagon.getOxirgiTamirKuni());
			 savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
			 savedVagon.setKorxona(vagon.getKorxona());
			 savedVagon.setCountry(vagon.getCountry());

			 savedVagon.setKramaOng1(vagon.getKramaOng1());
			 savedVagon.setKramaOng1Nomeri(vagon.getKramaOng1Nomeri());

			 savedVagon.setKramaOng2(vagon.getKramaOng2());
			 savedVagon.setKramaOng2Nomeri(vagon.getKramaOng2Nomeri());

			 savedVagon.setKramaChap1(vagon.getKramaChap1());
			 savedVagon.setKramaChap1Nomeri(vagon.getKramaChap1Nomeri());

			 savedVagon.setKramaChap2(vagon.getKramaChap2());
			 savedVagon.setKramaChap2Nomeri(vagon.getKramaChap2Nomeri());

			 savedVagon.setKbalka1(vagon.getKbalka1());
			 savedVagon.setKbalka1Nomeri(vagon.getKbalka1Nomeri());

			 savedVagon.setKbalka2(vagon.getKbalka2());
			 savedVagon.setKbalka2Nomeri(vagon.getKbalka2Nomeri());

			 savedVagon.setKgildirak1(vagon.getKgildirak1());
			 savedVagon.setKgildirak1Nomeri(vagon.getKgildirak1Nomeri());

			 savedVagon.setKgildirak2(vagon.getKgildirak2());
			 savedVagon.setKgildirak2Nomeri(vagon.getKgildirak2Nomeri());

			 savedVagon.setKgildirak3(vagon.getKgildirak3());
			 savedVagon.setKgildirak3Nomeri(vagon.getKgildirak3Nomeri());

			 savedVagon.setKgildirak4(vagon.getKgildirak4());
			 savedVagon.setKgildirak4Nomeri(vagon.getKgildirak4Nomeri());

			 savedVagon.setIzoh(vagon.getIzoh());

			 LocalDateTime today = LocalDateTime.now();
			 LocalDateTime minusHours = today.plusHours(5);
			 DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			 havDate = minusHours.format(myFormatObj);

			 Optional<LastActionTimes> optionalMalumotTime = utyTimeRepository.findById(1);
			 optionalMalumotTime.get().setHavMalumotDate(havDate);
			 utyTimeRepository.save(optionalMalumotTime.get());

			 return new ApiResponse(savedVagon.getNomer() + " nomerli vagon ma'lumotlari o'zgartirildi", true);
		 }else
			 return new ApiResponse("Siz faqat VCHD-3 dagi vagon ma'lumotlarini o'zgartirishingiz mumkin", false);
	}

	@Override
	public ApiResponse updateVagonAndj(VagonMalumotUpdateDto vagon, long id) {

		 Optional<VagonMalumot> exist = malumotRepository.findById(id);

		 VagonMalumot savedVagon = exist.get();

		 if( savedVagon.getDepoNomi().equalsIgnoreCase("VCHD-5") && vagon.getDepoNomi().equalsIgnoreCase("VCHD-5")){

			 savedVagon.setId(id);
			 savedVagon.setDepoNomi(vagon.getDepoNomi());
			 savedVagon.setRemontTuri(vagon.getRemontTuri());
			 savedVagon.setOxirgiTamirKuni(vagon.getOxirgiTamirKuni());
			 savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
			 savedVagon.setKorxona(vagon.getKorxona());
			 savedVagon.setCountry(vagon.getCountry());

			 savedVagon.setKramaOng1(vagon.getKramaOng1());
			 savedVagon.setKramaOng1Nomeri(vagon.getKramaOng1Nomeri());

			 savedVagon.setKramaOng2(vagon.getKramaOng2());
			 savedVagon.setKramaOng2Nomeri(vagon.getKramaOng2Nomeri());

			 savedVagon.setKramaChap1(vagon.getKramaChap1());
			 savedVagon.setKramaChap1Nomeri(vagon.getKramaChap1Nomeri());

			 savedVagon.setKramaChap2(vagon.getKramaChap2());
			 savedVagon.setKramaChap2Nomeri(vagon.getKramaChap2Nomeri());

			 savedVagon.setKbalka1(vagon.getKbalka1());
			 savedVagon.setKbalka1Nomeri(vagon.getKbalka1Nomeri());

			 savedVagon.setKbalka2(vagon.getKbalka2());
			 savedVagon.setKbalka2Nomeri(vagon.getKbalka2Nomeri());

			 savedVagon.setKgildirak1(vagon.getKgildirak1());
			 savedVagon.setKgildirak1Nomeri(vagon.getKgildirak1Nomeri());

			 savedVagon.setKgildirak2(vagon.getKgildirak2());
			 savedVagon.setKgildirak2Nomeri(vagon.getKgildirak2Nomeri());

			 savedVagon.setKgildirak3(vagon.getKgildirak3());
			 savedVagon.setKgildirak3Nomeri(vagon.getKgildirak3Nomeri());

			 savedVagon.setKgildirak4(vagon.getKgildirak4());
			 savedVagon.setKgildirak4Nomeri(vagon.getKgildirak4Nomeri());

			 savedVagon.setIzoh(vagon.getIzoh());

			LocalDateTime today = LocalDateTime.now();
			LocalDateTime minusHours = today.plusHours(5);
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			andjDate = minusHours.format(myFormatObj);

			Optional<LastActionTimes> optionalMalumotTime = utyTimeRepository.findById(1);
			optionalMalumotTime.get().setAndjMalumotDate(andjDate);
			utyTimeRepository.save(optionalMalumotTime.get());

			 return new ApiResponse(savedVagon.getNomer() + " nomerli vagon ma'lumotlari o'zgartirildi", true);
		 }else
			 return new ApiResponse("Siz faqat VCHD-5 dagi vagon ma'lumotlarini o'zgartirishingiz mumkin", false);
	}


	@Override
	public ApiResponse updateVagonChiqish(VagonMalumotChiqishDto vagon, Long id) {

		Optional<VagonMalumot> exist = malumotRepository.findById(id);
		if(!exist.isPresent())
			return new ApiResponse( "Bu nomerli vagon topilmadi", false);

		VagonMalumot savedVagon = exist.get();
		VagonMalumot saved = malumotRepository.findById(id).get();

		if (savedVagon.getKorxona().equals("O'TY(ГАЖК)")) {

			savedVagon.setRamaOng1Nomeri(vagon.getRamaOng1Nomeri());
			savedVagon.setRamaOng2Nomeri(vagon.getRamaOng2Nomeri());
			savedVagon.setRamaChap1Nomeri(vagon.getRamaChap1Nomeri());
			savedVagon.setRamaChap2Nomeri(vagon.getRamaChap2Nomeri());
			savedVagon.setBalka1Nomeri(vagon.getBalka1Nomeri());
			savedVagon.setBalka2Nomeri(vagon.getBalka2Nomeri());
			savedVagon.setGildirak1Nomeri(vagon.getGildirak1Nomeri());
			savedVagon.setGildirak2Nomeri(vagon.getGildirak2Nomeri());
			savedVagon.setGildirak3Nomeri(vagon.getGildirak3Nomeri());
			savedVagon.setGildirak4Nomeri(vagon.getGildirak4Nomeri());


		}else {
			List<VagonMalumot> korxonalar = malumotRepository.findAllByEgasi("MDH(СНГ)","Sanoat(ПРОМ)");

			for (VagonMalumot korxona : korxonalar) {

				if (vagon.getRamaOng1Nomeri().equals(korxona.getKramaOng1Nomeri()) || vagon.getRamaOng1Nomeri().equals(korxona.getKramaOng2Nomeri()) || vagon.getRamaOng1Nomeri().equals(korxona.getKramaChap1Nomeri()) || vagon.getRamaOng1Nomeri().equals(korxona.getKramaChap2Nomeri()))
					savedVagon.setRamaOng1Nomeri(vagon.getRamaOng1Nomeri());
				if (vagon.getRamaOng2Nomeri().equals(korxona.getKramaOng1Nomeri()) || vagon.getRamaOng2Nomeri().equals(korxona.getKramaOng2Nomeri()) || vagon.getRamaOng2Nomeri().equals(korxona.getKramaChap1Nomeri()) || vagon.getRamaOng2Nomeri().equals(korxona.getKramaChap2Nomeri()))
					savedVagon.setRamaOng2Nomeri(vagon.getRamaOng2Nomeri());
				if (vagon.getRamaChap1Nomeri().equals(korxona.getKramaOng1Nomeri()) || vagon.getRamaChap1Nomeri().equals(korxona.getKramaOng2Nomeri()) || vagon.getRamaChap1Nomeri().equals(korxona.getKramaChap1Nomeri()) || vagon.getRamaChap1Nomeri().equals(korxona.getKramaChap2Nomeri()))
					savedVagon.setRamaChap1Nomeri(vagon.getRamaChap1Nomeri());
				if (vagon.getRamaChap2Nomeri().equals(korxona.getKramaOng1Nomeri()) || vagon.getRamaChap2Nomeri().equals(korxona.getKramaOng2Nomeri()) || vagon.getRamaChap2Nomeri().equals(korxona.getKramaChap1Nomeri()) || vagon.getRamaChap2Nomeri().equals(korxona.getKramaChap2Nomeri()))
					savedVagon.setRamaChap2Nomeri(vagon.getRamaChap2Nomeri());
				if (vagon.getBalka1Nomeri().equals(korxona.getKbalka1Nomeri()) || vagon.getBalka1Nomeri().equals(korxona.getKbalka2Nomeri()))
					savedVagon.setBalka1Nomeri(vagon.getBalka1Nomeri());
				if (vagon.getBalka2Nomeri().equals(korxona.getKbalka1Nomeri()) || vagon.getBalka2Nomeri().equals(korxona.getKbalka2Nomeri()))
					savedVagon.setBalka2Nomeri(vagon.getBalka2Nomeri());
				if (vagon.getGildirak1Nomeri().equals(korxona.getKgildirak1Nomeri()) || vagon.getGildirak1Nomeri().equals(korxona.getKgildirak2Nomeri()) || vagon.getGildirak1Nomeri().equals(korxona.getKgildirak3Nomeri()) || vagon.getGildirak1Nomeri().equals(korxona.getKgildirak4Nomeri()))
					savedVagon.setGildirak1Nomeri(vagon.getGildirak1Nomeri());
				if (vagon.getGildirak2Nomeri().equals(korxona.getKgildirak1Nomeri()) || vagon.getGildirak2Nomeri().equals(korxona.getKgildirak2Nomeri()) || vagon.getGildirak2Nomeri().equals(korxona.getKgildirak3Nomeri()) || vagon.getGildirak2Nomeri().equals(korxona.getKgildirak4Nomeri()))
					savedVagon.setGildirak2Nomeri(vagon.getGildirak2Nomeri());
				if (vagon.getGildirak3Nomeri().equals(korxona.getKgildirak1Nomeri()) || vagon.getGildirak3Nomeri().equals(korxona.getKgildirak2Nomeri()) || vagon.getGildirak3Nomeri().equals(korxona.getKgildirak3Nomeri()) || vagon.getGildirak3Nomeri().equals(korxona.getKgildirak4Nomeri()))
					savedVagon.setGildirak3Nomeri(vagon.getGildirak3Nomeri());
				if (vagon.getGildirak4Nomeri().equals(korxona.getKgildirak1Nomeri()) || vagon.getGildirak4Nomeri().equals(korxona.getKgildirak2Nomeri()) || vagon.getGildirak4Nomeri().equals(korxona.getKgildirak3Nomeri()) || vagon.getGildirak4Nomeri().equals(korxona.getKgildirak4Nomeri()))
					savedVagon.setGildirak4Nomeri(vagon.getGildirak4Nomeri());

			}
		}

		savedVagon.setRamaOng1(vagon.getRamaOng1());

		savedVagon.setRamaOng2(vagon.getRamaOng2());

		savedVagon.setRamaChap1(vagon.getRamaChap1());

		savedVagon.setRamaChap2(vagon.getRamaChap2());

		savedVagon.setBalka1(vagon.getBalka1());

		savedVagon.setBalka2(vagon.getBalka2());

		savedVagon.setGildirak1(vagon.getGildirak1());

		savedVagon.setGildirak2(vagon.getGildirak2());

		savedVagon.setGildirak3(vagon.getGildirak3());

		savedVagon.setGildirak4(vagon.getGildirak4());

		savedVagon.setIzohChiqish(vagon.getIzohChiqish());


		if (savedVagon.getRamaOng1Nomeri() != vagon.getRamaOng1Nomeri()){

			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());
			
			malumotRepository.save(savedVagon);

			return new ApiResponse(vagon.getRamaOng1Nomeri() + " (1-o'ng) nomerli yon rama  bazaga saqlanmagan", false);
		}
		if (savedVagon.getRamaOng2Nomeri() != vagon.getRamaOng2Nomeri()){
			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());
			
			malumotRepository.save(savedVagon);

			return new ApiResponse(vagon.getRamaOng2Nomeri() + " (2-o'ng) nomerli yon rama  bazaga saqlanmagan", false);
		}
		if (savedVagon.getRamaChap1Nomeri() != vagon.getRamaChap1Nomeri()){
			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());
			malumotRepository.save(savedVagon);

			return new ApiResponse(vagon.getRamaChap1Nomeri() + " (1-chap) nomerli yon rama  bazaga saqlanmagan", false);
		}
		if (savedVagon.getRamaChap2Nomeri() != vagon.getRamaChap2Nomeri()){
			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());
			malumotRepository.save(savedVagon);

			return new ApiResponse(vagon.getRamaChap2Nomeri() + " (2-chap) nomerli yon rama  bazaga saqlanmagan", false);
		}
		if (savedVagon.getBalka1Nomeri() != vagon.getBalka1Nomeri()){
			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());
			malumotRepository.save(savedVagon);

			return new ApiResponse(vagon.getBalka1Nomeri() + " (1-balka) nomerli ressor usti balka  bazaga saqlanmagan", false);
		}
		if (savedVagon.getBalka2Nomeri() != vagon.getBalka2Nomeri()){
			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());
			malumotRepository.save(savedVagon);

			return new ApiResponse(vagon.getBalka2Nomeri() + " (2-balka) nomerli ressor usti balka  bazaga saqlanmagan", false);
		}

		if (savedVagon.getGildirak1Nomeri() != vagon.getGildirak1Nomeri()){
			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());
			malumotRepository.save(savedVagon);


			return new ApiResponse(vagon.getGildirak1Nomeri() + " (1-g'ildirak juftligi) nomerli g'ildirak juftligi  bazaga saqlanmagan", false);
		}
		if (savedVagon.getGildirak2Nomeri() != vagon.getGildirak2Nomeri()){
			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());
			malumotRepository.save(savedVagon);

			return new ApiResponse(vagon.getGildirak2Nomeri() + " (2-g'ildirak juftligi) nomerli g'ildirak juftligi  bazaga saqlanmagan", false);
		}
		if (savedVagon.getGildirak3Nomeri() != vagon.getGildirak3Nomeri()){
			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());
			malumotRepository.save(savedVagon);

			return new ApiResponse(vagon.getGildirak3Nomeri() + " (3-g'ildirak juftligi) nomerli g'ildirak juftligi  bazaga saqlanmagan", false);
		}

		if (savedVagon.getGildirak4Nomeri() != vagon.getGildirak4Nomeri()){
			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());
			
			malumotRepository.save(savedVagon);

			return new ApiResponse(vagon.getGildirak4Nomeri() + " (4-g'ildirak juftligi) nomerli g'ildirak juftligi  bazaga saqlanmagan", false);
		}

		malumotRepository.save(savedVagon);

		return new ApiResponse(savedVagon.getNomer() + " nomerli vagon chiqish ma'lumotlari o'zgartirildi", true);

	}

	@Override
	public ApiResponse updateVagonSamChiqish(VagonMalumotChiqishDto vagon, Long id) {

		Optional<VagonMalumot> exist = malumotRepository.findById(id);
		if(!exist.isPresent())
			return new ApiResponse( "Bu nomerli vagon topilmadi", false);

		VagonMalumot savedVagon = exist.get();

		if (!savedVagon.getDepoNomi().equalsIgnoreCase("VCHD-6"))
			return new ApiResponse("Siz faqat VCHD-6 ga tegishli vagonlarni o'zgartira olasiz", false);

		VagonMalumot saved = malumotRepository.findById(id).get();

		if (savedVagon.getKorxona().equals("O'TY(ГАЖК)")) {

			savedVagon.setRamaOng1Nomeri(vagon.getRamaOng1Nomeri());
			savedVagon.setRamaOng2Nomeri(vagon.getRamaOng2Nomeri());
			savedVagon.setRamaChap1Nomeri(vagon.getRamaChap1Nomeri());
			savedVagon.setRamaChap2Nomeri(vagon.getRamaChap2Nomeri());
			savedVagon.setBalka1Nomeri(vagon.getBalka1Nomeri());
			savedVagon.setBalka2Nomeri(vagon.getBalka2Nomeri());
			savedVagon.setGildirak1Nomeri(vagon.getGildirak1Nomeri());
			savedVagon.setGildirak2Nomeri(vagon.getGildirak2Nomeri());
			savedVagon.setGildirak3Nomeri(vagon.getGildirak3Nomeri());
			savedVagon.setGildirak4Nomeri(vagon.getGildirak4Nomeri());


		}else {
			List<VagonMalumot> korxonalar = malumotRepository.findAllByEgasi("MDH(СНГ)","Sanoat(ПРОМ)");

			for (VagonMalumot korxona : korxonalar) {

				if (vagon.getRamaOng1Nomeri().equals(korxona.getKramaOng1Nomeri()) || vagon.getRamaOng1Nomeri().equals(korxona.getKramaOng2Nomeri()) || vagon.getRamaOng1Nomeri().equals(korxona.getKramaChap1Nomeri()) || vagon.getRamaOng1Nomeri().equals(korxona.getKramaChap2Nomeri()))
					savedVagon.setRamaOng1Nomeri(vagon.getRamaOng1Nomeri());
				if (vagon.getRamaOng2Nomeri().equals(korxona.getKramaOng1Nomeri()) || vagon.getRamaOng2Nomeri().equals(korxona.getKramaOng2Nomeri()) || vagon.getRamaOng2Nomeri().equals(korxona.getKramaChap1Nomeri()) || vagon.getRamaOng2Nomeri().equals(korxona.getKramaChap2Nomeri()))
					savedVagon.setRamaOng2Nomeri(vagon.getRamaOng2Nomeri());
				if (vagon.getRamaChap1Nomeri().equals(korxona.getKramaOng1Nomeri()) || vagon.getRamaChap1Nomeri().equals(korxona.getKramaOng2Nomeri()) || vagon.getRamaChap1Nomeri().equals(korxona.getKramaChap1Nomeri()) || vagon.getRamaChap1Nomeri().equals(korxona.getKramaChap2Nomeri()))
					savedVagon.setRamaChap1Nomeri(vagon.getRamaChap1Nomeri());
				if (vagon.getRamaChap2Nomeri().equals(korxona.getKramaOng1Nomeri()) || vagon.getRamaChap2Nomeri().equals(korxona.getKramaOng2Nomeri()) || vagon.getRamaChap2Nomeri().equals(korxona.getKramaChap1Nomeri()) || vagon.getRamaChap2Nomeri().equals(korxona.getKramaChap2Nomeri()))
					savedVagon.setRamaChap2Nomeri(vagon.getRamaChap2Nomeri());
				if (vagon.getBalka1Nomeri().equals(korxona.getKbalka1Nomeri()) || vagon.getBalka1Nomeri().equals(korxona.getKbalka2Nomeri()))
					savedVagon.setBalka1Nomeri(vagon.getBalka1Nomeri());
				if (vagon.getBalka2Nomeri().equals(korxona.getKbalka1Nomeri()) || vagon.getBalka2Nomeri().equals(korxona.getKbalka2Nomeri()))
					savedVagon.setBalka2Nomeri(vagon.getBalka2Nomeri());
				if (vagon.getGildirak1Nomeri().equals(korxona.getKgildirak1Nomeri()) || vagon.getGildirak1Nomeri().equals(korxona.getKgildirak2Nomeri()) || vagon.getGildirak1Nomeri().equals(korxona.getKgildirak3Nomeri()) || vagon.getGildirak1Nomeri().equals(korxona.getKgildirak4Nomeri()))
					savedVagon.setGildirak1Nomeri(vagon.getGildirak1Nomeri());
				if (vagon.getGildirak2Nomeri().equals(korxona.getKgildirak1Nomeri()) || vagon.getGildirak2Nomeri().equals(korxona.getKgildirak2Nomeri()) || vagon.getGildirak2Nomeri().equals(korxona.getKgildirak3Nomeri()) || vagon.getGildirak2Nomeri().equals(korxona.getKgildirak4Nomeri()))
					savedVagon.setGildirak2Nomeri(vagon.getGildirak2Nomeri());
				if (vagon.getGildirak3Nomeri().equals(korxona.getKgildirak1Nomeri()) || vagon.getGildirak3Nomeri().equals(korxona.getKgildirak2Nomeri()) || vagon.getGildirak3Nomeri().equals(korxona.getKgildirak3Nomeri()) || vagon.getGildirak3Nomeri().equals(korxona.getKgildirak4Nomeri()))
					savedVagon.setGildirak3Nomeri(vagon.getGildirak3Nomeri());
				if (vagon.getGildirak4Nomeri().equals(korxona.getKgildirak1Nomeri()) || vagon.getGildirak4Nomeri().equals(korxona.getKgildirak2Nomeri()) || vagon.getGildirak4Nomeri().equals(korxona.getKgildirak3Nomeri()) || vagon.getGildirak4Nomeri().equals(korxona.getKgildirak4Nomeri()))
					savedVagon.setGildirak4Nomeri(vagon.getGildirak4Nomeri());

			}
		}

		savedVagon.setRamaOng1(vagon.getRamaOng1());

		savedVagon.setRamaOng2(vagon.getRamaOng2());

		savedVagon.setRamaChap1(vagon.getRamaChap1());

		savedVagon.setRamaChap2(vagon.getRamaChap2());

		savedVagon.setBalka1(vagon.getBalka1());

		savedVagon.setBalka2(vagon.getBalka2());

		savedVagon.setGildirak1(vagon.getGildirak1());

		savedVagon.setGildirak2(vagon.getGildirak2());

		savedVagon.setGildirak3(vagon.getGildirak3());

		savedVagon.setGildirak4(vagon.getGildirak4());

		savedVagon.setIzohChiqish(vagon.getIzohChiqish());


		if (savedVagon.getRamaOng1Nomeri() != vagon.getRamaOng1Nomeri()){

			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());

			malumotRepository.save(savedVagon);

			return new ApiResponse(vagon.getRamaOng1Nomeri() + " (1-o'ng) nomerli yon rama  bazaga saqlanmagan", false);
		}
		if (savedVagon.getRamaOng2Nomeri() != vagon.getRamaOng2Nomeri()){
			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());

			malumotRepository.save(savedVagon);

			return new ApiResponse(vagon.getRamaOng2Nomeri() + " (2-o'ng) nomerli yon rama  bazaga saqlanmagan", false);
		}
		if (savedVagon.getRamaChap1Nomeri() != vagon.getRamaChap1Nomeri()){
			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());
			malumotRepository.save(savedVagon);

			return new ApiResponse(vagon.getRamaChap1Nomeri() + " (1-chap) nomerli yon rama  bazaga saqlanmagan", false);
		}
		if (savedVagon.getRamaChap2Nomeri() != vagon.getRamaChap2Nomeri()){
			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());
			malumotRepository.save(savedVagon);

			return new ApiResponse(vagon.getRamaChap2Nomeri() + " (2-chap) nomerli yon rama  bazaga saqlanmagan", false);
		}
		if (savedVagon.getBalka1Nomeri() != vagon.getBalka1Nomeri()){
			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());
			malumotRepository.save(savedVagon);

			return new ApiResponse(vagon.getBalka1Nomeri() + " (1-balka) nomerli ressor usti balka  bazaga saqlanmagan", false);
		}
		if (savedVagon.getBalka2Nomeri() != vagon.getBalka2Nomeri()){
			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());
			malumotRepository.save(savedVagon);

			return new ApiResponse(vagon.getBalka2Nomeri() + " (2-balka) nomerli ressor usti balka  bazaga saqlanmagan", false);
		}

		if (savedVagon.getGildirak1Nomeri() != vagon.getGildirak1Nomeri()){
			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());
			malumotRepository.save(savedVagon);


			return new ApiResponse(vagon.getGildirak1Nomeri() + " (1-g'ildirak juftligi) nomerli g'ildirak juftligi  bazaga saqlanmagan", false);
		}
		if (savedVagon.getGildirak2Nomeri() != vagon.getGildirak2Nomeri()){
			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());
			malumotRepository.save(savedVagon);

			return new ApiResponse(vagon.getGildirak2Nomeri() + " (2-g'ildirak juftligi) nomerli g'ildirak juftligi  bazaga saqlanmagan", false);
		}
		if (savedVagon.getGildirak3Nomeri() != vagon.getGildirak3Nomeri()){
			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());
			malumotRepository.save(savedVagon);

			return new ApiResponse(vagon.getGildirak3Nomeri() + " (3-g'ildirak juftligi) nomerli g'ildirak juftligi  bazaga saqlanmagan", false);
		}

		if (savedVagon.getGildirak4Nomeri() != vagon.getGildirak4Nomeri()){
			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());

			malumotRepository.save(savedVagon);

			return new ApiResponse(vagon.getGildirak4Nomeri() + " (4-g'ildirak juftligi) nomerli g'ildirak juftligi  bazaga saqlanmagan", false);
		}

		malumotRepository.save(savedVagon);

		return new ApiResponse(savedVagon.getNomer() + " nomerli vagon chiqish ma'lumotlari o'zgartirildi", true);

	}

	@Override
	public ApiResponse updateVagonHavChiqish(VagonMalumotChiqishDto vagon, Long id) {

		Optional<VagonMalumot> exist = malumotRepository.findById(id);
		if(!exist.isPresent())
			return new ApiResponse( "Bu nomerli vagon topilmadi", false);

		VagonMalumot savedVagon = exist.get();

		if (!savedVagon.getDepoNomi().equalsIgnoreCase("VCHD-3"))
			return new ApiResponse("Siz faqat VCHD-3 ga tegishli vagonlarni o'zgartira olasiz", false);

		VagonMalumot saved = malumotRepository.findById(id).get();

		if (savedVagon.getKorxona().equals("O'TY(ГАЖК)")) {

			savedVagon.setRamaOng1Nomeri(vagon.getRamaOng1Nomeri());
			savedVagon.setRamaOng2Nomeri(vagon.getRamaOng2Nomeri());
			savedVagon.setRamaChap1Nomeri(vagon.getRamaChap1Nomeri());
			savedVagon.setRamaChap2Nomeri(vagon.getRamaChap2Nomeri());
			savedVagon.setBalka1Nomeri(vagon.getBalka1Nomeri());
			savedVagon.setBalka2Nomeri(vagon.getBalka2Nomeri());
			savedVagon.setGildirak1Nomeri(vagon.getGildirak1Nomeri());
			savedVagon.setGildirak2Nomeri(vagon.getGildirak2Nomeri());
			savedVagon.setGildirak3Nomeri(vagon.getGildirak3Nomeri());
			savedVagon.setGildirak4Nomeri(vagon.getGildirak4Nomeri());


		}else {
			List<VagonMalumot> korxonalar = malumotRepository.findAllByEgasi("MDH(СНГ)","Sanoat(ПРОМ)");

			for (VagonMalumot korxona : korxonalar) {

				if (vagon.getRamaOng1Nomeri().equals(korxona.getKramaOng1Nomeri()) || vagon.getRamaOng1Nomeri().equals(korxona.getKramaOng2Nomeri()) || vagon.getRamaOng1Nomeri().equals(korxona.getKramaChap1Nomeri()) || vagon.getRamaOng1Nomeri().equals(korxona.getKramaChap2Nomeri()))
					savedVagon.setRamaOng1Nomeri(vagon.getRamaOng1Nomeri());
				if (vagon.getRamaOng2Nomeri().equals(korxona.getKramaOng1Nomeri()) || vagon.getRamaOng2Nomeri().equals(korxona.getKramaOng2Nomeri()) || vagon.getRamaOng2Nomeri().equals(korxona.getKramaChap1Nomeri()) || vagon.getRamaOng2Nomeri().equals(korxona.getKramaChap2Nomeri()))
					savedVagon.setRamaOng2Nomeri(vagon.getRamaOng2Nomeri());
				if (vagon.getRamaChap1Nomeri().equals(korxona.getKramaOng1Nomeri()) || vagon.getRamaChap1Nomeri().equals(korxona.getKramaOng2Nomeri()) || vagon.getRamaChap1Nomeri().equals(korxona.getKramaChap1Nomeri()) || vagon.getRamaChap1Nomeri().equals(korxona.getKramaChap2Nomeri()))
					savedVagon.setRamaChap1Nomeri(vagon.getRamaChap1Nomeri());
				if (vagon.getRamaChap2Nomeri().equals(korxona.getKramaOng1Nomeri()) || vagon.getRamaChap2Nomeri().equals(korxona.getKramaOng2Nomeri()) || vagon.getRamaChap2Nomeri().equals(korxona.getKramaChap1Nomeri()) || vagon.getRamaChap2Nomeri().equals(korxona.getKramaChap2Nomeri()))
					savedVagon.setRamaChap2Nomeri(vagon.getRamaChap2Nomeri());
				if (vagon.getBalka1Nomeri().equals(korxona.getKbalka1Nomeri()) || vagon.getBalka1Nomeri().equals(korxona.getKbalka2Nomeri()))
					savedVagon.setBalka1Nomeri(vagon.getBalka1Nomeri());
				if (vagon.getBalka2Nomeri().equals(korxona.getKbalka1Nomeri()) || vagon.getBalka2Nomeri().equals(korxona.getKbalka2Nomeri()))
					savedVagon.setBalka2Nomeri(vagon.getBalka2Nomeri());
				if (vagon.getGildirak1Nomeri().equals(korxona.getKgildirak1Nomeri()) || vagon.getGildirak1Nomeri().equals(korxona.getKgildirak2Nomeri()) || vagon.getGildirak1Nomeri().equals(korxona.getKgildirak3Nomeri()) || vagon.getGildirak1Nomeri().equals(korxona.getKgildirak4Nomeri()))
					savedVagon.setGildirak1Nomeri(vagon.getGildirak1Nomeri());
				if (vagon.getGildirak2Nomeri().equals(korxona.getKgildirak1Nomeri()) || vagon.getGildirak2Nomeri().equals(korxona.getKgildirak2Nomeri()) || vagon.getGildirak2Nomeri().equals(korxona.getKgildirak3Nomeri()) || vagon.getGildirak2Nomeri().equals(korxona.getKgildirak4Nomeri()))
					savedVagon.setGildirak2Nomeri(vagon.getGildirak2Nomeri());
				if (vagon.getGildirak3Nomeri().equals(korxona.getKgildirak1Nomeri()) || vagon.getGildirak3Nomeri().equals(korxona.getKgildirak2Nomeri()) || vagon.getGildirak3Nomeri().equals(korxona.getKgildirak3Nomeri()) || vagon.getGildirak3Nomeri().equals(korxona.getKgildirak4Nomeri()))
					savedVagon.setGildirak3Nomeri(vagon.getGildirak3Nomeri());
				if (vagon.getGildirak4Nomeri().equals(korxona.getKgildirak1Nomeri()) || vagon.getGildirak4Nomeri().equals(korxona.getKgildirak2Nomeri()) || vagon.getGildirak4Nomeri().equals(korxona.getKgildirak3Nomeri()) || vagon.getGildirak4Nomeri().equals(korxona.getKgildirak4Nomeri()))
					savedVagon.setGildirak4Nomeri(vagon.getGildirak4Nomeri());

			}
		}

		savedVagon.setRamaOng1(vagon.getRamaOng1());

		savedVagon.setRamaOng2(vagon.getRamaOng2());

		savedVagon.setRamaChap1(vagon.getRamaChap1());

		savedVagon.setRamaChap2(vagon.getRamaChap2());

		savedVagon.setBalka1(vagon.getBalka1());

		savedVagon.setBalka2(vagon.getBalka2());

		savedVagon.setGildirak1(vagon.getGildirak1());

		savedVagon.setGildirak2(vagon.getGildirak2());

		savedVagon.setGildirak3(vagon.getGildirak3());

		savedVagon.setGildirak4(vagon.getGildirak4());

		savedVagon.setIzohChiqish(vagon.getIzohChiqish());


		if (savedVagon.getRamaOng1Nomeri() != vagon.getRamaOng1Nomeri()){

			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());

			malumotRepository.save(savedVagon);

			return new ApiResponse(vagon.getRamaOng1Nomeri() + " (1-o'ng) nomerli yon rama  bazaga saqlanmagan", false);
		}
		if (savedVagon.getRamaOng2Nomeri() != vagon.getRamaOng2Nomeri()){
			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());

			malumotRepository.save(savedVagon);

			return new ApiResponse(vagon.getRamaOng2Nomeri() + " (2-o'ng) nomerli yon rama  bazaga saqlanmagan", false);
		}
		if (savedVagon.getRamaChap1Nomeri() != vagon.getRamaChap1Nomeri()){
			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());
			malumotRepository.save(savedVagon);

			return new ApiResponse(vagon.getRamaChap1Nomeri() + " (1-chap) nomerli yon rama  bazaga saqlanmagan", false);
		}
		if (savedVagon.getRamaChap2Nomeri() != vagon.getRamaChap2Nomeri()){
			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());
			malumotRepository.save(savedVagon);

			return new ApiResponse(vagon.getRamaChap2Nomeri() + " (2-chap) nomerli yon rama  bazaga saqlanmagan", false);
		}
		if (savedVagon.getBalka1Nomeri() != vagon.getBalka1Nomeri()){
			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());
			malumotRepository.save(savedVagon);

			return new ApiResponse(vagon.getBalka1Nomeri() + " (1-balka) nomerli ressor usti balka  bazaga saqlanmagan", false);
		}
		if (savedVagon.getBalka2Nomeri() != vagon.getBalka2Nomeri()){
			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());
			malumotRepository.save(savedVagon);

			return new ApiResponse(vagon.getBalka2Nomeri() + " (2-balka) nomerli ressor usti balka  bazaga saqlanmagan", false);
		}

		if (savedVagon.getGildirak1Nomeri() != vagon.getGildirak1Nomeri()){
			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());
			malumotRepository.save(savedVagon);


			return new ApiResponse(vagon.getGildirak1Nomeri() + " (1-g'ildirak juftligi) nomerli g'ildirak juftligi  bazaga saqlanmagan", false);
		}
		if (savedVagon.getGildirak2Nomeri() != vagon.getGildirak2Nomeri()){
			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());
			malumotRepository.save(savedVagon);

			return new ApiResponse(vagon.getGildirak2Nomeri() + " (2-g'ildirak juftligi) nomerli g'ildirak juftligi  bazaga saqlanmagan", false);
		}
		if (savedVagon.getGildirak3Nomeri() != vagon.getGildirak3Nomeri()){
			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());
			malumotRepository.save(savedVagon);

			return new ApiResponse(vagon.getGildirak3Nomeri() + " (3-g'ildirak juftligi) nomerli g'ildirak juftligi  bazaga saqlanmagan", false);
		}

		if (savedVagon.getGildirak4Nomeri() != vagon.getGildirak4Nomeri()){
			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());

			malumotRepository.save(savedVagon);

			return new ApiResponse(vagon.getGildirak4Nomeri() + " (4-g'ildirak juftligi) nomerli g'ildirak juftligi  bazaga saqlanmagan", false);
		}

		malumotRepository.save(savedVagon);

		return new ApiResponse(savedVagon.getNomer() + " nomerli vagon chiqish ma'lumotlari o'zgartirildi", true);

	}

	@Override
	public ApiResponse updateVagonAndjChiqish(VagonMalumotChiqishDto vagon, Long id) {

		Optional<VagonMalumot> exist = malumotRepository.findById(id);
		if(!exist.isPresent())
			return new ApiResponse( "Ma'lumot topilmadi", false);

		VagonMalumot savedVagon = exist.get();

		if (!savedVagon.getDepoNomi().equalsIgnoreCase("VCHD-5"))
			return new ApiResponse("Siz faqat VCHD-5 ga tegishli vagonlarni o'zgartira olasiz", false);

		VagonMalumot saved = malumotRepository.findById(id).get();

		if (savedVagon.getKorxona().equals("O'TY(ГАЖК)")) {

			savedVagon.setRamaOng1Nomeri(vagon.getRamaOng1Nomeri());
			savedVagon.setRamaOng2Nomeri(vagon.getRamaOng2Nomeri());
			savedVagon.setRamaChap1Nomeri(vagon.getRamaChap1Nomeri());
			savedVagon.setRamaChap2Nomeri(vagon.getRamaChap2Nomeri());
			savedVagon.setBalka1Nomeri(vagon.getBalka1Nomeri());
			savedVagon.setBalka2Nomeri(vagon.getBalka2Nomeri());
			savedVagon.setGildirak1Nomeri(vagon.getGildirak1Nomeri());
			savedVagon.setGildirak2Nomeri(vagon.getGildirak2Nomeri());
			savedVagon.setGildirak3Nomeri(vagon.getGildirak3Nomeri());
			savedVagon.setGildirak4Nomeri(vagon.getGildirak4Nomeri());


		}else {
			List<VagonMalumot> korxonalar = malumotRepository.findAllByEgasi("MDH(СНГ)","Sanoat(ПРОМ)");

			for (VagonMalumot korxona : korxonalar) {

				if (vagon.getRamaOng1Nomeri().equals(korxona.getKramaOng1Nomeri()) || vagon.getRamaOng1Nomeri().equals(korxona.getKramaOng2Nomeri()) || vagon.getRamaOng1Nomeri().equals(korxona.getKramaChap1Nomeri()) || vagon.getRamaOng1Nomeri().equals(korxona.getKramaChap2Nomeri()))
					savedVagon.setRamaOng1Nomeri(vagon.getRamaOng1Nomeri());
				if (vagon.getRamaOng2Nomeri().equals(korxona.getKramaOng1Nomeri()) || vagon.getRamaOng2Nomeri().equals(korxona.getKramaOng2Nomeri()) || vagon.getRamaOng2Nomeri().equals(korxona.getKramaChap1Nomeri()) || vagon.getRamaOng2Nomeri().equals(korxona.getKramaChap2Nomeri()))
					savedVagon.setRamaOng2Nomeri(vagon.getRamaOng2Nomeri());
				if (vagon.getRamaChap1Nomeri().equals(korxona.getKramaOng1Nomeri()) || vagon.getRamaChap1Nomeri().equals(korxona.getKramaOng2Nomeri()) || vagon.getRamaChap1Nomeri().equals(korxona.getKramaChap1Nomeri()) || vagon.getRamaChap1Nomeri().equals(korxona.getKramaChap2Nomeri()))
					savedVagon.setRamaChap1Nomeri(vagon.getRamaChap1Nomeri());
				if (vagon.getRamaChap2Nomeri().equals(korxona.getKramaOng1Nomeri()) || vagon.getRamaChap2Nomeri().equals(korxona.getKramaOng2Nomeri()) || vagon.getRamaChap2Nomeri().equals(korxona.getKramaChap1Nomeri()) || vagon.getRamaChap2Nomeri().equals(korxona.getKramaChap2Nomeri()))
					savedVagon.setRamaChap2Nomeri(vagon.getRamaChap2Nomeri());
				if (vagon.getBalka1Nomeri().equals(korxona.getKbalka1Nomeri()) || vagon.getBalka1Nomeri().equals(korxona.getKbalka2Nomeri()))
					savedVagon.setBalka1Nomeri(vagon.getBalka1Nomeri());
				if (vagon.getBalka2Nomeri().equals(korxona.getKbalka1Nomeri()) || vagon.getBalka2Nomeri().equals(korxona.getKbalka2Nomeri()))
					savedVagon.setBalka2Nomeri(vagon.getBalka2Nomeri());
				if (vagon.getGildirak1Nomeri().equals(korxona.getKgildirak1Nomeri()) || vagon.getGildirak1Nomeri().equals(korxona.getKgildirak2Nomeri()) || vagon.getGildirak1Nomeri().equals(korxona.getKgildirak3Nomeri()) || vagon.getGildirak1Nomeri().equals(korxona.getKgildirak4Nomeri()))
					savedVagon.setGildirak1Nomeri(vagon.getGildirak1Nomeri());
				if (vagon.getGildirak2Nomeri().equals(korxona.getKgildirak1Nomeri()) || vagon.getGildirak2Nomeri().equals(korxona.getKgildirak2Nomeri()) || vagon.getGildirak2Nomeri().equals(korxona.getKgildirak3Nomeri()) || vagon.getGildirak2Nomeri().equals(korxona.getKgildirak4Nomeri()))
					savedVagon.setGildirak2Nomeri(vagon.getGildirak2Nomeri());
				if (vagon.getGildirak3Nomeri().equals(korxona.getKgildirak1Nomeri()) || vagon.getGildirak3Nomeri().equals(korxona.getKgildirak2Nomeri()) || vagon.getGildirak3Nomeri().equals(korxona.getKgildirak3Nomeri()) || vagon.getGildirak3Nomeri().equals(korxona.getKgildirak4Nomeri()))
					savedVagon.setGildirak3Nomeri(vagon.getGildirak3Nomeri());
				if (vagon.getGildirak4Nomeri().equals(korxona.getKgildirak1Nomeri()) || vagon.getGildirak4Nomeri().equals(korxona.getKgildirak2Nomeri()) || vagon.getGildirak4Nomeri().equals(korxona.getKgildirak3Nomeri()) || vagon.getGildirak4Nomeri().equals(korxona.getKgildirak4Nomeri()))
					savedVagon.setGildirak4Nomeri(vagon.getGildirak4Nomeri());

			}
		}

		savedVagon.setRamaOng1(vagon.getRamaOng1());

		savedVagon.setRamaOng2(vagon.getRamaOng2());

		savedVagon.setRamaChap1(vagon.getRamaChap1());

		savedVagon.setRamaChap2(vagon.getRamaChap2());

		savedVagon.setBalka1(vagon.getBalka1());

		savedVagon.setBalka2(vagon.getBalka2());

		savedVagon.setGildirak1(vagon.getGildirak1());

		savedVagon.setGildirak2(vagon.getGildirak2());

		savedVagon.setGildirak3(vagon.getGildirak3());

		savedVagon.setGildirak4(vagon.getGildirak4());

		savedVagon.setIzohChiqish(vagon.getIzohChiqish());


		if (savedVagon.getRamaOng1Nomeri() != vagon.getRamaOng1Nomeri()){

			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());

			malumotRepository.save(savedVagon);

			return new ApiResponse(vagon.getRamaOng1Nomeri() + " (1-o'ng) nomerli yon rama  bazaga saqlanmagan", false);
		}
		if (savedVagon.getRamaOng2Nomeri() != vagon.getRamaOng2Nomeri()){
			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());

			malumotRepository.save(savedVagon);

			return new ApiResponse(vagon.getRamaOng2Nomeri() + " (2-o'ng) nomerli yon rama  bazaga saqlanmagan", false);
		}
		if (savedVagon.getRamaChap1Nomeri() != vagon.getRamaChap1Nomeri()){
			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());
			malumotRepository.save(savedVagon);

			return new ApiResponse(vagon.getRamaChap1Nomeri() + " (1-chap) nomerli yon rama  bazaga saqlanmagan", false);
		}
		if (savedVagon.getRamaChap2Nomeri() != vagon.getRamaChap2Nomeri()){
			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());
			malumotRepository.save(savedVagon);

			return new ApiResponse(vagon.getRamaChap2Nomeri() + " (2-chap) nomerli yon rama  bazaga saqlanmagan", false);
		}
		if (savedVagon.getBalka1Nomeri() != vagon.getBalka1Nomeri()){
			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());
			malumotRepository.save(savedVagon);

			return new ApiResponse(vagon.getBalka1Nomeri() + " (1-balka) nomerli ressor usti balka  bazaga saqlanmagan", false);
		}
		if (savedVagon.getBalka2Nomeri() != vagon.getBalka2Nomeri()){
			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());
			malumotRepository.save(savedVagon);

			return new ApiResponse(vagon.getBalka2Nomeri() + " (2-balka) nomerli ressor usti balka  bazaga saqlanmagan", false);
		}

		if (savedVagon.getGildirak1Nomeri() != vagon.getGildirak1Nomeri()){
			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());
			malumotRepository.save(savedVagon);


			return new ApiResponse(vagon.getGildirak1Nomeri() + " (1-g'ildirak juftligi) nomerli g'ildirak juftligi  bazaga saqlanmagan", false);
		}
		if (savedVagon.getGildirak2Nomeri() != vagon.getGildirak2Nomeri()){
			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());
			malumotRepository.save(savedVagon);

			return new ApiResponse(vagon.getGildirak2Nomeri() + " (2-g'ildirak juftligi) nomerli g'ildirak juftligi  bazaga saqlanmagan", false);
		}
		if (savedVagon.getGildirak3Nomeri() != vagon.getGildirak3Nomeri()){
			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());
			malumotRepository.save(savedVagon);

			return new ApiResponse(vagon.getGildirak3Nomeri() + " (3-g'ildirak juftligi) nomerli g'ildirak juftligi  bazaga saqlanmagan", false);
		}

		if (savedVagon.getGildirak4Nomeri() != vagon.getGildirak4Nomeri()){
			savedVagon.setRamaOng1(saved.getRamaOng1());
			savedVagon.setRamaOng1Nomeri(saved.getRamaOng1Nomeri());

			savedVagon.setRamaOng2(saved.getRamaOng2());
			savedVagon.setRamaOng2Nomeri(saved.getRamaOng2Nomeri());

			savedVagon.setRamaChap1(saved.getRamaChap1());
			savedVagon.setRamaChap1Nomeri(saved.getRamaChap1Nomeri());

			savedVagon.setRamaChap2(saved.getRamaChap2());
			savedVagon.setRamaChap2Nomeri(saved.getRamaChap2Nomeri());

			savedVagon.setBalka1(saved.getBalka1());
			savedVagon.setBalka1Nomeri(saved.getBalka1Nomeri());

			savedVagon.setBalka2(saved.getBalka2());
			savedVagon.setBalka2Nomeri(saved.getBalka2Nomeri());

			savedVagon.setGildirak1(saved.getGildirak1());
			savedVagon.setGildirak1Nomeri(saved.getGildirak1Nomeri());

			savedVagon.setGildirak2(saved.getGildirak2());
			savedVagon.setGildirak2Nomeri(saved.getGildirak2Nomeri());

			savedVagon.setGildirak3(saved.getGildirak3());
			savedVagon.setGildirak3Nomeri(saved.getGildirak3Nomeri());

			savedVagon.setGildirak4(saved.getGildirak4());
			savedVagon.setGildirak4Nomeri(saved.getGildirak4Nomeri());

			savedVagon.setIzohChiqish(saved.getIzohChiqish());

			malumotRepository.save(savedVagon);

			return new ApiResponse(vagon.getGildirak4Nomeri() + " (4-g'ildirak juftligi) nomerli g'ildirak juftligi  bazaga saqlanmagan", false);
		}

		malumotRepository.save(savedVagon);

		return new ApiResponse(savedVagon.getNomer() + " nomerli vagon chiqish ma'lumotlari o'zgartirildi", true);

	}

	@Override
	public VagonMalumot findById(Long id) {
		return malumotRepository.findById(id).get();
	}


	@Override
	public VagonMalumot getVagonById(long id) {
	Optional<VagonMalumot> exist =	malumotRepository.findById(id);
		if (exist.isPresent()){

		}
		return exist.orElseGet(VagonMalumot::new);
	}

	@Override
	public ApiResponse deleteVagonById(long id) throws NotFoundException {
		Optional<VagonMalumot> exist = malumotRepository.findById(id);
		if(exist.isPresent()) {
			malumotRepository.deleteById(id);
			return new ApiResponse(exist.get().getNomer() + " nomerli vagon bazadan o'chirildi", true);
		}else {
			return new ApiResponse(exist.get().getNomer() + " nomerli vagon bazada mavjud emas", false);
		}
	}

	@Override
	public ApiResponse deleteVagonSam(long id) throws NotFoundException {
		VagonMalumot exist=	malumotRepository.findById(id).get();
		if(exist.getDepoNomi().equals("VCHD-6") ) {
			malumotRepository.deleteById(id);

			LocalDateTime today = LocalDateTime.now();
			LocalDateTime minusHours = today.plusHours(5);
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			samDate = minusHours.format(myFormatObj);

			Optional<LastActionTimes> optionalMalumotTime = utyTimeRepository.findById(1);
			optionalMalumotTime.get().setSamMalumotDate(samDate);
			utyTimeRepository.save(optionalMalumotTime.get());

			return new ApiResponse(exist.getNomer() + " nomerli vagon bazadan o'chirildi", true);

		}else {
			return new ApiResponse("Siz faqat VCHD-6 dagi vagon ma'lumotlarini o'chirishingiz mumkin", false);
		}
	}

	@Override
	public ApiResponse deleteVagonHav(long id) throws NotFoundException{
		VagonMalumot exist=	malumotRepository.findById(id).get();
		if(exist.getDepoNomi().equals("VCHD-3") ) {
			malumotRepository.deleteById(id);

			LocalDateTime today = LocalDateTime.now();
			LocalDateTime minusHours = today.plusHours(5);
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			havDate = minusHours.format(myFormatObj);

			Optional<LastActionTimes> optionalMalumotTime = utyTimeRepository.findById(1);
			optionalMalumotTime.get().setHavMalumotDate(havDate);
			utyTimeRepository.save(optionalMalumotTime.get());

			return new ApiResponse(exist.getNomer() + " nomerli vagon bazadan o'chirildi", true);
		}else {
			return new ApiResponse("Siz faqat VCHD-3 dagi vagon ma'lumotlarini o'chirishingiz mumkin", false);
		}
	}

	@Override
	public ApiResponse deleteVagonAndj(long id) throws NotFoundException{
		VagonMalumot exist=	malumotRepository.findById(id).get();
		if(exist.getDepoNomi().equals("VCHD-5") ) {
			malumotRepository.deleteById(id);

			LocalDateTime today = LocalDateTime.now();
			LocalDateTime minusHours = today.plusHours(5);
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			andjDate = minusHours.format(myFormatObj);

			Optional<LastActionTimes> optionalMalumotTime = utyTimeRepository.findById(1);
			optionalMalumotTime.get().setAndjMalumotDate(andjDate);
			utyTimeRepository.save(optionalMalumotTime.get());

			return new ApiResponse(exist.getNomer() + " nomerli vagon bazadan o'chirildi", true);
		}else {
			return new ApiResponse(" Siz faqat VCHD-5 dagi vagon ma'lumotlarini o'chirishingiz mumkin", false);
		}
	}

	@Override
	public ApiResponse searchByNomer(Integer nomer) {
		Optional<VagonMalumot> exist=malumotRepository.findByNomer(nomer);
		if (exist.isPresent()){
			return new ApiResponse(exist.get(), true);
		}else {
			return new ApiResponse(nomer + " nomerli vagon mavjud emas ", false);
		}

	}

//		filterniki
	@Override
	public List<VagonMalumot> filterByDate(String saqlanganVaqt) {
		return malumotRepository.filterByDate( saqlanganVaqt);
	}

	@Override
	public List<VagonMalumot> filterByCountry(String country) {
		return malumotRepository.filterByCountry( country);
	}

	@Override
	public List<VagonMalumot> filterByDepoNomi(String depoNomi) {
		return malumotRepository.filterByDepoNomi( depoNomi);
	}

	@Override
	public List<VagonMalumot> filterByCountryAndDate(String country, String saqlanganVaqt) {
		return malumotRepository.filterByCountryAndDate( country, saqlanganVaqt);
	}

	@Override
	public List<VagonMalumot> filterByDepoNomiAndCountry(String depoNomi, String country) {
		return malumotRepository.filterByDepoNomiAndCountry(depoNomi, country);
	}

	@Override
	public List<VagonMalumot> filterByDepoNomiAndDate(String depoNomi, String saqlanganVaqt) {
		return malumotRepository.filterByDepoNomiAndDate( depoNomi, saqlanganVaqt);
	}

	@Override
	public List<VagonMalumot> filterByDepoNomiCountryAndDate(String depoNomi, String country, String saqlanganVaqt) {
		return malumotRepository.filterByDepoNomiCountryAndDate( depoNomi, country, saqlanganVaqt);
	}

}
