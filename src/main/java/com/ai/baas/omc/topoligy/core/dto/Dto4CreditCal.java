package com.ai.baas.omc.topoligy.core.dto;

import com.ai.baas.omc.topoligy.core.pojo.OmcObj;

import java.io.Serializable;

/**
* @ClassName: Dto4CreditCal 
* @Description: 信控计算所需要参数 
* @author lvsj
* @date 2015年10月31日 下午3:07:40 
*
 */
public class Dto4CreditCal implements Serializable {
	private static final long serialVersionUID = 1L;
	private OmcObj owner;
	private String eventid;  //事件ID
	private String eventtype; //事件类型
	private String amount;     //本次发生额
	private String Amount_mark; //
	private String Amount_type; //单位
	private String expanded_info;//扩展信息
	
	public OmcObj getOwner() {
		return owner;
	}
	public void setOwner(OmcObj owner) {
		this.owner = owner;
	}
	public String getEventid() {
		return eventid;
	}
	public void setEventid(String eventid) {
		this.eventid = eventid;
	}
	public String getEventtype() {
		return eventtype;
	}
	public void setEventtype(String eventtype) {
		this.eventtype = eventtype;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getAmount_mark() {
		return Amount_mark;
	}
	public void setAmount_mark(String amount_mark) {
		Amount_mark = amount_mark;
	}
	public String getAmount_type() {
		return Amount_type;
	}
	public void setAmount_type(String amount_type) {
		Amount_type = amount_type;
	}
	public String getExpanded_info() {
		return expanded_info;
	}
	public void setExpanded_info(String expanded_info) {
		this.expanded_info = expanded_info;
	}

	@Override
	public String toString() {
		return "Dto4CreditCal [eventid=" + eventid + ", eventtype=" + eventtype + ", amount=" + amount
				+ ", Amount_mark=" + Amount_mark + ", Amount_type=" + Amount_type + ", expanded_info=" + expanded_info
				+ "]";
	}
	

	
	

}
