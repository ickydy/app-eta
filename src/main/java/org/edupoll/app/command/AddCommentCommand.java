package org.edupoll.app.command;

import lombok.Data;

@Data
public class AddCommentCommand {
	private Integer boardId;
	private Long postId;
	private String content;
}
