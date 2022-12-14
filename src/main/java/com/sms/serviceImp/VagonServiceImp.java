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
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.property.TextAlignment;
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
import com.sms.dto.VagonDto;
import com.sms.model.VagonModel;
import com.sms.repository.VagonRepository;
import com.sms.service.VagonService;

@Service
public class VagonServiceImp implements VagonService{

	@Autowired
	private VagonRepository vagonRepository;
	@Autowired
	private TimeRepository utyTimeRepository;

	LocalDateTime today = LocalDateTime.now();
	LocalDateTime minusHours = today.plusHours(5);
	DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    String currentDate  = minusHours.format(myFormatObj);
	
	String samDate ;
	String havDate ;
	String andjDate;
	
	public void createPdf(List<VagonModel> vagons, HttpServletResponse response) throws IOException {

		String home = System.getProperty("user.home");
		  File file = new File(home + "/Downloads" + "/Qoldiq.pdf");
		  if (!file.getParentFile().exists())
		      file.getParentFile().mkdirs();
		  if (!file.exists())
		      file.createNewFile();
		List<VagonModel> allVagons = vagons;
		try {
			response.setHeader("Content-Disposition",
                    "attachment;fileName=\"" + "Qoldiq vagonlar.pdf" +"\"");
			response.setContentType("application/pdf");
			
			
			PdfWriter writer = new PdfWriter(file.getAbsolutePath());
			PdfDocument pdfDoc = new PdfDocument(writer);
			Document doc = new Document(pdfDoc);

			String FONT_FILENAME = "./src/main/resources/arial.ttf";
			PdfFont font = PdfFontFactory.createFont(FONT_FILENAME, PdfEncodings.IDENTITY_H);
			doc.setFont(font);

			Paragraph paragraph = new Paragraph("Qoldiq vagonlar");
			paragraph.setBackgroundColor(Color.DARK_GRAY);
			paragraph.setFontColor(Color.WHITE);// Setting background color to cell1
			paragraph.setBorder(Border.NO_BORDER);            // Setting border to cell1
			paragraph.setTextAlignment(TextAlignment.CENTER); // Setting text alignment to cell1
			paragraph.setFontSize(16);

			float[] columnWidth = {30f,200f,200f,200f,200f,200f,200f,200f,200f,200f};
			Table table = new Table(columnWidth);
			table.setTextAlignment(TextAlignment.CENTER);
			table.addCell(new Cell().add("\n ??? "));
			table.addCell(new Cell().add("\n Nomeri"));
			table.addCell(new Cell().add("\n Vagon turi"));
			table.addCell(new Cell().add("\n VCHD"));
			table.addCell(new Cell().add("\n Ta'mir turi"));
			table.addCell(new Cell().add("Ishlab chiqarilgan yili"));
			table.addCell(new Cell().add("Depoga kelgan vaqti"));
			table.addCell(new Cell().add("\n Saqlangan vaqti"));
			table.addCell(new Cell().add("\n Egasi"));
			table.addCell(new Cell().add("\n Izoh"));
			int i=0;
			for(VagonModel vagon:allVagons) {
				i++;
				table.addCell(new Cell().add(String.valueOf(i)));
				table.addCell(new Cell().add(String.valueOf(vagon.getNomer())));
				table.addCell(new Cell().add(vagon.getVagonTuri()));
				table.addCell(new Cell().add(vagon.getDepoNomi()));
				table.addCell(new Cell().add(vagon.getRemontTuri()));
				table.addCell(new Cell().add(String.valueOf(vagon.getIshlabChiqarilganYili())));
				table.addCell(new Cell().add(String.valueOf(vagon.getKelganVaqti())));
				table.addCell(new Cell().add(String.valueOf(vagon.getCreatedDate())));
				table.addCell(new Cell().add(vagon.getCountry()));
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
	public void pdfTableFile(List<Integer> vagonsToDownloadTables, HttpServletResponse response) throws IOException {

			String home = System.getProperty("user.home");
			File file = new File(home + "/Downloads" + "/Qoldiq vagonlar (Jadval).pdf");
			if (!file.getParentFile().exists())
				file.getParentFile().mkdirs();
			if (!file.exists())
				file.createNewFile();
			List<Integer> allVagons = vagonsToDownloadTables;
			try {
				response.setHeader("Content-Disposition",
						"attachment;fileName=\"" + "Qoldiq vagonlar (Jadval).pdf" +"\"");
				response.setContentType("application/pdf");


				PdfWriter writer = new PdfWriter(file.getAbsolutePath());
				PdfDocument pdfDoc = new PdfDocument(writer);
				Document doc = new Document(pdfDoc);

				String FONT_FILENAME = "./src/main/resources/arial.ttf";
				PdfFont font = PdfFontFactory.createFont(FONT_FILENAME, PdfEncodings.IDENTITY_H);
				doc.setFont(font);

				Paragraph paragraph = new Paragraph("Qoldiq vagonlar");
				paragraph.setBackgroundColor(Color.DARK_GRAY);
				paragraph.setFontColor(Color.WHITE);// Setting background color to cell1
				paragraph.setBorder(Border.NO_BORDER);            // Setting border to cell1
				paragraph.setTextAlignment(TextAlignment.CENTER); // Setting text alignment to cell1
				paragraph.setFontSize(16);

				float[] columnWidth = {200f,200f,200f,200f,200f};
				Table table = new Table(columnWidth);
				table.setTextAlignment(TextAlignment.CENTER);
				table.addCell(new Cell().add("\n Vagon turi"));
				table.addCell(new Cell().add("\n VCHD-3"));
				table.addCell(new Cell().add("\n VCHD-5"));
				table.addCell(new Cell().add("\n VCHD-6"));
				table.addCell(new Cell().add("\n Jami"));

				List<Integer> x= allVagons;

				table.addCell(new Cell().add("Yopiq vagon (????????)"));
				table.addCell(new Cell().add(String.valueOf(x.get(0))));
				table.addCell(new Cell().add(String.valueOf(x.get(1))));
				table.addCell(new Cell().add(String.valueOf(x.get(2))));
				table.addCell(new Cell().add(String.valueOf(x.get(3))));

				table.addCell(new Cell().add("Platforma(????)"));
				table.addCell(new Cell().add(String.valueOf(x.get(4))));
				table.addCell(new Cell().add(String.valueOf(x.get(5))));
				table.addCell(new Cell().add(String.valueOf(x.get(6))));
				table.addCell(new Cell().add(String.valueOf(x.get(7))));

				table.addCell(new Cell().add("Yarim ochiq vagon(????)"));
				table.addCell(new Cell().add(String.valueOf(x.get(8))));
				table.addCell(new Cell().add(String.valueOf(x.get(9))));
				table.addCell(new Cell().add(String.valueOf(x.get(10))));
				table.addCell(new Cell().add(String.valueOf(x.get(11))));

				table.addCell(new Cell().add("Sisterna(????)"));
				table.addCell(new Cell().add(String.valueOf(x.get(12))));
				table.addCell(new Cell().add(String.valueOf(x.get(13))));
				table.addCell(new Cell().add(String.valueOf(x.get(14))));
				table.addCell(new Cell().add(String.valueOf(x.get(15))));

				table.addCell(new Cell().add("Boshqa turdagi(????????)"));
				table.addCell(new Cell().add(String.valueOf(x.get(16))));
				table.addCell(new Cell().add(String.valueOf(x.get(17))));
				table.addCell(new Cell().add(String.valueOf(x.get(18))));
				table.addCell(new Cell().add(String.valueOf(x.get(19))));

				table.addCell(new Cell().add("Jami"));
				table.addCell(new Cell().add(String.valueOf(x.get(20))));
				table.addCell(new Cell().add(String.valueOf(x.get(21))));
				table.addCell(new Cell().add(String.valueOf(x.get(22))));
				table.addCell(new Cell().add(String.valueOf(x.get(23))));

				doc.add(paragraph);
				doc.add(table);
				doc.close();
				FileInputStream in = new FileInputStream(file.getAbsoluteFile());
				FileCopyUtils.copy(in, response.getOutputStream());

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
	}

	@Override
	public VagonModel findById(Long id) {
		Optional<VagonModel> optional = vagonRepository.findById(id);
		if (optional.isPresent())
			return optional.get();
		return null;
	}

	public String getCurrentDate() {
		return currentDate;
	}	

	public String getSamDate() {
		Optional<LastActionTimes> optionalQoldiqTime = utyTimeRepository.findById(1);
		if (!optionalQoldiqTime.isPresent())
			return currentDate;
		return optionalQoldiqTime.get().getSamQoldiqDate();
	}

	public String getHavDate() {
		Optional<LastActionTimes> optionalQoldiqTime = utyTimeRepository.findById(1);
		if (!optionalQoldiqTime.isPresent())
			return currentDate;
		return optionalQoldiqTime.get().getHavQoldiqDate();
	}

	public String getAndjDate() {
		Optional<LastActionTimes> optionalQoldiqTime = utyTimeRepository.findById(1);
		if (!optionalQoldiqTime.isPresent())
			return currentDate;
		return optionalQoldiqTime.get().getAndjQoldiqDate();
	}


	@Override
	public List<VagonModel> findAll() {
		Optional<LastActionTimes> byId = utyTimeRepository.findById(1);

		if (!byId.isPresent()) {
			LastActionTimes times = new LastActionTimes();

			times.setId(1);
			times.setSamQoldiqDate(currentDate);
			times.setHavQoldiqDate(currentDate);
			times.setAndjQoldiqDate(currentDate);

			times.setSamMalumotDate(currentDate);
			times.setHavMalumotDate(currentDate);
			times.setAndjMalumotDate(currentDate);

			times.setSamUtyDate(currentDate);
			times.setHavUtyDate(currentDate);
			times.setAndjUtyDate(currentDate);

			times.setSamBiznesDate(currentDate);
			times.setHavBiznesDate(currentDate);
			times.setAndjBiznesDate(currentDate);

			utyTimeRepository.save(times);
		}
		return vagonRepository.findAll();
	}
	
	// bosh admin qoshadi
	@Override
	public ApiResponse saveVagon(VagonDto vagon) {

		Optional<VagonModel> exist=	vagonRepository.findByNomer(vagon.getNomer());
		if(exist.isPresent())
			return new ApiResponse(exist.get().getNomer() + " nomerli vagon avval saqlangan", false);
		VagonModel savedVagon = new VagonModel();
		savedVagon.setNomer(vagon.getNomer());
		savedVagon.setDepoNomi(vagon.getDepoNomi());
		savedVagon.setRemontTuri(vagon.getRemontTuri());
		savedVagon.setVagonTuri(vagon.getVagonTuri());
		savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
		savedVagon.setKelganVaqti(vagon.getKelganVaqti());
		savedVagon.setIzoh(vagon.getIzoh());
		savedVagon.setCountry(vagon.getCountry());

		LocalDateTime today = LocalDateTime.now();
		LocalDateTime minusHours = today.plusHours(5);
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String currentDate  = minusHours.format(myFormatObj);

		savedVagon.setCreatedDate(currentDate);

		vagonRepository.save(savedVagon);
		return new ApiResponse(vagon.getNomer() + " nomerli vagon ma'lumotlari saqlandi", true);
	}
	@Override
	public ApiResponse saveVagonSam(VagonDto vagon) {
		Optional<VagonModel> exist=	vagonRepository.findByNomer(vagon.getNomer());
		if(exist.isPresent())
			return new ApiResponse(exist.get().getNomer() + " nomerli vagon avval saqlangan", false);
		if(!vagon.getDepoNomi().equalsIgnoreCase("VCHD-6"))
			return new ApiResponse("Siz faqat VCHD-6 ga ma'lumot qo'shishingiz mumkin", false);
		VagonModel savedVagon = new VagonModel();
		savedVagon.setNomer(vagon.getNomer());
		savedVagon.setDepoNomi(vagon.getDepoNomi());
		savedVagon.setRemontTuri(vagon.getRemontTuri());
		savedVagon.setVagonTuri(vagon.getVagonTuri());
		savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
		savedVagon.setIzoh(vagon.getIzoh());
		savedVagon.setCountry(vagon.getCountry());
		savedVagon.setKelganVaqti(vagon.getKelganVaqti());
		
		LocalDateTime today = LocalDateTime.now();
		LocalDateTime minusHours = today.plusHours(5);
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	    samDate = minusHours.format(myFormatObj);

		savedVagon.setCreatedDate(samDate);

		Optional<LastActionTimes> optionalQoldiqTime = utyTimeRepository.findById(1);
		optionalQoldiqTime.get().setSamQoldiqDate(samDate);
		utyTimeRepository.save(optionalQoldiqTime.get());

		vagonRepository.save(savedVagon);
		return new ApiResponse(vagon.getNomer() + " nomerli vagon ma'lumotlari saqlandi", true);
	}
	@Override
	public ApiResponse saveVagonHav(VagonDto vagon) {
		Optional<VagonModel> exist=	vagonRepository.findByNomer(vagon.getNomer());
		if(exist.isPresent())
			return new ApiResponse(exist.get().getNomer() + " nomerli vagon avval saqlangan", false);
		if(!vagon.getDepoNomi().equalsIgnoreCase("VCHD-3"))
			return new ApiResponse("Siz faqat VCHD-3 ga ma'lumot qo'shishingiz mumkin", false);
		VagonModel savedVagon = new VagonModel();
		savedVagon.setNomer(vagon.getNomer());
		savedVagon.setDepoNomi(vagon.getDepoNomi());
		savedVagon.setRemontTuri(vagon.getRemontTuri());
		savedVagon.setVagonTuri(vagon.getVagonTuri());
		savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
		savedVagon.setIzoh(vagon.getIzoh());
		savedVagon.setCountry(vagon.getCountry());
		savedVagon.setKelganVaqti(vagon.getKelganVaqti());
		
		LocalDateTime today = LocalDateTime.now();
		LocalDateTime minusHours = today.plusHours(5);
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		havDate = minusHours.format(myFormatObj);

		savedVagon.setCreatedDate(havDate);

		Optional<LastActionTimes> optionalQoldiqTime = utyTimeRepository.findById(1);
		optionalQoldiqTime.get().setHavQoldiqDate(havDate);
		utyTimeRepository.save(optionalQoldiqTime.get());

		vagonRepository.save(savedVagon);
		return new ApiResponse(vagon.getNomer() + " nomerli vagon ma'lumotlari saqlandi", true);
	}

	@Override
	public ApiResponse saveVagonAndj(VagonDto vagon) {
		Optional<VagonModel> exist=	vagonRepository.findByNomer(vagon.getNomer());
		if(exist.isPresent())
			return new ApiResponse(exist.get().getNomer() + " nomerli vagon avval saqlangan", false);
		if(!vagon.getDepoNomi().equalsIgnoreCase("VCHD-5"))
			return new ApiResponse("Siz faqat VCHD-5 ga ma'lumot qo'shishingiz mumkin", false);
		VagonModel savedVagon = new VagonModel();
		savedVagon.setNomer(vagon.getNomer());
		savedVagon.setDepoNomi(vagon.getDepoNomi());
		savedVagon.setRemontTuri(vagon.getRemontTuri());
		savedVagon.setVagonTuri(vagon.getVagonTuri());
		savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
		savedVagon.setIzoh(vagon.getIzoh());
		savedVagon.setCountry(vagon.getCountry());
		savedVagon.setCreatedDate(minusHours.format(myFormatObj));
		savedVagon.setKelganVaqti(vagon.getKelganVaqti());
		
		LocalDateTime today = LocalDateTime.now();
		LocalDateTime minusHours = today.plusHours(5);
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		andjDate = minusHours.format(myFormatObj);

		savedVagon.setCreatedDate(andjDate);

		Optional<LastActionTimes> optionalQoldiqTime = utyTimeRepository.findById(1);
		optionalQoldiqTime.get().setAndjQoldiqDate(andjDate);
		utyTimeRepository.save(optionalQoldiqTime.get());

		vagonRepository.save(savedVagon);
		return new ApiResponse(vagon.getNomer() + " nomerli vagon ma'lumotlari saqlandi", true);
	}

	
	@Override
	public ApiResponse updateVagon(VagonDto vagon, long id) {	
		 Optional<VagonModel> exist = vagonRepository.findById(id);
		 if(!exist.isPresent())
			 return new ApiResponse("Vagon topilmadi", false);
		 VagonModel savedVagon = exist.get();
		 savedVagon.setId(id);
		 savedVagon.setNomer(vagon.getNomer());
		 savedVagon.setVagonTuri(vagon.getVagonTuri());
		 savedVagon.setDepoNomi(vagon.getDepoNomi());
		 savedVagon.setRemontTuri(vagon.getRemontTuri());
		 savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
		 savedVagon.setKelganVaqti(vagon.getKelganVaqti());
		 savedVagon.setIzoh(vagon.getIzoh());
		 savedVagon.setCountry(vagon.getCountry());


		vagonRepository.save(savedVagon);
		return new ApiResponse(vagon.getNomer() + " nomerli vagon ma'lumotlari o'zgartirildi", true);
	}
	
	@Override
	public ApiResponse updateVagonSam(VagonDto vagon, long id) {

		 Optional<VagonModel> exist = vagonRepository.findById(id);
		 if(vagon.getDepoNomi().equalsIgnoreCase("VCHD-6")) {
			 VagonModel savedVagon = exist.get();
			 savedVagon.setId(id);
			 savedVagon.setNomer(vagon.getNomer());
			 savedVagon.setVagonTuri(vagon.getVagonTuri());
			 savedVagon.setDepoNomi(vagon.getDepoNomi());
			 savedVagon.setRemontTuri(vagon.getRemontTuri());
			 savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
			 savedVagon.setIzoh(vagon.getIzoh());
			 savedVagon.setCountry(vagon.getCountry());
			 savedVagon.setKelganVaqti(vagon.getKelganVaqti());
			 
			 LocalDateTime today = LocalDateTime.now();
			 LocalDateTime minusHours = today.plusHours(5);
			 DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			 samDate = minusHours.format(myFormatObj);

			 Optional<LastActionTimes> optionalQoldiqTime = utyTimeRepository.findById(1);
			 optionalQoldiqTime.get().setSamQoldiqDate(samDate);
			 utyTimeRepository.save(optionalQoldiqTime.get());

			 vagonRepository.save(savedVagon);
			 return new ApiResponse(vagon.getNomer() + " nomerli vagon ma'lumotlari o'zgartirildi", true);
		 }else
			 return new ApiResponse("Siz faqat VCHD-6 ga ma'lumot kiritishingiz mumkin", false);

	}

	@Override
	public ApiResponse updateVagonHav(VagonDto vagon, long id) {
		 Optional<VagonModel> exist = vagonRepository.findById(id);
		 if(vagon.getDepoNomi().equalsIgnoreCase("VCHD-3")) {
			 
			 VagonModel savedVagon = exist.get();
			 savedVagon.setId(id);
			 savedVagon.setNomer(vagon.getNomer());
			 savedVagon.setVagonTuri(vagon.getVagonTuri());
			 savedVagon.setDepoNomi(vagon.getDepoNomi());
			 savedVagon.setRemontTuri(vagon.getRemontTuri());
			 savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
			 savedVagon.setIzoh(vagon.getIzoh());
			 savedVagon.setCountry(vagon.getCountry());
			 savedVagon.setKelganVaqti(vagon.getKelganVaqti());
			 
			 LocalDateTime today = LocalDateTime.now();
			 LocalDateTime minusHours = today.plusHours(5);
			 DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			 havDate = minusHours.format(myFormatObj);

			 Optional<LastActionTimes> optionalQoldiqTime = utyTimeRepository.findById(1);
			 optionalQoldiqTime.get().setHavQoldiqDate(havDate);
			 utyTimeRepository.save(optionalQoldiqTime.get());

			 vagonRepository.save(savedVagon);
			 return new ApiResponse(vagon.getNomer() + " nomerli vagon ma'lumotlari o'zgartirildi", true);
		 }else
			 return new ApiResponse("Siz faqat VCHD-3 ga ma'lumot kiritishingiz mumkin", false);
	}

	@Override
	public ApiResponse updateVagonAndj(VagonDto vagon, long id) {

		 Optional<VagonModel> exist = vagonRepository.findById(id);
		 if(vagon.getDepoNomi().equalsIgnoreCase("VCHD-5")){
			 VagonModel savedVagon = exist.get();
			 savedVagon.setId(id);
			 savedVagon.setNomer(vagon.getNomer());
			 savedVagon.setVagonTuri(vagon.getVagonTuri());
			 savedVagon.setDepoNomi(vagon.getDepoNomi());
			 savedVagon.setRemontTuri(vagon.getRemontTuri());
			 savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
			 savedVagon.setIzoh(vagon.getIzoh());
			 savedVagon.setCountry(vagon.getCountry());
			 savedVagon.setKelganVaqti(vagon.getKelganVaqti());
			 
			 LocalDateTime today = LocalDateTime.now();
			 LocalDateTime minusHours = today.plusHours(5);
			 DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			 andjDate = minusHours.format(myFormatObj);

			 Optional<LastActionTimes> optionalQoldiqTime = utyTimeRepository.findById(1);
			 optionalQoldiqTime.get().setAndjQoldiqDate(andjDate);
			 utyTimeRepository.save(optionalQoldiqTime.get());

			 vagonRepository.save(savedVagon);
			 return new ApiResponse(vagon.getNomer() + " nomerli vagon ma'lumotlari o'zgartirildi", true);
		 }else
			 return new ApiResponse("Siz faqat VCHD-5 ga ma'lumot kiritishingiz mumkin", false);
	}


	@Override
	public VagonModel getVagonById(long id) {
	Optional<VagonModel> exist=	vagonRepository.findById(id);
		return exist.orElseGet(VagonModel::new);
	}

	@Override
	public ApiResponse deleteVagonById(long id) throws NotFoundException {
		Optional<VagonModel> optional = vagonRepository.findById(id);
		if (optional.isPresent()) {
			VagonModel vagonModel = optional.get();
			vagonRepository.deleteById(id);
			return new ApiResponse(vagonModel.getNomer() + " nomerli vagon o'chirildi", true);
		}else
			return new ApiResponse( " Ma'lumot topilmadi", false);
	}

	@Override
	public ApiResponse deleteVagonSam(long id) throws NotFoundException {
		vagonRepository.deleteById(id);
		LocalDateTime today = LocalDateTime.now();
		LocalDateTime minusHours = today.plusHours(5);
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		samDate = minusHours.format(myFormatObj);

		Optional<LastActionTimes> optionalQoldiqTime = utyTimeRepository.findById(1);
		optionalQoldiqTime.get().setSamQoldiqDate(samDate);
		utyTimeRepository.save(optionalQoldiqTime.get());
		return new ApiResponse(vagonRepository.findById(id).get().getNomer() + " nomerli vagon o'chirildi", true);
	}

	@Override
	public ApiResponse deleteVagonHav(long id) throws NotFoundException{
		vagonRepository.deleteById(id);
		LocalDateTime today = LocalDateTime.now();
		LocalDateTime minusHours = today.plusHours(5);
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		havDate = minusHours.format(myFormatObj);

		Optional<LastActionTimes> optionalQoldiqTime = utyTimeRepository.findById(1);
		optionalQoldiqTime.get().setHavQoldiqDate(havDate);
		utyTimeRepository.save(optionalQoldiqTime.get());
		return new ApiResponse(vagonRepository.findById(id).get().getNomer() + " nomerli vagon o'chirildi", true);
	}

	@Override
	public ApiResponse deleteVagonAndj(long id) throws NotFoundException{
		vagonRepository.deleteById(id);
		LocalDateTime today = LocalDateTime.now();
		LocalDateTime minusHours = today.plusHours(5);
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		andjDate = minusHours.format(myFormatObj);

		Optional<LastActionTimes> optionalQoldiqTime = utyTimeRepository.findById(1);
		optionalQoldiqTime.get().setAndjQoldiqDate(andjDate);
		utyTimeRepository.save(optionalQoldiqTime.get());
		return new ApiResponse(vagonRepository.findById(id).get().getNomer() + " nomerli vagon o'chirildi", true);
	}
//searchniki
	@Override
	public ApiResponse findByKeyword(Integer participant) {

		Optional<VagonModel> exist=vagonRepository.findByNomer(participant );
		if(exist.isPresent()) {
			VagonModel vagonModel = exist.get();
			return new ApiResponse(vagonModel, true);
		}
		return new ApiResponse(participant + " nomerli vagon topilmadi", false);
	}
// filterniki	
	@Override
	public List<VagonModel> findAllByDepoNomiVagonTuriAndCountry(String depoNomi, String vagonTuri, String country) {
		return vagonRepository.findAllByDepoNomiVagonTuriAndCountry(depoNomi, vagonTuri, country);
	}


	@Override
	public List<VagonModel> findAllByDepoNomiAndVagonTuri(String depoNomi, String vagonTuri) {
		return vagonRepository.findAllByDepoNomiAndVagonTuri(depoNomi, vagonTuri);
	}

	@Override
	public List<VagonModel> findAllByDepoNomiAndCountry(String depoNomi, String country) {
		return vagonRepository.findAllByDepoNomiAndCountry(depoNomi, country);
	}

	@Override
	public List<VagonModel> findAllByDepoNomi(String depoNomi) {
		return vagonRepository.findAllByDepoNomi(depoNomi);
	}

	@Override
	public List<VagonModel> findAllByVagonTuriAndCountry(String vagonTuri, String country) {
		return vagonRepository.findAllByVagonTuriAndCountry(vagonTuri, country);
	}

	@Override
	public List<VagonModel> findAllBycountry(String country) {
		return vagonRepository.findAllBycountry(country);
		
	}

	@Override
	public List<VagonModel> findAllByVagonTuri(String vagonTuri) {
		return vagonRepository.findAllByVagonTuri(vagonTuri);
		
	}


	@Override
	public Integer getCount(String string) {	
		return vagonRepository.getCount(string);
	}
	
	@Override
	public Integer getVagonsCount(String kriti, String depoNomi) {	
		return vagonRepository.getVagonsCount(kriti, depoNomi);
	}

	@Override
	public Integer getCount(String string, String country) {
		return vagonRepository.getCount(string, country);
	}

	@Override
	public Integer getVagonsCount(String kriti, String depoNomi, String country) {
		return vagonRepository.getVagonsCount(kriti, depoNomi, country);
	}
	
}
