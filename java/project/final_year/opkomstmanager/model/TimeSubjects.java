package project.final_year.opkomstmanager.model;

public class TimeSubjects {
    String time, subjects;

    public TimeSubjects(String time, String subjects) {
        this.time = time;
        this.subjects = subjects;
    }

    public TimeSubjects() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }
}
