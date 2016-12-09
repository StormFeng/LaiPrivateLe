package com.lailem.app.chat.util;

import android.os.Build;
import android.util.Log;

import com.lailem.app.AppContext;
import com.lailem.app.chat.listener.OnPacketListener;
import com.lailem.app.jni.JniSharedLibraryWrapper;
import com.lailem.app.utils.Const;
import com.socks.library.KLog;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.provider.PrivacyProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.bytestreams.ibb.provider.CloseIQProvider;
import org.jivesoftware.smackx.bytestreams.ibb.provider.DataPacketProvider;
import org.jivesoftware.smackx.bytestreams.ibb.provider.OpenIQProvider;
import org.jivesoftware.smackx.bytestreams.socks5.provider.BytestreamsProvider;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.ping.PingManager;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.search.UserSearch;

import java.util.Collection;

public class IMManager implements ConnectionCreationListener, ConnectionListener {

    private XMPPConnection con;
    private static IMManager instance;
    public static long loginSuccessTime;

    private IMManager() {
        initConnection();
    }

    public static IMManager getInstance() {
        if (instance == null) {
            instance = new IMManager();
        }

        return instance;
    }

    public synchronized void loginIM() {
        KLog.i("登陆即时即时聊天系统");
        if (isConnected()) {
            KLog.i("已连接");
            if (!isLogined()) {
                KLog.i("未登陆，正在进行登陆。。。");
                login();
            } else {
                KLog.i("已登陆");
            }
        } else {
            KLog.i("未连接，先进行连接");
            connect();
        }
    }

    public void logoutIM() {
        disConnect();
        instance = null;
    }

    public void sendMessage(Message message) /*throws Exception*/ {
        if (isLogined()) {//已经登陆成功才发送消息，否则抛异常
            con.sendPacket(message);
        } else {
            throw new RuntimeException("no login IM");
        }
    }

