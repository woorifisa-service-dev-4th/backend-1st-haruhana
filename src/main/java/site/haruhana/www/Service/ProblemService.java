package site.haruhana.www.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.haruhana.www.entity.Problem;
import site.haruhana.www.dto.ProblemDto;
import site.haruhana.www.entity.ProblemCategory;
import site.haruhana.www.repository.ProblemRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;

    /**
     * 모든 문제 검색하여 반환
     */
    public List<Problem> getAllProblems() {
        log.info("Fetching all problems from the database");
        return problemRepository.findAll();
    }

    /**
     * 주어진 ID의 문제를 dto 로 변환하여 반환
     */
    public ProblemDto getProblemById(Long id) {
        log.info("Fetching problem with ID: {}", id);
        Problem problem = problemRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Problem not found"));
        log.error("Problem not found with ID: {}", id);
        // 생성자를 사용하여 ProblemDto 객체 생성
        return new ProblemDto(problem.getId(), problem.getTitle(), problem.getDescription(), problem.getLevel(), problem.getProblemCategory());
    }


    /**
     * 문제 생성
     */
    public Problem createProblem(Problem problem) {
        log.info("Creating new problem with title: {}", problem.getTitle());
        return problemRepository.save(problem);
    }

    /**
     * 문제 업데이트
     */
    @Transactional
    public Problem updateProblem(Long id, Problem updatedProblem) {
        log.info("Updating problem with ID: {}", id);
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Problem not found"));

        // 개별 필드를 추출하여 메서드에 전달
        problem.updateProblem(
                updatedProblem.getTitle(),
                updatedProblem.getDescription(),
                updatedProblem.getLevel(),
                updatedProblem.getProblemCategory()
        );

        return problemRepository.save(problem);
    }


    /**
     * 문제 삭세
     */
    public void deleteProblem(Long id) {
        log.info("Deleting problem with ID: {}", id);
        problemRepository.deleteById(id);
    }

    /**
     * 주어진 카테고리의 문제들을 페이지네이션하여 반환합니다.
     *
     * @param problemCategory 조회할 문제의 카테고리
     * @param pageable 페이지네이션 정보를 포함하는 Pageable 객체
     * @return 해당 카테고리에 속하는 문제들의 페이지
     */
    public Page<Problem> getProblemsByCategory(ProblemCategory problemCategory, Pageable pageable) {
        log.info("Fetching problems by category: {}", problemCategory);
        return problemRepository.findByProblemCategory(problemCategory, pageable);
    }
}
