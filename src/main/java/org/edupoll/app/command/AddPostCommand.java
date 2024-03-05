package org.edupoll.app.command;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddPostCommand {
	private Integer boardId;
	private String title;
	private String content;
	private List<MultipartFile> images;
}
