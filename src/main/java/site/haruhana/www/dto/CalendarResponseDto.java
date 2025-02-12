package site.haruhana.www.dto;

import lombok.Getter;
import java.util.List;

@Getter
public class CalendarResponseDto {
    private int year;
    private int month;
    private List<CalendarEntryDto> data;

    public CalendarResponseDto(int year, int month, List<CalendarEntryDto> data) {
        this.year = year;
        this.month = month;
        this.data = data;
    }
}

