package com.my.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class ChannelExecTest {
    public static void main(String[] args) throws Exception {
        JSch jsch = new JSch();

        Session session = null;
        ChannelShell channel = null;
        try {
            session = jsch.getSession("test", "127.0.0.1", 22);
            session.setPassword("Dpass!#wd2");
            session.setConfig("StrictHostKeyChecking", "no");
            session.setTimeout(60000);
            session.connect();

            channel = (ChannelShell) session.openChannel("shell");
            channel.connect();

            InputStream in = channel.getInputStream();
            OutputStream out = channel.getOutputStream();

            readEcho(channel, in);

            out.write("Dpass!#wd2".getBytes());
            out.write("\n".getBytes());
            out.flush();

            readEcho(channel, in);

            sendCommd(out);

            readEcho(channel, in);

            sendCommd(out);

            readEcho(channel, in);

        } catch (JSchException e) {
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
    }

    private static void sendCommd(OutputStream out) throws IOException {
        out.write("Dpa2ss!#wd5".getBytes());
        out.write("\n".getBytes());
        out.flush();
    }

    private static void readEcho(ChannelShell channel, InputStream in) throws IOException {
        boolean flag = false;
        byte[] buf = new byte[1024];
        while (true) {
            while (in.available() > 0) {
                int len = in.read(buf, 0, 1024);
                System.out.println(new String(buf, 0, len));
                if (len < 0 || len < 1024) {
                    flag = true;
                    break;
                }
            }
            if (channel.isClosed() || flag) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (Exception ee) {
            }
        }
    }
}
