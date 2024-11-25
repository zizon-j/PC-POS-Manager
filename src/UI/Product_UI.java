package UI;

import DTO.CategoryDTO;

import DAO.ProductDAO;
import DTO.ProductDTO;
import Jdbc.PCPosDBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;

//메인화면
public class Product_UI extends JPanel implements ActionListener {

	String category[];
	String table_header[] = {"상품번호", "카테고리", "상품명", "가격", "재고량"};

	private JComboBox categoryBox;
	private JTextField menuSearchField;
	public JButton SearchBtn, amountBtn, addBtn, updateBtn, DelBtn;
	private DefaultTableModel table;
	private JTable ProductTable;
	private ProductDAO productDAO;
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private int selectedRow = -1, product_no,price,stock, category_no;
	private String product_name, category_name;
	private ProductDTO productDTO;
	public Product_UI() {

		conn = new PCPosDBConnection().getConnection();
		productDAO = new ProductDAO(conn);

		//상단 버튼
		categoryBox = new JComboBox();
		menuSearchField = new JTextField("", 20);
		SearchBtn = new JButton("검색");
		SearchBtn.setPreferredSize(new Dimension(80, 30));
		categoryBox.setPreferredSize(new Dimension(130, 30));
		menuSearchField.setPreferredSize(new Dimension(180, 30));

		//중단 테이블
		table = new DefaultTableModel(table_header, 100) {
			public boolean isCellEditable(int i, int c) {
				return false;
			}
		};
		ProductTable = new JTable(table);
		ProductTable.setRowHeight(30);
		ProductTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ProductTable.getTableHeader().setReorderingAllowed(false);
		ProductTable.setRowSelectionAllowed(true);
		ProductTable.setColumnSelectionAllowed(false);
		
		//테이블 클릭 시 정보 저장
		ProductTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				selectedRow = ProductTable.rowAtPoint(e.getPoint());
				if (selectedRow != -1) {
					product_no = (int) ProductTable.getValueAt(selectedRow, 0);
					category_name = ProductTable.getValueAt(selectedRow,1).toString();
					category_no= -1;
					for (int i=0;i< categoryBox.getItemCount(); i++) {
						if (categoryBox.getItemAt(i).equals(category_name)) {
							category_no = i;
							break;
						}
					}
					product_name = ProductTable.getValueAt(selectedRow,2).toString();
					price = (int) ProductTable.getValueAt(selectedRow,3);
					stock = (int) ProductTable.getValueAt(selectedRow, 4);

					productDTO = new ProductDTO(product_no, product_name, price, stock, category_no);

			} else
				JOptionPane.showMessageDialog(getParent(), "메뉴를 선택해주세요.");

			}
		});
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
		top.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		top.add(categoryBox);
		top.add(menuSearchField);
		top.add(SearchBtn);
		top.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 130));

		//중단 상품테이블
		JPanel middle = new JPanel();
		middle.add(ProT);
		add(middle, BorderLayout.CENTER);

		//하단 버튼
		JPanel bottom = new JPanel();
		add(bottom, BorderLayout.SOUTH);
		bottom.setLayout(new FlowLayout(FlowLayout.RIGHT, 40, 0));
		bottom.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 100));
		bottom.add(amountBtn);
		bottom.add(addBtn);
		bottom.add(updateBtn);
		bottom.add(DelBtn);

		DBTable();
		DBCategory();

		//검색버튼 완료
		SearchBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String s = ae.getActionCommand();
						try{
							if(s.equals("검색")){
							String name = menuSearchField.getText();
							String category = (String) categoryBox.getSelectedItem();
								if (category.equals("카테고리")) {
									category = null;
								}
							List<ProductDTO> product = productDAO.findById2(name, category);

							table.setRowCount(0);
							for (ProductDTO p : product) {
								Object[] row ={
										p.getProduct_no(),
										p.getCategory_name(),
										p.getProduct_name(),
										p.getPrice(),
										p.getStock()
									};
									table.addRow(row);
								};
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
			}
		});

		//검색필드에서 엔터쳐도 검색 작동
		menuSearchField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String name = menuSearchField.getText();
				String category = (String) categoryBox.getSelectedItem();
				if (category.equals("카테고리")) {
					category = null;
				}
				List<ProductDTO> product = productDAO.findById2(name, category);

				table.setRowCount(0);
				for (ProductDTO p : product) {
					Object[] row ={
							p.getProduct_no(),
							p.getCategory_name(),
							p.getProduct_name(),
							p.getPrice(),
							p.getStock()
					};
					table.addRow(row);
				};
			}
		});

		//콤보박스에서 선택만 해도 검색 작동 // 카테고리추가 갱신에서 오류 발생
