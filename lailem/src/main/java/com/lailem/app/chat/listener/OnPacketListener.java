package com.lailem.app.chat.listener;

import com.lailem.app.chat.util.MessageFactory;
import com.socks.library.KLog;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

/**
 * 消息等监听器
 * 
 * @author admin
 * 
 */
public class OnPacketListener implements PacketListener {

	@Override
	public void processPacket(Packet packet) {
        KLog.i("heartbeat");
		if (packet!=null && packet instanceof Message) {
			MessageFactory.getFactory().onMessage(((Message) packet).getBody());
		}

	}

}
