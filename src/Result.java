/*
 * 
 * 
 * 
 * Cette classe va stocker le resultat du traitement
 * 
 * 
 * 
 */


public class Result {

	private String result;

	
	/*
	 * 
	 * 
	 * Ce constructeur c'est juste pour éviter des erreurs
	 * dans la classe Handle
	 */
	
	public Result(){


	}
	
	/**
	 * @param result
	 */
	
	
	public Result(String result) {

		this.result = result;
	}

	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}




}
