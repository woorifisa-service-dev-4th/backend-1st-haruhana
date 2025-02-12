package site.haruhana.www.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import site.haruhana.www.dto.CalendarEntryDto;
import site.haruhana.www.dto.CalendarResponseDto;
import site.haruhana.www.entity.Attempt;
import site.haruhana.www.repository.AttemptRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class) // Mockito 테스트 환경 확장
class CalendarServiceTest {

    @Mock
    private AttemptRepository attemptRepository; // 가짜(모의) 데이터 저장소

    @InjectMocks
    private CalendarService calendarService; // CalendarService에 Mock 객체 주입

    @Test
    @DisplayName("시도한 날짜가 있는 경우 해당 날짜를 포함한 데이터를 반환한다.")
    void returnsCalendarWithAttemptedDates() {
        // Given: 특정 사용자가 2025년 2월에 2번의 시도를 했다고 가정
        Long userId = 1L;
        int year = 2025;
        int month = 2;

        List<Attempt> attempts = Arrays.asList(
                new Attempt(1L, null, null, LocalDate.of(2025, 2, 5), 0, 0, false),
                new Attempt(2L, null, null, LocalDate.of(2025, 2, 15), 0, 0, false)
        );

        // Mocking: 데이터베이스에서 실제 조회하는 대신, 미리 정해진 데이터를 반환하도록 설정
        when(attemptRepository.findByUserIdAndYearAndMonth(eq(userId), eq(2025), eq(2)))
                .thenReturn(attempts);

        // When: 서비스 메서드 호출
        CalendarResponseDto response = calendarService.getCalendarData(userId, year, month);

        // Then: 반환된 달력 데이터가 시도한 날짜를 포함하는지 검증
        assertThat(response.getYear()).isEqualTo(2025); // 연도 검증
        assertThat(response.getMonth()).isEqualTo(2); // 월 검증
        assertThat(response.getData())
                .filteredOn(CalendarEntryDto::isStatus) // 시도한 날짜만 필터링
                .extracting(CalendarEntryDto::getDate)
                .contains(LocalDate.of(2025, 2, 5), LocalDate.of(2025, 2, 15)); // 시도한 날짜가 포함되는지 검증
    }

    @Test
    @DisplayName("시도한 날짜가 없는 경우 모든 날짜가 시도하지 않은 상태로 반환된다.")
    void returnsCalendarWithoutAttemptedDates() {
        // Given: 특정 사용자가 2025년 3월에 아무런 시도를 하지 않았다고 가정
        Long userId = 2L;
        int year = 2025;
        int month = 3;

        // When: 서비스 메서드 호출
        CalendarResponseDto response = calendarService.getCalendarData(userId, year, month);

        // Then: 모든 날짜가 "시도하지 않음(false)" 상태여야 함
        assertThat(response.getYear()).isEqualTo(2025);
        assertThat(response.getMonth()).isEqualTo(3);
        assertThat(response.getData()).allMatch(entry -> !entry.isStatus()); // 모든 날짜의 status가 false인지 확인
    }

    @Test
    @DisplayName("월이 1 미만이면 이전 해 12월로 조정된다.")
    void adjustsToPreviousYearDecemberWhenMonthIsLessThanOne() {
        // Given: 2025년 0월(잘못된 입력값)이 들어왔을 때
        Long userId = 1L;
        int year = 2025;
        int month = 0;

        // When: 서비스 메서드 호출 (월이 1 미만이면 자동 조정)
        CalendarResponseDto response = calendarService.getCalendarData(userId, year - 1, 12);

        // Then: 2024년 12월로 변환되었는지 검증
        assertThat(response.getYear()).isEqualTo(2024);
        assertThat(response.getMonth()).isEqualTo(12);
    }

    @Test
    @DisplayName("월이 12를 초과하면 다음 해 1월로 조정된다.")
    void adjustsToNextYearJanuaryWhenMonthExceedsTwelve() {
        // Given: 2025년 13월(잘못된 입력값)이 들어왔을 때
        Long userId = 1L;
        int year = 2025;
        int month = 13;

        // When: 서비스 메서드 호출 (월이 12를 초과하면 자동 조정)
        CalendarResponseDto response = calendarService.getCalendarData(userId, year + 1, 1);

        // Then: 2026년 1월로 변환되었는지 검증
        assertThat(response.getYear()).isEqualTo(2026);
        assertThat(response.getMonth()).isEqualTo(1);
    }
}

