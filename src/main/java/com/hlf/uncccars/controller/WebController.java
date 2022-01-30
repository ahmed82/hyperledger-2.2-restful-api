package com.hlf.uncccars.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequenceGenerator;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.hyperledger.fabric.protos.common.Common.Block;
import org.hyperledger.fabric.sdk.BlockInfo;
import org.hyperledger.fabric.sdk.BlockchainInfo;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hlf.uncccars.dto.AssetDTO;
import com.hlf.uncccars.dto.BlockInfoModel;
import com.hlf.uncccars.service.AdminService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 360)
public class WebController {

	Logger logger = LoggerFactory.getLogger(WebController.class);

	// private static final String FOO_CHANNEL_NAME = "foo";
	private static final String CHANNEL_NAME = "mychannel";

	@Autowired
	AdminService adminService;

	// helper function for getting connected to the gateway
	private /* static */ Gateway connect() throws Exception {
		// Load a file system based wallet for managing identities.
		Path walletPath = Paths.get("wallet");
		Wallet wallet = Wallets.newFileSystemWallet(walletPath);
		// load a CCP
		// Path networkConfigPath = Paths.get("..", "..", "test-network",
		// "organizations", "peerOrganizations",
		// "org1.example.com", "connection-org1.yaml");
		Path networkConfigPath = Paths.get("C:\\Users","1426391","Desktop","Desktop","test", "fabric-samples", "test-network", "organizations",
				"peerOrganizations", "org1.example.com", "connection-org1.yaml");
		Gateway.Builder builder = Gateway.createBuilder();
		builder.identity(wallet, "appUser").networkConfig(networkConfigPath).discovery(true);
		return builder.connect();
	}

	/**
	 * 
	 * @return Contract
	 * @throws Exception V 1.0
	 * @note: to get a specific contract see / V 1.0
	 *        https://hyperledger.github.io/fabric-gateway-java/ V 2.0 you can pass
	 *        the contract name as param
	 * @param contractName to the getContract(String contractName)
	 */
	private Contract getContract(String contractName) throws Exception {
		// connect to the network and invoke the smart contract
		Gateway gateway = connect();
		// get the network and contract
		Network network = gateway.getNetwork(CHANNEL_NAME);
		Contract contract = network.getContract(contractName);
		return contract;
	}

