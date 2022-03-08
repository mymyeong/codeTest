package com.mymyeong.musinsa.userpoint;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Past;

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
public class UserPoint {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_POINT_SEQ_GENERATOR")
	private Long no;

	@Past
	@Temporal(TemporalType.TIMESTAMP)
	private Date earnDate;

	/** 충전 포인트 */
	private Integer earnPoint;

	/** 남은 포인트 */
	private Integer deamandPoint;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private User user;
}
