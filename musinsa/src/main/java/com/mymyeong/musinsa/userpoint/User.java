package com.mymyeong.musinsa.userpoint;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
@ToString(exclude = "userPoints")
public class User {

	@Id
	@GeneratedValue
	private Long no;

	/** 사용자 ID */
	@Column(name = "USER_ID", nullable = false, length = 20)
	private String userId;

	/** 사용자 이름 */
	@Column(name = "USER_NAME", nullable = false, length = 50)
	private String userName;

	/** 사용자 포인트 내역 */
	@OneToMany(mappedBy = "user")
	@JsonIgnore
	private List<UserPoint> userPoints;

}