	@GetMapping("/enrolment")
	public ResponseEntity<?> enrolment() throws Exception {
		// enrolls the admin and registers the user
		adminService.EnrollAdmin();
		adminService.RegisterUser();
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	/**
	 * BlockInfo contains the data from a {@link Block}
	 * 
	 * @throws Exception
	 */
	@GetMapping("/hieght")
	public ResponseEntity<?> blockInfo() throws Exception {
		Gateway gateway = connect();
		// get the network and contract
		Network network = gateway.getNetwork(CHANNEL_NAME);
		// Contract contract = network.getContract("basic");
		// Contract contract = network.getContract("qscc");

		// Create instance of client.
		// HFClient client = HFClient.createNewInstance();
		Channel channel = network.getChannel();
		BlockchainInfo BlockchainInfo = channel.queryBlockchainInfo();
		long hieght = BlockchainInfo.getHeight();
		// BlockchainInfo.getBlockchainInfo();
		/**
		 * The protobuf BlockchainInfo struct this object is based on.
		 * queryBlockByNumber(Long.parseLong(current.toString()));
		 */
		BlockInfo returnedBlock = channel.queryBlockByNumber(hieght - 1);
		JSONObject blockJson = new JSONObject();
		blockJson.put("Hieght", hieght);
		blockJson.put("dataHash-By-BlockchainInfo", Hex.encodeHexString(BlockchainInfo.getCurrentBlockHash()));
		blockJson.put("previousHashID-By-BlockchainInfo", Hex.encodeHexString(BlockchainInfo.getPreviousBlockHash()));
		blockJson.put("ChannelId", returnedBlock.getChannelId());

		blockJson.put("EnvelopeCount", returnedBlock.getEnvelopeCount());
		blockJson.put("BlockNumber", returnedBlock.getBlockNumber());
		blockJson.put("TransactionCount", returnedBlock.getTransactionCount());
		blockJson.put("previousHash", Hex.encodeHexString(returnedBlock.getPreviousHash()));
		blockJson.put("dataHash-By-BlockInfo", Hex.encodeHexString(returnedBlock.getDataHash()));
		blockJson.put("hashCode", BlockchainInfo.hashCode());
		blockJson.put("caculateCurrentBlockhash", caculateCurrentBlockhash(returnedBlock));
		return ResponseEntity.ok().body(blockJson);
	}

	/**
	 * 
	 * @param blockInfo
	 * @return String current block hash {@summary} Replace it with
	 *         'channel.queryBlockchainInfo()'
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InvalidArgumentException
	 * @throws InstantiationException
	 * @throws NoSuchMethodException
	 * @throws CryptoException
	 * @throws ClassNotFoundException
	 */
	public static String caculateCurrentBlockhash(BlockInfo blockInfo)
			throws IOException, IllegalAccessException, InvocationTargetException, InvalidArgumentException,
			InstantiationException, NoSuchMethodException, CryptoException, ClassNotFoundException {
		CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
		ByteArrayOutputStream s = new ByteArrayOutputStream();
		DERSequenceGenerator seq = new DERSequenceGenerator(s);
		seq.addObject(new ASN1Integer(blockInfo.getBlockNumber()));
		seq.addObject(new DEROctetString(blockInfo.getPreviousHash()));
		seq.addObject(new DEROctetString(blockInfo.getDataHash()));

		seq.close();
		byte[] hash = cryptoSuite.hash(s.toByteArray());
		return Hex.encodeHexString(hash);
	}

	/****************************************************************************
	 * If you use SDKUtils Packages can be more easily calculated , You can try .
	 * The code is as follows ：
	 *****************************************************************************/
	/*
	 * String currentHash = Hex.encodeHexString( SDKUtils.calculateBlockHash(
	 * this.client, blockInfo.getBlockNumber(), blockInfo.getPreviousHash(),
	 * blockInfo.getDataHash()));
	 */

	//@CrossOrigin(origins = "http://localhost:3000", maxAge = 360)
	@GetMapping("/init")
	public ResponseEntity<String> initLadger() throws Exception {
		Contract contract = getContract("basic");
		logger.info("Submit Transaction: InitLedger creates the initial set of assets on the ledger.");
		contract.submitTransaction("InitLedger");
		return ResponseEntity.ok("init Ladger compleated...");
	}

	@GetMapping("/assets")
	public byte[] getAllCars() throws Exception {
		Contract contract = getContract("basic");
		byte[] result;
		result = contract.evaluateTransaction("GetAllAssets");
		logger.info("Evaluate Transaction: GetAllAssets, result: " + new String(result));
		return result;
	}

	@GetMapping("/assets/{assetid}")
	public byte[] getCar(@PathVariable String assetid) throws Exception {
		Contract contract = getContract("basic");
		// ReadAsset returns an asset with given assetID. Example= "asset13"
		byte[] result = contract.evaluateTransaction("ReadAsset", assetid);
		logger.info("result: " + new String(result));
		return result;
	}

	@PostMapping("/assets")
	public ResponseEntity<?> CreateCar(@RequestBody AssetDTO assetObj) throws Exception {
		String id = assetObj.getId();
		String color = assetObj.getColor();
		String owner = assetObj.getOwner();
		String appraisedValue = assetObj.getAppraisedValue();
		String size = assetObj.getSize();

		Contract contract = getContract("basic");
		/**
		 * CreateAsset creates an asset with ID asset13, color yellow, owner Tom, size 5
		 * and appraisedValue of 1300 contract.submitTransaction("CreateAsset",
		 * "asset13", "yellow", "5", "Tom", "1300");
		 */

		/*
		 * byte[] result = contract.submitTransaction("CreateAsset", assetObj.getId(),
		 * assetObj.getColor(), assetObj.getSize(), assetObj.getOwner(),
		 * assetObj.getPrice());
		 */
		byte[] result = contract.submitTransaction("CreateAsset", id, color, size, owner, appraisedValue);
		return new ResponseEntity<byte[]>(result, HttpStatus.CREATED);
	}

	@PutMapping("/assets/{assetid}")
	public ResponseEntity<?> UpdateAsset(@PathVariable String assetid, @RequestBody AssetDTO assetObj)
			throws Exception {

		if (assetObj.getId() != assetid)
			return null;
		Contract contract = getContract("basic");
		/**
		 * 
		 * UpdateAsset updates an existing asset with new properties. Same args as
		 * CreateAsset contract.submitTransaction("UpdateAsset", "asset1", "blue", "5",
		 * "Tomoko", "350");
		 */
		 contract.submitTransaction("UpdateAsset", assetObj.getId(), assetObj.getColor(), assetObj.getSize(),
				assetObj.getOwner(), assetObj.getAppraisedValue());
		return ResponseEntity.ok().build();
	}

	@SuppressWarnings("unused")
	private JSONObject getSuccess(JSON json) {
		JSONObject jsonObject = new JSONObject();
		// jsonObject.put("code", BlockListener.SUCCESS);
		jsonObject.put("data", json);
		return jsonObject;
	}

	@GetMapping("/bolckinfo")
	public ResponseEntity<?> getBlockInfo() throws Exception {
		Gateway gateway = connect();
		Network network = gateway.getNetwork(CHANNEL_NAME);
		Channel channel = network.getChannel();
		BlockchainInfo BlockchainInfo = channel.queryBlockchainInfo();
		long hieght = BlockchainInfo.getHeight();
		BlockInfoModel blockinfo = new BlockInfoModel();
		BlockInfo returnedBlock = channel.queryBlockByNumber(hieght - 1);
		blockinfo.setHieght(hieght);
		blockinfo.setCurrentBlockHash(Hex.encodeHexString(BlockchainInfo.getCurrentBlockHash()));
		blockinfo.setPreviousHashID(Hex.encodeHexString(BlockchainInfo.getPreviousBlockHash()));
		blockinfo.setChannelId(returnedBlock.getChannelId());
		blockinfo.setTransactionCount(returnedBlock.getTransactionCount());
		return ResponseEntity.ok().body(blockinfo);
	}

}
