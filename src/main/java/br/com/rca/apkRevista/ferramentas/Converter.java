package br.com.rca.apkRevista.ferramentas;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.ghost4j.document.DocumentException;
import org.ghost4j.document.PDFDocument;
import org.ghost4j.renderer.RendererException;
import org.ghost4j.renderer.SimpleRenderer;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class Converter {
	public static JSONObject stringToJSONObject(String json){
		JSONObject retorno = new JSONObject();
		json = json.substring(2,json.length()-2);
		String[] colunas = json.split("\",\"");
		for (String coluna : colunas) {
			String key   = coluna.substring(               0, coluna.indexOf("\":"));
			String value = coluna.substring(key.length() + 3,coluna.length());
			try {
				retorno.put(key, value);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return retorno;
	}
	public static List<Image> filePdfToImagens(File file, int resolution, String formato, boolean saveToFile) {
		PDFDocument pdf         = new PDFDocument();
		SimpleRenderer renderer = new SimpleRenderer();
		try {
			pdf.load(file);
			renderer.setResolution(resolution);
			List<Image> retorno = renderer.render(pdf);
			if(saveToFile){
				for (int i = 0; i < retorno.size(); i++) {
					String caminhoDaGravacao = file.getPath().replaceAll(".pdf", "") + i + "." + formato; 
					File outputfile          = new File(caminhoDaGravacao);
					ImageIO.write((RenderedImage) retorno.get(i), formato, outputfile);			
				}
			}
			return retorno;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (RendererException e) {
			e.printStackTrace();
			return null;
		} catch (DocumentException e) {
			e.printStackTrace();
			return null;
		}
	}
	private static BufferedImage imageToBufferedImage(Image img) {
	    if (img instanceof BufferedImage)
	        return (BufferedImage) img;
	    
	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}	

	public static byte[] imageToByte (Image imagem) {
		 BufferedImage bufferedImage = imageToBufferedImage(imagem);
		 WritableRaster raster = bufferedImage .getRaster();
		 DataBufferByte data   = (DataBufferByte) raster.getDataBuffer();
		 return (data.getData());
	}
	public static byte[] fileToByte(File arquivo) {
		try {
			return FileUtils.readFileToByteArray(arquivo);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	/*public static Image byteToImage(File imagem01) {
		try {
			return ImageIO.read(imagem01);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}*/
	public static Image byteToImage(byte[] imagem01) {
        BufferedImage bufferedImage=null;
        try {
            InputStream inputStream = new ByteArrayInputStream(imagem01);
            bufferedImage = ImageIO.read(inputStream);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return bufferedImage;
	}
}
