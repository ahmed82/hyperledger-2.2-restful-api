package com.hlf.uncccars.controller;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.codec.binary.Hex;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractException;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.hyperledger.fabric.protos.common.Common.Block;
import org.hyperledger.fabric.sdk.BlockInfo;
import org.hyperledger.fabric.sdk.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.hlf.uncccars.service.AdminService;

@RestController
@RequestMapping("/api")
public class WebController {

	Logger logger = LoggerFactory.getLogger(WebController.class);
	
	//private static final String FOO_CHANNEL_NAME = "foo";
	private static final String CHANNEL_NAME = "mychannel";

	@Autowired
	AdminService adminService;

	// helper function for getting connected to the gateway
	private /* static */ Gateway connect() throws Exception {
		// Load a file system based wallet for managing identities.
		Path walletPath = Paths.get("wallet");
		Wallet wallet = Wallets.newFileSystemWallet(walletPath);
		// load a CCP
		Path networkConfigPath = Paths.get("..", "..", "test-network", "organizations", "peerOrganizations",
				"org1.example.com", "connection-org1.yaml");

		Gateway.Builder builder = Gateway.createBuilder();
		builder.identity(wallet, "appUser").networkConfig(networkConfigPath).discovery(true);
		return builder.connect();
	}

	private Contract getContract() throws Exception {
		// connect to the network and invoke the smart contract
		Gateway gateway = connect();
		// get the network and contract
		Network network = gateway.getNetwork("mychannel");
		Contract contract = network.getContract("basic");
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
	 * @throws Exception 
	 */
	public JSONObject blockInfo() throws Exception {
		Gateway gateway = connect();
		// get the network and contract
		Network network = gateway.getNetwork("mychannel");
		//Contract contract = network.getContract("basic");
		Contract contract = network.getContract("qscc");
		byte[] resultByte = contract.evaluateTransaction(
		    "GetBlockByNumber",
		    CHANNEL_NAME
		/* ,String(blockNum) */
		);
	   // Channel fooChannel = constructChannel(FOO_CHANNEL_NAME, client, sampleOrg);
		// blockInfo = channel.queryBlockByNumber(Long.parseLong(current.toString()));
		byte[] blockNumber = resultByte ;
		JSONObject blockJson = new JSONObject();
		blockJson.put("blockNumber", blockNumber);
		//blockJson.put("dataHash", Hex.encodeHexString(blockInfo.getDataHash()));
		//blockJson.put("previousHashID", Hex.encodeHexString(blockInfo.getPreviousHash()));
		// blockJson.put("calculatedBlockHash",
		// Hex.encodeHexString(SDKUtils.calculateBlockHash(org.getClient(), blockNumber,
		// blockInfo.getPreviousHash(), blockInfo.getDataHash())));
		//blockJson.put("envelopeCount", blockInfo.getEnvelopeCount());
		// return new ResponseEntity<String>(blockJson, HttpStatus.OK);
		return getSuccess(blockJson);
	}

	@GetMapping("/init")
	public ResponseEntity<String> initLadger() throws Exception {
		Contract contract = getContract();
		logger.info("Submit Transaction: InitLedger creates the initial set of assets on the ledger.");
		contract.submitTransaction("InitLedger");
		return ResponseEntity.ok("init Ladger compleated...");
	}

	@GetMapping("/cars")
	public byte[] getAllCars() throws Exception {
		Contract contract = getContract();
		byte[] result;
		result = contract.evaluateTransaction("GetAllAssets");
		logger.info("Evaluate Transaction: GetAllAssets, result: " + new String(result));
		return result;
	}

	@GetMapping("/car/{assetid}")
	public byte[] getCar(@PathVariable String assetid) throws Exception {
		Contract contract = getContract();
		// ReadAsset returns an asset with given assetID. Example= "asset13"
		byte[] result = contract.evaluateTransaction("ReadAsset", assetid);
		logger.info("result: " + new String(result));
		return result;
	}

	@PostMapping("/car")
	public ResponseEntity<?> CreateCar(@RequestBody AssetDTO assetObj) throws Exception {

		Contract contract = getContract();
		/**
		 * CreateAsset creates an asset with ID asset13, color yellow, owner Tom, size 5
		 * and appraisedValue of 1300 contract.submitTransaction("CreateAsset",
		 * "asset13", "yellow", "5", "Tom", "1300");
		 */
		contract.submitTransaction("CreateAsset", assetObj.getId(), assetObj.getColor(), assetObj.getSize(),
				assetObj.getOwner(), assetObj.getPrice());
		return new ResponseEntity<Contract>(contract, HttpStatus.CREATED);
	}

	@PutMapping("/car/{assetid}")
	public ResponseEntity<?> UpdateAsset(@PathVariable String assetid, @RequestBody AssetDTO assetObj)
			throws Exception {

		if (assetObj.getId() != assetid)
			return null;
		Contract contract = getContract();
		/**
		 * CreateAsset creates an asset with ID asset13, color yellow, owner Tom, size 5
		 * UpdateAsset updates an existing asset with new properties. Same args as
		 * CreateAsset contract.submitTransaction("UpdateAsset", "asset1", "blue", "5",
		 * "Tomoko", "350");
		 */
		contract.submitTransaction("UpdateAsset", assetObj.getId(), assetObj.getColor(), assetObj.getSize(),
				assetObj.getOwner(), assetObj.getPrice());
		return new ResponseEntity<Contract>(contract, HttpStatus.OK);
	}

	private JSONObject getSuccess(JSON json) {
		JSONObject jsonObject = new JSONObject();
		// jsonObject.put("code", BlockListener.SUCCESS);
		jsonObject.put("data", json);
		return jsonObject;
	}

}
