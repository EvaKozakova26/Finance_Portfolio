package com.mystocks.model;

import com.opencsv.bean.CsvBindByPosition;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Kurzovni listek
 */
public class ExchangeRateRaw {

	/**
	 * ISIN
	 */
	@CsvBindByPosition(position = 0)
	private String code;

	@CsvBindByPosition(position = 1)
	private String name;

	@CsvBindByPosition(position = 2)
	private String bic;

	@CsvBindByPosition(position = 3)
	private String date;

	@CsvBindByPosition(position = 4)
	private String refPrice;

	@CsvBindByPosition(position = 5)
	private String endRate;

	@CsvBindByPosition(position = 6)
	private String dayMinPrice;

	@CsvBindByPosition(position = 7)
	private String dayMaxPrice;

	// TODO: 01.11.2020 prelozit pocet zobchodovanych c.p.
	/**
	 * pocet zobchodovannych cennych papiruplu
	 */
	@CsvBindByPosition(position = 8)
	private String dayCount;

	@CsvBindByPosition(position = 9)
	private String objemObchodu;

	@CsvBindByPosition(position = 10)
	private String pocetObchodu;

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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getRefPrice() {
		return refPrice;
	}

	public void setRefPrice(String refPrice) {
		this.refPrice = refPrice;
	}

	public String getEndRate() {
		return endRate;
	}

	public void setEndRate(String endRate) {
		this.endRate = endRate;
	}

	public String getDayMinPrice() {
		return dayMinPrice;
	}

	public void setDayMinPrice(String dayMinPrice) {
		this.dayMinPrice = dayMinPrice;
	}

	public String getDayMaxPrice() {
		return dayMaxPrice;
	}

	public void setDayMaxPrice(String dayMaxPrice) {
		this.dayMaxPrice = dayMaxPrice;
	}

	public String getDayCount() {
		return dayCount;
	}

	public void setDayCount(String dayCount) {
		this.dayCount = dayCount;
	}

	public String getObjemObchodu() {
		return objemObchodu;
	}

	public void setObjemObchodu(String objemObchodu) {
		this.objemObchodu = objemObchodu;
	}

	public String getPocetObchodu() {
		return pocetObchodu;
	}

	public void setPocetObchodu(String pocetObchodu) {
		this.pocetObchodu = pocetObchodu;
	}
}
