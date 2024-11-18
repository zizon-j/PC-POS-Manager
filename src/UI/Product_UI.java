package Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;

//재고수정 버튼
class AmountUpdate extends JFrame implements ActionListener{
	
	JLabel name;
	JTextField amountField;
	String menu;
	JButton ok, cancel;
	public AmountUpdate(String title) {
		setTitle(title);
		name = new JLabel("선택메뉴의 수량 : ");
		amountField = new JTextField("",10);
		name.setBounds(80,30,150,40);
		amountField.setBounds(200,35,100,30);
		ok = new JButton("변경");
		cancel = new JButton("취소");
		ok.setBounds(100,90,80,25);
		cancel.setBounds(200,90,80,25);
		ok.addActionListener(this);
		cancel.addActionListener(this);
		
		Container ct = getContentPane();
		ct.setLayout(null);
		
		ct.add(name);
		ct.add(amountField);
		ct.add(ok);
		ct.add(cancel);
	}
	public void actionPerformed(ActionEvent ae) {
		String s = ae.getActionCommand();
		if(s.equals("취소")) {
			dispose();
		} else if(s.equals("변경")) {
			//
		}
	}
}

//카테고리 추가 버튼
class AddCate extends JFrame implements ActionListener{
	
	JLabel name;
	JTextField amountField;
	String menu;
	JButton ok, cancel;
	public AddCate(String title) {
		setTitle(title);
		name = new JLabel("카테고리 명  : ");
		amountField = new JTextField("",10);
		name.setBounds(80,30,150,40);
		amountField.setBounds(200,35,100,30);
		ok = new JButton("추가");
		cancel = new JButton("취소");
		ok.setBounds(100,90,80,25);
		cancel.setBounds(200,90,80,25);
		ok.addActionListener(this);
		cancel.addActionListener(this);
		
		Container ct = getContentPane();
		ct.setLayout(null);
		
		ct.add(name);
		ct.add(amountField);
		ct.add(ok);
		ct.add(cancel);
	}
	public void actionPerformed(ActionEvent ae) {
		String s = ae.getActionCommand();
		if(s.equals("취소")) {
			dispose();
		} else if(s.equals("변경")) {
			//
		}
	}
}

//메뉴 추가 버튼
class AddMenu extends JFrame implements ActionListener {
	
	String category[]  = {"카테고리", "1", "2", "3", "4", "5"};

	JLabel label1, label2, label3, label4;
	JComboBox cate;
	JTextField text2,text3,text4;
	JButton cPlus, add, cancel;
	public AddMenu(String title) {
		setTitle(title);
		
		Container ct = getContentPane();
		ct.setLayout(new GridLayout(5,1));
		
		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout(FlowLayout.LEFT));
		label1 = new JLabel("카테고리        :");
		cate = new JComboBox(category);
		cPlus = new JButton("+");
		cPlus.addActionListener(this);
		p1.add(label1); p1.add(cate); p1.add(cPlus);
		ct.add(p1);
		
		JPanel p2 = new JPanel();
		p2.setLayout(new FlowLayout(FlowLayout.LEFT));
		label2 = new JLabel("상품명             :");
		text2 = new JTextField(10); // 상품명
		p2.add(label2); p2.add(text2);
		ct.add(p2);
		
		JPanel p3 = new JPanel();
		p3.setLayout(new FlowLayout(FlowLayout.LEFT));
		label3 = new JLabel("가격                 :");
		text3 = new JTextField(10); // 가격
		p3.add(label3); p3.add(text3);
		ct.add(p3);
		
		JPanel p4 = new JPanel();
		p4.setLayout(new FlowLayout(FlowLayout.LEFT));
		label4 = new JLabel("재고                 :");
		text4 = new JTextField(10); //재고
		p4.add(label4); p4.add(text4);
		ct.add(p4);
		
		JPanel p5 = new JPanel();
		p5.setLayout(new FlowLayout(FlowLayout.RIGHT));
		add = new JButton("추가");
		cancel = new JButton("취소");
		p5.add(add); p5.add(cancel);
		ct.add(p5);
		add.addActionListener(this);
		cancel.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent ae) {
		String s = ae.getActionCommand();
		if(s.equals("취소")) {
			dispose();
		} else if(s.equals("추가")) {
			//
		} else if(s.equals("+")) {
			AddCate AC = new AddCate("카테고리 추가");
			AC.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			AC.setSize(400,200);
			AC.setLocation(1050,550);
			AC.show();
		}
	}
}

//메뉴 수정 버튼
class MenuUpdate extends JFrame implements ActionListener {
	
	String category[]  = {"카테고리", "1", "2", "3", "4", "5"};

	JLabel label1, label2, label3, label4;
	JComboBox cate;
	JTextField text2,text3,text4;
	JButton add, cancel;
	public MenuUpdate(String title) {
		setTitle(title);
		
		Container ct = getContentPane();
		ct.setLayout(new GridLayout(5,1));
		
		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout(FlowLayout.LEFT));
		label1 = new JLabel("카테고리        :");
		cate = new JComboBox(category);
		p1.add(label1); p1.add(cate);
		ct.add(p1);
		
		JPanel p2 = new JPanel();
		p2.setLayout(new FlowLayout(FlowLayout.LEFT));
		label2 = new JLabel("상품명             :");
		text2 = new JTextField(10);
		p2.add(label2); p2.add(text2);
		ct.add(p2);
		
		JPanel p3 = new JPanel();
		p3.setLayout(new FlowLayout(FlowLayout.LEFT));
		label3 = new JLabel("가격                 :");
		text3 = new JTextField(10);
		p3.add(label3); p3.add(text3);
		ct.add(p3);
		
