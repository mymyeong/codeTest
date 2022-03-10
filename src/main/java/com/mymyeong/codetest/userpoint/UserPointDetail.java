package com.mymyeong.codetest.userpoint;

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
@Data
@AllArgsConstructor
@ToString
public class UserPointDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_POINT_DETAIL_SEQ_GENERATOR")
	@JsonIgnore
	private Long no;
	
	/** 사용자 NO */
	private Long userNo;

	/** 상세 적립 NO */
	@JsonIgnore
	private Long pointDetailNo;

	/** 처리 일자 */
	@Column
	private LocalDateTime processDate;

	/** 포인트 적립/사용 status */
	@Enumerated(EnumType.STRING)
	private PointStatus pointStatus;

	/** 충전/사용 포인트 */
	private Integer pointAmount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private UserPoint userPoint;

}
