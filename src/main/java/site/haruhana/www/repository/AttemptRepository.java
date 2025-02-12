package site.haruhana.www.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.haruhana.www.entity.Attempt;

import java.time.LocalDate;
import java.util.List;

public interface AttemptRepository extends JpaRepository<Attempt, Long> {

    /**
     * 특정 사용자의 지정된 기간 내 시도(Attempt) 목록을 조회합니다.
     *
     * @param userId    조회할 사용자의 ID
     * @param startDate 조회 시작 날짜 (포함)
     * @param endDate   조회 종료 날짜 (포함)
     * @return 조회된 시도 목록
     */
    List<Attempt> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    /**
     * 특정 사용자의 지정된 연도와 월에 해당하는 시도(Attempt) 목록을 조회합니다.
     *
     * 내부적으로 해당 연월의 시작일과 마지막 일을 계산하여 findByUserIdAndDateBetween을 호출합니다.
     *
     * @param userId 조회할 사용자의 ID
     * @param year   조회할 연도
     * @param month  조회할 월 (1~12)
     * @return 조회된 시도 목록
     */
    default List<Attempt> findByUserIdAndYearAndMonth(Long userId, int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return findByUserIdAndDateBetween(userId, startDate, endDate);
    }
}

