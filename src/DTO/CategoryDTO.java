package DTO;

public class CategoryDTO {
    int category_no;
    String category_name;

    public CategoryDTO() {
    }

    public CategoryDTO(int category_no, String category_name) {
        this.category_no = category_no;
        this.category_name = category_name;
    }

    public CategoryDTO(int category_no) {
        this.category_no = category_no;
    }

    public CategoryDTO(String category_name) {
        this.category_name = category_name;
    }

    public int getCategory_no() {
        return category_no;
    }

    public void setCategory_no(int category_no) {
        this.category_no = category_no;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }
}
