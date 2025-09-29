package util;

enum CarType {
    Coupe("轿车"),
    Bus("客车");
    private final String cartype;
    CarType(String string) {
        this.cartype = string;
    }

    public String toString() {
        return cartype;
    }
}

enum RentType{
    Long_term("长期租赁"),
    Regular("定期租赁");
    private final String Description;
    RentType(String string) {
        this.Description = string;
    }
    public static RentType fromString(String text) {
        for (RentType type : RentType.values()) {
            if (type.toString().equals(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
    public String toString() {
        return this.Description;
    }
}