    private void initConnection() {
        try {
            if (con == null) {
                configure(ProviderManager.getInstance());
                ConnectionConfiguration connConfig = new ConnectionConfiguration(JniSharedLibraryWrapper.hostForChat(), Integer.parseInt(JniSharedLibraryWrapper.portForChat()));

                connConfig.setReconnectionAllowed(true);
                connConfig.setSecurityMode(SecurityMode.disabled); // SecurityMode.required/disabled
                connConfig.setSASLAuthenticationEnabled(false); // true/false
                connConfig.setCompressionEnabled(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    connConfig.setTruststoreType("AndroidCAStore");
                    connConfig.setTruststorePassword(null);
                    connConfig.setTruststorePath(null);
                } else {
                    connConfig.setTruststorePath("/system/etc/security/cacerts.bks");
                    connConfig.setTruststoreType("BKS");
                }
                connConfig.setSendPresence(true);
                connConfig.setRosterLoadedAtLogin(false);

                con = new XMPPConnection(connConfig);
                con.addConnectionCreationListener(this);
                con.addConnectionListener(this);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void login() {
        if (con != null && con.isConnected() && !con.isAuthenticated()) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    synchronized (instance) {
                        try {
                            String user_id = AppContext.getInstance().getLoginUid();
                            KLog.i("loginUid:::" + user_id);
                            KLog.i("loginUid size:::" + user_id.length());
                            String token = AppContext.getInstance().getProperty(Const.USER_TOKEN);
                            KLog.i("token size:::" + token.length());
                            con.login(user_id, token);
                            KLog.i("login success");
                            initPacketListener();
                            setPresence(0);
                            loginSuccessTime = System.currentTimeMillis();
                        } catch (Exception e) {
                            KLog.i("登陆异常");
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }else{
            loginSuccessTime = System.currentTimeMillis();
        }
    }

    private void initPacketListener() {
        AndFilter filter = new AndFilter(new PacketTypeFilter(Packet.class));
        OnPacketListener listener = new OnPacketListener();
        con.addPacketListener(listener, filter);
    }

    private synchronized void connect() {
        if (con != null && !con.isConnected()) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    synchronized (instance) {
                        try {
                            con.connect();
                            ServiceDiscoveryManager serviceDiscoMgr = ServiceDiscoveryManager.getInstanceFor(con);
                            SmackConfiguration.setDefaultPingInterval(180);
                            PingManager.getInstanceFor(con);
                            serviceDiscoMgr.addFeature("http://jabber.org/protocol/disco#info");
                            serviceDiscoMgr.addFeature("bug-fix-gtalksms");
                        } catch (XMPPException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

        }

    }

    /**
     * 设置状态
     *
     * @param state
     */
    private void setPresence(final int state) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                Presence presence = null;

                switch (state) {
                    case 0:
                        presence = new Presence(Presence.Type.available);
                        con.sendPacket(presence);
                        KLog.i("设置在线");
                        break;
                    case 1:
                        presence = new Presence(Presence.Type.available);
                        presence.setMode(Presence.Mode.chat);
                        con.sendPacket(presence);
                        KLog.i("Q我吧");
                        break;
                    case 2:
                        presence = new Presence(Presence.Type.available);
                        presence.setMode(Presence.Mode.dnd);
                        con.sendPacket(presence);
                        KLog.i("忙碌");
                        break;
                    case 3:
                        presence = new Presence(Presence.Type.available);
                        presence.setMode(Presence.Mode.away);
                        con.sendPacket(presence);
                        KLog.i("离开");
                        break;
                    case 4:
                        Roster roster = con.getRoster();
                        Collection<RosterEntry> entries = roster.getEntries();
                        for (RosterEntry entity : entries) {
                            presence = new Presence(Presence.Type.unavailable);
                            presence.setPacketID(Packet.ID_NOT_AVAILABLE);
                            presence.setFrom(con.getUser());
                            presence.setTo(entity.getUser());
                            con.sendPacket(presence);
                        }
                        KLog.i("告知其他用户-隐身");

                        presence = new Presence(Presence.Type.unavailable);
                        presence.setPacketID(Packet.ID_NOT_AVAILABLE);
                        presence.setFrom(con.getUser());
                        presence.setTo(StringUtils.parseBareAddress(con.getUser()));
                        con.sendPacket(presence);
                        KLog.i("告知本用户的其他客户端-隐身");
                        break;
                    case 5:
                        presence = new Presence(Presence.Type.unavailable);
                        con.sendPacket(presence);
                        KLog.i("离线");
                        break;
                    default:
                        break;
                }

            }
        }).start();

    }

    private void disConnect() {
        if (null != con && con.isConnected()) {
            con.disconnect();
            con = null;
        }
    }

    private boolean isLogined() {

        if (con == null){
            KLog.i("con.isSocketClosed():::"+con.isSocketClosed());
            return false;
        }
        return con.isAuthenticated();
    }

    private boolean isConnected() {
        boolean isConnected = false;
        if (con != null) {
            isConnected = con.isConnected();
            KLog.i("isConnected:::" + isConnected);
        }
        return isConnected;
    }

    /**
     * ConnectionCreationListener
     */
    @Override
    public void connectionCreated(Connection connection) {
        KLog.i("connectionCreated");
        login();
    }

    /**
     * 手动关闭连接时会调用该方法
     * ConnectionListener
     */
    @Override
    public void connectionClosed() {
        KLog.i("connectionClosed");
    }

    /**
     * 断开网络时会执行该方法
     * ConnectionListener
     */
    @Override
    public void connectionClosedOnError(Exception e) {

        KLog.i("connectionClosedOnError");

    }

    /**
     * ConnectionListener
     */
    @Override
    public void reconnectingIn(int seconds) {
        KLog.i("reconnectingIn:::" + seconds);
    }

    /**
     * ConnectionListener
     */
    @Override
    public void reconnectionSuccessful() {
        KLog.i("reconnectionSuccessful");
        login();
    }

    /**
     * ConnectionListener
     */
    @Override
    public void reconnectionFailed(Exception e) {
        KLog.i("reconnectionFailed:::" + e.getMessage());
    }

    private void configure(ProviderManager pm) {

        // Private Data Storage
        pm.addIQProvider("query", "jabber:iq:private", new PrivateDataManager.PrivateDataIQProvider());

        // Time
        try {
            pm.addIQProvider("query", "jabber:iq:time", Class.forName("org.jivesoftware.smackx.packet.Time"));
        } catch (ClassNotFoundException e) {
            Log.w("TestClient", "Can't load class for org.jivesoftware.smackx.packet.Time");
        }

        // Roster Exchange
        pm.addExtensionProvider("x", "jabber:x:roster", new RosterExchangeProvider());

        // Message Events
        pm.addExtensionProvider("x", "jabber:x:event", new MessageEventProvider());

        // Chat State
        pm.addExtensionProvider("active", "http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
        pm.addExtensionProvider("composing", "http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
        pm.addExtensionProvider("paused", "http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
        pm.addExtensionProvider("inactive", "http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
        pm.addExtensionProvider("gone", "http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());

        // XHTML
        pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im", new XHTMLExtensionProvider());

        // Group Chat Invitations
        pm.addExtensionProvider("x", "jabber:x:conference", new GroupChatInvitation.Provider());

        // Service Discovery # Items
        pm.addIQProvider("query", "http://jabber.org/protocol/disco#items", new DiscoverItemsProvider());

        // Service Discovery # Info
        pm.addIQProvider("query", "http://jabber.org/protocol/disco#info", new DiscoverInfoProvider());

        // Data Forms
        pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());

        // MUC User
        pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user", new MUCUserProvider());

        // MUC Admin
        pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin", new MUCAdminProvider());

        // MUC Owner
        pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner", new MUCOwnerProvider());

        // Delayed Delivery
        pm.addExtensionProvider("x", "jabber:x:delay", new DelayInformationProvider());

        // Version
        try {
            pm.addIQProvider("query", "jabber:iq:version", Class.forName("org.jivesoftware.smackx.packet.Version"));
        } catch (ClassNotFoundException e) {
            // Not sure what's happening here.
        }

        // VCard
        pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());

        // Offline Message Requests
        pm.addIQProvider("offline", "http://jabber.org/protocol/offline", new OfflineMessageRequest.Provider());

        // Offline Message Indicator
        pm.addExtensionProvider("offline", "http://jabber.org/protocol/offline", new OfflineMessageInfo.Provider());

        // Last Activity
        pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());

        // User Search
        pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());

        // SharedGroupsInfo
        pm.addIQProvider("sharedgroup", "http://www.jivesoftware.org/protocol/sharedgroup", new SharedGroupsInfo.Provider());

        // JEP-33: Extended Stanza Addressing
        pm.addExtensionProvider("addresses", "http://jabber.org/protocol/address", new MultipleAddressesProvider());

        // FileTransfer
        pm.addIQProvider("si", "http://jabber.org/protocol/si", new StreamInitiationProvider());
        pm.addIQProvider("query", "http://jabber.org/protocol/bytestreams", new BytestreamsProvider());
        pm.addIQProvider("open", "http://jabber.org/protocol/ibb", new OpenIQProvider());
        pm.addIQProvider("close", "http://jabber.org/protocol/ibb", new CloseIQProvider());
        pm.addExtensionProvider("data", "http://jabber.org/protocol/ibb", new DataPacketProvider());

        // Privacy
        pm.addIQProvider("query", "jabber:iq:privacy", new PrivacyProvider());
        pm.addIQProvider("command", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider());
        pm.addExtensionProvider("malformed-action", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.MalformedActionError());
        pm.addExtensionProvider("bad-locale", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.BadLocaleError());
        pm.addExtensionProvider("bad-payload", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.BadPayloadError());
        pm.addExtensionProvider("bad-sessionid", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.BadSessionIDError());
        pm.addExtensionProvider("session-expired", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.SessionExpiredError());
    }

}
