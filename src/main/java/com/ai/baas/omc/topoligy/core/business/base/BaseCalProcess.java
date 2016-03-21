package com.ai.baas.omc.topoligy.core.business.base;

import com.ai.baas.omc.topoligy.core.business.InfomationProcessor;
import com.ai.baas.omc.topoligy.core.exception.OmcException;
import com.ai.baas.omc.topoligy.core.manager.container.ConfigContainer;
import com.google.gson.JsonObject;

public abstract class BaseCalProcess {

	private ConfigContainer config;
	private InfomationProcessor information ;
	private JsonObject input;
	private JsonObject output;

	public  BaseCalProcess(ConfigContainer cfg,InfomationProcessor info,JsonObject data){
		this.setConfig(cfg);
		this.setInformation(info);
		this.setInput(data);
	}
	


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

	public void setConfig(ConfigContainer config) {
		this.config = config;
	}

	public void setInformation(InfomationProcessor information) {
		this.information = information;
	}
	
	public ConfigContainer getConfig() {
		return config;
	}

	public InfomationProcessor getInformation() {
		return information;
	}
	
	public abstract void prepare(JsonObject data) throws OmcException;
	public abstract void prepare(String cfg) throws OmcException;
}
