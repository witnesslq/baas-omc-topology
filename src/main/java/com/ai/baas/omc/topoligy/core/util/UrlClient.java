package com.ai.baas.omc.topoligy.core.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import com.ai.baas.omc.topoligy.core.constant.OWNERTYPE;
import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.pojo.OmcObj;
import com.ai.baas.omc.topoligy.core.pojo.RealTimeBalance;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public final class UrlClient {
	private static final Logger logger = LoggerFactory.getLogger(UrlClient.class);
	private static final String INF_IN_TENANTID = "tenantid";
	private static final String INF_IN_OWERTYPE = "owertype";
	private static final String INF_IN_OWERID = "owerid";
	private static final String INF_IN_BUSINESSCODE = "businesscode";
	private static final String INF_IN_PRODUCTTYPE = "producttype";
	
    private static final String INF_OUT_OWNER = "owner";
    private static final String INF_OUT_TENANTID = "tenantid";
	private static final String INF_OUT_OWERTYPE = "owertype";
	private static final String INF_OUT_OWERID = "owerid";
	private static final String INF_OUT_BUSINESSCODE = "businesscode";
	
	private static final String INF_OUT_RETURNCODE = "returncode";
	private static final String INF_OUT_REALBALANCE = "realbalance";
	private static final String INF_OUT_REALBILL = "realbill";
	private static final String INF_OUT_BALANCE = "balance";
	private static final String INF_OUT_UNSETTLEBILL = "unsettlebill";
	private static final String INF_OUT_UNINTOBILL = "unIntoBill";
	private static final String INF_OUT_FSTUNSETTLEMON = "fstunsettlemon";
	//private static final String INF_OUT_UNSETTLEMONS = "unsettlemons";
	private static final String INF_OUT_CREDITLINE = "creditline";
	private static final String INF_OUT_ACCTMONTH = "acctmonth";
	private static final String INF_OUT_EXPANDINFO = "expandinfo";
	private static final String CFG_RETURNCODE = "MMP-000000";

	public static final String URL_CLIENT = "omc.bwo.url";
	
	private static String url;

	private static UrlClient urlClient;
	private static  URL client;
	
	private UrlClient(){

	}
	
	public static UrlClient getInstance(){
		if (urlClient==null){
			urlClient = new UrlClient();
		}
		return urlClient;
	}
	
	public static void loadResource(Map<String,String> config) throws OmcException {
		if (urlClient==null){
			urlClient = new UrlClient();
		}
		
		try {
			url = config.get(UrlClient.URL_CLIENT);
			client = new URL(url);
		} catch (Exception e) {
			urlClient = null;
			throw new OmcException("loadResource", e);
		}
	}
	
	public RealTimeBalance doQuery(String producttype, OmcObj omcobj) throws OmcException{
		String output;
		JsonObject jsonObject = new JsonObject();
		String ownertype;
		if (OWNERTYPE.ACCT.equals(omcobj.getOwertype())){
			ownertype = "1";
		}else{
			ownertype = "0";
		}
		jsonObject.addProperty(UrlClient.INF_IN_PRODUCTTYPE, producttype);
		jsonObject.addProperty(UrlClient.INF_IN_TENANTID, omcobj.getTenantid());
		jsonObject.addProperty(UrlClient.INF_IN_OWERTYPE,ownertype);
		jsonObject.addProperty(UrlClient.INF_IN_OWERID, omcobj.getOwerid());
		jsonObject.addProperty(UrlClient.INF_IN_BUSINESSCODE, StringUtils.lowerCase(omcobj.getBusinesscode()));
//		0 － 用户 
//		1 － 账户 
		String input = jsonObject.toString();
		
		RealTimeBalance realtimebalance ;

        HttpURLConnection httpConnection = null;
        
		try {
			httpConnection = (HttpURLConnection) client.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type", "application/json");
			OutputStream outputStream = httpConnection.getOutputStream();
			outputStream.write(input.getBytes());
			outputStream.flush();
			
			if (httpConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				logger.error(""+ 	httpConnection.getResponseCode()); 
				logger.error("*****"+ 	 URLEncoder.encode(httpConnection.getResponseMessage(), "utf-8"));
				logger.error("*****"+ 	 httpConnection.toString());
				
			
			       throw new OmcException("Failed : HTTP error code:"
			                + httpConnection.getResponseCode(), httpConnection.getResponseMessage());
			       
			}
			
			BufferedReader responseBuffer = new BufferedReader(new InputStreamReader(
			                 (httpConnection.getInputStream())));

           while ((output = responseBuffer.readLine()) != null) {
        	   break;
           }
           realtimebalance = getBalance(output);
           
		} catch (Exception e1) {
			  throw new OmcException("UrlClient.doQuery 查询异常",e1);
		}finally {
			if (httpConnection != null){
		       httpConnection.disconnect();
			}
		}
		return realtimebalance;
	}
	
	private RealTimeBalance getBalance(String input) throws OmcException{
		
		RealTimeBalance realTimeBalance = new RealTimeBalance();
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(input, JsonObject.class);
		String returncode = jsonObject.get(UrlClient.INF_OUT_RETURNCODE).getAsString();
		if (!CFG_RETURNCODE.equals(returncode)){
			throw new OmcException("getBalance", "调用模拟销账异常,"+UrlClient.CFG_RETURNCODE+"["+returncode+"]");
		}
		
		try{
			String realbalance = jsonObject.get(UrlClient.INF_OUT_REALBALANCE).getAsString();
			String realbill = jsonObject.get(UrlClient.INF_OUT_REALBILL).getAsString();
			String balance = jsonObject.get(UrlClient.INF_OUT_BALANCE).getAsString();
			String unsettlebill = jsonObject.get(UrlClient.INF_OUT_UNSETTLEBILL).getAsString();
			String unIntoBill = jsonObject.get(UrlClient.INF_OUT_UNINTOBILL).getAsString();
			String fstunsettlemon = jsonObject.get(UrlClient.INF_OUT_FSTUNSETTLEMON).getAsString();
			//String unsettlemons = jsonObject.get(UrlClient.INF_OUT_UNSETTLEMONS).getAsString();
			String creditline = jsonObject.get(UrlClient.INF_OUT_CREDITLINE).getAsString();
			String acctmonth = jsonObject.get(UrlClient.INF_OUT_ACCTMONTH).getAsString();
			String expandinfo = jsonObject.get(UrlClient.INF_OUT_EXPANDINFO).toString();
			String owner = jsonObject.get(UrlClient.INF_OUT_OWNER).toString();
			
			Gson gsonower = new Gson();
			JsonObject jsonObjectowner = gsonower.fromJson(owner, JsonObject.class);
			
			String tenantid = jsonObjectowner.get(UrlClient.INF_OUT_TENANTID).getAsString();
			String owertype = jsonObjectowner.get(UrlClient.INF_OUT_OWERTYPE).getAsString();
			String owerid = jsonObjectowner.get(UrlClient.INF_OUT_OWERID).getAsString();
			String businesscode = jsonObjectowner.get(UrlClient.INF_OUT_BUSINESSCODE).getAsString();
	
			String newownertype;
			if ("1".equals(owertype)){
				newownertype = OWNERTYPE.ACCT;
			}else{
				newownertype = OWNERTYPE.SERV;
			}
			realTimeBalance.setOwner(new OmcObj(tenantid,newownertype,owerid,StringUtils.upperCase(businesscode)));
			
			realTimeBalance.setBalance(new BigDecimal(balance));
			realTimeBalance.setCreditline(new BigDecimal(creditline));
			realTimeBalance.setRealBalance(new BigDecimal(realbalance));
			realTimeBalance.setRealBill(new BigDecimal(realbill));
			realTimeBalance.setUnIntoBill(new BigDecimal(unIntoBill));
			realTimeBalance.setUnSettleBill(new BigDecimal(unsettlebill));
			realTimeBalance.setFstUnSettleMon(fstunsettlemon);
			realTimeBalance.setAcctMonth(acctmonth);
			realTimeBalance.setUnsettleMons(DateUtils.monthDiffs(fstunsettlemon,acctmonth));
			realTimeBalance.setExtInfo(expandinfo);
		}catch(Exception e){
			throw new OmcException("getBalance", "调用模拟销账返回值异常",e);
		}
		return realTimeBalance;
	}
}
