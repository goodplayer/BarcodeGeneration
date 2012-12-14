package mx.meido.barcodegeneration.servlet;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.meido.barcodegeneration.util.MatrixToImageWriter;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * Servlet implementation class QRequest
 */
public final class QRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private MultiFormatWriter mfw;
	Map<EncodeHintType,Object> hints;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QRequest() {
        super();
    }
    
    @Override
    public void init(ServletConfig config) throws ServletException {
    	super.init(config);

    	mfw = new MultiFormatWriter();
    	//防止乱码
    	hints = new HashMap<EncodeHintType,Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF8");
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
    		throws ServletException, IOException {
    	super.doPost(req, resp);
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String contents = request.getParameter("contents");
		if(contents != null){
			contents = new String(contents.getBytes("ISO-8859-1"), "UTF-8");
		}
		if(contents == null || contents.length() == 0){
			contents = "请输入内容";
		}
		
		int width = 200;
		int height = 200;
		try{
			width = Integer.valueOf(request.getParameter("width"));
			height = Integer.valueOf(request.getParameter("height"));
			if(width < 1 || width >1000)
				width = 200;
			if(height < 1 || height > 1000)
				width = 200;
		}catch (Exception e) {
		}

		boolean directPicture = false;
		try{
			directPicture = Boolean.valueOf(request.getParameter("dirPic"));
		}catch (Exception e) {
		}
		
		int chooseBarcodeFormat = 1;
		try{
			chooseBarcodeFormat = Integer.valueOf(request.getParameter("barcodeFormat"));
		}catch (Exception e) {
		}
		BarcodeFormat barcodeFormat = BarcodeFormat.QR_CODE;
		switch(chooseBarcodeFormat){
		case 1:
		default:
			break;
		case 2:barcodeFormat = BarcodeFormat.RSS_14;break;
		case 3:barcodeFormat = BarcodeFormat.AZTEC;break;
		case 4:barcodeFormat = BarcodeFormat.CODABAR;break;
		case 5:barcodeFormat = BarcodeFormat.CODE_128;break;
		case 6:barcodeFormat = BarcodeFormat.CODE_39;break;
		case 7:barcodeFormat = BarcodeFormat.CODE_93;break;
		case 8:barcodeFormat = BarcodeFormat.DATA_MATRIX;break;
		case 9:barcodeFormat = BarcodeFormat.EAN_13;break;
		case 10:barcodeFormat = BarcodeFormat.EAN_8;break;
		case 11:barcodeFormat = BarcodeFormat.ITF;break;
		case 12:barcodeFormat = BarcodeFormat.MAXICODE;break;
		case 13:barcodeFormat = BarcodeFormat.PDF_417;break;
		case 14:barcodeFormat = BarcodeFormat.RSS_EXPANDED;break;
		case 15:barcodeFormat = BarcodeFormat.UPC_A;break;
		case 16:barcodeFormat = BarcodeFormat.UPC_E;break;
		case 17:barcodeFormat = BarcodeFormat.UPC_EAN_EXTENSION;break;
		}

		
		try {
			//加入hints,指定编码,防止乱码
			BitMatrix bitMatrix = mfw.encode(contents, barcodeFormat, width, height, hints);
			response.setContentType("image/png");
			MatrixToImageWriter.writeToStream(bitMatrix, "png", response.getOutputStream());
		} catch (WriterException e) {
		}
	}

}
