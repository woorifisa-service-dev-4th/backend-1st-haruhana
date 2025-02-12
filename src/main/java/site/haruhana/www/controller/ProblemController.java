package site.haruhana.www.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.haruhana.www.Service.ProblemService;
import site.haruhana.www.dto.ProblemDto;
import site.haruhana.www.entity.Problem;
import site.haruhana.www.entity.ProblemCategory;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/problems")
public class ProblemController {

    private final ProblemService problemService;

    @GetMapping
    public ResponseEntity<List<Problem>> getAllProblems() {
        return ResponseEntity.ok(problemService.getAllProblems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProblemDto> getProblemById(@PathVariable Long id) {
        try {
            ProblemDto problemDto = problemService.getProblemById(id);
            return ResponseEntity.ok(problemDto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Problem> createProblem(@RequestBody Problem problem) {
        return new ResponseEntity<>(problemService.createProblem(problem), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Problem> updateProblem(@PathVariable Long id, @RequestBody Problem problem) {
        return ResponseEntity.ok(problemService.updateProblem(id, problem));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProblem(@PathVariable Long id) {
        problemService.deleteProblem(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/problems")
    public ResponseEntity<List<Problem>> getProblemsByCategory(
            @RequestParam("category") ProblemCategory category, // Enum 타입으로 직접 받음
            @PageableDefault Pageable pageable) {
        Page<Problem> page = problemService.getProblemsByCategory(category, pageable);
        List<Problem> problems = page.getContent();
        if (problems.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(problems);
    }








}
