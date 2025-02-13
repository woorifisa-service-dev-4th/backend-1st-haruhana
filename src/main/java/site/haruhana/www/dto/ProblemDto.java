package site.haruhana.www.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.haruhana.www.entity.ProblemCategory;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProblemDto {
    private Long id;
    private String title;
    private String description;
    private Integer level;
    private ProblemCategory problemCategory;
}
