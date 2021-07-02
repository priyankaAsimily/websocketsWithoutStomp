package com.devglan.config;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.postgresql.PGConnection;
import org.postgresql.PGNotification;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

class Listener extends Thread
{
    private Connection conn;
    private PGConnection pgconn;
    private WebSocketSession session;

    Listener(Connection conn, WebSocketSession sessionID) throws SQLException
    {
        this.conn = conn;
        session = sessionID;
        this.pgconn = conn.unwrap(PGConnection.class);
        Statement stmt = conn.createStatement();
        stmt.execute("LISTEN command");
        stmt.close();
    }

    public void run()
    {
        try
        {
            while (true)
            {
                PGNotification notifications[] = pgconn.getNotifications();
                if (notifications != null)
                {
                    for (int i=0; i < notifications.length; i++)
                        System.out.println("Got notification: " + notifications[i].getName());
         	        	TextMessage textMessage = new TextMessage("Got notification");
                    	try {
							session.sendMessage(textMessage);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                }
                Thread.sleep(500);
            }
        }
        catch (SQLException sqle)
        {
            sqle.printStackTrace();
        }
        catch (InterruptedException ie)
        {
            ie.printStackTrace();
        }
    }
}