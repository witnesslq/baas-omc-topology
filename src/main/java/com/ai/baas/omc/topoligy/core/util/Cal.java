package com.ai.baas.omc.topoligy.core.util;

import com.ai.baas.omc.topoligy.core.constant.FeeSource;

import java.math.BigDecimal;
import java.math.RoundingMode;


public final class Cal {
	private Cal(){
	}
	public static final BigDecimal bigDecimalFromDouble(Double value,String resourcetype){
		BigDecimal bigDecimal = BigDecimal.valueOf(value);

		if (resourcetype.equals(FeeSource.FROM_CHARGE)){
			BigDecimal divisor = new BigDecimal(1000000);
			return bigDecimal.divide(divisor,2, RoundingMode.HALF_UP);
		} else if(resourcetype.equals(FeeSource.FROM_BALANCE)){
			BigDecimal divisor = new BigDecimal(1000);
			return bigDecimal.divide(divisor,2, RoundingMode.HALF_UP);
		}else if(resourcetype.equals(FeeSource.FROM_CREDIT)){
			BigDecimal divisor = new BigDecimal(100);
			return bigDecimal.divide(divisor,2, RoundingMode.HALF_UP);
		}else{
			return bigDecimal;
		}
	}
	
	public static final BigDecimal bigDecimalFromDoubleStr(String value,String resourcetype){
		Double doublevalue = Double.parseDouble(value);
		return bigDecimalFromDouble(doublevalue,resourcetype);
	}
	
	public static final BigDecimal bigDecimalFromLong(Long value,String resourcetype){
		Double doublevalue = Double.parseDouble(Long.toString(value));
		return bigDecimalFromDouble(doublevalue,resourcetype);
	}
}
