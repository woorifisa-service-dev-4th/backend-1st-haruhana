package site.haruhana.www.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "attempts")
public class Attempt {

    /**
     * 풀이 식별자
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attempt_id")
    private Long id;

    /**
     * 풀이 시도한 문제 정보
     */
    @ManyToOne(fetch = FetchType.LAZY) // 하나의 문제에 대해 여러번의 풀이 진행 가능
    @JoinColumn(name = "problem_id", nullable = false) // 단방향
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Problem problem;

    /**
     * 풀이 시도한 사용자 정보
     */
    @ManyToOne(fetch = FetchType.LAZY) // 한명의 사용자가 여러번의 풀이 진행 가능
    @JoinColumn(name = "user_id", nullable = false) // 단방향
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    /**
     * 풀이 시도 날짜
     */
    @Column(name = "attempt_date")
    private LocalDate date;

    /**
     * 문제 푸는데 걸린 시간
     */
    @Column(name = "attempt_time")
    private int time;

    /**
     * 사용자 제출 값
     */
    private int userInput;

    /**
     * 풀이의 성공 여부
     */
    private boolean success;

}
