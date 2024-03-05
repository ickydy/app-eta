package org.edupoll.app.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.edupoll.app.entity.Account;
import org.edupoll.app.entity.Image;
import org.edupoll.app.entity.Post;
import org.edupoll.app.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MultipartFileService {

	private final ImageRepository imageRepository;

	public void saveMultipartFiles(List<MultipartFile> multipartFiles, Account account, Post post) {
		multipartFiles.stream().forEach(multipartFile -> {
			if (multipartFile.isEmpty()) {
				return;
			}
			String uuid = UUID.randomUUID().toString();
			int index = multipartFile.getOriginalFilename().lastIndexOf(".");
			String extension = multipartFile.getOriginalFilename().substring(index);
			Image image = Image.builder() //
					.account(account) //
					.post(post) //
					.url(post.getId() + "/" + uuid + extension) //
					.build();
			File dir = new File("c:\\eta-upload\\", String.valueOf(post.getId()));
			dir.mkdir();
			File target = new File(dir, uuid + extension);
			try {
				multipartFile.transferTo(target);
			} catch (Exception e) {
				e.printStackTrace();
			}

			imageRepository.save(image);
		});
	}
}
