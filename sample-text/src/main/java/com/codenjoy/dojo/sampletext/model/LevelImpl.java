package com.codenjoy.dojo.sampletext.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import java.util.LinkedList;
import java.util.List;

/**
 * Полезный утилитный класс для обработки текстовых заданий
 */
public class LevelImpl implements Level {

    private List<String> questions = new LinkedList<>();
    private List<String> answers = new LinkedList<>();

    public LevelImpl(String... questionAnswers) {
        for (String qa : questionAnswers) {
            String[] split = qa.split("=");
            questions.add(split[0]);
            answers.add(split[1]);
        }
    }

    @Override
    public List<String> questions() {
        return questions;
    }

    @Override
    public List<String> answers() {
        return answers;
    }

    @Override
    public int size() {
        return questions.size();
    }
}
