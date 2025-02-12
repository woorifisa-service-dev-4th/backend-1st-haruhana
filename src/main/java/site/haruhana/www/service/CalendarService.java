package site.haruhana.www.service;

import org.springframework.stereotype.Service;
import site.haruhana.www.dto.CalendarEntryDto;
import site.haruhana.www.dto.CalendarResponseDto;
import site.haruhana.www.entity.Attempt;
import site.haruhana.www.repository.AttemptRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalendarService {
    private final AttemptRepository attemptRepository;

    public CalendarService(AttemptRepository attemptRepository) {
        this.attemptRepository = attemptRepository;
    }

    /**
     * 특정 사용자의 지정된 연도와 월의 달력 데이터를 반환합니다.
     * 해당 월에 사용자가 시도한 날짜를 확인하고, 전체 달력에서 시도 여부를 포함한 데이터를 생성합니다.
     *
     * @param userId 조회할 사용자의 ID
     * @param year   조회할 연도
     * @param month  조회할 월 (1~12, 범위를 벗어나면 자동 조정)
     * @return 해당 월의 모든 날짜와 시도 여부를 포함하는 달력 데이터
     */
    public CalendarResponseDto getCalendarData(Long userId, int year, int month) {
        // 월 범위 조정 (0 이하 → 이전 해 12월, 13 이상 → 다음 해 1월)
        if (month < 1) {
            year -= 1;
            month = 12;
        } else if (month > 12) {
            year += 1;
            month = 1;
        }

        // 특정 유저가 해당 년/월에 시도한 모든 Attempt 데이터 가져오기
        List<Attempt> attempts = attemptRepository.findByUserIdAndYearAndMonth(userId, year, month);

        // 날짜별 상태 저장할 리스트
        List<CalendarEntryDto> calendarData = new ArrayList<>();

        // 사용자가 시도한 날짜 저장
        List<LocalDate> attemptedDates = new ArrayList<>();
        for (Attempt attempt : attempts) {
            calendarData.add(new CalendarEntryDto(attempt.getDate(), true));  // 시도했으면 true
            attemptedDates.add(attempt.getDate());
        }

        // 이번 달의 모든 날짜를 생성하여 시도하지 않은 날짜를 false 처리
        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());

        for (LocalDate date = firstDay; !date.isAfter(lastDay); date = date.plusDays(1)) {
            if (!attemptedDates.contains(date)) {
                calendarData.add(new CalendarEntryDto(date, false));  // 시도 안 하면 false
            }
        }

        return new CalendarResponseDto(year, month, calendarData);
    }
}


