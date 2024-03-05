package org.edupoll.app.data;

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
public class TimeplaceData {
	private Integer day;
	private Integer start;
	private Integer end;
}
