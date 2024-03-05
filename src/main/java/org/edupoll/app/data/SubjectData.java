package org.edupoll.app.data;

import java.util.List;

import org.edupoll.app.entity.Type;

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
public class SubjectData {
	private Integer id;
	private String code;
	private String name;
	private String professor;
	private Type type;
	private Integer grade;
	private String time;
	private String place;
	private Integer credit;
	private Integer popular;
	private String target;
	private Integer lectureId;
	private Double lectureRate;
	private Double theory;
	private Double practice;
	private String notice;
	
	private Integer timetableSubjectId;
	
	private List<TimeplaceData> timeplaceDatas;
}
