package yelp;



class User {

	/*{
		'type': 'user',
		'user_id': (encrypted user id),
		'name': (first name),
		'review_count': (review count),
		'average_stars': (floating point average, like 4.31),
		'votes': {(vote type): (count)}
	}*/
	private String user_id;
	private String name;
	private String type;
	private int review_count;
	private double average_stars;
	private Votes votes;

	public User() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString(){
		return "User: "+this.name+"\n"+"id: "+ this.user_id +"\nReview Count: " + this.review_count + 
				"\nAverage Stars: "+ this.average_stars + "\nVotes: " + this.votes;
	}
	
	
}
