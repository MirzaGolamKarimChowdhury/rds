package rds;

public class Student {
    private String id;
    private String password;
    private String name;
    private String email;
    private String degreeName;
    private String cellPhone;
    private String dob;
    private String sex;
    private String citizenship;
    private String fatherName;
    private String motherName;
    private String guardianName;
    private String mailingAddress;
    private String phoneNumber;
    private String nidNumber;
    private String birthRegNumber;
    private String maritalStatus;
    private String bloodGroup;
    private String parentAddress;
    private double cgpa;
    private int creditPassed;
    private int probation;
    private String entryTerm;
    private String major1;
    private String major2;
    private String minor;

    public Student(String id, String password, String name, String email, String degreeName, String cellPhone,
                   String dob, String sex, String citizenship, String fatherName, String motherName,
                   String guardianName, String mailingAddress, String phoneNumber) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = email;
        this.degreeName = degreeName;
        this.cellPhone = cellPhone;
        this.dob = dob;
        this.sex = sex;
        this.citizenship = citizenship;
        this.fatherName = fatherName;
        this.motherName = motherName;
        this.guardianName = guardianName;
        this.mailingAddress = mailingAddress;
        this.phoneNumber = phoneNumber;
        this.nidNumber = "";
        this.birthRegNumber = "";
        this.maritalStatus = "";
        this.bloodGroup = "";
        this.parentAddress = "";
        this.cgpa = 0.0;
        this.creditPassed = 0;
        this.probation = 0;
        this.entryTerm = "Summer 2025";
        this.major1 = "N/A";
        this.major2 = "N/A";
        this.minor = "None";
    }

    public String toFileString() {
        return id + "|" + password + "|" + name + "|" + email + "|" + degreeName + "|" + cellPhone + "|" + dob + "|" +
               sex + "|" + citizenship + "|" + fatherName + "|" + motherName + "|" + guardianName + "|" +
               mailingAddress + "|" + phoneNumber + "|" + nidNumber + "|" + birthRegNumber + "|" +
               maritalStatus + "|" + bloodGroup + "|" + parentAddress;
    }

    public String getId() { return id; }
    public String getFirst7DigitsId() { return id.substring(0, 7); }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDegreeName() { return degreeName; }
    public String getCellPhone() { return cellPhone; }
    public void setCellPhone(String cellPhone) { this.cellPhone = cellPhone; }
    public String getDob() { return dob; }
    public String getSex() { return sex; }
    public String getCitizenship() { return citizenship; }
    public String getFatherName() { return fatherName; }
    public void setFatherName(String fatherName) { this.fatherName = fatherName; }
    public String getMotherName() { return motherName; }
    public void setMotherName(String motherName) { this.motherName = motherName; }
    public String getGuardianName() { return guardianName; }
    public void setGuardianName(String guardianName) { this.guardianName = guardianName; }
    public String getMailingAddress() { return mailingAddress; }
    public void setMailingAddress(String mailingAddress) { this.mailingAddress = mailingAddress; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getNidNumber() { return nidNumber; }
    public void setNidNumber(String nidNumber) { this.nidNumber = nidNumber; }
    public String getBirthRegNumber() { return birthRegNumber; }
    public void setBirthRegNumber(String birthRegNumber) { this.birthRegNumber = birthRegNumber; }
    public String getMaritalStatus() { return maritalStatus; }
    public void setMaritalStatus(String maritalStatus) { this.maritalStatus = maritalStatus; }
    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
    public String getParentAddress() { return parentAddress; }
    public void setParentAddress(String parentAddress) { this.parentAddress = parentAddress; }
    public double getCgpa() { return cgpa; }
    public int getCreditPassed() { return creditPassed; }
    public int getProbation() { return probation; }
    public String getEntryTerm() { return entryTerm; }
    public String getMajor1() { return major1; }
    public String getMajor2() { return major2; }
    public String getMinor() { return minor; }
}
