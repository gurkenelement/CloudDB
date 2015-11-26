package ecs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import common.messages.KVAdminMessage;
import common.messages.MessageHandler;
import metadata.Metadata;
import common.messages.KVAdminMessage.StatusType;

public class ServerConnection extends Thread{
	private MessageHandler messageHandler;
	private InputStream input;
	private OutputStream output;
	private int cacheSize;
	private String displacementStrategy;
	private Metadata metadata;
	private Logger logger;

	/**
	 * This class handles the connection to the server.
	 * @param input		Input stream of the connected kvserver socket.
	 * @param output	Output stream of the connected kvserver socket.
	 * @param cacheSize	cacheSize of the KVServer.
	 * @param displacementStrategy	displacementStrategy of the KVServer.
	 * @param metadata	Metadata of the ECS,
	 * @param logger	Logger of the ECS.
	 */

	public ServerConnection(final InputStream input,final OutputStream output, final int cacheSize, final String displacementStrategy, final Metadata metadata, final Logger logger){
	
		this.input = input;
		this.output = output;
		this.cacheSize = cacheSize;
		this.displacementStrategy = displacementStrategy;
		this.metadata = metadata;
		this.logger = logger;
		messageHandler = new MessageHandler(input, output, logger);
	}
	
	/**
	 * Sends initialization message to the KVServer.
	 */
	@Override
	public void run(){
		KVAdminMessage initMessage = new KVAdminMessage();
		initMessage.setCacheSize(cacheSize);
		initMessage.setDisplacementStrategy(displacementStrategy);
		initMessage.setMetadata(metadata);
		initMessage.setStatusType(StatusType.INIT);

		try {
			messageHandler.sendMessage(initMessage.serialize().getMsg());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void startServer(){
		KVAdminMessage startMessage = new KVAdminMessage();
		startMessage.setStatusType(StatusType.START);

		try {
			messageHandler.sendMessage(startMessage.serialize().getMsg());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void stopServer(){
		KVAdminMessage stopMessage = new KVAdminMessage();
		stopMessage.setStatusType(KVAdminMessage.StatusType.STOP);

		try {
			messageHandler.sendMessage(stopMessage.serialize().getMsg());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void shutDown(){
		KVAdminMessage shutDownMessage = new KVAdminMessage();
		shutDownMessage.setStatusType(KVAdminMessage.StatusType.SHUTDOWN);

		try {
			messageHandler.sendMessage(shutDownMessage.serialize().getMsg());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	
	public void setWriteLock(){
		KVAdminMessage writeLockMessage = new KVAdminMessage();
		writeLockMessage.setStatusType(KVAdminMessage.StatusType.WRITELOCK);

		try {
			messageHandler.sendMessage(writeLockMessage.serialize().getMsg());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void releaseWriteLock(){
		KVAdminMessage writeLockMessage = new KVAdminMessage();
		writeLockMessage.setStatusType(KVAdminMessage.StatusType.WRITELOCK);

		try {
			messageHandler.sendMessage(writeLockMessage.serialize().getMsg());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void receiveData(){
		KVAdminMessage receiveMessage = new KVAdminMessage();
		receiveMessage.setStatusType(KVAdminMessage.StatusType.RECEIVE);

		try {
			messageHandler.sendMessage(receiveMessage.serialize().getMsg());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void moveData(String from, String to, String ip){
		try {


			KVAdminMessage moveMessage = new KVAdminMessage();
			moveMessage.setStatusType(StatusType.MOVE);
			moveMessage.setFrom(from);
			moveMessage.setTo(to);
			moveMessage.setIp(ip);
			messageHandler.sendMessage(moveMessage.serialize().getMsg());
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void update(Metadata metadata){
		KVAdminMessage updateMessage = new KVAdminMessage();
		updateMessage.setStatusType(StatusType.UPDATE);
		updateMessage.setMetadata(metadata);
		try {
			messageHandler.sendMessage(updateMessage.serialize().getMsg());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public byte [] getInput(){
		try {
			return messageHandler.receiveMessage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
