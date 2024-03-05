package org.edupoll.app.command;

import lombok.Data;

@Data
public class UpdateFriendCommand {
	private Long friendId;
	private String status;
}
