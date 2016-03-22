package com.ai.baas.omc.topoligy.core.pojo;

import java.io.Serializable;

import com.ai.baas.omc.topoligy.core.constant.OmcCalKey;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public final class OmcObj implements Serializable {

	private static final long serialVersionUID = -8316527956692995970L;
	
	private String tenantid;
	private String owertype;
	private String owerid;
	private String businesscode;
	
	public OmcObj(String tenantId,String owerType,String owerId,String businessCode){
		this.tenantid = tenantId;
		this.owertype = owerType;
		this.owerid = owerId;
		this.businesscode = businessCode;
	}
	
	public OmcObj(JsonObject jObject){
		this.tenantid = jObject.get(OmcCalKey.OMC_TENANT_ID).getAsString();
		this.owertype = jObject.get(OmcCalKey.OMC_OWNER_TYPE).getAsString();
		this.owerid = jObject.get(OmcCalKey.OMC_OWNER_ID).getAsString();
		this.businesscode = jObject.get(OmcCalKey.OMC_BUSINESS_CODE).getAsString();
	}
	public OmcObj(String json){
		Gson gson = new Gson();
		JsonObject jObject = gson.fromJson(json, JsonObject.class);

		this.tenantid = jObject.get(OmcCalKey.OMC_TENANT_ID).getAsString();
		this.owertype = jObject.get(OmcCalKey.OMC_OWNER_TYPE).getAsString();
		this.owerid = jObject.get(OmcCalKey.OMC_OWNER_ID).getAsString();
		this.businesscode = jObject.get(OmcCalKey.OMC_BUSINESS_CODE).getAsString();
	}
	
	public String getTenantid() {
		return tenantid;
	}
	public String getOwertype() {
		return owertype;
	}
	public String getOwerid() {
		return owerid;
	}
	public String getBusinesscode() {
		return businesscode;
	}
	@Override
	public String toString() {
		return toJson().toString();
	}
	public JsonObject toJson() {
		JsonObject jObject = new JsonObject();
		jObject.addProperty(OmcCalKey.OMC_TENANT_ID, tenantid);
		jObject.addProperty(OmcCalKey.OMC_OWNER_TYPE, owertype);
		jObject.addProperty(OmcCalKey.OMC_OWNER_ID, owerid);
		jObject.addProperty(OmcCalKey.OMC_BUSINESS_CODE, businesscode);
		return jObject;
	}
	
}
