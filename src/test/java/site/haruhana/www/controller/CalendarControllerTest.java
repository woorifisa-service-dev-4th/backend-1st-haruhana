package site.haruhana.www.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import site.haruhana.www.dto.BaseResponse;
import site.haruhana.www.dto.CalendarEntryDto;
import site.haruhana.www.dto.CalendarResponseDto;
import site.haruhana.www.service.CalendarService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class CalendarControllerTest {

    @Mock
    private CalendarService calendarService;

    @InjectMocks
    private CalendarController calendarController;

    @Nested
    @DisplayName("GET /mypage/calendar")
    class GetCalendar {

        @Test
        @DisplayName("특정 연도와 월을 입력하면 해당 월의 캘린더 데이터를 반환한다")
        void shouldReturnCalendarDataForGivenYearAndMonth() {
            // Given (테스트 준비)
            Long userId = 1L;
            int year = 2025;
            int month = 2;

            // 해당 월의 캘린더 데이터를 미리 정의 (Mock 데이터)
            List<CalendarEntryDto> calendarEntries = Arrays.asList(
                    new CalendarEntryDto(LocalDate.of(2025, 2, 5), true),
                    new CalendarEntryDto(LocalDate.of(2025, 2, 15), true)
            );
            CalendarResponseDto responseDto = new CalendarResponseDto(year, month, calendarEntries);

            // Mock 설정: calendarService.getCalendarData()가 호출되면 미리 정의한 responseDto 반환
            given(calendarService.getCalendarData(userId, year, month)).willReturn(responseDto);

            // When (실제 테스트 실행)
            BaseResponse<CalendarResponseDto> response = calendarController.getCalendar(userId, month);

            // Then (결과 검증)
            assertEquals("캘린더 데이터 조회 성공", response.getMessage()); // 응답 메시지 확인
            assertNotNull(response.getData()); // 데이터가 null이 아님을 확인
            assertEquals(year, response.getData().getYear()); // 반환된 연도가 요청한 연도와 동일한지 확인
            assertEquals(month, response.getData().getMonth()); // 반환된 월이 요청한 월과 동일한지 확인
            assertEquals(2, response.getData().getData().size()); // 반환된 일정 개수 검증
        }

        @Test
        @DisplayName("월을 입력하지 않으면 현재 연도와 월의 캘린더 데이터를 반환한다")
        void shouldReturnCalendarDataForCurrentYearAndMonthWhenMonthIsNull() {
            // Given (테스트 준비)
            Long userId = 1L;
            LocalDate now = LocalDate.now();
            int year = now.getYear();
            int month = now.getMonthValue();

            // 현재 월에 대한 캘린더 데이터가 없는 경우를 가정
            List<CalendarEntryDto> calendarEntries = Collections.emptyList();
            CalendarResponseDto responseDto = new CalendarResponseDto(year, month, calendarEntries);

            // Mock 설정: calendarService.getCalendarData()가 호출되면 미리 정의한 responseDto 반환
            given(calendarService.getCalendarData(userId, year, month)).willReturn(responseDto);

            // When (실제 테스트 실행)
            BaseResponse<CalendarResponseDto> response = calendarController.getCalendar(userId, null);

            // Then (결과 검증)
            assertEquals("캘린더 데이터 조회 성공", response.getMessage()); // 응답 메시지 확인
            assertNotNull(response.getData()); // 데이터가 null이 아님을 확인
            assertEquals(year, response.getData().getYear()); // 반환된 연도가 현재 연도와 동일한지 확인
            assertEquals(month, response.getData().getMonth()); // 반환된 월이 현재 월과 동일한지 확인
            assertTrue(response.getData().getData().isEmpty()); // 일정 데이터가 없는지 확인
        }
    }
}