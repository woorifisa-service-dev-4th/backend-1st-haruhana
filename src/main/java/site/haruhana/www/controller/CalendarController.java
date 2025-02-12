package site.haruhana.www.controller;

import org.springframework.web.bind.annotation.*;
import site.haruhana.www.dto.BaseResponse;
import site.haruhana.www.dto.CalendarResponseDto;
import site.haruhana.www.service.CalendarService;

import java.time.LocalDate;

@RestController
@RequestMapping("/mypage/calendar")
public class CalendarController {
    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    /**
     * 특정 사용자의 캘린더 데이터를 조회합니다.
     * 조회할 월을 전달하지 않으면, 기본적으로 현재 연도와 월의 데이터를 반환합니다.
     *
     * @param userId 조회할 사용자의 ID
     * @param month  조회할 월 (1~12, 선택적, 기본값: 현재 월)
     * @return 캘린더 데이터와 성공 응답 메시지를 포함한 {@link BaseResponse}
     */
    @GetMapping
    public BaseResponse<CalendarResponseDto> getCalendar(
            @RequestParam Long userId,
            @RequestParam(required = false) Integer month) { // month를 Optional하게 받음

        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int targetMonth = (month != null) ? month : now.getMonthValue(); // 기본값 설정

        CalendarResponseDto calendarData = calendarService.getCalendarData(userId, year, targetMonth);
        return BaseResponse.onSuccess("캘린더 데이터 조회 성공", calendarData);
    }

}
