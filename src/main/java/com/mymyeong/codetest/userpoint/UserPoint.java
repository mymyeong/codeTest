package com.mymyeong.codetest.userpoint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 
 * 유저 POINT 적립/사용 정보
 * 
 * @author mymyeong
 *
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserPoint {

	@Id
	private String no;

	/** 사용자 NO */
	@NotNull
	private Long userNo;

	/** 처리일자 */
	private LocalDateTime processDate;

	/** 포인트 적립/사용 status */
	@Enumerated(EnumType.STRING)
	private PointStatus pointStatus;

	private BigDecimal pointAmount;

	/** 사용자 포인트 내역 */
	@OneToMany(mappedBy = "userPoint")
	@JsonIgnore
	private List<UserPointDetail> userPointsDetail;

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" +
				"no = " + no + ", " +
				"userNo = " + userNo + ", " +
				"processDate = " + processDate + ", " +
				"pointStatus = " + pointStatus + ", " +
				"pointAmount = " + pointAmount + ")";
	}

	public String getNo() {
		return no;
	}

	public Long getUserNo() {
		return userNo;
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

	public List<UserPointDetail> getUserPointsDetail() {
		return userPointsDetail;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public void setUserNo(Long userNo) {
		this.userNo = userNo;
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

	public void setUserPointsDetail(List<UserPointDetail> userPointsDetail) {
		this.userPointsDetail = userPointsDetail;
	}
}
