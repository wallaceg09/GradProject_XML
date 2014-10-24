select m.messageId, text, createtime, rcount
from message m join query_14 b
on m.messageId= b.messageId join query14_a c
on m.messageid= c.omessageid
and m.threadId= &threadId;