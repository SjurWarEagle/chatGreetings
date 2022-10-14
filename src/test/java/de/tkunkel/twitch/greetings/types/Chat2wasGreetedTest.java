package de.tkunkel.twitch.greetings.types;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Chat2wasGreetedTest {

    @Test
    public void markAsGreeted() {
        Chat2wasGreeted chat2wasGreeted=new Chat2wasGreeted();
        boolean wasGreeted = chat2wasGreeted.wasGreetedToday("A", "B");
        Assertions.assertFalse(wasGreeted);
        chat2wasGreeted.markAsGreeted("A","B");
        wasGreeted = chat2wasGreeted.wasGreetedToday("A", "B");
        Assertions.assertTrue(wasGreeted);
    }
}
