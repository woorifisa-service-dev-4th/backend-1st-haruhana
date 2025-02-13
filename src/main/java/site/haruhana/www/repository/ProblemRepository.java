package site.haruhana.www.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import site.haruhana.www.entity.Problem;
import site.haruhana.www.entity.ProblemCategory;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
    Page<Problem> findByProblemCategory(ProblemCategory category, Pageable pageable);
}