package com.mymyeong.codetest.userpoint;

import java.time.LocalDateTime;

public interface UserPointDetailUseInterface {

	public Long getPointDetailNo();

	public void setPointDetailNo(Long pointDetailNo);

	public Long getPointAmount();

	public void setPointAmount(Long pointAmount);

	public LocalDateTime getProcessDate();

	public void setProcessDate(LocalDateTime processDate);
}
