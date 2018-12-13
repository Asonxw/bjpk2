package com.as.boot;
/**
* @ClassName: ModOrder_DWD
* @Description:定位胆 
* @author Ason
* @date 2018年12月13日 下午3:15:13
 */
public class ModOrder_DWD {

	private String method;
	private String code;
	private String nums;
	private String piece;
	private String price;
	private String odds;
	private String point;
	private String amount;
	
	public ModOrder_DWD(String method, String code, String piece, String price, String odds,
			String point, String amount){
		this.method = method;
		this.piece = piece;
		price = price.startsWith(".")?("0"+price):price;
		this.price = price;
		this.odds = odds;
		this.point = point;
		amount = amount.startsWith(".")?("0"+amount):amount;
		this.amount = amount; 
		this.code = serializeCode(code);
	}
	
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = serializeCode(code);
	}
	public String getNums() {
		return nums;
	}
	public void setNums(String nums) {
		this.nums = nums;
	}
	public String getPiece() {
		return piece;
	}
	public void setPiece(String piece) {
		this.piece = piece;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getOdds() {
		return odds;
	}
	public void setOdds(String odds) {
		this.odds = odds;
	}
	public String getPoint() {
		return point;
	}
	public void setPoint(String point) {
		this.point = point;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	/**
	 * @Title: serializePosition  
	 * @Description: 格式化position
	 * @author: Ason
	 * @param position
	 * @return      
	 * @return: String      
	 * @throws
	 */
	public String serializePosition(String position){
		String aimPistion = "";
		for (int i = 0; i < position.length(); i++) 
			aimPistion += Integer.parseInt(position.charAt(i)+"")+1+",";
		return aimPistion.substring(0,aimPistion.length()-1);
	}
	
	/**
	 * @Title: serializePosition  
	 * @Description: 格式化position
	 * @author: Ason
	 * @param position
	 * @return      
	 * @return: String      
	 * @throws
	 */
	public String serializeCode(String code){
		String aimCode = "";
		code = code.replace("[", "").replace("]", "");
		String[] codeArr = code.split(",");
		//计算注数
		this.nums = codeArr.length+"";
		for (int i = 0; i < codeArr.length; i++) {
			for (int j = 0; j < codeArr[i].length(); j++) {
				aimCode += codeArr[i].charAt(j)+",";
			}
			aimCode = aimCode.substring(0,aimCode.length()-1)+"|";
		}
		return aimCode.substring(0,aimCode.length()-1).replace("| ,", "|");
	}
}
