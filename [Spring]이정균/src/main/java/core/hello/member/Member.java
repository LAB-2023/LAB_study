package core.hello.member;

public class Member {
    private String name;
    private Long id;
    private Grade grade;

    public Member(Long id, String name, Grade grade) {
        this.id = id;
        this.name = name;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public Grade getGrade() {
        return grade;
    }
}
