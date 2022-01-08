package com.hlf.uncccars.dto;

public class BlockInfoModel {

	private String channelId;
	private long hieght;
	// private long blockNumber;
	/**
	 * actual type is byte[], converted to String after using Hex.encodeHexString()
	 */
	private String previousHashID;
	private String currentBlockHash;
	// private long EnvelopeCount;
	private int TransactionCount;

	public BlockInfoModel() {
		super();
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public long getHieght() {
		return hieght;
	}

	public void setHieght(long hieght) {
		this.hieght = hieght;
	}

	public String getPreviousHashID() {
		return previousHashID;
	}

	public void setPreviousHashID(String previousHashID) {
		this.previousHashID = previousHashID;
	}

	public String getCurrentBlockHash() {
		return currentBlockHash;
	}

	public void setCurrentBlockHash(String dataHash) {
		this.currentBlockHash = dataHash;
	}

	public long getTransactionCount() {
		return TransactionCount;
	}

	public void setTransactionCount(int transactionCount) {
		TransactionCount = transactionCount;
	}

}
