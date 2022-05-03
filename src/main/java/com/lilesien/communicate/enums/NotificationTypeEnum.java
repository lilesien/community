package com.lilesien.communicate.enums;

public enum NotificationTypeEnum {

    REPLAY_QUESTION(1,"回复了问题"),
    REPLAY_COMMENT(2,"回复了评论");

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    private int type;
    private String name;

    NotificationTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static String nameOfType(int type){
        for (NotificationTypeEnum value : values()) {
            if (value.getType() == type){
                return value.getName();
            }
        }
        return "";
    }
}

