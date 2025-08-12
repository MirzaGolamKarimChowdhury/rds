package rds;

public class Course {
    private String code;
    private String name;
    private String section;
    private String time; // Day code: RA, ST, MW
    private String timeSlot; // Specific time, e.g., "1:00-2:30"
    private String facultyInitial;
    private int credit;

    public Course(String code, String name, String section, String time, String timeSlot, String facultyInitial, int credit) {
        this.code = code;
        this.name = name;
        this.section = section;
        this.time = time;
        this.timeSlot = timeSlot;
        this.facultyInitial = facultyInitial;
        this.credit = credit;
    }

    public String toFileString() {
        return code + "|" + name + "|" + section + "|" + time + "|" + timeSlot + "|" + facultyInitial + "|" + credit;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public String getSection() { return section; }
    public String getTime() { return time; }
    public String getTimeSlot() { return timeSlot; }
    public String getFacultyInitial() { return facultyInitial; }
    public int getCredit() { return credit; }

    public boolean conflictsWith(Course other) {
        return this.time.equals(other.time) && this.timeSlot.equals(other.timeSlot) && !this.code.equals(other.code);
    }

    public String toString() {
        return code + " - " + name + " (Section: " + section + ", Time: " + timeSlot + " " + time + ", Faculty: " + facultyInitial + ", Credits: " + credit + ")";
    }

    public String toPreAdvisingString() {
        return code + " - " + name;
    }
}