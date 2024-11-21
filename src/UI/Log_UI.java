//package UI;
//
//import javax.swing.*;
//import java.awt.*;
//import java.util.logging.*;
//
//public class Log_UI extends JPanel {
//    //보류
//    private static final Logger logger = Logger.getLogger(Log_UI.class.getName());
//    private JTextArea logArea;
//
//    public Log_UI() {
//        setLayout(new BorderLayout());
//
//        // JTextArea를 생성하고 스크롤 가능하게 설정
//        logArea = new JTextArea();
//        logArea.setEditable(false); // 사용자가 수정하지 못하게 설정
//        JScrollPane scrollPane = new JScrollPane(logArea);
//
//        // 로그 메시지를 보여줄 JTextArea를 패널에 추가
//        add(scrollPane, BorderLayout.CENTER);
//
//        // Logger에 커스텀 Handler 추가
//        setupLogger();
//
//        // 초기 로그 메시지
//        logger.info("Log UI initialized.");
//    }
//
//    private void setupLogger() {
//        // 기존 Handlers 제거
//        Logger rootLogger = Logger.getLogger("");
//        for (Handler handler : rootLogger.getHandlers()) {
//            rootLogger.removeHandler(handler);
//        }
//
//        // 새로운 JTextArea Handler 추가
//        TextAreaHandler textAreaHandler = new TextAreaHandler();
//        textAreaHandler.setFormatter(new SimpleFormatter()); // 기본 포매터 설정
//        rootLogger.addHandler(textAreaHandler);
//        rootLogger.setLevel(Level.ALL); // 모든 로그 레벨 허용
//    }
//
//    // Custom Handler: Logger 메시지를 JTextArea에 추가
//    private class TextAreaHandler extends Handler {
//        @Override
//        public void publish(LogRecord record) {
//            if (logArea != null && isLoggable(record)) {
//                SwingUtilities.invokeLater(() -> {
//                    try {
//                        logArea.append(getFormatter().format(record)); // 메시지 추가
//                        logArea.setCaretPosition(logArea.getDocument().getLength()); // 스크롤 최신 위치로 이동
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                });
//            }
//        }
//
//        @Override
//        public void flush() {
//            // 필요한 경우 구현
//        }
//
//        @Override
//        public void close() throws SecurityException {
//            // 필요한 경우 구현
//        }
//    }
//
//    public void log(String message) {
//        if (logger != null) {
//            logger.info(message); // Logger에 로그 메시지 추가
//        }
//    }
//
//    public static void main(String[] args) {
//        // JFrame 생성 및 설정
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("Log UI");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setSize(600, 400);
//
//            // Log_UI 패널을 JFrame에 추가
//            Log_UI logUI = new Log_UI();
//            frame.add(logUI);
//
//            frame.setVisible(true);
//
//            // 테스트 로그 메시지 출력
//            logUI.log("This is a test log message.");
//            logUI.log("Another log message appears here.");
//        });
//    }
//}
