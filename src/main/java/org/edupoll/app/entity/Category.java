package org.edupoll.app.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
public class Category {
	@Id
	private Integer id;
	@ManyToOne
	private Campus campus;
	private String name;
	private Integer arrange;
	private Integer parentId;
}
