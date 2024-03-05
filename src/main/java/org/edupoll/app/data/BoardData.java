package org.edupoll.app.data;

import java.util.List;

import org.edupoll.app.entity.Board;
import org.edupoll.app.entity.Kind;

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
public class BoardData {
	private Kind kind;
	private List<Board> boards;
}