		JPanel p4 = new JPanel();
		p4.setLayout(new FlowLayout(FlowLayout.LEFT));
		label4 = new JLabel("재고                 :");
		text4 = new JTextField(10);
		p4.add(label4); p4.add(text4);
		text4.setEnabled(false);
		ct.add(p4);
		
		JPanel p5 = new JPanel();
		p5.setLayout(new FlowLayout(FlowLayout.RIGHT));
		add = new JButton("변경");
		cancel = new JButton("취소");
		p5.add(add); p5.add(cancel);
		ct.add(p5);
		add.addActionListener(this);
		cancel.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent ae) {
		String s = ae.getActionCommand();
		if(s.equals("취소")) {
			dispose();
		} else if(s.equals("변경")) {
			//
		} else if(s.equals("+")) {
			AddCate AC = new AddCate("카테고리 추가");
			AC.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			AC.setSize(400,200);
			AC.setLocation(1050,550);
			AC.show();
		}
	}
}

//메뉴 삭제
class DelMenu extends JFrame implements ActionListener{
	
	JLabel name;
	JButton ok, cancel;
	public DelMenu(String title) {
		setTitle(title);
		name = new JLabel("선택한 메뉴를 삭제하시겠습니까?");
		name.setBounds(95,40,200,20);
		ok = new JButton("삭제");
		cancel = new JButton("취소");
		ok.setBounds(100,90,80,25);
		cancel.setBounds(200,90,80,25);
		ok.addActionListener(this);
		cancel.addActionListener(this);
		
		Container ct = getContentPane();
		ct.setLayout(null);
		
		ct.add(name);
		ct.add(ok);
		ct.add(cancel);
	}
	public void actionPerformed(ActionEvent ae) {
		String s = ae.getActionCommand();
		if(s.equals("취소")) {
			dispose();
		} else if(s.equals("삭제")) {
			//
		}
	}
}


//메인화면
public class Product_UI extends JPanel implements ActionListener {
	
	String category[]  = {"카테고리", "1", "2", "3", "4", "5"};
	String table_header[] = {"카테고리", "상품명", "가격", "재고량"};
	
	JComboBox categoryBox;
	JTextField menuSearchField;
	JButton SearchBtn, amountBtn, addBtn ,updateBtn, DelBtn;
	DefaultTableModel table;
	JTable ProductTable;
	
	public Product_UI() {
		//상단 버튼
		categoryBox = new JComboBox(category);
		menuSearchField = new JTextField("",20);
		SearchBtn = new JButton("검색");
		SearchBtn.setPreferredSize(new Dimension(80, 30));
		categoryBox.setPreferredSize(new Dimension(130, 30));
		menuSearchField.setPreferredSize(new Dimension(180, 30));

		//중단 테이블
		table = new DefaultTableModel(table_header, 100);
        ProductTable = new JTable(table);
        ProductTable.setRowHeight(30);
        JScrollPane ProT = new JScrollPane(ProductTable);
        ProT.setPreferredSize(new Dimension(1300, 680));   
        
        //하단 버튼
		amountBtn = new JButton("재고수정");
		addBtn = new JButton("메뉴추가");
		updateBtn = new JButton("메뉴수정");
		DelBtn = new JButton("메뉴삭제");
		amountBtn.setPreferredSize(new Dimension(100, 30));
		addBtn.setPreferredSize(new Dimension(100, 30));
		updateBtn.setPreferredSize(new Dimension(100, 30));
		DelBtn.setPreferredSize(new Dimension(100, 30));
		amountBtn.addActionListener(this);
		addBtn.addActionListener(this);
		updateBtn.addActionListener(this);
		DelBtn.addActionListener(this);
		
		//보더레이아웃
		setLayout(new BorderLayout());
		
		//상단 검색관련
		JPanel top = new JPanel();
		add(top, BorderLayout.NORTH);
		top.setLayout(new FlowLayout(FlowLayout.RIGHT,10,0));
		top.add(categoryBox);
		top.add(menuSearchField);
		top.add(SearchBtn);
		top.setBorder(BorderFactory.createEmptyBorder(10,0,10,130));
		
		//중단 상품테이블
		JPanel middle = new JPanel();
		middle.add(ProT);
		add(middle, BorderLayout.CENTER);
		
		//하단 버튼
		JPanel bottom = new JPanel();
		add(bottom,BorderLayout.SOUTH);
		bottom.setLayout(new FlowLayout(FlowLayout.RIGHT,40,0));
		bottom.setBorder(BorderFactory.createEmptyBorder(0,0,50,100));
		bottom.add(amountBtn);
		bottom.add(addBtn);
		bottom.add(updateBtn);
		bottom.add(DelBtn);
	}
	
	public void actionPerformed(ActionEvent ae) {
		String s = ae.getActionCommand();
		if (s.equals("재고수정")) {
			AmountUpdate au = new AmountUpdate("재고수정");
			au.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			au.setSize(400,200);
			au.setLocation(1050,550);
			au.show();
		}
		else if (s.equals("메뉴추가")) {
			AddMenu am = new AddMenu("메뉴추가");
			am.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			am.setSize(330,270);
			am.setLocation(1100,550);
			am.show();
		}
		else if (s.equals("메뉴수정")) {
			MenuUpdate mu = new MenuUpdate("메뉴수정");
			mu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			mu.setSize(330,270);
			mu.setLocation(1100,550);
			mu.show();
		}
		else if (s.equals("메뉴삭제")) {
			DelMenu dm = new DelMenu("메뉴삭제");
			dm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			dm.setSize(400,200);
			dm.setLocation(1050,550);
			dm.show();
		}
				
	}

}