/*		categoryBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String name = menuSearchField.getText();
				String category = (String) categoryBox.getSelectedItem();
				if (category.equals("카테고리")) {
					category = null;
				}
				List<ProductDTO> product = productDAO.findById2(name, category);

				table.setRowCount(0);
				for (ProductDTO p : product) {
					Object[] row ={
							p.getProduct_no(),
							p.getCategory_name(),
							p.getProduct_name(),
							p.getPrice(),
							p.getStock()
					};
					table.addRow(row);
				};
			}
		}); */

	}


	//테이블 내용 불러오기
	public void DBTable() {
		try {
			List<ProductDTO> product = productDAO.findAll();
			table.setRowCount(0);
			for (ProductDTO p : product) {
				Object[] row = {
						p.getProduct_no(),
						p.getCategory_name(),
						p.getProduct_name(),
						p.getPrice(),
						p.getStock()
				};
				table.addRow(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//카테고리 내용 불러오기
	public void DBCategory() {
		categoryBox.removeAllItems();
		try {
			List<CategoryDTO> category = productDAO.findAll_c();
			for (CategoryDTO c : category) {
				Object[] row = {
						c.getCategory_no(),
						c.getCategory_name(),
				};
				categoryBox.addItem(row[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent ae) {
		String s = ae.getActionCommand();
		//재고수정 완성
		if (s.equals("재고수정")) {
			int selectedRow = ProductTable.getSelectedRow();
			if(selectedRow == -1) {
					JOptionPane.showMessageDialog(this, "메뉴를 선택해주세요.");
				}
				else {
				AmountUpdate au = new AmountUpdate("재고수정", productDTO);
				au.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				au.setSize(400, 200);
				au.setLocation(1050, 550);
				au.show();
				}
		} 
		//메뉴추가 완성
		else if (s.equals("메뉴추가")) {
			AddMenu am = new AddMenu("메뉴추가");
			am.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			am.setSize(330, 270);
			am.setLocation(1100, 550);
			am.show();

		}
		
		//메뉴수정 완성
		else if (s.equals("메뉴수정")) {
			int selectedRow = ProductTable.getSelectedRow();
			if(selectedRow == -1) {
					JOptionPane.showMessageDialog(this, "메뉴를 선택해주세요.");
				} else {
					MenuUpdate mu = new MenuUpdate("메뉴수정", productDTO);
					mu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					mu.setSize(330, 270);
					mu.setLocation(1100, 550);
					mu.show();
				}
		}

		//메뉴삭제 완성
		else if (s.equals("메뉴삭제")) {
			int selectedRow = ProductTable.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(Product_UI.this, "메뉴를 선택해주세요.", "선택 오류", JOptionPane.WARNING_MESSAGE);
				return; // 이 후의 코드를 실행하지 않도록 종료
			}
			String del_product = String.valueOf(table.getValueAt(selectedRow, 2));
			if (selectedRow != -1) {
				int option = JOptionPane.showConfirmDialog(Product_UI.this,
						"정말로 \"" +del_product +"\" 상품 을(를) 삭제하시겠습니까?", "상품삭제", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (option == JOptionPane.YES_OPTION) {//확인 버튼을 눌렀다면
					table.removeRow(selectedRow);
					productDAO.delete(del_product);
					try { //메뉴삭제시 auto_increment 자동 갱신
						String sql = "alter table product auto_increment=1";
						pstmt = conn.prepareStatement(sql);
						pstmt.executeUpdate();
						String sql2 = "SET @count = 0";
						pstmt.executeUpdate(sql2);
						String sql3 = "UPDATE product SET product_no = @count := @count + 1";
						pstmt.executeUpdate(sql3);
						DBTable();

					} catch (SQLException e) {
						e.printStackTrace();
					} finally {
						try {
							if (rs != null)
								rs.close();
							if (pstmt != null)
								pstmt.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}



	//재고수정 버튼 완성
	class AmountUpdate extends JFrame implements ActionListener {

		JLabel name;
		JTextField amountField;
		String menu;
		JButton ok, cancel;
		ProductDTO productDTO;
		public AmountUpdate(String title, ProductDTO product) {
			setTitle(title);
			name = new JLabel("선택메뉴의 수량 : ");
			amountField = new JTextField("", 10);
			name.setBounds(80, 30, 150, 40);
			amountField.setBounds(200, 35, 100, 30);
			ok = new JButton("변경");
			cancel = new JButton("취소");
			ok.setBounds(100, 90, 80, 25);
			cancel.setBounds(200, 90, 80, 25);
			ok.addActionListener(this);
			cancel.addActionListener(this);
			productDTO = product;

			Container ct = getContentPane();
			ct.setLayout(null);

			ct.add(name);
			ct.add(amountField);
			ct.add(ok);
			ct.add(cancel);

			amountField.setText(productDTO.getStock()+"");
		}

		public void actionPerformed(ActionEvent ae) {
			String s = ae.getActionCommand();
			if (s.equals("취소")) {
				dispose();
			} else if (s.equals("변경")) {
				try {

					int newStock = Integer.parseInt(amountField.getText());
					productDTO.setStock(newStock);
					boolean	isBoolean = productDAO.update(productDTO);
					DBTable();
					if (isBoolean) {
						JOptionPane.showMessageDialog(this, "수정되었습니다.");
						dispose(); // 성공 시 창 닫기
					} else {
						JOptionPane.showMessageDialog(this, "수정에 실패했습니다.");
					}
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(this, "유효한 숫자를 입력하세요.");
				} catch (Exception e) {
					JOptionPane.showMessageDialog(this, "오류가 발생했습니다: " + e.getMessage());
				}
				}
			}
		}


	//카테고리 추가 버튼
	class AddCate extends JFrame implements ActionListener {

		JLabel name;
		JTextField categoryField;
		String menu;
		JButton ok, cancel;
		AddMenu addMenu;
		public AddCate(String title, AddMenu addMenu) {
			setTitle(title);
			this.addMenu = addMenu;
			name = new JLabel("카테고리 명  : ");
			categoryField = new JTextField("", 10);
			name.setBounds(80, 30, 150, 40);
			categoryField.setBounds(200, 35, 100, 30);
			ok = new JButton("추가");
			cancel = new JButton("취소");
			ok.setBounds(100, 90, 80, 25);
			cancel.setBounds(200, 90, 80, 25);
			ok.addActionListener(this);
			cancel.addActionListener(this);

			Container ct = getContentPane();
			ct.setLayout(null);

			ct.add(name);
			ct.add(categoryField);
			ct.add(ok);
			ct.add(cancel);
		}

		//카테고리 추가 완성
		public void actionPerformed(ActionEvent ae) {
			String s = ae.getActionCommand();
			if (s.equals("취소")) {
				dispose();
			} else if (s.equals("추가")) {
				String newCategory = categoryField.getText();
				if(newCategory == null || newCategory.trim().isEmpty()){
					JOptionPane.showMessageDialog(this, "카테고리 이름을 입력해주세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
					return;
				}
				CategoryDTO categoryDTO = new CategoryDTO();
				categoryDTO.setCategory_name(newCategory);
				try {
					productDAO.insertC(categoryDTO);
					JOptionPane.showMessageDialog(this, "카테고리가 추가되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
					dispose();
					addMenu.dispose();
					DBCategory();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}



	//메뉴 추가 버튼  완성
	class AddMenu extends JFrame implements ActionListener {

		String category[];

		JLabel label1, label2, label3, label4;
		JComboBox cate;
		JTextField text2, text3, text4;
		JButton cPlus, add, cancel;

		public AddMenu(String title) {
			setTitle(title);

			Container ct = getContentPane();
			ct.setLayout(new GridLayout(5, 1));

			JPanel p1 = new JPanel();
			p1.setLayout(new FlowLayout(FlowLayout.LEFT));
			label1 = new JLabel("카테고리        :");
			cate = new JComboBox();
			cPlus = new JButton("+");
			cPlus.addActionListener(this);
			p1.add(label1);
			p1.add(cate);
			p1.add(cPlus);
			ct.add(p1);

			JPanel p2 = new JPanel();
			p2.setLayout(new FlowLayout(FlowLayout.LEFT));
			label2 = new JLabel("상품명             :");
			text2 = new JTextField(10); // 상품명
			p2.add(label2);
			p2.add(text2);
			ct.add(p2);

			JPanel p3 = new JPanel();
			p3.setLayout(new FlowLayout(FlowLayout.LEFT));
			label3 = new JLabel("가격                 :");
			text3 = new JTextField(10); // 가격
			p3.add(label3);
			p3.add(text3);
			ct.add(p3);

			JPanel p4 = new JPanel();
			p4.setLayout(new FlowLayout(FlowLayout.LEFT));
			label4 = new JLabel("재고                 :");
			text4 = new JTextField(10); //재고
			p4.add(label4);
			p4.add(text4);
			ct.add(p4);

			JPanel p5 = new JPanel();
			p5.setLayout(new FlowLayout(FlowLayout.RIGHT));
			add = new JButton("추가");
			cancel = new JButton("취소");
			p5.add(add);
			p5.add(cancel);
			ct.add(p5);
			add.addActionListener(this);
			cancel.addActionListener(this);

			DBCategory();

		}


		//카테고리 내용 불러오기
		public void DBCategory() {
			try {
				List<CategoryDTO> category = productDAO.findAll_c();

				for (CategoryDTO c : category) {
					Object[] row = {
							c.getCategory_no(),
							c.getCategory_name(),
					};
					cate.addItem(row[1]);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		

		//메뉴추가 완성
		public void actionPerformed(ActionEvent ae) {
			String s = ae.getActionCommand();
			if (s.equals("취소")) {
				dispose();
			} else if (s.equals("추가")) {
				String category=cate.getSelectedItem().toString();
				if(category.equals("카테고리")) {
					JOptionPane.showMessageDialog(this, "카테고리를 선택해주세요.");
				} else {
					String productName = text2.getText();
					String price = text3.getText();
					String stock = text4.getText();
					int category_no = cate.getSelectedIndex()+1;

					try {
						if (productName == null || productName.equals("") || price == null || price.equals("") || stock == null || stock.equals("")) {
							JOptionPane.showMessageDialog(this, "값을 입력해주세요.");
						} else {
							// 먼저 product_name이 이미 존재하는지 확인
							if (isProductNameExist(productName)) {
								JOptionPane.showMessageDialog(this, "이미 존재하는 메뉴 이름입니다.");
							} else {
								ProductDTO productDTO = new ProductDTO();

								productDTO.setCategory_no(category_no);
								productDTO.setCategory_name(category);
								productDTO.setPrice(Integer.parseInt(price));
								productDTO.setStock(Integer.parseInt(stock));
								productDTO.setProduct_name(productName);

								productDAO.insert(productDTO);
								JOptionPane.showMessageDialog(this, productName + " 메뉴가 추가되었습니다.");
								dispose();
								try { //메뉴삽입시 auto_increment 자동 갱신
									String sql = "alter table product auto_increment=1";
									pstmt = conn.prepareStatement(sql);
									pstmt.executeUpdate();
									String sql2 = "SET @count = 0";
									pstmt.executeUpdate(sql2);
									String sql3 = "UPDATE product SET product_no = @count := @count + 1";
									pstmt.executeUpdate(sql3);
									DBTable();
								} catch (SQLException e) {
									e.printStackTrace();
								} finally {
									try {
										if (rs != null)
											rs.close();
										if (pstmt != null)
											pstmt.close();
									} catch (SQLException e) {
										e.printStackTrace();
									}
								}
								DBTable();
							}
						}
					} catch(NumberFormatException e) {
						JOptionPane.showMessageDialog(this, "가격과 재고는 정수로 입력해주세요.");
					}
				}
			} else if (s.equals("+")) {
				AddCate AC = new AddCate("카테고리 추가", this);
				AC.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				AC.setSize(400, 200);
				AC.setLocation(1050, 550);
				AC.show();
			}
		}
	}

	// 메뉴추가 시 product_name이 이미 존재하는지 확인하는 메소드
	private boolean isProductNameExist(String productName) {
		String query = "SELECT COUNT(*) FROM product WHERE product_name = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, productName);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0;  // product_name이 존재하면 true 반환
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;  // 존재하지 않으면 false 반환
	}


	//메뉴 수정 버튼
	class MenuUpdate extends JFrame implements ActionListener {

		String category[];

		JLabel label1, label2, label3, label4;
		JComboBox cate;
		JTextField text2, text3, text4;
		JButton add, cancel;
		ProductDTO productDTO;
		public MenuUpdate(String title, ProductDTO productDTO) {
			setTitle(title);

			Container ct = getContentPane();
			ct.setLayout(new GridLayout(5, 1));

			JPanel p1 = new JPanel();
			p1.setLayout(new FlowLayout(FlowLayout.LEFT));
			label1 = new JLabel("카테고리        :");
			cate = new JComboBox();
			p1.add(label1);
			p1.add(cate);
			ct.add(p1);

			JPanel p2 = new JPanel();
			p2.setLayout(new FlowLayout(FlowLayout.LEFT));
			label2 = new JLabel("상품명             :");
			text2 = new JTextField(10);
			p2.add(label2);
			p2.add(text2);
			ct.add(p2);

			JPanel p3 = new JPanel();
			p3.setLayout(new FlowLayout(FlowLayout.LEFT));
			label3 = new JLabel("가격                 :");
			text3 = new JTextField(10);
			p3.add(label3);
			p3.add(text3);
			ct.add(p3);

			JPanel p4 = new JPanel();
			p4.setLayout(new FlowLayout(FlowLayout.LEFT));
			label4 = new JLabel("재고                 :");
			text4 = new JTextField(10);
			p4.add(label4);
			p4.add(text4);
			text4.setEnabled(false);
			ct.add(p4);

			JPanel p5 = new JPanel();
			p5.setLayout(new FlowLayout(FlowLayout.RIGHT));
			add = new JButton("변경");
			cancel = new JButton("취소");
			p5.add(add);
			p5.add(cancel);
			ct.add(p5);
			add.addActionListener(this);
			cancel.addActionListener(this);

			this.productDTO = productDTO;

			DBCategory();
			
			//선택값으로 기본값
			cate.setSelectedIndex(productDTO.getCategory_no());
			text2.setText(productDTO.getProduct_name());
			text3.setText(productDTO.getPrice()+"");
			text4.setText(productDTO.getStock()+"");
		}

		//카테고리 내용 불러오기
		public void DBCategory() {
			try {
				List<CategoryDTO> category = productDAO.findAll_c();

				for (CategoryDTO c : category) {
					Object[] row = {
							c.getCategory_no(),
							c.getCategory_name(),
					};
					cate.addItem(row[1]);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void actionPerformed(ActionEvent ae) {

			String s = ae.getActionCommand();
			if (s.equals("취소")) {
				dispose();
			} else if (s.equals("변경")) {
				int cateNo = (cate.getSelectedIndex());
				if (cateNo == 0 ) {  // 0이 기본값으로, 선택되지 않았을 때로 가정
					JOptionPane.showMessageDialog(this, "카테고리를 선택해주세요.");
					return; // 카테고리 선택이 안 되었으면 함수를 종료
				}
				// 제품명 입력값 체크
				String productNameText = text2.getText();
				if (product_name.isEmpty()) {
					JOptionPane.showMessageDialog(this, "제품명을 입력해주세요.");
					return;
				}

				// 가격 입력값 체크
				String priceText = text3.getText();
				if (priceText.isEmpty()) {
					JOptionPane.showMessageDialog(this, "가격을 입력해주세요.");
					return;
				}

				int price = 0;
				try {
					price = Integer.parseInt(priceText); // 가격이 숫자인지 확인
					if (price <= 0) {
						JOptionPane.showMessageDialog(this, "가격은 0보다 큰 값을 입력해주세요.");
						return;
					}
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(this, "가격은 숫자만 입력해주세요.");
					return; // 숫자가 아니면 함수 종료
				}

				// 유효성 검사를 통과했다면 DTO 객체에 값을 설정
				productDTO.setCategory_no(cateNo+1);
				productDTO.setProduct_name(productNameText);
				productDTO.setPrice(price);
				productDTO.setStock(stock);
				productDTO.setProduct_no(product_no);
				// DAO 업데이트 호출
				if (productDAO.update_menu(productDTO)) {
					JOptionPane.showMessageDialog(this, "제품 정보가 성공적으로 업데이트되었습니다.");
				} else {
					JOptionPane.showMessageDialog(this, "제품 정보 업데이트에 실패했습니다.");
				}
				dispose();
				DBTable();
			}
		}
	}
}


//메인화면






