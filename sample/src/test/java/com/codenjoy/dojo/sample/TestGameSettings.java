package com.codenjoy.dojo.sample;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

import com.codenjoy.dojo.sample.services.GameSettings;

import static com.codenjoy.dojo.sample.services.GameSettings.Keys.*;
import static com.codenjoy.dojo.services.round.RoundSettings.Keys.ROUNDS_ENABLED;

public class TestGameSettings extends GameSettings {

    public TestGameSettings() {
        bool(ROUNDS_ENABLED, false);
        integer(WIN_SCORE, 30);
        integer(LOSE_PENALTY, 100);
        integer(WIN_ROUND_SCORE, 200);

        // тут можно переопределить настройки для всех тестов
    }
}
