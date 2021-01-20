package com.codenjoy.dojo.sample.model;

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


import com.codenjoy.dojo.sample.model.level.Level;
import com.codenjoy.dojo.sample.model.level.LevelImpl;
import com.codenjoy.dojo.sample.services.Events;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class SingleTest {

    private EventListener listener1;
    private EventListener listener2;
    private EventListener listener3;
    private Game game1;
    private Game game2;
    private Game game3;
    private Dice dice;
    private Sample field;

    // появляется другие игроки, игра становится мультипользовательской
    @Before
    public void setup() {
        Level level = new LevelImpl(
                "☼☼☼☼☼☼" +
                "☼   $☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        dice = mock(Dice.class);
        field = new Sample(level, dice);
        PrinterFactory factory = new PrinterFactoryImpl();

        listener1 = mock(EventListener.class);
        game1 = new Single(new Player(listener1), factory);
        game1.on(field);

        listener2 = mock(EventListener.class);
        game2 = new Single(new Player(listener2), factory);
        game2.on(field);

        listener3 = mock(EventListener.class);
        game3 = new Single(new Player(listener3), factory);
        game3.on(field);

        dice(1, 4);
        game1.newGame();

        dice(2, 2);
        game2.newGame();

        dice(3, 4);
        game3.newGame();
    }

    private void dice(int x, int y) {
        when(dice.next(anyInt())).thenReturn(x, y);
    }

    private void asrtFl1(String expected) {
        assertEquals(expected, game1.getBoardAsString());
    }

    private void asrtFl2(String expected) {
        assertEquals(expected, game2.getBoardAsString());
    }

    private void asrtFl3(String expected) {
        assertEquals(expected, game3.getBoardAsString());
    }

    // рисуем несколько игроков
    @Test
    public void shouldPrint() {
        asrtFl1("☼☼☼☼☼☼\n" +
                "☼☺ ☻$☼\n" +
                "☼    ☼\n" +
                "☼ ☻  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");

        asrtFl2(
                "☼☼☼☼☼☼\n" +
                "☼☻ ☻$☼\n" +
                "☼    ☼\n" +
                "☼ ☺  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");

        asrtFl3(
                "☼☼☼☼☼☼\n" +
                "☼☻ ☺$☼\n" +
                "☼    ☼\n" +
                "☼ ☻  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");
    }

    // Каждый игрок может упраыляться за тик игры независимо
    @Test
    public void shouldJoystick() {
        game1.getJoystick().act();
        game1.getJoystick().down();
        game2.getJoystick().right();
        game3.getJoystick().down();

        field.tick();

        asrtFl1("☼☼☼☼☼☼\n" +
                "☼x  $☼\n" +
                "☼☺ ☻ ☼\n" +
                "☼  ☻ ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");
    }

    // игроков можно удалять из игры
    @Test
    public void shouldRemove() {
        game3.close();

        field.tick();

        asrtFl1("☼☼☼☼☼☼\n" +
                "☼☺  $☼\n" +
                "☼    ☼\n" +
                "☼ ☻  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");
    }

    // игрок может взорваться на бомбе
    @Test
    public void shouldKill() {
        game1.getJoystick().down();
        game1.getJoystick().act();
        game3.getJoystick().left();

        field.tick();

        asrtFl1("☼☼☼☼☼☼\n" +
                "☼x☻ $☼\n" +
                "☼☺   ☼\n" +
                "☼ ☻  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");

        game3.getJoystick().left();
        field.tick();

        asrtFl1("☼☼☼☼☼☼\n" +
                "☼X  $☼\n" +
                "☼☺   ☼\n" +
                "☼ ☻  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");

        verify(listener3).event(Events.LOOSE);
        assertTrue(game3.isGameOver());

        dice(4, 1);
        game3.newGame();

        field.tick();

        asrtFl1("☼☼☼☼☼☼\n" +
                "☼   $☼\n" +
                "☼☺   ☼\n" +
                "☼ ☻  ☼\n" +
                "☼   ☻☼\n" +
                "☼☼☼☼☼☼\n");
    }

    // игрок может подобрать золото
    @Test
    public void shouldGetGold() {
        game3.getJoystick().right();

        dice(1, 2);

        field.tick();

        asrtFl1("☼☼☼☼☼☼\n" +
                "☼☺  ☻☼\n" +
                "☼    ☼\n" +
                "☼$☻  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");

        verify(listener3).event(Events.WIN);
    }

    // игрок не может пойи на другого игрока
    @Test
    public void shouldCantGoOnHero() {
        game1.getJoystick().right();
        game3.getJoystick().left();

        field.tick();

        asrtFl1("☼☼☼☼☼☼\n" +
                "☼ ☺☻$☼\n" +
                "☼    ☼\n" +
                "☼ ☻  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");
    }
}
