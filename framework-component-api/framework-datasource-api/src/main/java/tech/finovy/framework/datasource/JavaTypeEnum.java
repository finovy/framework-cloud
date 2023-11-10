package tech.finovy.framework.datasource;

public enum JavaTypeEnum {

    BYTE("BYTE"),
    SHORT("SHORT"),
    INT("INT"),
    LONG("LONG"),
    FLOAT("FLOAT"),
    DOUBLE("DOUBLE"),
    BOOLEAN("BOOLEAN"),
    CHAR("CHAR"),
    BLOB("BLOB"),

    NULL("NULL"),

    DATE("DATE"),
    STRING("STRING");

    private String type;

    JavaTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static JavaTypeEnum typeOf(String type) {
        for (JavaTypeEnum otype : JavaTypeEnum.values()) {
            if (otype.type.equals(type)) {
                return otype;
            }
        }
        throw new RuntimeException("not find this enum :"+type);
    }
}
