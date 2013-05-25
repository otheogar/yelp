package yelp;

class Votes {
	private int funny;
	private int useful;
	private int cool;
	public Votes(){

	}
	

	@Override
	public String toString(){
		return "funny : " + this.funny + ", useful: "+ this.useful + ", cool:"+ this.cool;
	}
}
