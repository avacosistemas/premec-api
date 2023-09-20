package ar.com.avaco.ws.rest.reporte;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFEventHelper extends PdfPageEventHelper {

	@Override
	public void onStartPage(PdfWriter writer, Document document) {
		try {
			String primerpagina = "primer-pagina.jpg";
			String segundapagina = "segunda-pagina.jpg";

			ClassLoader classLoader = getClass().getClassLoader();

			URL resourcePP = classLoader.getResource(primerpagina);
			URL resourceSP = classLoader.getResource(segundapagina);

			File filePP = new File(resourcePP.toURI());
			File fileSP = new File(resourceSP.toURI());

			Image background = null;
			if (document.getPageNumber() == 1) {
				background = Image.getInstance(filePP.getAbsolutePath());
			} else {
				background = Image.getInstance(fileSP.getAbsolutePath());
			}

			// This scales the image to the page,
			// use the image's width & height if you don't want to scale.
			float width = document.getPageSize().getWidth();
			float height = background.getHeight() * document.getPageSize().getWidth() / background.getWidth();
		
			writer.getDirectContentUnder().addImage(background, width, 0, 0, height, 0, 0);
		
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
