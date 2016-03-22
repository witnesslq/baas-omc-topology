package com.ai.baas.omc.topoligy.core.business.base;

import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.manager.container.ConfigContainer;
import com.ai.baas.omc.topoligy.core.pojo.OmcObj;
import com.google.gson.JsonObject;

public abstract class BaseProcess {

	private  ConfigContainer config;
	private OmcObj omcobj;
	private JsonObject input;
	private JsonObject output;


	public BaseProcess(ConfigContainer cfg, OmcObj obj, JsonObject data) throws OmcException {
		this.config = cfg;
		this.omcobj = obj;
		this.input = data;
	}

	public abstract void prepare(JsonObject data) throws OmcException;
	public abstract void prepare(String cfg) throws OmcException;
	
	public abstract void process() throws OmcException;

	public JsonObject getInput() {
		return input;
	}

	public void setInput(JsonObject input) {
		this.input = input;
	}

	public JsonObject getOutput() {
		return output;
	}

	public void setOutput(JsonObject output) {
		this.output = output;
	}

	public ConfigContainer getConfig() {
		return config;
	}

	public void setConfig(ConfigContainer config) {
		this.config = config;
	}

	public OmcObj getOmcobj() {
		return omcobj;
	}

	public void setOmcobj(OmcObj omcobj) {
		this.omcobj = omcobj;
	}

}
