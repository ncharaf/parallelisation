
public class Handle extends Thread {
	/*
	 * Comme c'est un thread  on doit redefinir la methode Run qui va etre appele 
	 * 
	 * et c'est elle qui va se charger du traitement et en appelant la methode traitement
	 * 
	 * 
	 * Monsieur Le Duc vous nous aviez demandé comme quoi la méthode elle soit appelé par
	 * défaut on a refléchi on a trouvé une solution c'est que la classe doit avoir comme
	 * attribut de type  Result et elle va le stocker à l'interieur  comme ça on a pas 
	 * besoin de connaitre le nom de la fonction qui effectue le traitement
	 * 
	 * 
	 * 
	 * 
	 */


	private Data d;
	private Result r;


	public Handle (Data d){
		this.d=d;
	}



	public Result traitement (){

		return   new Result();
	}


	public Result getResult(){
		return this.r;
	}

}
