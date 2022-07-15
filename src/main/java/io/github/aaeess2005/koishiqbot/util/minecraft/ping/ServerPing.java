package io.github.aaeess2005.koishiqbot.util.minecraft.ping;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class ServerPing {
    public final InetSocketAddress host;
    public static final int TIMEOUT = 14_514;

    public ServerPing(InetSocketAddress host) {
        this.host = host;
    }

    private static int readVarInt(DataInputStream in) throws IOException {
        int i = 0;
        int j = 0;
        while (true) {
            int k = in.readByte();
            i |= (k & 0x7F) << j++ * 7;
            if (j > 5)
                throw new IOException("VarInt too big");
            if ((k & 0x80) != 128) break;
        }
        return i;
    }

    private static void writeVarInt(DataOutputStream out, int paramInt) throws IOException {
        while (true) {
            if ((paramInt & 0xFFFFFF80) == 0) {
                out.writeByte(paramInt);
                return;
            }
            out.writeByte(paramInt & 0x7F | 0x80);
            paramInt >>>= 7;
        }
    }
    public JsonObject fetchData() throws IOException {
        //Open Connection
        Socket socket = new Socket();
        try {
            socket.setSoTimeout(TIMEOUT);
            socket.connect(host, TIMEOUT);
        } catch (IOException e) {
            throw e;
        }

        //Init Stream
        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        InputStream inputStream = socket.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream handshake = new DataOutputStream(b);

        //Transport Data
        handshake.writeByte(0x00); //Packet ID for Handshake
        writeVarInt(handshake, 4); //Protocol Version
        writeVarInt(handshake, this.host.getHostString().length()); //Host Length
        handshake.writeBytes(this.host.getHostString()); //Host String
        handshake.writeShort(host.getPort()); //Port
        writeVarInt(handshake, 1); //State (1 for Handshake)

        writeVarInt(dataOutputStream, b.size()); //Prepend Size
        dataOutputStream.write(b.toByteArray()); //Write Handshake Packet


        dataOutputStream.writeByte(0x01); //Size is Only 1
        dataOutputStream.writeByte(0x00); //Packet ID for Ping
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        readVarInt(dataInputStream); // size of packet, ignored
        int id = readVarInt(dataInputStream); // packet id

        if (id == -1)
            throw new IOException("Premature end of stream.");
        if (id != 0x00) // we want a status response
            throw new IOException("Invalid packetID");

        int length = readVarInt(dataInputStream); // length of json string

        if (length == -1)
            throw new IOException("Premature end of stream.");
        if (length == 0)
            throw new IOException("Invalid string length.");

        byte[] in = new byte[length];
        dataInputStream.readFully(in);  // read json string
        String json = new String(in);

        dataOutputStream.writeByte(0x09); // size of packet
        dataOutputStream.writeByte(0x01); // 0x01 for ping
        dataOutputStream.writeLong(System.currentTimeMillis()); // current time

        readVarInt(dataInputStream);
        id = readVarInt(dataInputStream);

        if (id == -1)
            throw new IOException("Premature end of stream.");
        if (id != 0x01)
            throw new IOException("Invalid packetID");

        long pingTime = dataInputStream.readLong(); // read response

        dataOutputStream.close();
        outputStream.close();
        inputStreamReader.close();
        inputStream.close();
        socket.close();

        JsonObject object = JsonParser.parseString(json).getAsJsonObject();
        object.addProperty("ping", System.currentTimeMillis() - pingTime);

        return object;
    }
}