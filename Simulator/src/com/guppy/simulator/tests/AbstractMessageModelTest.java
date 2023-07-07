package com.guppy.simulator.tests;

import com.guppy.simulator.broadcast.message.AbstractMessageModel;

public class AbstractMessageModelTest {
    @Test
    public void testGenerateMessageId() {
        AbstractMessageModel.MessageId messageId1 = new AbstractMessageModel().generateMessageId();
        AbstractMessageModel.MessageId messageId2 = new AbstractMessageModel().generateMessageId();

        Assertions.assertNotNull(messageId1);
        Assertions.assertNotNull(messageId2);
        Assertions.assertNotEquals(messageId1, messageId2);
    }


}
