package org.edupoll.app.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board {
	@Id
	private Integer id;
	@ManyToOne
	private Campus campus;
	@ManyToOne
	private Kind kind;
	private String name;
	private String description;
	private Boolean anonymous;
	@OneToMany(mappedBy = "board")
	@OrderBy("write_at desc")
	private List<Post> posts;
}
