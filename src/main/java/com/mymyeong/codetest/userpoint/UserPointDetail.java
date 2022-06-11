package com.mymyeong.codetest.userpoint;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 
 * 유저 POINT
 * 
 * @author mymyeong
 *
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "userPoint")
public class UserPointDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_POINT_DETAIL_SEQ_GENERATOR")
	@JsonIgnore
	private String no;

	/** 사용자 NO */
	private Long userNo;

	/** 상세 적립 NO */
	@JsonIgnore
	private String pointDetailNo;

	/** 처리 일자 */
	@Column
	private LocalDateTime processDate;

	/** 포인트 적립/사용 status */
	@Enumerated(EnumType.STRING)
	private PointStatus pointStatus;

	/** 충전/사용 포인트 */
	private BigDecimal pointAmount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	@ToString.Exclude
	private UserPoint userPoint;

	public void setUserPoint(UserPoint userPoint) {
		this.userPoint = userPoint;
	}

	public UserPoint getUserPoint() {
		return userPoint;
	}

	public Long getUserNo() {
		return userNo;
	}

	public String getPointDetailNo() {
		return pointDetailNo;
	}

	public LocalDateTime getProcessDate() {
		return processDate;
	}

	public PointStatus getPointStatus() {
		return pointStatus;
	}

	public BigDecimal getPointAmount() {
		return pointAmount;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public void setUserNo(Long userNo) {
		this.userNo = userNo;
	}

	public void setPointDetailNo(String pointDetailNo) {
		this.pointDetailNo = pointDetailNo;
	}

	public void setProcessDate(LocalDateTime processDate) {
		this.processDate = processDate;
	}

	public void setPointStatus(PointStatus pointStatus) {
		this.pointStatus = pointStatus;
	}

	public void setPointAmount(BigDecimal pointAmount) {
		this.pointAmount = pointAmount;
	}
}
