package ru.itis.homework.utils;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class TableInfo {

    protected String tableName;
    protected String columnName;
    protected String dataType;
}
