package yelp;

import java.util.Date;


class Review {
	/*{
		'type': 'review',
		'business_id': (encrypted business id),
		'user_id': (encrypted user id),
		'stars': (star rating, rounded to half-stars),
		'text': (review text),
		'date': (date, formatted like '2012-03-14'),
		'votes': {(vote type): (count)}
	}*/
	private String type;
	private String user_id;
	private String business_id;
	private double stars;
	private String text;
	private Date date;
	private Votes votes;
	
	

	public Review() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString(){
		return "User: "+this.user_id+"\n"+"Business id: "+ this.business_id +"\nStars: " + this.stars + 
				"\nDate: "+ this.date.toString() + "\nVotes: " + this.votes;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getBusiness_id() {
		return business_id;
	}

	public void setBusiness_id(String business_id) {
		this.business_id = business_id;
	}

	public double getStars() {
		return stars;
	}

	public void setStars(double stars) {
		this.stars = stars;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Votes getVotes() {
		return votes;
	}

	public void setVotes(Votes votes) {
		this.votes = votes;
	}



}
