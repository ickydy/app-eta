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
public class Subject {
	@Id
	private Integer id;
	@ManyToOne
	private Campus campus;
	private String code;
	private String name;
	private String professor;
	@ManyToOne
	private Type type;
	private Integer grade;
	private String time;
	private String place;
	private Integer credit;
	private Integer popular;
	private String target;
	private String notice;
	private Integer lectureId;
	private Double lectureRate;
	private Double theory;
	private Double practice;
}
