package org.lessons.java.bonus;

public class DbException extends RuntimeException{

        public DbException(String message) {
            super(message);
        }

        public DbException(String message, Throwable cause) {
            super(message, cause);
        }




    }
