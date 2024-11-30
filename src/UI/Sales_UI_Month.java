package UI;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;

public class Sales_UI_Month extends JPanel {
    private LocalDate currentDate; // 현재 날짜
    private JLabel monthLabel; // 월 표시
    private JPanel gridPanel; // 날짜 버튼 그리드 패널
    private JLabel totalSales;

    public Sales_UI_Month() {
        setLayout(new BorderLayout());
        currentDate = LocalDate.now(); // 날짜 초기화

        // 상단 월 이동 패널
        JPanel headerPanel = new JPanel(new BorderLayout());
        JButton prevButton = new JButton("<");
        JButton nextButton = new JButton(">");
        monthLabel = new JLabel(getMonthTitle(), JLabel.CENTER);

        // 총 매출
        totalSales = new JLabel("총 매출: ", JLabel.CENTER);
        totalSales.setFont(new Font("굴림", Font.BOLD, 20));
        headerPanel.add(totalSales, BorderLayout.NORTH);

        prevButton.addActionListener(e -> updateMonth(-1)); // 이전 달로 이동
        nextButton.addActionListener(e -> updateMonth(1)); // 다음 달로 이동

        headerPanel.add(prevButton, BorderLayout.WEST);
        headerPanel.add(monthLabel, BorderLayout.CENTER);
        headerPanel.add(nextButton, BorderLayout.EAST);

        // 중앙 달력 그리드 패널
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(0,7)); // 7열(요일)

        // 초기 달력 생성
        createCalendarGrid();

        // 패널 구성
        add(headerPanel, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);
    }

    // 현재 월 제목 반환
    private String getMonthTitle() {
        return currentDate.getYear() + "년 " + currentDate.getMonthValue() + "월";
    }

    // 월 업데이트
    private void updateMonth(int increment) {
        currentDate = currentDate.plusMonths(increment); // 월 이동
        monthLabel.setText(getMonthTitle()); // 월 제목 갱신
        createCalendarGrid(); // 달려 갱신
        revalidate();
        repaint();
    }

    // 달력 그리드 생성
    private void createCalendarGrid() {
        gridPanel.removeAll(); // 기존 그리드 제거

        // 요일 추가
        String[] days = {"일", "월", "화", "수", "목", "금", "토"};
        for(String day : days) {
            JLabel dayLabel = new JLabel(day, JLabel.CENTER);
            gridPanel.add(dayLabel);
        }

        // 날짜 계산
        YearMonth yearMonth = YearMonth.from(currentDate);
        int daysInMonth = yearMonth.lengthOfMonth(); // 해당 월의 일 수
        int firstdayOfWeek = yearMonth.atDay(1).getDayOfWeek().getValue(); // 시작 요일

        // 시작 요일 전의 빈 칸 추가
        for (int i = 1; i < firstdayOfWeek + 1; i++) {
            gridPanel.add(new JLabel(""));
        }

        // 날짜 버튼 추가
        for (int day = 1; day <= daysInMonth; day++) {
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BorderLayout()); // BorderLayout을 사용하여 텍스트 추가

            // 날짜 버튼 생성
            JButton dayButton = new JButton(String.valueOf(day));
            dayButton.setFocusPainted(false); // 버튼의 포커스를 표시하지 않음

            // "매출: " 텍스트 추가
            JLabel salesLabel = new JLabel("매출: ", JLabel.CENTER);
            buttonPanel.add(salesLabel, BorderLayout.NORTH); // 텍스트를 버튼 위에 배치

            // 날짜 버튼과 텍스트를 panel에 추가
            buttonPanel.add(dayButton, BorderLayout.CENTER);

            // 버튼을 그리드에 추가
            gridPanel.add(buttonPanel);
        }

    }

}
