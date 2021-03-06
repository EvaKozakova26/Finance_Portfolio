package com.mystocks.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Kurzovni listek
 */
public class ExchangeRate {

	/**
	 * ISIN
	 */
	private String code;
	private String name;
	private String bic;
	private Date date;
	private BigDecimal refPrice;
	private BigDecimal endRate;
	private BigDecimal dayMaxPrice;
	private BigDecimal dayMinPrice;
	// TODO: 01.11.2020 prelozit pocet zobchodovanych c.p.
	private int dayCount;
	private BigDecimal objemObchodu;
	private int pocetObchodu;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBic() {
		return bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public BigDecimal getRefPrice() {
		return refPrice;
	}

	public void setRefPrice(BigDecimal refPrice) {
		this.refPrice = refPrice;
	}

	public BigDecimal getEndRate() {
		return endRate;
	}

	public void setEndRate(BigDecimal endRate) {
		this.endRate = endRate;
	}

	public BigDecimal getDayMaxPrice() {
		return dayMaxPrice;
	}

	public void setDayMaxPrice(BigDecimal dayMaxPrice) {
		this.dayMaxPrice = dayMaxPrice;
	}

	public BigDecimal getDayMinPrice() {
		return dayMinPrice;
	}

	public void setDayMinPrice(BigDecimal dayMinPrice) {
		this.dayMinPrice = dayMinPrice;
	}

	public int getDayCount() {
		return dayCount;
	}

	public void setDayCount(int dayCount) {
		this.dayCount = dayCount;
	}

	public BigDecimal getObjemObchodu() {
		return objemObchodu;
	}

	public void setObjemObchodu(BigDecimal objemObchodu) {
		this.objemObchodu = objemObchodu;
	}

	public int getPocetObchodu() {
		return pocetObchodu;
	}

	public void setPocetObchodu(int pocetObchodu) {
		this.pocetObchodu = pocetObchodu;
	}
}
