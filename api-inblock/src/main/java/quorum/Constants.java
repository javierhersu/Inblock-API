package quorum;

public class Constants {
	public static final String GROUP_ID = "/04loj4H1HZNCnqalbIHEcNxQdk3a/IZ3K+fuMmLY4A=";
	
	public static final String PRIVATE_KEY_1 = "8f2a55949038a9610f50fb23b5883af3b4ecb3c3bb792cbcefbd1542c692be63";
	public static final String PRIVATE_KEY_2 = "c87509a1c067bbde78beb793e6fa76530b6382a4c0241e5e4a9ec0a0f44dc0d3";
	public static final String PRIVATE_KEY_3 = "ae6ae8e5ccbfb04590405997ee2d52d2b330726137b875053c36d94e974d162f";
	public static final String PRIVATE_KEY_4 = "2e0f059b21147be07b394b82d26060493a21a5a93065d95e235a4f341e5e04d0";
	public static final String PRIVATE_KEY_5 = "2af6510bbd31c07ce5f5e35f8cc5a5939d4652b45d16fd959474abc246131031"; 
	public static final String PRIVATE_KEY_6 = "fc0e366455ed8f4d7989972d6b8dd3e655c8cff4d31af564f09b811a89c747ec";
	public static final String PRIVATE_KEY_7 = "201c7700ebc6475914aff6d952f69569e8aee18f759478e107095b4763a379cd";
	
	public static final String PUBLIC_KEY_1 = "A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=";
	public static final String PUBLIC_KEY_2 = "Ko2bVqD+nNlNYL5EE7y3IdOnviftjiizpjRt+HTuFBs=";
	public static final String PUBLIC_KEY_3 = "k2zXEin4Ip/qBGlRkJejnGWdP9cjkK+DAvKNW31L2C8=";
	public static final String PUBLIC_KEY_4 = "25rSsJ99CofeZ/D2DFPr2e7dtWaYoje5kdGIzikLS0A=";
	public static final String PUBLIC_KEY_5 = "Vi5wOJ7AxoB+Pn4o16HGIQR2Tf+Ey/pnOpokfySt5VE=";
	public static final String PUBLIC_KEY_6 = "yNRXNIwqZ+SI8zeSqPmt7eOMbvvy/Cb2NoF1qKBfgwU=";
	public static final String PUBLIC_KEY_7 = "me/6Hfk4XQ5K3GCGdnnveZpknAF8iCMYf+I6DQBCZVs=";

	public static final String HOST_1 = "http://51.137.102.41:20000";
	public static final String HOST_2 = "http://51.137.102.41:20002";
	public static final String HOST_3 = "http://51.137.102.41:20004";
	public static final String HOST_4 = "http://51.137.102.41:20006";
	public static final String HOST_5 = "http://51.137.102.41:20008";
	public static final String HOST_6 = "http://51.137.102.41:20010";
	public static final String HOST_7 = "http://51.137.102.41:20012";
	
	public static final String CONTRACT_ADDRESS = "0xf2c9323b9dc2e557f1e1074bf7682b750d5cd2fa";
	
	public static final String SQL_USER = "postgres";
	public static final String SQL_PASSWORD = "EYPostgreSQL2020???";
	public static final String SQL_URL = "jdbc:postgresql://localhost:5432/inblockDB";
	public static final String SQL_DRIVER = "org.postgresql.Driver";
	
/********************* GET KEYS & HOSTS *************************************************/
    
    public String getPrivateKey(String user) {
    	if(user.contentEquals("inBLOCK") || user.contentEquals("inBlock") || user.contentEquals("AEF")) {
    		return Constants.PRIVATE_KEY_1;
    	}else if(user.contentEquals("AEAT")) {
    		return Constants.PRIVATE_KEY_2;
    	}else if(user.contentEquals("Santander")) {
    		return Constants.PRIVATE_KEY_3;
    	}else if(user.contentEquals("BBVA")) {
    		return Constants.PRIVATE_KEY_4;
    	}else if(user.contentEquals("Bankia")) {
    		return Constants.PRIVATE_KEY_5;
    	}else if(user.contentEquals("Caixabank")) {
    		return Constants.PRIVATE_KEY_6;
    	}else if(user.contentEquals("Sabadell")) {
    		return Constants.PRIVATE_KEY_7;
    	}else {
    		return "";
    	}
    }
    
    public String getPublicKey(String user) {
    	if(user.contentEquals("inBLOCK") || user.contentEquals("inBlock") || user.contentEquals("AEF")) {
    		return Constants.PUBLIC_KEY_1;
    	}else if(user.contentEquals("AEAT")) {
    		return Constants.PUBLIC_KEY_2;
    	}else if(user.contentEquals("Santander")) {
    		return Constants.PUBLIC_KEY_3;
    	}else if(user.contentEquals("BBVA")) {
    		return Constants.PUBLIC_KEY_4;
    	}else if(user.contentEquals("Bankia")) {
    		return Constants.PUBLIC_KEY_5;
    	}else if(user.contentEquals("Caixabank")) {
    		return Constants.PUBLIC_KEY_6;
    	}else if(user.contentEquals("Sabadell")) {
    		return Constants.PUBLIC_KEY_7;
    	}else {
    		return "";
    	}
    }
    
    public String getHost(String user) {
    	if(user.contentEquals("inBLOCK") || user.contentEquals("inBlock") || user.contentEquals("AEF")) {
    		return Constants.HOST_1;
    	}else if(user.contentEquals("AEAT")) {
    		return Constants.HOST_2;
    	}else if(user.contentEquals("Santander")) {
    		return Constants.HOST_3;
    	}else if(user.contentEquals("BBVA")) {
    		return Constants.HOST_4;
    	}else if(user.contentEquals("Bankia")) {
    		return Constants.HOST_5;
    	}else if(user.contentEquals("Caixabank")) {
    		return Constants.HOST_6;
    	}else if(user.contentEquals("Sabadell")) {
    		return Constants.HOST_7;
    	}else {
    		return "";
    	}
    }
}
