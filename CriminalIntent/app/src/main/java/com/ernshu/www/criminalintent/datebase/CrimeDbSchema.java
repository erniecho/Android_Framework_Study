package com.ernshu.www.criminalintent.datebase;

public class CrimeDbSchema {
    /* The crimetable class only exists to define the String constants needed to describe the moving
    * pieces of your table definition. The first piece of that definition is
    * the name of the table in your datebase, CrimeTable.NAME*/
    public static final class CrimeTable {
        public static final String NAME = "crimes";
        /*describe the columns, you will be able to refer to the column named "title" in a Java-safe
        * way: CrimeTable.Cols.TITLE. That makes it much safer to update your program if you ever
        * need to change the name of column or add additional data to the table.*/
        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
            public static final String SUSPECT = "suspect";
        }
    }
}
