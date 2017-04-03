import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.logging.*;

import static java.util.stream.Collectors.toList;

public class Group {
    private String groupName;
    private List<JournalEntry> entries = new ArrayList<JournalEntry>();
    private List<Teacher> teachers = new ArrayList<Teacher>();
    private List<Pupil> pupils = new ArrayList<Pupil>();
    private List<Subject> subjects = new ArrayList<Subject>();
    private Logger log = new Logger();

    public Group(String groupName, List<Teacher> teachers, List<Subject> subjects, List<Pupil> pupils) {
        this.groupName = groupName;
        this.teachers = teachers;
        this.pupils = pupils;
        this.subjects = subjects;
        log.info("New group: " + toString());
    }

    public List<JournalEntry> getEntries(Pupil pupil) {
        return entries
                .stream()
                .filter(journalEntry -> journalEntry.getPupil().getId() == pupil.getId())
                .collect(toList());
    }

    public List<JournalEntry> getEntries(Subject subject) {
        return entries
                .stream()
                .filter(journalEntry -> journalEntry.getSubject().getId() == subject.getId())
                .collect(toList());
    }

    public void printObj(Object obj) {
        System.out.println(obj.toString());
    }

    public List<JournalEntry> getEntries() {
        return entries;
    }


    public void createEntry(Subject subject, Teacher teacher, Pupil pupil, int grade, String note) {
        if (!check(pupil)) {
            //System.out.println("IllegalStateException()");
            throw new IllegalArgumentException(pupil.toString() + " isn't in this group");
        }
        if (!check(subject)) {
            throw new IllegalArgumentException(subject.toString() + " isn't in this group");
        }
        if (teacher.getSubject().getId() != subject.getId()) {
            throw new IllegalArgumentException(teacher.toString() + " doesn't teach this " + subject.toString());
        }
        JournalEntry entry = new JournalEntry(subject, teacher, pupil, grade, note);
        entries.add(entry);
        log.info("New entry: " + entry.toString());

    }

    public void createEntry(Subject subject, Teacher teacher, Pupil pupil, int grade) {
        createEntry(subject, teacher, pupil, grade, "");
    }

    public List<User> getUserId(String name) {
        List<User> users = new ArrayList<>();
        users.addAll(teachers);
        users.addAll(pupils);
        return users
                .stream()
                .filter(object -> object.getName() == name)
                .collect(toList());
    }

    public User getUserById(int id) {
        List<User> users = new ArrayList<User>();
        users.addAll(teachers);
        users.addAll(pupils);
        /*if (users.size() == 0) {
            throw new NullPointerException();
        }
        for (User user : users) {
            if(user.getId() == id) {
                return user;
            }
        }
        return new User();//very bad useless code(

        //Or i can use this -> return users.get(id) // but it isn't correct*/
        return users
                .stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElse(new User());
    }


    private boolean check(Pupil pupilToCheck) {
        log.debug("Pupil check" + pupilToCheck.toString());
        return pupils
                .stream()
                .anyMatch((pupil -> pupil.getId() == pupilToCheck.getId()));

    }

    private boolean check(Subject subjectToCheck) {
        log.debug("Subject check" + subjectToCheck.toString());
        return subjects
                .stream()
                .anyMatch((pupil -> pupil.getId() == subjectToCheck.getId()));
    }

    public double getAvgGrade() {
        return entries
                .stream()
                .mapToInt(JournalEntry::getMark)
                .average()
                .orElse(-1);
    }

    public double getAvgGrade(Pupil pupil) {
        return entries
                .stream()
                .filter(journalEntry -> journalEntry.getPupil().getId()==pupil.getId())
                .mapToInt(JournalEntry::getMark)
                .average()
                .orElse(-1);
    }

    public double getAvgGrade(Teacher teacher) {
        return entries
                .stream()
                .filter(journalEntry -> journalEntry.getTeacher().getId()==teacher.getId())
                .mapToInt(JournalEntry::getMark)
                .average()
                .orElse(-1);
    }

    public String getGroupName() {
        return groupName;
    }

    public List<Pupil> getPupils() {
        return new ArrayList<Pupil>(pupils);
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    @Override
    public String toString() {
        return "Group{" +
                "groupName='" + groupName + '\'' +
                ", entries=" + entries +
                ", teachers=" + teachers +
                ", pupils=" + pupils +
                ", subjects=" + subjects +
                ", log=" + log +
                '}';
    }
}
