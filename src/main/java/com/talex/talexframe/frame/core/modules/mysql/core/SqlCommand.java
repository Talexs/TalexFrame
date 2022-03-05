package com.talex.talexframe.frame.core.modules.mysql.core;

/**
 * <br /> {@link com.talex.frame.talexframe.function.mysql.core Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/21 15:39 <br /> Project: TalexFrame <br />
 */
public enum SqlCommand {

    DELETE_DATA("DELETE FROM `%table_name%` WHERE `%username%` = \"%value%\""),
    UPDATE_DATA("UPDATE `%table_name%` SET `amount`= %amount% WHERE username = \"%username%\" "),
    SELECT_ALL_DATA("SELECT * FROM `%table_name%`"),
    SELECT_DATA("SELECT * FROM `%table_name%` WHERE %select_type% = \"%username%\"");

    private final String command;

    SqlCommand(String command) {
        this.command = command;
    }

    public String commandToString() {
        return this.command;
    }

}
