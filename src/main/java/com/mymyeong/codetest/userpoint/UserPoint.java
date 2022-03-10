package com.mymyeong.codetest.userpoint;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 
 * 유저 POINT 적립/사용 정보
 * 
 * @author mymyeong
 *
 */
@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
@ToString
public class UserPoint {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_POINT_SEQ_GENERATOR")
	@JsonIgnore
	private Long no;

	/** 사용자 NO */
	@NotNull
	private Long userNo;

	/** 처리일자 */
	private LocalDateTime processDate;

	/** 포인트 적립/사용 status */
	@Enumerated(EnumType.STRING)
	private PointStatus pointStatus;

	private Long pointAmount;

	/** 사용자 포인트 내역 */
	@OneToMany(mappedBy = "userPoint")
	@JsonIgnore
	private List<UserPointDetail> userPointsDetail;

}
