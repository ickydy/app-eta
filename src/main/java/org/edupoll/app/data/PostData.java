package org.edupoll.app.data;

import java.util.List;

import org.edupoll.app.entity.Board;
import org.edupoll.app.entity.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostData {
	private Board board;
	private List<Post> posts;
}
