package com.saiily.excle.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.saiily.excle.annotation.ExcelColumn;
import com.saiily.excle.annotation.ExcelRow;

@ExcelRow(rowAcross = 2)
public class UserVo {

	@ExcelColumn(colIndex = 0, isAcross = true, format = "0")
	private int id;
	@ExcelColumn(colIndex = 1, isAcross = true, headName = "名字")
	private String name;

	@ExcelColumn(colIndex = 2, isAcross = true, isUnique = true, headName = "身份证号")
	private String cardNo;

	@ExcelColumn(colIndex = 3, isAcross = true, format = "#,##0.00")
	private BigDecimal salary;

	@ExcelColumn(colIndex = 4, rowIndex = 0)
	private String personInsTitle;

	@ExcelColumn(colIndex = 4, rowIndex = 1)
	private String firmInsTitle;

	@ExcelColumn(colIndex = 5, rowIndex = 0, format = "#,##0.00")
	private BigDecimal personIns;

	@ExcelColumn(colIndex = 5, rowIndex = 1, format = "#,##0.00")
	private BigDecimal firmIns;

	@ExcelColumn(colIndex = 6, isAcross = true, headName = "创建时间", format = "yyyy/mm/dd")
	private Date createTime;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public BigDecimal getSalary() {
		return salary;
	}

	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}

	public String getPersonInsTitle() {
		return personInsTitle;
	}

	public void setPersonInsTitle(String personInsTitle) {
		this.personInsTitle = personInsTitle;
	}

	public String getFirmInsTitle() {
		return firmInsTitle;
	}

	public void setFirmInsTitle(String firmInsTitle) {
		this.firmInsTitle = firmInsTitle;
	}

	public BigDecimal getPersonIns() {
		return personIns;
	}

	public void setPersonIns(BigDecimal personIns) {
		this.personIns = personIns;
	}

	public BigDecimal getFirmIns() {
		return firmIns;
	}

	public void setFirmIns(BigDecimal firmIns) {
		this.firmIns = firmIns;
	}

	@Override
	public String toString() {
		return "UserVo [id=" + id + ", name=" + name + ", cardNo=" + cardNo + ", salary=" + salary + ", personInsTitle="
				+ personInsTitle + ", firmInsTitle=" + firmInsTitle + ", personIns=" + personIns + ", firmIns="
				+ firmIns + ",  createTime=" + createTime + "]";
	}

}
