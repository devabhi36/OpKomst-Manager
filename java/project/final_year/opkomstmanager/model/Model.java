package project.final_year.opkomstmanager.model;

public class Model {
    private boolean isSelected;
    private String subjects;

    public Model(boolean isSelected, String subject) {
        this.isSelected = isSelected;
        this.subjects = subject;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }
}
