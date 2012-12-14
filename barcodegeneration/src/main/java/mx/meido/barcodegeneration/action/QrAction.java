package mx.meido.barcodegeneration.action;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import mx.meido.barcodegeneration.util.MatrixToImageWriter;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

public class QrAction implements ActionBean {

	private ActionBeanContext context;
	public ActionBeanContext getContext() {
		return context;
	}
	public void setContext(ActionBeanContext context) {
		this.context = context;
	}
	
	private String contents;
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	
	private int width;
	private int height;
	public int getHeight() {
		return height;
	}
	public int getWidth() {
		return width;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	
	private boolean dirPic;
	public void setDirPic(boolean dirPic) {
		this.dirPic = dirPic;
	}
	public boolean isDirPic() {
		return dirPic;
	}
	
	private int barcodeFormat;
	public int getBarcodeFormat() {
		return barcodeFormat;
	}
	public void setBarcodeFormat(int barcodeFormat) {
		this.barcodeFormat = barcodeFormat;
	}

	@DefaultHandler
	public Resolution generate(){
		try {
			if(contents != null){
				contents = new String(contents.getBytes("ISO-8859-1"), "UTF-8");
			}
			if(contents == null || contents.length() == 0){
				contents = "请输入内容";
			}
			
			System.out.println(contents);
			System.out.println(width);
			System.out.println(height);
			System.out.println(dirPic);
			
			if(width < 1 || width > 1000)
				width = 200;
			if(height < 1 || height > 1000)
				height = 200;
			
			BarcodeFormat tbarcodeFormat = BarcodeFormat.QR_CODE;
			switch(barcodeFormat){
			case 1:
			default:
				break;
			case 2:tbarcodeFormat = BarcodeFormat.RSS_14;break;
			case 3:tbarcodeFormat = BarcodeFormat.AZTEC;break;
			case 4:tbarcodeFormat = BarcodeFormat.CODABAR;break;
			case 5:tbarcodeFormat = BarcodeFormat.CODE_128;break;
			case 6:tbarcodeFormat = BarcodeFormat.CODE_39;break;
			case 7:tbarcodeFormat = BarcodeFormat.CODE_93;break;
			case 8:tbarcodeFormat = BarcodeFormat.DATA_MATRIX;break;
			case 9:tbarcodeFormat = BarcodeFormat.EAN_13;break;
			case 10:tbarcodeFormat = BarcodeFormat.EAN_8;break;
			case 11:tbarcodeFormat = BarcodeFormat.ITF;break;
			case 12:tbarcodeFormat = BarcodeFormat.MAXICODE;break;
			case 13:tbarcodeFormat = BarcodeFormat.PDF_417;break;
			case 14:tbarcodeFormat = BarcodeFormat.RSS_EXPANDED;break;
			case 15:tbarcodeFormat = BarcodeFormat.UPC_A;break;
			case 16:tbarcodeFormat = BarcodeFormat.UPC_E;break;
			case 17:tbarcodeFormat = BarcodeFormat.UPC_EAN_EXTENSION;break;
			}

			//加入hints,指定编码,防止乱码
			BitMatrix bitMatrix = mfw.encode(contents, tbarcodeFormat, width, height, hints);
			InputStream is = MatrixToImageWriter.getImageBytesInputStream(bitMatrix, "png");
			
			return new StreamingResolution("image/png", is);
		} catch (Exception e) {
			e.printStackTrace();
			return new ForwardResolution("");
		}
	}
	
	private static MultiFormatWriter mfw;
	private static Map<EncodeHintType,Object> hints;
	static{
    	mfw = new MultiFormatWriter();
    	//防止乱码
    	hints = new HashMap<EncodeHintType,Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF8");
	}
}
