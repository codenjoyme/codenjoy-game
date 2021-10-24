package com.codenjoy.dojo.sample.services;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2020 Codenjoy
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


import com.codenjoy.dojo.sample.model.Level;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.incativity.InactivitySettings;
import com.codenjoy.dojo.services.level.LevelsSettings;
import com.codenjoy.dojo.services.round.RoundSettings;
import com.codenjoy.dojo.services.semifinal.SemifinalSettings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.sample.services.GameSettings.Keys.*;

public class GameSettings extends SettingsImpl
        implements SettingsReader<GameSettings>,
                   InactivitySettings<GameSettings>,
                   RoundSettings<GameSettings>,
                   LevelsSettings<GameSettings>,
                   SemifinalSettings<GameSettings> {

    public enum Keys implements Key {

        WIN_SCORE("Win score"),
        WIN_ROUND_SCORE("Win round score"),
        LOSE_PENALTY("Lose penalty");

        private String key;

        Keys(String key) {
            this.key = key;
        }

        @Override
        public String key() {
            return key;
        }
    }

    @Override
    public List<Key> allKeys() {
        return Arrays.asList(Keys.values());
    }

    public GameSettings() {
        initInactivity();
        initRound();
        initSemifinal();
        initLevels();

        integer(WIN_SCORE, 30);
        integer(WIN_ROUND_SCORE, 100);
        integer(LOSE_PENALTY, 20);

        Levels.setup(this);
    }

    public Level level(int level, Dice dice) {
        return new Level(getRandomLevelMap(level, dice));
    }
}