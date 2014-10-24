DELETE FROM Message WHERE (MessageID,ThreadID) in (SELECT e1.MessageID,e1.ThreadID FROM  e1,e2 where e1.ThreadID= 
e2.ThreadID and e1.MessageID=e2.MessageID);
