package org.edupoll.app.command;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterCommand {
	@Size(min = 6)
	private String username;

	@Size(min = 4, max = 12)
	private String password;

	@Size(min = 2, max = 7)
	private String nickname;
}
