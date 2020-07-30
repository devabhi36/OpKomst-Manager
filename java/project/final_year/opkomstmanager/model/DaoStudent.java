package project.final_year.opkomstmanager.model;

public class DaoStudent {
    private String Name, Email, Department, ContactNo, RollNo, Year, Password, EnrollmentNo, Lateral, Semester, dpurl, token;

    public DaoStudent(){}

    public DaoStudent(String Name, String Email, String Department, String ContactNo, String RollNo, String Year, String Password,
                      String EnrollmentNo, String Lateral, String Semester, String dpurl, String token) {
        this.Name = Name;
        this.Email = Email;
        this.Department = Department;
        this.ContactNo = ContactNo;
        this.RollNo = RollNo;
        this.Year = Year;
        this.Password = Password;
        this.EnrollmentNo = EnrollmentNo;
        this.Lateral = Lateral;
        this.Semester = Semester;
        this.dpurl = dpurl;
        this.token = token;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String Department) {
        this.Department = Department;
    }

    public String getContact() {
        return ContactNo;
    }

    public void setContact(String ContactNo) {
        this.ContactNo = ContactNo;
    }

    public String getRoll() {
        return RollNo;
    }

    public void setRoll(String RollNo) {
        this.RollNo = RollNo;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String Year) {
        this.Year = Year;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getEnrollmentNo() {
        return EnrollmentNo;
    }

    public void setEnrollmentNo(String EnrollmentNo) {
        this.EnrollmentNo = EnrollmentNo;
    }

    public String getLateral() {
        return Lateral;
    }

    public void setLateral(String Lateral) {
        this.Lateral = Lateral;
    }

    public String getSemester() {
        return Semester;
    }

    public void setSemester(String Semester) {
        this.Semester = Semester;
    }

    public String getDpurl() {
        return dpurl;
    }

    public void setDpurl(String dpurl) {
        this.dpurl = dpurl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
