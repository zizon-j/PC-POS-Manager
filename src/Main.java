import javax.swing.*;

public class Main extends JFrame{

    JTabbedPane main = new JTabbedPane();

    public Main(){
        super("PC방 POS");
        // 각 add 뒤에 자신이 만든 페이지 추가
        main.add("회원관리",new JTextArea());
        main.add("운영매출", new JTextArea());
        main.add("상품관리",new JTextArea());
        main.add("로그분석", new JTextArea());
        main.add("주문내역", new JTextArea());

        add(main);

        setSize(1600, 900);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args)
    {
        new Main();
    }
}