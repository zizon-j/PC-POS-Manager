package UI;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class S_sd_UI extends JDialog {
    private JComboBox<Integer> startYear, startMonth, startDay;
    private JComboBox<Integer> endYear, endMonth, endDay;
    private JButton confirmButton;
    private LocalDate[] selectedDates = new LocalDate[2]; // 시작/종료 날짜 저장

    public S_sd_UI(JFrame parent) {
        super(parent, "기간 설정", true); // 모달 다이얼로그
        setLayout(new GridLayout(3, 1, 10, 10));

        // 현재 날짜 가져오기
        LocalDate now = LocalDate.now();

        // 시작 날짜 선택 UI
        JPanel startPanel = new JPanel(new FlowLayout());
        startPanel.add(new JLabel("시작 날짜:"));
        startYear = createYearComboBox(now.getYear());
        startMonth = createMonthComboBox();
        startDay = createDayComboBox();
        startPanel.add(startYear);
        startPanel.add(startMonth);
        startPanel.add(startDay);

        // 종료 날짜 선택 UI
        JPanel endPanel = new JPanel(new FlowLayout());
        endPanel.add(new JLabel("종료 날짜:"));
        endYear = createYearComboBox(now.getYear());
        endMonth = createMonthComboBox();
        endDay = createDayComboBox();
        endPanel.add(endYear);
        endPanel.add(endMonth);
        endPanel.add(endDay);

        // 확인 버튼
        confirmButton = new JButton("조회");
        confirmButton.addActionListener(e -> {
            // 날짜 설정 가져오기
            int sy = (int) startYear.getSelectedItem();
            int sm = (int) startMonth.getSelectedItem();
            int sd = (int) startDay.getSelectedItem();
            int ey = (int) endYear.getSelectedItem();
            int em = (int) endMonth.getSelectedItem();
            int ed = (int) endDay.getSelectedItem();

            selectedDates[0] = LocalDate.of(sy, sm, sd);
            selectedDates[1] = LocalDate.of(ey, em, ed);
            dispose(); // 다이얼로그 닫기
        });

        // 구성
        add(startPanel);
        add(endPanel);
        add(confirmButton);
        pack();
        setLocationRelativeTo(parent); // 부모 창 중앙에 표시
    }

    private JComboBox<Integer> createYearComboBox(int currentYear) {
        JComboBox<Integer> comboBox = new JComboBox<>();
        for (int year = currentYear - 10; year <= currentYear + 10; year++) {
            comboBox.addItem(year);
        }
        comboBox.setSelectedItem(currentYear);
        return comboBox;
    }

    private JComboBox<Integer> createMonthComboBox() {
        JComboBox<Integer> comboBox = new JComboBox<>();
        for (int month = 1; month <= 12; month++) {
            comboBox.addItem(month);
        }
        comboBox.setSelectedItem(LocalDate.now().getMonthValue());
        return comboBox;
    }

    private JComboBox<Integer> createDayComboBox() {
        JComboBox<Integer> comboBox = new JComboBox<>();
        for (int day = 1; day <= 31; day++) {
            comboBox.addItem(day);
        }
        comboBox.setSelectedItem(LocalDate.now().getDayOfMonth());
        return comboBox;
    }

    public LocalDate[] getSelectedDates() {
        return selectedDates;
    }
}