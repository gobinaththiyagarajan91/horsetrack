package com.simulator.horsetrack.util;

import java.util.Objects;


public class InputValidator {

    public static boolean checkNotNullAndNotEmpty(String field) {
        return (Objects.nonNull(field) && !field.trim().isEmpty());
    }


}
