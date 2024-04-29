package tech.finovy.framework.enums;

public class CommonEnum {
    private CommonEnum() {
    }

    public enum YesOrNO {
        NO(0, "NO"),
        YES(1, "YES");

        private int type;
        private String name;

        YesOrNO(int type, String name) {
            this.type = type;
            this.name = name;
        }

        public int getType() {return type;}
        public String getName() {return name;}

        public static YesOrNO typeOf(int type) {
            for (YesOrNO yesOrNO: YesOrNO.values()) {
                if (yesOrNO.type == type) {
                    return yesOrNO;
                }
            }
            return null;
        }
    }


}
