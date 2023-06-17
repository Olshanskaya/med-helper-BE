package med.helper.enums;

public enum ElementStatus {
    ACTIVE("ACTIVE"), DELETED("DELETED");
    private String name;

    ElementStatus(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
