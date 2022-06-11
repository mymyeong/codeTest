package com.mymyeong.codetest.userpoint;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface UserPointDetailUseInterface {

	String getPointDetailId();

	void setPointDetailId(String pointDetailId);

	BigDecimal getPointAmount();

	void setPointAmount(BigDecimal pointAmount);

	LocalDateTime getProcessDate();

	void setProcessDate(LocalDateTime processDate);
}
